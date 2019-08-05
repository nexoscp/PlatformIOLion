group = "platformio"
version = "0.1"

buildscript {
  repositories {
    mavenCentral()
    jcenter()
  }
  dependencies { classpath(kotlin("gradle-plugin", "1.3.41")) }
}
repositories {
    mavenCentral()
    jcenter()
}

plugins {
  kotlin("jvm") version "1.3.41"
  id("org.jetbrains.intellij") version "0.4.9"
}


// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
  version = "2019.2"
  setPlugins("com.jetbrains.plugins.ini4idea:192.5728.26")
}
