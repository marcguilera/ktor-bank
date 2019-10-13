package com.marcguilera.bank.account.controller

import com.marcguilera.bank.account.ACCOUNTS_ENDPOINT
import com.marcguilera.bank.account.ACCOUNT_ENDPOINT
import com.marcguilera.bank.account.AccountInfo
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post

/**
 * An [AccountController] which defines the routes and what to do
 * with them but delegates the functionality to the child.
 */
abstract class AbstractAccountController : AccountController {

    override fun Routing.installRoutes() {
        post(ACCOUNTS_ENDPOINT) {
            call.respond(Created, create(call.receive<AccountInfo.DTO>()))
        }
        get(ACCOUNTS_ENDPOINT) {
            call.respond(OK, getAll())
        }
        get(ACCOUNT_ENDPOINT) {
            call.respond(OK, getOne(call.accountId()))
        }
    }

    private fun ApplicationCall.accountId()
            = parameters["accountId"]!!
}