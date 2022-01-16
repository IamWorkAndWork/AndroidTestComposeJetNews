package com.example.testcomposejetnews.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController


object JetnewsDestinations {
    const val HOME_ROUTE = "home"
    const val INTERESTS_ROUTE = "interests"
}

class JetnewsNavigationActions(navController: NavHostController) {
    val navigationToHome: () -> Unit = {
        navController.navigate(JetnewsDestinations.HOME_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToInterests: () -> Unit =
        {
            navController.navigate(JetnewsDestinations.INTERESTS_ROUTE) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
}