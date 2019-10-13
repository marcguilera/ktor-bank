object Dependencies {
    object Java {
        const val logback = "ch.qos.logback:logback-classic:${Versions.logback}"
        const val consul = "com.orbitz.consul:consul-client:${Versions.consul}"

        object Test {
            const val junitRunner = "org.junit.platform:junit-platform-runner:${Versions.junitRunner}"
            const val jsonAssert = "org.skyscreamer:jsonassert:${Versions.jsonAssert}"
        }

        object Jackson {
            const val kotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jackson}"
            const val params = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.10.0"
        }
    }

    object Kotlin {
        private const val prefix = "org.jetbrains.kotlin:kotlin"
        private const val version = Versions.kotlin

        const val stdlib = "$prefix-stdlib-jdk8:$version"
        const val reflect = "$prefix-reflect:$version"
        const val logging = "io.github.microutils:kotlin-logging:${Versions.kotlinLogging}"
        const val retry = "com.michael-bull.kotlin-retry:kotlin-retry:${Versions.retry}"

        object Test {
            const val assertk = "com.willowtreeapps.assertk:assertk:${Versions.assertk}"
            const val mockito = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.kotlinMockito}"
            object Spek {
                private const val prefix = "org.jetbrains.spek:spek"
                const val api = "$prefix-api:${Versions.spek}"
                const val junit = "$prefix-junit-platform-engine:${Versions.spek}"
                const val subject = "$prefix-subject-extension:${Versions.spek}"
            }
        }
    }

    object Ktor {
        private const val prefix = "io.ktor:ktor"

        const val jackson = "$prefix-jackson:${Versions.ktor}"

        object Server {
            private const val sprefix = "$prefix-server"

            const val core = "$sprefix-core:${Versions.ktor}"
            const val netty = "$sprefix-netty:${Versions.ktor}"
            const val test = "$sprefix-tests:${Versions.ktor}"
        }

        object Client {
            private const val cprefix = "$prefix-client"

            const val cio = "$cprefix-cio:${Versions.ktor}"
            const val core = "$cprefix-core:${Versions.ktor}"
            const val coreJvm = "$cprefix-core-jvm:${Versions.ktor}"
            const val json = "$cprefix-json:${Versions.ktor}"
            const val jsonJvm = "$cprefix-json-jvm:${Versions.ktor}"
            const val clientJackson = "$cprefix-jackson:${Versions.ktor}"
        }

    }

    object Kodein {
        private const val prefix = "org.kodein.di:kodein-di"
        private const val version = Versions.kodein

        const val core = "$prefix-core:$version"
        const val generic = "$prefix-generic-jvm:$version"

        object Ktor {
            private const val kprefix = "$prefix-framework-ktor-server"

            const val controller = "$kprefix-controller-jvm:$version"
            const val server = "$kprefix-jvm:$version"
        }
    }

}