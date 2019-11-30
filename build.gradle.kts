import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "platformio"

buildscript {
  repositories {
    mavenCentral()
    jcenter()
  }
  dependencies { classpath(kotlin("gradle-plugin", "1.3.61")) }
}
repositories {
    mavenCentral()
    jcenter()
}

dependencies {
  implementation("org.jetbrains.kotlinx","kotlinx-coroutines-core", "1.3.2")
  implementation( "com.beust", "klaxon", "5.0.13")
  testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.5.2")
  testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.5.2")
  testCompile("org.assertj", "assertj-swing-junit","3.9.2")
  testCompile("org.hamcrest","hamcrest-all","1.3")
  testCompile("org.mockito", "mockito-junit-jupiter", "3.2.0")
}

plugins {
  kotlin("jvm") version "1.3.61"
  id("org.jetbrains.intellij") version "0.4.14"
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

