package com.marcguilera.bank.transfer.controller

import com.marcguilera.bank.transfer.Transfer
import com.marcguilera.bank.transfer.TransferId
import com.marcguilera.bank.transfer.TransferInfo
import org.kodein.di.ktor.controller.KodeinController

interface TransferController : KodeinController {
    suspend fun create(info: TransferInfo): Transfer
    suspend fun getOne(id: TransferId): Transfer
    suspend fun getAll(): Iterable<Transfer>
}