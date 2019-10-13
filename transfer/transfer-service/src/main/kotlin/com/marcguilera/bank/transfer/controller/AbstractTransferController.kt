package com.marcguilera.bank.transfer.controller

import com.marcguilera.bank.transfer.TRANSFERS_ENDPOINT
import com.marcguilera.bank.transfer.TRANSFER_ENDPOINT
import com.marcguilera.bank.transfer.TransferInfo
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post

abstract class AbstractTransferController : TransferController  {
    override fun Routing.installRoutes() {
        post(TRANSFERS_ENDPOINT) {
            call.respond(Created, create(call.receive<TransferInfo.DTO>()))
        }
        get(TRANSFERS_ENDPOINT) {
            call.respond(getAll())
        }
        get(TRANSFER_ENDPOINT) {
            call.respond(getOne(call.transferId()))
        }
    }

    private fun ApplicationCall.transferId()
            = parameters["transferId"]!!
}