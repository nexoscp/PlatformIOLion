package platformio.services.impl

import com.beust.klaxon.Klaxon
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.ToolWindowManager
import platformio.project.Generator
import platformio.project.currentProject
import platformio.services.Board
import platformio.services.Frequency
import platformio.services.Memory
import platformio.services.PlatformIOService
import platformio.toolwindow.console.ID
import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

class CommandLinePlatformIOService : PlatformIOService {

    private val executorService = Executors.newSingleThreadExecutor()

    val boards: Future<List<Board>> = loadBoardsInternal()

    companion object {
        val log = Logger.getInstance(CommandLinePlatformIOService::class.java);
    }

    override fun loadAllBoards(): List<Board> {
        return boards.get()
    }

    private fun loadBoardsInternal(): Future<List<Board>> {
        return executorService.submit(Callable<List<Board>> {
            val commandLine = GeneralCommandLine("platformio", "boards", "--json-output")
            val processHandler = CapturingProcessHandler(commandLine)

            var start = System.currentTimeMillis()
            val output = processHandler.runProcess()
            log.info("Time to load boards ${System.currentTimeMillis() - start}")

            if (output.exitCode == 0) {
                start = System.currentTimeMillis()
                val result = (Klaxon().parseArray<BoardModel>(output.stdout)
                        ?.map { it.toBoards() }
                        ?.flatten()
                        ?: emptyList())
                log.info("Time to deserialize boards ${System.currentTimeMillis() - start}")
                result
            } else {
                log.error("Cannot load boards. ${output.stderr}")
                Notifications.Bus.notify(Notification("PlatformIO", "Cannot load boards", "Check logs for details.", NotificationType.ERROR))
                emptyList()
            }
        })
    }

    override fun addBoardConfiguration(board: Board) {
        log.info("Init board $board")
        val addConfigurationCommandLine = GeneralCommandLine("platformio", "init", "--board", board.id, "--project-option", "framework='${board.framework}'")
        addConfigurationCommandLine.workDirectory = File(currentProject().basePath)
        val process = addConfigurationCommandLine.createProcess()
        Generator.log.info("Process log ${process.inputStream.reader().readLines().joinToString("\n")}")
        val errorMessage = "Failed to init board $board \n" + process.errorStream.reader().readLines().joinToString("\n")
        if (process.waitFor() != 0) {
            log.error(errorMessage)
        }
    }

    override fun initCLionProject() {

        val project = currentProject()

        val init = GeneralCommandLine("platformio", "init", "--ide", "clion")
        init.workDirectory = File(project.basePath)
        val processHandler = OSProcessHandler(init)

        processHandler.addProcessListener(object : ProcessListener {
            override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {}

            override fun processTerminated(event: ProcessEvent) {
                VirtualFileManager.getInstance().asyncRefresh(null)
            }

            override fun processWillTerminate(event: ProcessEvent, willBeDestroyed: Boolean) {}

            override fun startNotified(event: ProcessEvent) {}
        })

        val console = TextConsoleBuilderFactory
                .getInstance()
                .createBuilder(project)
                .console
        console.attachToProcess(processHandler)
        val contentManager = ToolWindowManager
                .getInstance(project)
                .getToolWindow(ID)
                .contentManager
        val content = contentManager.factory.createContent(console.component, "platformio init", false)
        contentManager.addContent(content)
        processHandler.startNotify()
    }

    data class BoardModel(
            var connectivity: Array<String>?,
            var debug: String? = null,
            val fcpu: Int,
            val frameworks: Array<String>,
            val id: String,
            val mcu: String,
            val name: String,
            val platform: String,
            val ram: Long,
            val rom: Long,
            val url: String,
            val vendor: String
    ) {
        fun toBoards(): Collection<Board> {
            return frameworks.map {
                Board(
                        id = id,
                        name = name,
                        platform = platform,
                        framework = it,
                        mcu = mcu,
                        frequency = Frequency(fcpu),
                        ram = Memory(ram),
                        flash = Memory(rom))
            }
        }
    }
}



