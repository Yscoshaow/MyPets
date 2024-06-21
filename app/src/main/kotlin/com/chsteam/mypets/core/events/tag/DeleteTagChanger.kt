package com.chsteam.mypets.core.events.tag

import com.chsteam.mypets.core.database.Tag
import com.chsteam.mypets.core.database.TagDao
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject

class DeleteTagChanger : TagChanger, KoinComponent {

    private val tagDao: TagDao by inject()

    override suspend fun changeTags(tag: Tag) {
        tagDao.delete(tag)
    }
}