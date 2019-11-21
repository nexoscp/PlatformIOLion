package platformio.project

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import java.util.concurrent.TimeUnit


fun currentProject(): Project {
    val dataContext = DataManager.getInstance().dataContextFromFocusAsync
    return dataContext.blockingGet(1, TimeUnit.SECONDS)?.getData(PlatformDataKeys.PROJECT) as Project
}