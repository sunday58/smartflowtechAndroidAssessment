package com.golvia.smartflowtechandroidassessment.ui.inventory.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.golvia.smartflowtechandroidassessment.R
import com.golvia.smartflowtechandroidassessment.ui.inventory.components.EmptyStateMessage
import com.golvia.smartflowtechandroidassessment.ui.inventory.components.ErrorStateMessage
import com.golvia.smartflowtechandroidassessment.ui.inventory.components.InventoryItem
import com.golvia.smartflowtechandroidassessment.ui.inventory.states.UiState
import com.golvia.smartflowtechandroidassessment.ui.inventory.viewModel.GetInventoryViewModel

/**
 * davidsunday
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    viewModel: GetInventoryViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit,
    onAddItemClick: () -> Unit,
){
    LaunchedEffect(Unit) {
        viewModel.getInventoryItems()
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            viewModel.searchInventory(searchQuery)
        } else {
            viewModel.getInventoryItems()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.smartflow_inventory_system)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ){ paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ){
                when (val state = uiState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    is UiState.Success -> {
                        val inventoryData = state.data
                        val isSearching = searchQuery.isNotEmpty()
                        if (inventoryData.isNullOrEmpty() && !isSearching) {
                            EmptyStateMessage(
                                message = stringResource(R.string.no_inventory_item_found_please_add_an_item),
                                onRetryClick = { viewModel.getInventoryItems() }
                            )
                        } else {
                                InventoryItem(
                                    inventoryItem = inventoryData.orEmpty(),
                                    onSearch = { viewModel.searchInventory(it) },
                                    onItemClick = { onItemClick(it) },
                                    onFabClick = onAddItemClick
                                )

                        }
                    }

                    is UiState.Error -> {
                        ErrorStateMessage(
                            errorMessage = state.message,
                            onRetryClick = { viewModel.getInventoryItems() }
                        )
                    }

                    is UiState.SuccessUpdate -> {
                        Log.d("SuccessUpdate", "SuccessUpdate")
                    }

                    UiState.Default -> Log.d("Default", "Default")
                    is UiState.NoBody -> Log.d("NoBody", "NoBody")
                }
            }
        }
    }
}