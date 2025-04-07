package com.golvia.smartflowtechandroidassessment.ui.naviagtion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController

/**
 * davidsunday
 */

@Composable
fun InventoryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = "inventory_list",
        modifier = modifier
    ){

    }
}