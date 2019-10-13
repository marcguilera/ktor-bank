plugins {
    application
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":account:account-api"))
    implementation(project(":balance:balance-api"))
    implementation(project(":balance:balance-client"))
    implementation(project(":transfer:transfer-api"))
    implementation(project(":account:account-client"))
    implementation(project(":lib:service"))
    testImplementation(project(":lib:test"))
}

application {
    mainClassName = "com.marcguilera.bank.transfer.TransferServiceKt"
}

tasks.withType<Jar> {
    manifest {
        attributes(
                mapOf(
                        "Main-Class" to application.mainClassName
                )
        )
    }
}