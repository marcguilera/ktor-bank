package com.marcguilera.bank.balance.domain

import com.marcguilera.bank.error.StatusCodeException.BadRequest
import com.marcguilera.bank.error.StatusCodeException.NotFound

/**
 * Thrown when a balance can't be found in the system.
 */
class BalanceNotFoundException(message: String? = null, cause: Throwable? = null) : NotFound(message, cause)
class IllegalAmountException(message: String? = null, cause: Throwable? = null) : BadRequest(message, cause)