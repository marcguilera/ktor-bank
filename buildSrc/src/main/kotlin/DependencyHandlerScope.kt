import org.gradle.kotlin.dsl.DependencyHandlerScope

val DependencyHandlerScope.kotlin get() = setOf(
        Dependencies.Kotlin.stdlib,
        Dependencies.Kotlin.reflect
)

val DependencyHandlerScope.kodein get() = setOf(
        Dependencies.Kodein.core,
        Dependencies.Kodein.generic
)

val DependencyHandlerScope.test get() = setOf(
        Dependencies.Kotlin.Test.assertk,
        Dependencies.Kotlin.Test.mockito,
        Dependencies.Kotlin.Test.Spek.api,
        Dependencies.Kotlin.Test.Spek.junit,
        Dependencies.Kotlin.Test.Spek.subject,
        Dependencies.Java.Test.junitRunner,
        Dependencies.Java.Test.jsonAssert
)

val DependencyHandlerScope.logging get() = setOf(
        Dependencies.Kotlin.logging,
        Dependencies.Java.logback
)

val DependencyHandlerScope.ktor get() = setOf(
        Dependencies.Ktor.jackson,
        Dependencies.Ktor.Server.core,
        Dependencies.Ktor.Server.netty,
        Dependencies.Ktor.Client.core,
        Dependencies.Ktor.Client.coreJvm,
        Dependencies.Ktor.Client.cio,
        Dependencies.Ktor.Client.json,
        Dependencies.Ktor.Client.jsonJvm,
        Dependencies.Ktor.Client.clientJackson,
        Dependencies.Kodein.Ktor.controller,
        Dependencies.Kodein.Ktor.server,
        Dependencies.Kotlin.retry,
        Dependencies.Java.Jackson.params
)

val DependencyHandlerScope.ktorTest get() = setOf(
        Dependencies.Ktor.Server.test
)

val DependencyHandlerScope.consul get() = setOf(
        Dependencies.Java.consul
)