package com.marcguilera.bank.test.coroutine

import assertk.fail
import kotlinx.coroutines.runBlocking
import org.jetbrains.spek.api.dsl.ActionBody
import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.dsl.TestBody
import org.jetbrains.spek.api.dsl.TestContainer
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

fun SpecBody.given(description: String, body: SpecBody.() -> Unit)
        = given(description) { runBlocking { body() } }

fun SpecBody.on(description: String, body: suspend ActionBody.() -> Unit)
        = on(description) { runBlocking { body() } }

fun TestContainer.it(description: String, body: suspend TestBody.() -> Unit)
        = it(description) { runBlocking { body() } }

fun <T> assertThat(f: suspend () -> T)
        = assertk.assertThat { runBlocking { f() } }

fun <T> assertThat(t: T)
        = assertk.assertThat(t)

fun catch(body: suspend () -> Any)
        = try {
            runBlocking { body() }
            fail("No exception thrown")
        } catch (e: Throwable) {
            e
        }