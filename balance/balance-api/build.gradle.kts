plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":account:account-api"))
    kotlin.forEach(::implementation)
}
