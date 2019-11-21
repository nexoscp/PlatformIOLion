import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "platformio"
version = "0.1"

buildscript {
  repositories {
    mavenCentral()
    jcenter()
  }
  dependencies { classpath(kotlin("gradle-plugin", "1.3.50")) }
}
repositories {
    mavenCentral()
    jcenter()
}

dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
  implementation( "com.beust:klaxon:5.0.13")
  api("org.junit.jupiter:junit-jupiter-api:5.3.1")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.1")
  testCompile("org.assertj:assertj-swing-junit:3.9.2")
  testCompile("org.hamcrest:hamcrest-all:1.3")
  testCompile("org.mockito:mockito-junit-jupiter:3.1.0")
}

plugins {
  kotlin("jvm") version "1.3.50"
  id("org.jetbrains.intellij") version "0.4.10"
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
  version = "2019.2"
  type = "CL"
  setPlugins("com.jetbrains.plugins.ini4idea:192.5728.26")
  downloadSources = true
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
  freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
}