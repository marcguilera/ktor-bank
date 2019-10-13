plugins {
    application
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":account:account-api"))
    implementation(project(":lib:service"))
    testImplementation(project(":lib:test"))
}

application {
    mainClassName = "com.marcguilera.bank.account.AccountServiceKt"
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