package com.golvia.smartflowtechandroidassessment.ui.inventory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


/**
 * davidsunday
 */

@Composable
fun SearchBar(
    onSearch: (String) -> Unit
) {
    OutlinedTextField(
        value = "",
        onValueChange = {
            onSearch(it)
        },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
        placeholder = { Text("Search...") },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
    )
}