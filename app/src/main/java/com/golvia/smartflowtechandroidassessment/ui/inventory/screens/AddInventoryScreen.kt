package com.golvia.smartflowtechandroidassessment.ui.inventory.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.golvia.smartflowtechandroidassessment.R
import com.golvia.smartflowtechandroidassessment.data.InventoryRequest
import com.golvia.smartflowtechandroidassessment.data.InventoryResponseItem
import com.golvia.smartflowtechandroidassessment.ui.inventory.states.UiState
import com.golvia.smartflowtechandroidassessment.ui.inventory.viewModel.AddInventoryViewModel

/**
 * davidsunday
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProductScreen(
    viewModel: AddInventoryViewModel = hiltViewModel(),
    categories: List<Int>,
    onSaveProduct: () -> Unit,
    onBackClick: () -> Unit,
    inventoryData: InventoryResponseItem? = null
) {
    val title = remember { mutableStateOf(inventoryData?.title ?: "") }
    val price = remember { mutableStateOf(inventoryData?.price?.toString() ?: "") }
    val description = remember { mutableStateOf(inventoryData?.description ?: "") }
    val selectedCategoryId = remember { mutableStateOf(inventoryData?.category?.categoryId) }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isFormValid = title.value.isNotBlank()
            && price.value.toDoubleOrNull() != null
            && description.value.isNotBlank()
            && selectedCategoryId.value != null
            && selectedImages.isNotEmpty()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedImages = uris
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Success -> Log.d("SuccessUpdate", "SuccessUpdate")
            is UiState.SuccessUpdate ->  onSaveProduct()
            is UiState.Error -> { /* Maybe show Snackbar here */ }
            else -> Unit
        }
    }

    val focusRequester = remember { FocusRequester() }
    val isFocused by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    BackHandler {
        if (imeVisible) {
            keyboardController?.hide()
        } else {
            onBackClick()
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
    ){ paddinValues ->
        AddInventoryComponent(
            paddinValues,
            title,
            focusRequester,
            isFocused,
            price,
            description,
            selectedCategoryId,
            categories,
            imagePickerLauncher,
            selectedImages,
            viewModel,
            isFormValid,
            uiState
        )
    }

}

@Composable
private fun AddInventoryComponent(
    paddinValues: PaddingValues,
    title: MutableState<String>,
    focusRequester: FocusRequester,
    isFocused: Boolean,
    price: MutableState<String>,
    description: MutableState<String>,
    selectedCategoryId: MutableState<Int?>,
    categories: List<Int>,
    imagePickerLauncher: ManagedActivityResultLauncher<String, List<@JvmSuppressWildcards Uri>>,
    selectedImages: List<Uri>,
    viewModel: AddInventoryViewModel,
    isFormValid: Boolean,
    uiState: UiState
) {
    var isFocused1 = isFocused
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 80.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = paddinValues.calculateBottomPadding()
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Add New Product", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Title") },
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused1 = focusState.isFocused
                }
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = price.value,
            onValueChange = { price.value = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused1 = focusState.isFocused
                }
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Description") },
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused1 = focusState.isFocused
                }
                .fillMaxWidth()
        )

        // Category Dropdown (Integer list)
        var categoryDropdownExpanded by remember { mutableStateOf(false) }
        Box {
            OutlinedTextField(
                value = selectedCategoryId.toString(),
                onValueChange = {},
                label = { Text("Category") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused1 = focusState.isFocused
                    }
                    .clickable { categoryDropdownExpanded = true },
                enabled = false
            )
            DropdownMenu(
                expanded = categoryDropdownExpanded,
                onDismissRequest = { categoryDropdownExpanded = false }
            ) {
                categories.forEach { id ->
                    DropdownMenuItem(
                        text = { Text("Category $id") },
                        onClick = {
                            selectedCategoryId.value = id
                            categoryDropdownExpanded = false
                        }
                    )
                }
            }
        }

        // Image Picker
        Button(onClick = {
            imagePickerLauncher.launch("image/*")
        }) {
            Text("Pick Images")
        }

        // Image Previews
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(selectedImages.size) { uri ->
                Image(
                    painter = rememberAsyncImagePainter(selectedImages[uri]),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Product Button
        Button(
            onClick = {
                val inventoryRequest = InventoryRequest(
                    title = title.value,
                    price = price.value.toDoubleOrNull() ?: 0.0,
                    description = description.value,
                    categoryId = selectedCategoryId.value ?: 1,
                    images = arrayListOf("https://www.autoshippers.co.uk/blog/wp-content/uploads/bugatti-centodieci.jpg")
                )
                viewModel.addInventoryItems(inventoryRequest)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            enabled = isFormValid && uiState !is UiState.Loading
        ) {
            if (uiState is UiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp),
                    color = Color.Red,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Save Product")
            }
        }

    }
}

