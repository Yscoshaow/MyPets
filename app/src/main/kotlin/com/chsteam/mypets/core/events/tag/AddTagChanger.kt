package com.chsteam.mypets.core.events.tag

import com.chsteam.mypets.core.database.Tag
import com.chsteam.mypets.core.database.TagDao
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddTagChanger : TagChanger, KoinComponent {

    private val tagDao: TagDao by inject()

    override suspend fun changeTags(tag: Tag) {
        tagDao.insert(tag)
    }
}