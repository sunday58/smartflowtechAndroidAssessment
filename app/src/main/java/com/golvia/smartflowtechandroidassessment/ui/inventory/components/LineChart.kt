package com.golvia.smartflowtechandroidassessment.ui.inventory.components

/**
 * davidsunday
 */

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.golvia.smartflowtechandroidassessment.data.ChartItem
import com.golvia.smartflowtechandroidassessment.ui.theme.MaximumRed
import com.golvia.smartflowtechandroidassessment.utils.formatShortDescriptionWithDecimals

private val defaultMaxHeight = 158.dp
private val defaultYLabelWidth = 36.dp
private val defaultTopPadding = 32.dp
private val xLabelHeight = 24.dp
private val labelStyle = TextStyle(
    fontSize = 10.sp,
    lineHeight = 12.sp,
    fontWeight = FontWeight(400),
    color = Gray
)

@Composable
fun LineChart(
    items: List<ChartItem>,
    maxHeight: Dp = defaultMaxHeight,
    maxYLabelWidth: Dp = defaultYLabelWidth,
    bottomLabel: String = String()
) {
    val maxValue = maxOf(items.maxOf { it.value }, 7.0)
    val optimalMaxValueAndTicks = getOptimalMaxValue(maxValue)
    val roundUpYLabelMax = optimalMaxValueAndTicks.first

    val density = LocalDensity.current
    val strokeWidth = with(density) { 1.dp.toPx() }
    val spaceBetweenLines = with(density) { maxHeight.toPx() / 8 }

    Column(
        modifier = Modifier
            .border(
                width = 0.5.dp,
                color = LightGray,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 20.dp),
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
            LineChartContent(
                maxHeight = maxHeight,
                strokeWidth = strokeWidth,
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
                modifier = Modifier.fillMaxWidth(),
                text = bottomLabel,
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight(400),
                    color = Black,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
private fun LineChartContent(
    modifier: Modifier = Modifier,
    maxHeight: Dp,
    strokeWidth: Float,
    items: List<ChartItem>,
    maxValue: Double
) {
    val textPercentMeasure = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(maxHeight + defaultTopPadding)
    ) {
        val spaceBetweenLines = (size.width - 100) / (items.size - 1)

        drawLine(
            color = White,
            start = Offset(0f, size.height - xLabelHeight.toPx()),
            end = Offset(size.width, size.height - xLabelHeight.toPx()),
            strokeWidth = strokeWidth
        )

        //get the dot coordinates first to be able to draw the line between after and it does not hide the text
        val barPoints = mutableListOf<Offset>()
        items.forEachIndexed { index, item ->
            val barValue = item.value * 100 / maxValue
            val itemHeight = barValue * maxHeight.toPx() / 100
            val startX = index * spaceBetweenLines + 40
            val barTop = size.height - itemHeight - xLabelHeight.toPx()

            barPoints.add(Offset(startX, barTop.toFloat()))
        }
        items.forEachIndexed { index, item ->
            val barValue = item.value * 100 / maxValue
            val itemHeight = barValue * maxHeight.toPx() / 100
            val startX = index * spaceBetweenLines + 40
            val barTop = size.height - itemHeight - xLabelHeight.toPx()

            drawCircle(
                color = MaximumRed,
                center = Offset(startX, barTop.toFloat()),
                radius = 12f
            )

            if (index < items.size - 1) {
                drawLine(
                    color = MaximumRed,
                    start = barPoints[index],
                    end = barPoints[index + 1],
                    strokeWidth = 6f
                )
            }

            val leyEndValue = item.label
            val leyEndLayoutResult: TextLayoutResult =
                textPercentMeasure.measure(text = leyEndValue)

            if (item.value > 0.0) {
                val labelValue = item.value.formatShortDescriptionWithDecimals(true)
                val labelLayoutResult: TextLayoutResult =
                    textPercentMeasure.measure(text = labelValue)
                drawText(
                    textMeasurer = textPercentMeasure,
                    text = labelValue,
                    style = TextStyle(
                        background = White,
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight(400),
                        color = Black,
                        textAlign = TextAlign.Center
                    ),
                    topLeft = Offset(
                        (startX - labelLayoutResult.size.width / 2.4).toFloat(),
                        barTop.toFloat() - 60
                    )
                )
            }

            drawText(
                textMeasurer = textPercentMeasure,
                text = leyEndValue,
                style = labelStyle,
                topLeft = Offset(
                    (startX - leyEndLayoutResult.size.width / 2.4).toFloat(),
                    maxHeight.toPx() + leyEndLayoutResult.size.height
                )
            )
        }
    }
}

private fun getOptimalMaxValue(maxValue: Double): Pair<Double, Int> {
    var optMax = maxValue * 2
    var optTicks = 0
    for (i in 5..10) {
        val tmpMaxValue = bestTick(maxValue, i)
        if ((optMax > tmpMaxValue) && (tmpMaxValue > (maxValue + maxValue * 0.1))) {
            optMax = tmpMaxValue
            optTicks = i
        }
    }
    return Pair(optMax, optTicks)
}

@Preview(showBackground = true)
@Composable
fun LineChartPreview() {
    LineChart(
        items = listOf(
            ChartItem(label = "Bank Transfer", value = 4.00),
            ChartItem(label = "Segment", value = 1.00),
            ChartItem(label = "Segment", value = 2.00),
            ChartItem(label = "Segment", value = 3.00),
            ChartItem(label = "Segment", value = 0.00)
        ),
        bottomLabel = "Days"
    )
}