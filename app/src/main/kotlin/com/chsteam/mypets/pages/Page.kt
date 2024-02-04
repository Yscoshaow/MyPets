package com.chsteam.mypets.pages

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

interface Page {
    @Composable
    fun Main(navController: NavController)
}