package com.golvia.smartflowtechandroidassessment.ui.inventory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.zenith.ui.compose.general.charts.TooltipPopup
import com.golvia.smartflowtechandroidassessment.data.ChartItem
import com.golvia.smartflowtechandroidassessment.ui.theme.MaximumRed
import com.golvia.smartflowtechandroidassessment.ui.theme.PinkDark
import com.golvia.smartflowtechandroidassessment.ui.theme.SilverSand
import com.golvia.smartflowtechandroidassessment.utils.formatShortDescriptionWithDecimals


/**
 * davidsunday
 */

private val defaultMaxHeight = 450.dp
private val defaultYLabelWidth = 40.dp
private val defaultTopPadding = 32.dp
private val xLabelHeight = 24.dp
private val labelStyle = TextStyle(
    fontSize = 10.sp,
    lineHeight = 12.sp,
    fontWeight = FontWeight(400),
    color = Color.Gray
)


@Composable
fun DetailHorizontalBarChart(
    items: List<ChartItem>,
    maxWidth: Dp = defaultMaxHeight,
    leftLabel: String = "Product Name"
) {
    val maxValue = items.maxOf { it.value }
    val roundUpXLabelMax = ((maxValue + 100) / 100) * 100

    Row(
        modifier = Modifier
            .border(0.5.dp, LightGray, RoundedCornerShape(10.dp))
            .padding(start = 4.dp, top = 16.dp, bottom = 16.dp, end = 16.dp)
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.padding(start = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            items.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.wrapContentSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Box(
                            modifier = Modifier
                                .height(30.dp)
                                .fillMaxWidth(fraction = (item.value / roundUpXLabelMax).toFloat())
                                .background(PinkDark, RoundedCornerShape(8.dp)),
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 8.dp),
                                text = if (item.label.length > 50) "${item.label.take(50)}…" else item.label,
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight(400),
                                    color = Black
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = item.value.formatShortDescriptionWithDecimals(true),
                                modifier = Modifier
                                    .padding(start = 16.dp),
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight(400),
                                    color = Gray
                                )
                            )
                        }

                    }
                }
            }

            HorizontalDivider(color = SilverSand, thickness = 0.5.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val steps = 5
                val stepValue = roundUpXLabelMax / steps

                (0..steps).forEach { step ->
                    Text(
                        text = (step * stepValue).formatShortDescriptionWithDecimals(true),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Gray
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun HorizontalBarChartContent(
    modifier: Modifier = Modifier,
    maxWidth: Dp,
    strokeWidth: Float,
    barCornerRadius: Float,
    items: List<ChartItem>,
    maxValue: Double
) {
    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxHeight()
                .width(maxWidth + xLabelHeight + defaultYLabelWidth)
                .drawBehind {
                    drawLine(
                        color = White,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                }
        ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        val selectedBar: MutableState<Int?> = remember { mutableStateOf(null) }
        var index = 0
        items.forEach { item ->
            HorizontalBar(
                item = item,
                value = item.value * 100 / maxValue,
                maxWidth = maxWidth,
                cornerRadius = barCornerRadius,
                selectedBar = selectedBar,
                index = index
            )
            index++
        }
    }
}

@Composable
private fun HorizontalBar(
    item: ChartItem,
    value: Double,
    maxWidth: Dp,
    cornerRadius: Float,
    selectedBar: MutableState<Int?>,
    index: Int
) {
    val itemWidth = remember(value) { value * maxWidth.value / 100 }

    TooltipPopup(
        modifier = Modifier,
        requesterView = { modifier ->
            Row(
                modifier = modifier.height(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(itemWidth.dp)
                        .padding(horizontal = 10.dp)
                        .height(10.dp)
                        .background(
                            color = if (selectedBar.value == index) MaximumRed else PinkDark,
                            shape = RoundedCornerShape(cornerRadius)
                        )
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = item.value.formatShortDescriptionWithDecimals(false),
                    style = TextStyle(
                        fontSize = 10.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight(400),
                        color = Black
                    )
                )
            }
        },
        requestViewOnClick = {

        },
        tooltipContent = {

        }
    )
}

private fun getOptimalMaxValue(maxValue: Double): Pair<Double, Int> {
    var optMax = maxValue * 2
    var optTicks = 0
    for (i in 5..10) {
        val tmpMaxValue = bestTick(maxValue, i)
        if ((optMax > tmpMaxValue) && (tmpMaxValue > (maxValue + maxValue * 0.05))) {
            optMax = tmpMaxValue
            optTicks = i
        }
    }
    return Pair(optMax, optTicks)
}

@Preview(showBackground = true)
@Composable
fun DetailBarChartHorizontalPreview() {
    DetailHorizontalBarChart(
        items = listOf(
            ChartItem(label = "Bank Transfer", value = 300000.00),
            ChartItem(label = "Segment", value = 100000.00),
            ChartItem(label = "Segment", value = 0.00),
            ChartItem(label = "Segment", value = 50.00),
            ChartItem(label = "Segment", value = 25.00),
            ChartItem(label = "Bank Transfer", value = 300000.00),
            ChartItem(label = "Segment", value = 100000.50),
            ChartItem(label = "Segment", value = 500000.00),
            ChartItem(label = "Segment", value = 50.00),
            ChartItem(label = "Segment", value = 25.00)
        )
    )
}