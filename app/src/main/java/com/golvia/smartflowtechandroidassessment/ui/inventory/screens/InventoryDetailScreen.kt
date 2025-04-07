package com.golvia.smartflowtechandroidassessment.ui.inventory.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.golvia.smartflowtechandroidassessment.R
import com.golvia.smartflowtechandroidassessment.data.InventoryResponseItem
import com.golvia.smartflowtechandroidassessment.ui.inventory.components.ErrorStateMessage
import com.golvia.smartflowtechandroidassessment.ui.inventory.components.FloatingActionButtonWithChildren
import com.golvia.smartflowtechandroidassessment.ui.inventory.states.UiState
import com.golvia.smartflowtechandroidassessment.ui.inventory.viewModel.DeleteInventoryViewModel
import com.golvia.smartflowtechandroidassessment.ui.inventory.viewModel.GetInventoryViewModel
import com.golvia.smartflowtechandroidassessment.utils.Constants.CURRENCY
import com.golvia.smartflowtechandroidassessment.utils.formatWithComma

/**
 * davidsunday
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryDetailScreen(
    viewModel: GetInventoryViewModel = hiltViewModel(),
    deleteViewModel: DeleteInventoryViewModel = hiltViewModel(),
    inventoryId: String?,
    onEditClick: (InventoryResponseItem?) -> Unit,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit
) {

    LaunchedEffect(Unit) {
        inventoryId?.let { viewModel.getInventoryDetailItems(it) }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val deleteState by deleteViewModel.uiState.collectAsStateWithLifecycle()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(deleteState) {
        when (deleteState) {
            is UiState.Success -> Log.d("SuccessUpdate", "SuccessUpdate")
            is UiState.SuccessUpdate ->  Log.d("SuccessUpdate", "SuccessUpdate")
            is UiState.Error -> {
                isLoading = false
            /* Maybe show Snackbar here */ }
            is UiState.NoBody -> {
                onDeleteClick()
                isLoading = false
            }
            is UiState.Loading -> {
                isLoading = true
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.smartflow_inventory_system)) },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ){ paddingValues ->
        DetailComponent(paddingValues, uiState, viewModel,isLoading, onEditClick, onBackClick)
    }
}

@Composable
private fun DetailComponent(
    modifier: PaddingValues,
    uiState: UiState,
    viewModel: GetInventoryViewModel,
    isLoading: Boolean,
    onEditClick: (InventoryResponseItem?) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedIndex by remember { mutableStateOf(0) }

    when (val state = uiState) {
        is UiState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(20.dp),
                color = Color.Red,
                strokeWidth = 2.dp
            )
        }

        is UiState.Success -> {
            Log.d("Success", "Success")
        }

        is UiState.Error -> {
            ErrorStateMessage(
                errorMessage = state.message,
                onRetryClick = { viewModel.getInventoryItems() }
            )
        }

        is UiState.SuccessUpdate -> {
            val inventoryData = state.data
            val images = inventoryData?.images.orEmpty()
            if (images.isNotEmpty()) {
                selectedIndex = selectedIndex.coerceIn(0, images.size - 1)
            }

            SpecialOffersScreen(
                inventoryData = inventoryData,
                productImages = images,
                selectedIndex = selectedIndex,
                onImageSelected = { newIndex ->
                    selectedIndex = newIndex.coerceIn(0, images.size - 1)
                },
                onEditClick = onEditClick,
                isLoading = isLoading,
                onBackClick = {
                    onBackClick()
                }
            )
        }

        UiState.Default -> Log.d("Default", "Default")
        is UiState.NoBody -> Log.d("NoBody", "NoBody")
    }
}


@Composable
fun SpecialOffersScreen(
    inventoryData: InventoryResponseItem?,
    productImages: List<String>?,
    selectedIndex: Int,
    onBackClick: () -> Unit,
    onImageSelected: (Int) -> Unit,
    onEditClick: (InventoryResponseItem?) -> Unit,
    isLoading: Boolean,
    viewModel: DeleteInventoryViewModel = hiltViewModel()
) {
    val selectedImage = productImages?.get(selectedIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBackClick() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Special offers",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = selectedImage,
                contentDescription = "Product Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(260.dp)
                    .clip(CircleShape)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
            ) {
                productImages?.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .padding(4.dp)
                            .background(
                                if (index == selectedIndex) Color.Black else Color.Gray,
                                shape = CircleShape
                            )
                    )
                }
            }

            // Arrows
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                IconButton(
                    onClick = {
                        if (selectedIndex > 0) onImageSelected(selectedIndex - 1)
                    }
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
                }
                IconButton(
                    onClick = {
                        if (selectedIndex < (productImages?.lastIndex ?: 0)) onImageSelected(selectedIndex + 1)
                    }
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            productImages?.forEachIndexed { index, imageRes ->
                AsyncImage(
                    model = imageRes,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (index == selectedIndex) Color.DarkGray else Color.LightGray)
                        .clickable { onImageSelected(index) }
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Surface(
            color = Color.White,
            shape = RoundedCornerShape(24.dp),
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(30.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(inventoryData?.title.orEmpty(), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(inventoryData?.description.orEmpty(), fontSize = 16.sp, color = Color.Gray)
                    }
                }

               Column(
                   modifier = Modifier
                       .padding(8.dp)
                       .fillMaxWidth(),
                   horizontalAlignment = Alignment.End
               ) {
                   Text(
                       text = CURRENCY + inventoryData?.price.formatWithComma(),
                       fontSize = 20.sp,
                       fontWeight = FontWeight.Bold
                   )
               }
            }
            FloatingActionButtonWithChildren(
                onEditClick = { onEditClick(inventoryData) },
                onDeleteClick = { inventoryData?.id?.let { viewModel.deleteInventoryItems(it) }}
                , isLoading = isLoading
            )

        }
    }
}
