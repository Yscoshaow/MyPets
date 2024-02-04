package com.chsteam.mypets.core.conversation

/**
 * Represents the state of a conversation.
 */
enum class ConversationState(
    /**
     * True if the conversation has started.
     */
    val isStarted: Boolean,
    /**
     * True if the conversation has ended.
     */
    val isEnded: Boolean
) {
    CREATED(false, false),
    ACTIVE(true, false),
    ENDED(true, true);

    /**
     * Returns true if the conversation is active.
     *
     * @return true if the conversation is active.
     */
    val isActive: Boolean
        get() = isStarted && !isEnded

    /**
     * Returns true if the conversation is inactive.
     *
     * @return true if the conversation is inactive.
     */
    val isInactive: Boolean
        get() = !isStarted || isEnded
}