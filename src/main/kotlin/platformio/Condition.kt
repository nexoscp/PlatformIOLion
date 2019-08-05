package platformio

import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import platformio.PlatformIO.log

class Condition : Condition<Project> {


    override fun value(project:Project): Boolean {
        log.info("Checking $project")
//        ModuleManager.getInstance(project).modules.all {m -> inspect(m)}
  //      val rootManager = ModuleRootManager.getInstance(project.)
    //    return (rootManager.contentRoots.find { file -> file.name == "platformio.INI" })?.exists() == true
        return true
    }

    private fun inspect(module: Module?):Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
