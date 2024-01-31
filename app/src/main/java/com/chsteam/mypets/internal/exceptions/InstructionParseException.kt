package com.chsteam.mypets.internal.exceptions

import java.lang.Exception


/**
 * Exception thrown when the instruction string has a wrong format.
 */
class InstructionParseException : Exception {
    /**
     * [Exception.Exception]
     *
     * @param message the exceptions message.
     */
    constructor(message: String?) : super(message)

    /**
     * [Exception.Exception]
     *
     * @param message the exceptions message.
     * @param cause   the Throwable that caused this exception.
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)

    /**
     * [Exception.Exception]
     *
     * @param cause the exceptions cause.
     */
    constructor(cause: Throwable?) : super(cause)

    companion object {
        private const val serialVersionUID = 7487088647464022627L
    }
}