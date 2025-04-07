package com.golvia.smartflowtechandroidassessment.ui.inventory.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import com.app.zenith.ui.compose.general.charts.TooltipPopup
import com.golvia.smartflowtechandroidassessment.data.ChartItem
import com.golvia.smartflowtechandroidassessment.ui.theme.MaximumRed
import com.golvia.smartflowtechandroidassessment.utils.formatShortDescriptionWithDecimals
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val defaultMaxHeight = 158.dp
private val defaultYLabelWidth = 45.dp
private val defaultTopPadding = 32.dp
private val xLabelHeight = 24.dp
private val labelStyle = TextStyle(
    fontSize = 10.sp,
    lineHeight = 12.sp,
    fontWeight = FontWeight(400),
    color = Color.Black
)

@Composable
fun DetailBarChart(
    items: List<ChartItem>,
    maxHeight: Dp = defaultMaxHeight,
    maxYLabelWidth: Dp = defaultYLabelWidth,
    bottomLabel: String = String()
) {
    val maxValue = items.maxOf { item -> item.value }
    val optimalMaxValueAndTicks = getOptimalMaxValue(maxValue)
    val roundUpYLabelMax = optimalMaxValueAndTicks.first

    val density = LocalDensity.current
    val strokeWidth = with(density) { 1.dp.toPx() }
    val barCornerRadius = with(density) { 10.dp.toPx() }
    val spaceBetweenLines = with(density) { maxHeight.toPx() / 8 }

    Column(
        modifier = Modifier
            .border(
                width = 0.5.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Box(
                modifier = Modifier
                    .width(maxYLabelWidth)
                    .height(maxHeight + defaultTopPadding)
            ) {
                val textMeasurer = rememberTextMeasurer()
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawText(
                        textMeasurer,
                        "0",
                        style = labelStyle,
                        topLeft = Offset(0f, size.height - 9.dp.toPx() - xLabelHeight.toPx())
                    )
                    var space = spaceBetweenLines
                    val valueBetweenYLabels = roundUpYLabelMax / 8
                    var valueYLabel = valueBetweenYLabels
                    if (valueYLabel > 0.0) {
                        while (space <= maxHeight.toPx()) {
                            drawYAxisLabel(textMeasurer, valueYLabel, space)
                            valueYLabel += valueBetweenYLabels
                            space += spaceBetweenLines
                        }
                    }
                }
            }
            BarChartContent(
                maxHeight = maxHeight,
                strokeWidth = strokeWidth,
                barCornerRadius = barCornerRadius,
                items = items,
                maxValue = roundUpYLabelMax
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp, start = 16.dp, end = 16.dp, top = 5.dp)
        ) {
            Spacer(modifier = Modifier.width(maxYLabelWidth))
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = bottomLabel,
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight(400),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

fun DrawScope.drawYAxisLabel(
    textMeasurer: TextMeasurer,
    valueYLabel: Double,
    space: Float
) {
    drawText(
        textMeasurer,
        valueYLabel.formatShortDescriptionWithDecimals(true),
        style = labelStyle,
        topLeft = Offset(0f, size.height - space - 5.dp.toPx() - xLabelHeight.toPx())
    )
}

@Composable
private fun BarChartContent(
    modifier: Modifier = Modifier,
    maxHeight: Dp,
    strokeWidth: Float,
    barCornerRadius: Float,
    items: List<ChartItem>,
    maxValue: Double
) {
    Row(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(maxHeight + defaultTopPadding + xLabelHeight)
                .drawBehind {
                    drawLine(
                        color = Color.White,
                        start = Offset(0f, size.height - xLabelHeight.toPx()),
                        end = Offset(size.width, size.height - xLabelHeight.toPx()),
                        strokeWidth = strokeWidth
                    )
                }
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        val selectedBar: MutableState<Int?> = remember { mutableStateOf(null) }
        var index = 0
        items.forEach { item ->
            Bar(
                item = item,
                value = item.value * 100 / maxValue,
                maxHeight = maxHeight,
                cornerRadius = barCornerRadius,
                selectedBar = selectedBar,
                index = index
            )
            index++
        }
    }
}

@Composable
private fun RowScope.Bar(
    item: ChartItem,
    value: Double,
    maxHeight: Dp,
    cornerRadius: Float,
    selectedBar: MutableState<Int?>,
    index: Int
) {
    val itemHeight = remember(value) { value * maxHeight.value / 100 }

    TooltipPopup(
        modifier = Modifier,
        requesterView = { modifier ->
            Column(
                modifier = modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (item.value > 0) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = item.value.formatShortDescriptionWithDecimals(false),
                        style = TextStyle(
                            fontSize = 10.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight(400),
                            color = Color.Black
                        )
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .height(itemHeight.dp)
                        .background(
                            color = if (selectedBar.value == index) MaximumRed else Color.Red,
                            shape = RoundedCornerShape(cornerRadius)
                        )
                )
                Box(
                    modifier = Modifier.height(xLabelHeight),
                    contentAlignment = Alignment.Center
                ) {
                    ChartAxisLabelText(text = item.label)
                }
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

fun bestTick(maxValue: Double, mostTicks: Int = 5): Double {
    val minimum = maxValue / mostTicks
    val magnitude = 10.0.pow(floor(ln(minimum) / ln(10.0)))
    val residual = minimum / magnitude
    val tick = if (residual > 5) 10 * magnitude
    else if (residual > 2) 5 * magnitude
    else if (residual > 1) 2 * magnitude
    else magnitude
    return (tick * mostTicks)
}

@Composable
fun ChartAxisLabelText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Gray,
    textAlign: TextAlign = TextAlign.Center,
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        style =
        TextStyle(
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight(400),
            color = color,
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun DetailBarChartPreview() {
    DetailBarChart(
        items = listOf(
            ChartItem(label = "Bank Transfer", value = 300000.00),
            ChartItem(label = "Segment", value = 100000.00),
            ChartItem(label = "Segment", value = 0.00),
            ChartItem(label = "Segment", value = 50.00),
            ChartItem(label = "Segment", value = 25.00)
        ),
        bottomLabel = "Days"
    )
}