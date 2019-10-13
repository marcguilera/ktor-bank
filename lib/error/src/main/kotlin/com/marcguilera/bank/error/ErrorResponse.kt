package com.marcguilera.bank.error

import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import java.time.Instant

data class ErrorResponse (
        val status: HttpStatusCode,
        val message: String?,
        val path: String,
        val time: Instant
)

suspend inline fun <reified T> HttpClient.post(url: String, requestBody: Any)
        = processResponse { post<T>(url) { body = requestBody; contentType(Json) } }

suspend inline fun <reified T> HttpClient.delete(url: String)
        = processResponse { delete<T>(url) }

suspend inline fun <reified T> HttpClient.get(url: String)
        = processResponse { get<T>(url) }

inline fun <reified T> processResponse(block: () -> T): T {
    try {
        return block()
    } catch (e: ClientRequestException) {
        throw StatusCodeException(e.response.status, e.message)
    }
}