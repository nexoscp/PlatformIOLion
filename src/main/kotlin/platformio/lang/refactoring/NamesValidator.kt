package platformio.lang.refactoring

import com.intellij.lang.refactoring.NamesValidator
import com.intellij.openapi.project.Project
import platformio.PlatformIO

class NamesValidator : NamesValidator {
    override fun isKeyword(name: String, project: Project?): Boolean {
        PlatformIO.log.info("NamesValidator#isKeyword: $name")
        return true
    }

    override fun isIdentifier(name: String, project: Project?): Boolean {
        PlatformIO.log.info("NamesValidator#isIdentifier: $name")
        return true
    }
}
