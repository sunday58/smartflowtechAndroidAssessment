package com.golvia.smartflowtechandroidassessment.ui.naviagtion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.golvia.smartflowtechandroidassessment.ui.inventory.screens.InventoryDetailScreen
import com.golvia.smartflowtechandroidassessment.ui.inventory.screens.InventoryScreen
import com.golvia.smartflowtechandroidassessment.ui.inventory.screens.NewProductScreen

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

        composable(route = "inventory_add") {
            val categoryList = listOf(1, 2, 3, 4)
            NewProductScreen(
                categories = categoryList,
                onSaveProduct = {
                    // Navigate back to the inventory list
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = "inventory_detail/{id}") {
            val id = it.arguments?.getString("id")
            InventoryDetailScreen(inventoryId = id, onBackClick = {
                navController.popBackStack()
            })
        }

    }
}