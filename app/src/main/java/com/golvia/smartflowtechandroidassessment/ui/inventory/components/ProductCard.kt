package com.golvia.smartflowtechandroidassessment.ui.inventory.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.golvia.smartflowtechandroidassessment.R
import com.golvia.smartflowtechandroidassessment.data.InventoryResponseItem

/**
 * davidsunday
 */

@Composable
fun ProductCard(
    inventoryItem: InventoryResponseItem,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.width(160.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick.invoke(inventoryItem.id)
            },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            val imageUrl = inventoryItem.images?.firstOrNull()
            val finalImageUrl = if (!imageUrl.isNullOrEmpty()) {
                imageUrl
            } else {
                R.drawable.ic_launcher_foreground
            }
            AsyncImage(
                model = finalImageUrl,
                contentDescription = inventoryItem.description.orEmpty(),
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Inside
            )
            Text(
                text = inventoryItem.title.orEmpty(),
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = inventoryItem.price.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = inventoryItem.creationAt.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}