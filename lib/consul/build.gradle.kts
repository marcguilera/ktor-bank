plugins {
    kotlin("jvm")
}

dependencies {
    kotlin.forEach(::implementation)
    kodein.forEach(::implementation)
    logging.forEach(::implementation)
    ktor.forEach(::implementation)
    consul.forEach(::implementation)
    implementation(project(":lib:config"))
}