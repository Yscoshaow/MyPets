package com.chsteam.mypets.core.conditions

import com.chsteam.mypets.api.Condition
import com.chsteam.mypets.core.Instruction
import com.chsteam.mypets.core.database.TagDao
import com.chsteam.mypets.core.utils.addPackage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TagCondition(instruction: Instruction) : Condition(instruction), KoinComponent {

    private val tagDao: TagDao by inject()

    private val tag: String

    init {
        tag = addPackage(instruction.pack, instruction.instruction)
    }

    override suspend fun execute(): Boolean {
        return tagDao.contains(tag)
    }
}