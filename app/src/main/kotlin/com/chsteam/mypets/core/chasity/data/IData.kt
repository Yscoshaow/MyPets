package com.chsteam.mypets.core.chasity.data

import com.chsteam.mypets.core.chasity.IChasity
import com.chsteam.mypets.core.chasity.module.IChasityModule

interface IData {
    fun covertToModule(iChasity: IChasity) : IChasityModule
}