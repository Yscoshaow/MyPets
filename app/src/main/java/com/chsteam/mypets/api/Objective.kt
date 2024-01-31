package com.chsteam.mypets.api

import androidx.compose.runtime.Composable
import com.chsteam.mypets.internal.Instruction

abstract class Objective(val instruction: Instruction) {

    abstract val typeName: String

    init {

    }

    @Composable
    abstract fun TaskCard()
}