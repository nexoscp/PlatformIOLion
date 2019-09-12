package platformio

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.rootManager
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.vfs.*
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil.findChildrenOfType
import ini4idea.lang.psi.IniSection

class Startup : StartupActivity {
    override fun runActivity(project: Project) {
        VirtualFileManager.getInstance().addVirtualFileListener(object : VirtualFileListener {
            override fun propertyChanged(event: VirtualFilePropertyEvent) {
                if (event.fileName == PlatformIO.INI) {
                    updateOnINI(project, event.file)
                }
            }

            override fun fileCreated(event: VirtualFileEvent) {
                if (event.fileName == PlatformIO.INI) {
                    updateOnINI(project, event.file)
                }
            }

            override fun fileCopied(event: VirtualFileCopyEvent) {
                if (event.fileName == PlatformIO.INI) {
                    updateOnINI(project, event.file)
                }
            }

            override fun contentsChanged(event: VirtualFileEvent) {
                if (event.fileName == PlatformIO.INI) {
                    updateOnINI(project, event.file)
                }
            }
        })

        val app = ApplicationManager.getApplication()
        app.invokeLater {
            app.runWriteAction {
                ModuleManager.getInstance(project).modules
                        .map { Pair(it, ModuleRootManager.getInstance(it)) }
                        .flatMap { (module, rootManager) -> module.rootManager.contentRoots.asIterable().map { Triple(it, module, rootManager) } }
                        .map { (root, module, rootManager) -> tuple(root, root.findChild(PlatformIO.INI), module, rootManager) }
                        .filter { (_, ini, _, _) -> ini != null && ini.exists() }
                        .map { (root, ini, module, rootManager) -> tuple(root, PsiManager.getInstance(project).findFile(ini!!), module, rootManager) }
                        .filter { (_, psi, _, _) -> psi != null }
                        .forEach { (root, psi, module, rootManager) -> markDirs(root, psi!!, module, rootManager) }
            }
        }
    }
}

fun updateOnINI(project: Project, file: VirtualFile) {
    val app = ApplicationManager.getApplication()
    app.invokeLater {
        app.runWriteAction {
            if (!project.isDisposed) {
                PlatformIO.log.warn("update on '$project' '$file'")
                val psi = PsiManager.getInstance(project).findFile(file)
                if (psi != null) {
                    ModuleManager.getInstance(project).modules.forEach {
                        markDirs(file.parent, psi, it, ModuleRootManager.getInstance(it))
                    }
                }
            }
        }
    }
}

fun markDirs(root: VirtualFile, psi: PsiFile, module: Module, rootManager: ModuleRootManager) {
    var srcDirName: String = "src"
    var testSrcDirName: String = "test"
    findChildrenOfType(psi, IniSection::class.java).forEach { iniSection ->
        PlatformIO.log.info("Section ${iniSection.nameText}")
        when (iniSection.nameText) {
            "[platformio]" -> {
                iniSection.iniPropertyList.forEach {
                    when (it.iniKey.iniKeyName.name) {
                        "src_dir" -> srcDirName = it.iniValue.toString()
                        "test_dir" -> testSrcDirName = it.iniValue.toString()
                    }
                }
            }
        }
    }
    val model = rootManager.modifiableModel
    val content = model.addContentEntry(root)
    val srcDir = root.findChild(srcDirName)
    if (srcDir != null
            && srcDir.exists()
            && srcDir.isDirectory
            && !rootManager.sourceRoots.contains(srcDir)
            && !rootManager.excludeRoots.contains(srcDir)) {

        content.addSourceFolder(srcDir, false)
    }
    val testSrcDir = root.findChild(testSrcDirName)
    if (testSrcDir != null
            && testSrcDir.exists()
            && testSrcDir.isDirectory
            && !rootManager.sourceRoots.contains(testSrcDir)
            && !rootManager.excludeRoots.contains(testSrcDir)) {

        content.addSourceFolder(testSrcDir, true)
    }
    //walk thru .pio/libdeps/*/*/ and add them as sourceroots
    val libdeps = root.findChild(".pio")?.findChild("libdeps")
    libdeps?.children?.forEach { libs -> run {
            libs?.children?.forEach { l -> run {
                    val src = l.findChild("src")
                    if (src == null) {
                        if (!rootManager.excludeRoots.contains(l) && !rootManager.sourceRoots.contains(l)) {
                            content.addSourceFolder(l, false)
                        } else {}
                    } else {
                        if (!rootManager.excludeRoots.contains(src) && !rootManager.sourceRoots.contains(src)) {
                            content.addSourceFolder(src, false)
                        } else {}
                    }
                }
            }
        }
    }
    model.commit()
}
