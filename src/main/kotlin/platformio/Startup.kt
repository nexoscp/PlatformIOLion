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

fun  <T1, T2, T3, T4> tuple(t1: T1, t2: T2, t3: T3, t4: T4):NTuple4<T1, T2, T3, T4> {
    return NTuple4(t1, t2, t3, t4)
}
data class NTuple4<T1, T2, T3, T4>(val t1: T1, val t2: T2, val t3: T3, val t4: T4)

infix fun <T1, T2, T3, T4> Triple<T1, T2, T3>.then(t4: T4): NTuple4<T1, T2, T3, T4> {
    return NTuple4(this.first, this.second, this.third, t4)
}

class Startup : StartupActivity {
    override fun runActivity(project: Project) {
        VirtualFileManager.getInstance().addVirtualFileListener(object: VirtualFileListener {
            override fun propertyChanged(event: VirtualFilePropertyEvent) {
                if(event.fileName == PlatformIO.INI) {
                    updateOnINI(project, event.file)
                }
            }

            override fun fileCreated(event: VirtualFileEvent) {
                if(event.fileName == PlatformIO.INI) {
                    updateOnINI(project, event.file)
                }
            }

            override fun fileCopied(event: VirtualFileCopyEvent) {
                if(event.fileName == PlatformIO.INI) {
                    updateOnINI(project, event.file)
                }
            }

            override fun contentsChanged(event: VirtualFileEvent) {
                if(event.fileName == PlatformIO.INI) {
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
                    .map { (root, ini, module, rootManager) -> tuple(root, PsiManager.getInstance(project).findFile(ini!!), module, rootManager)}
                    .filter { (_, psi, _, _) -> psi != null}
                    .forEach { (root, psi, module, rootManager) -> markDirs(root, psi!!, module, rootManager) }
        }
        }
    }
}

fun updateOnINI(project:Project, file:VirtualFile) {
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

fun markDirs(root:VirtualFile, psi: PsiFile, module: Module, rootManager: ModuleRootManager) {
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
    if(srcDir != null
            && srcDir.exists()
            && srcDir.isDirectory
            && !rootManager.sourceRoots.contains(srcDir)
            && !rootManager.excludeRoots.contains(srcDir)) {

        content.addSourceFolder(srcDir, false)
    }
    val testSrcDir = root.findChild(testSrcDirName)
    if(testSrcDir != null
            && testSrcDir.exists()
            && testSrcDir.isDirectory
            && !rootManager.sourceRoots.contains(testSrcDir)
            && !rootManager.excludeRoots.contains(testSrcDir)) {

        content.addSourceFolder(testSrcDir, true)
    }
    model.commit()
}
