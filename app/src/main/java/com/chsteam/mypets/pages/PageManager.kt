package com.chsteam.mypets.pages

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object PageManager {

    @Composable
    fun Main() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Pages.HOME.name) {
            Pages.values().forEach {  page ->
                composable(route = page.name) {
                    page.page.Main(navController = navController)
                }
            }
        }
    }

}