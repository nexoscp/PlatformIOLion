package platformio.services


interface PlatformIOService {
    fun loadAllBoards(): List<Board>
    fun addBoardConfiguration(board: Board)
    fun initCLionProject()
}

data class Board(
        val id: String,
        val name: String,
        val platform: String,
        val framework: String,
        val mcu: String,
        val frequency: Frequency,
        val ram: Memory,
        val flash: Memory
)

inline class Frequency(val value: Int) {
    fun toMHz(): String {
        return "${(value / 1000000)} MHz"
    }
}

inline class Memory(val value: Long) {
    fun toKB(): String {
        return "${value / 1024} kB"
    }
}




