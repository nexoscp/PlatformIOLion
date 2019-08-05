package platformio

object Section {
    // https://docs.platformio.org/en/latest/projectconf.html
    val tokens = listOf("platformio", "common_env_data", "env", "common")
    val sections = tokens + "env:"

    fun isValidSectionName(name:String):Boolean {
        return (tokens.contains(name) || isEnvSectionName(name))
    }

    private fun isEnvSectionName(name: String) = (name.startsWith("env:") && name.length > "env:".length)
}
