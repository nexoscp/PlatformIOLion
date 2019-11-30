package platformio

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.util.Condition
import com.intellij.openapi.vfs.VirtualFile

class Condition : Condition<Project> {
    override fun value(project:Project): Boolean {
       return ModuleManager.getInstance(project).modules.fold(false){ found, module -> inspectModule(found, module)}
    }

    private fun inspectModule(found:Boolean, module: Module):Boolean {
        if (found) return true
        val rootManager = ModuleRootManager.getInstance(module)
        return rootManager.contentRoots.fold(false) { f, contentRoot -> inspectContentRoot(f, contentRoot) }
    }

    private fun inspectContentRoot(found:Boolean, contentRoot: VirtualFile):Boolean {
        if(found) return true;
        return contentRoot.children.fold(false) { f, file -> isPlatformIOINI(f, file) }
    }

    private fun isPlatformIOINI(found: Boolean, file: VirtualFile):Boolean {
        if(found) return true
        return file.name == "platformio.ini"
    }
}
