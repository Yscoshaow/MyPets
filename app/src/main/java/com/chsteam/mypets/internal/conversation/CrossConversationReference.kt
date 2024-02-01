package com.chsteam.mypets.internal.conversation

import com.chsteam.mypets.api.config.quest.QuestPackage


/**
 * Represent a reference from one conversation to another.
 * The resolver will be used after loading all packages to resolve the conversation option in another package.
 *
 * @param sourcePack   the referring package
 * @param sourceConv   the referring conversation
 * @param sourceOption the referring option
 * @param resolver     the resolver that will be used to resolve the conversation option
 */
class CrossConversationReference(
    val sourcePack: QuestPackage,
    val sourceConv: String,
    val sourceOption: String,
    resolver: ConversationOptionResolver
) {
    val resolver: ConversationOptionResolver

    init {
        this.resolver = resolver
    }
}