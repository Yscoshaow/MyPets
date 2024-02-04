package com.chsteam.mypets.internal.conversation

interface ConversationIO {
    /**
     * Set the text of response chosen by the NPC. Should be called once per
     * conversation cycle.
     *
     * @param npcName  the name of the NPC
     * @param response the text the NPC chose
     */
    fun setNpcResponse(npcName: String, response: String)

    /**
     * Adds the text of the player option. Should be called for each option in a
     * conversation cycle.
     *
     * @param option the text of an option
     */
    fun addPlayerOption(option: String)

    /**
     * Displays all data to the player. Should be called after setting all
     * options.
     */
    fun display()

    /**
     * Clears the data. Should be called before the cycle begins to ensure
     * nothing is left from previous one.
     */
    fun clear()

    /**
     * Ends the work of this conversation IO. Should be called when the
     * conversation ends.
     */
    fun end()

    /**
     * @return if this conversationIO should send messages to the player when the conversation starts and ends
     */
    fun printMessages(): Boolean {
        return true
    }

    /**
     * Send message through ConversationIO
     *
     * @param message The message to send
     */
    fun print(message: String) {
        // Empty
    }
}