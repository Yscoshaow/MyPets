package com.chsteam.mypets.core.conversation

/**
 * The interceptor is used to intercept chat messages that are sent to the player.
 * This is useful to provide a distraction free conversation experience.
 */
interface Interceptor {
    /**
     * Send message to player bypassing Interceptor
     *
     * @param message the message
     */
    fun sendMessage(message: String)

    /**
     * Send message to player bypassing Interceptor
     *
     * @param message the message
     */
    fun sendMessage(vararg message: String)

    /**
     * Ends the work of this interceptor
     */
    fun end()
}