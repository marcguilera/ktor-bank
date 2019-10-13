plugins {
    application
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":account:account-api"))
    implementation(project(":balance:balance-api"))
    implementation(project(":lib:service"))
    testImplementation(project(":lib:test"))
}

application {
    mainClassName = "com.marcguilera.bank.balance.BalanceServiceKt"
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