package com.marcguilera.bank.account.domain

import com.marcguilera.bank.error.StatusCodeException.BadRequest
import com.marcguilera.bank.error.StatusCodeException.NotFound

/**
 * Thrown when an account can't be found in the system.
 */
class AccountNotFoundException(message: String? = null, cause: Throwable? = null) : NotFound(message, cause)

/**
 * Thrown when the currency provided is invalid (can't be parsed into a known one).
 */
class IllegalCurrencyException(message: String? = null, cause: Throwable? = null) : BadRequest(message, cause)