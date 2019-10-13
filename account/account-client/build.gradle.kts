plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":account:account-api"))
    implementation(project(":lib:service"))
    testImplementation(project(":lib:test"))
}