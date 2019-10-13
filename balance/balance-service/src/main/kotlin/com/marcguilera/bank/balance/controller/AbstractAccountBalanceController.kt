package com.marcguilera.bank.balance.controller

import com.marcguilera.bank.balance.BALANCES_ENDPOINT
import com.marcguilera.bank.balance.BALANCE_ENDPOINT
import com.marcguilera.bank.balance.BalanceItemInfo
import com.marcguilera.bank.balance.LAST_BALANCE_ENDPOINT
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode.Companion.Accepted
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post

abstract class AbstractAccountBalanceController : AccountBalanceController {

    override fun Routing.installRoutes() {
        get(BALANCES_ENDPOINT) {
            call.respond(OK, getAll(call.accountId()))
        }
        get(LAST_BALANCE_ENDPOINT) {
            call.respond(OK, getLast(call.accountId()))
        }
        get(BALANCE_ENDPOINT) {
            call.respond(OK, getOne(call.accountId(), call.balanceId()))
        }
        post(BALANCES_ENDPOINT) {
            call.respond(Created, create(call.accountId(), call.receive<BalanceItemInfo.DTO>()))
        }
        delete(BALANCE_ENDPOINT) {
            call.respond(Accepted, delete(call.accountId(), call.balanceId()))
        }
    }

    private fun ApplicationCall.accountId()
            = parameters["accountId"]!!

    private fun ApplicationCall.balanceId()
            = parameters["balanceId"]!!
}