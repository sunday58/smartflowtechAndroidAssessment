package com.golvia.smartflowtechandroidassessment.ui.naviagtion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.golvia.smartflowtechandroidassessment.ui.inventory.screens.InventoryScreen

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

        composable(route = "inventory_list") {
            InventoryScreen(
                onItemClick = { id ->
                    navController.navigate("inventory_detail/$id")
                },
                onAddItemClick = {
                    navController.navigate("inventory_add")
                })
        }
    }
}