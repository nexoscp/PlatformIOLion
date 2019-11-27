package platformio.services.impl

import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.greaterThan
import org.junit.Assert.assertThat
import org.junit.Test
import kotlin.test.assertTrue

class CommandLinePlatformIOServiceTest {
    private val commandLinePlatformIOService = CommandLinePlatformIOService()

    @Test
    fun loadsBoards() {
        val loadAllBoards = commandLinePlatformIOService.loadAllBoards()
        assertThat(loadAllBoards.size, `is`(greaterThan(500)))
    }

    @Test
    fun `Platform CLI is available`() {
        assertTrue(commandLinePlatformIOService.isAvailable())
    }
}