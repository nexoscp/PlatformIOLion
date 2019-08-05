package platformio

enum class Command(val command:String, val description:String) {
    ACCOUNT("account","Manage PIO Account"),
    BOARDS("boards","Embedded Board Explorer"),
    CI("ci", "Continuous Integration"),
    DEBUG("debug","PIO Unified Debugger"),
    DEVICE("device", "Monitor device or list existing"),
    HOME("home","PIO Home"),
    INIT("init", "Initialize PlatformIO project or update existing"),
    LIB("lib","Library CSH.Manager"),
    PLATFORM("platform","Platform CSH.Manager"),
    REMOTE("remote","PIO Remote"),
    RUN("run","Process project environments"),
    SETTINGS("settings","Manage PlatformIO settings"),
    TEST("test","Local Unit Testing"),
    UPDATE("update","Update installed platforms, packages and libraries"),
    UPGRADE("upgrade", "Upgrade PlatformIO to the latest version")
}
