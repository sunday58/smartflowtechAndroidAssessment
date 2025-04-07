package com.golvia.smartflowtechandroidassessment.ui.naviagtion

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.golvia.smartflowtechandroidassessment.data.InventoryResponseItem
import com.golvia.smartflowtechandroidassessment.ui.inventory.screens.InventoryDetailScreen
import com.golvia.smartflowtechandroidassessment.ui.inventory.screens.InventoryScreen
import com.golvia.smartflowtechandroidassessment.ui.inventory.screens.NewProductScreen
import com.google.gson.Gson

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

        composable(
            route = "inventory_add?inventory={inventory}",
            arguments = listOf(
                navArgument("inventory") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val categoryList = listOf(1, 2, 3, 4)
            val encodedArticleJson = backStackEntry.arguments?.getString("inventory")
            val inventory = encodedArticleJson?.let {
                val decodedJson = Uri.decode(it)
                Gson().fromJson(decodedJson, InventoryResponseItem::class.java)
            }

            NewProductScreen(
                categories = categoryList,
                inventoryData = inventory,
                onSaveProduct = {
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
            }, onEditClick = { item ->
                val inventoryJson = Gson().toJson(item)
                val encodedJson = Uri.encode(inventoryJson)
                navController.navigate("inventory_add?inventory=$encodedJson")
            },
                onDeleteClick = {
                    navController.popBackStack()
                })
        }

    }
}