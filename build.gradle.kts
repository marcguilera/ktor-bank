import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version Versions.kotlin apply false
    id("com.github.johnrengelman.shadow") version Versions.shadow apply false
}

allprojects {

    group = "com.marcguilera.bank"

    version = "1.0"

    repositories {
        mavenCentral()
        jcenter()
        maven("https://dl.bintray.com/michaelbull/maven")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {
    subprojects.forEach {
        archives(it)
    }
}