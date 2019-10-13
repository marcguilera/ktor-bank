package com.marcguilera.bank.transfer.domain

import com.marcguilera.bank.error.StatusCodeException.BadRequest
import com.marcguilera.bank.error.StatusCodeException.NotFound

class TransferNotFoundException(message: String? = null, cause: Throwable? = null) : NotFound(message, cause)
class IllegalTransferException(message: String? = null, cause: Throwable? = null) : BadRequest(message, cause)