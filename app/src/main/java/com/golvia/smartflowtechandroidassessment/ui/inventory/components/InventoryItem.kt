package com.golvia.smartflowtechandroidassessment.ui.inventory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.golvia.smartflowtechandroidassessment.R
import com.golvia.smartflowtechandroidassessment.data.ChartItem
import com.golvia.smartflowtechandroidassessment.data.InventoryResponseItem
import com.golvia.smartflowtechandroidassessment.utils.convertAnalyticsSendMoneyToChart
import com.golvia.smartflowtechandroidassessment.utils.convertSalesOverTimeToChart
import java.text.NumberFormat.Field.CURRENCY

/**
 * davidsunday
 */

@Composable
fun InventoryItem(
    inventoryItem: List<InventoryResponseItem>,
    onSearch: (String) -> Unit,
    onItemClick: (Int) -> Unit,
    onFabClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            SearchBar(
                onSearch = onSearch
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.inventory_list),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(end = 16.dp)
            ) {
                items(inventoryItem.size) { item ->
                    ProductCard(
                        inventoryItem = inventoryItem[item],
                        onClick = { onItemClick(inventoryItem[item].id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .verticalScroll( rememberScrollState() ),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                AnalyticsPieChart(
                    title = stringResource(R.string.sales, CURRENCY),
                    dataList = convertAnalyticsSendMoneyToChart(inventoryItem),
                    showAsInt = false
                )

                Spacer(modifier = Modifier.height(8.dp))

                AnalyticsBarChart(
                    title = stringResource(R.string.sales, CURRENCY),
                    dataList = convertSalesOverTimeToChart(
                        inventoryItem,
                    ),
                    bottomLabel = "Items"
                )

                Spacer(modifier = Modifier.height(8.dp))

                AnalyticsLineChart(
                    title = stringResource(R.string.sales),
                    dataList = convertSalesOverTimeToChart(
                        inventoryItem,
                    ),
                    bottomLabel = stringResource(R.string.sales)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        FloatingActionButton(
            onClick = onFabClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_item))
        }
    }
}

@Composable
fun AnalyticsPieChart(
    title: String,
    dataList: List<ChartItem>?,
    showAsInt: Boolean,
    duration: String? = null,
    isFromProduct: Boolean = false
) = Column {
    Text(
        modifier = Modifier.padding(start = 8.dp, bottom = 16.dp),
        text = title,
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight(500),
            color = Color.Black
        )
    )
    if (!dataList.isNullOrEmpty()) {
        PieChart(
            segments = dataList,
            modifier = Modifier
                .fillMaxWidth()
                .height(212.dp),
            showAsInt = showAsInt,
            duration = duration,
            isFromProduct = isFromProduct
        )
    } else {
        AnalyticsEmptyField(Modifier.height(212.dp))
    }
}

@Composable
fun AnalyticsBarChart(
    title: String,
    dataList: List<ChartItem>?,
    bottomLabel: String,
) = Column {
    Text(
        modifier = Modifier
            .padding(bottom = 24.dp),
        text = title,
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight(500),
            color = Color.Black
        )
    )
    if (!dataList.isNullOrEmpty() && !dataList.all { it.value == 0.0 }) {
        DetailBarChart(
            items = dataList,
            bottomLabel = bottomLabel
        )
    } else {
        AnalyticsEmptyField(Modifier.height(212.dp))
    }
}

@Composable
fun AnalyticsLineChart(
    title: String,
    dataList: List<ChartItem>?,
    bottomLabel: String,
) = Column {
    Text(
        modifier = Modifier
            .padding(bottom = 24.dp),
        text = title,
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight(500),
            color = Color.Black
        )
    )
    if (!dataList.isNullOrEmpty() && !dataList.all { it.value == 0.0 }) {
        LineChart(
            items = dataList,
            bottomLabel = bottomLabel
        )
    } else {
        AnalyticsEmptyField(Modifier.height(212.dp))
    }
}


