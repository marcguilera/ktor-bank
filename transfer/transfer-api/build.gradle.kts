plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":account:account-api"))
    implementation(project(":balance:balance-api"))
    kotlin.forEach(::implementation)
}
