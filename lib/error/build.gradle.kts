plugins {
    kotlin("jvm")
}

dependencies {
    kotlin.forEach(::implementation)
    ktor.forEach(::implementation)
    kodein.forEach(::implementation)
    logging.forEach(::implementation)
}