plugins {
    kotlin("jvm")
}

dependencies {
    kotlin.forEach(::implementation)
    kodein.forEach(::implementation)
}
