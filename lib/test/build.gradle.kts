import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

dependencies {
    kotlin.forEach(::api)
    kodein.forEach(::api)
    test.forEach(::api)
    ktorTest.forEach(::api)
    api(Dependencies.Java.Jackson.kotlin)
    api(project(":lib:identifier"))
    api(project(":lib:time"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}