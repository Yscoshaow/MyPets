package com.chsteam.mypets.core.events.tag

import com.chsteam.mypets.core.database.Tag

/**
 * Defines changes to be done on tags.
 */
interface TagChanger {
    /**
     * Apply the changes to the defined tags.
     * @param tag Tag data whose tags shall be changed.
     */
    fun changeTags(tag: Tag)
}
