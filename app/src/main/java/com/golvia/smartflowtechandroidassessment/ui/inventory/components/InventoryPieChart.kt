package com.golvia.smartflowtechandroidassessment.ui.inventory.components

/**
 * davidsunday
 */


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.golvia.smartflowtechandroidassessment.data.ChartItem
import com.golvia.smartflowtechandroidassessment.ui.theme.BrownRed
import com.golvia.smartflowtechandroidassessment.ui.theme.DarkRed
import com.golvia.smartflowtechandroidassessment.ui.theme.KellyGreen
import com.golvia.smartflowtechandroidassessment.ui.theme.MaximumRed
import com.golvia.smartflowtechandroidassessment.ui.theme.Melon
import com.golvia.smartflowtechandroidassessment.utils.formatShortDescriptionWithDecimals
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChart(segments: List<ChartItem>, modifier: Modifier = Modifier,
             showAsInt: Boolean,
             isFromProduct: Boolean = false,
             duration: String? = null) {
    val textPercentMeasure = rememberTextMeasurer()
    Box(
        modifier = Modifier
            .background(color = White)
            .border(
                width = 0.5.dp,
                color = LightGray,
                shape = RoundedCornerShape(10.dp)
            )
            .wrapContentSize()
    ) {
        Row(
            modifier = Modifier
                .background(color = White)
                .border(
                    width = 0.5.dp,
                    color = LightGray,
                    shape = RoundedCornerShape(10.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = modifier
                    .weight(0.7f)
            ) {
                Canvas(
                    modifier = Modifier.matchParentSize()
                ) {
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val radius = (size.minDimension / 3.2).toFloat() // Adjust the radius of the donut
                    val strokeWidth = radius / 1.5 // Adjust the thickness of the donut segments

                    var currentAngle = -90f

                    //first we draw the chart and then fill it with text so text is always visible
                    segments.forEachIndexed { index, segment ->
                        val sweepAngle = (segment.value / segments.sumOf { it.value }.toFloat()) * 360f
                        val gapAdjustment = 1.2f // Adjust as needed to remove gaps
                        val adjustedSweepAngle = sweepAngle + gapAdjustment

                        // Assign colors based on index
                        val color = if (isFromProduct) getProductColorForIndex(index) else getColorForIndex(index)

                        // Draw the segment
                        drawArc(
                            color = color,
                            startAngle = currentAngle,
                            sweepAngle = adjustedSweepAngle.toFloat(),
                            useCenter = false,
                            topLeft = Offset(centerX - radius, centerY - radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(strokeWidth.toFloat())
                        )
                        currentAngle += sweepAngle.toFloat()
                    }
                    segments.forEachIndexed { _, segment ->
                        val sweepAngle = (segment.value / segments.sumOf { it.value }.toFloat()) * 360f

                        // If the segment value is less than 5% of the sum, skip drawing text
                        if (segment.value / segments.sumOf { it.value } < 0.05) {
                            currentAngle += sweepAngle.toFloat()
                            return@forEachIndexed
                        }
                        // Calculate the midpoint of the arc
                        val arcMidAngle = currentAngle + sweepAngle / 2
                        val arcMidX = centerX + radius * cos(Math.toRadians(arcMidAngle)).toFloat()
                        val arcMidY = centerY + radius * sin(Math.toRadians(arcMidAngle)).toFloat()

                        val displayText = segment.value.formatShortDescriptionWithDecimals(showAsInt)

                        val textLayoutResult: TextLayoutResult =
                            textPercentMeasure.measure(text = displayText)
                        val textSize = textLayoutResult.size

                        // Draw the value text centered at the midpoint of the arc
                        drawText(
                            textMeasurer = textPercentMeasure,
                            text = displayText,
                            style = TextStyle(
                                fontSize = 10.sp,
                                lineHeight = 18.sp,
                                fontWeight = FontWeight(500),
                                color = White
                            ),
                            topLeft = Offset(
                                arcMidX - textSize.width / 2 + 8,
                                arcMidY - textSize.height / 2
                            )
                        )
                        currentAngle += sweepAngle.toFloat()
                    }
                }
            }
            // Draw legend

            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(vertical = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                segments.forEachIndexed { index, segment ->
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isFromProduct) getProductColorForIndex(index) else getColorForIndex(index),
                                    shape = RoundedCornerShape(3.dp)
                                )
                                .size(10.dp)
                        )
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = segment.label,
                            style = TextStyle(
                                fontSize = 10.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight(400),
                                color = Black,
                                textAlign = TextAlign.Center,
                            )
                        )
                    }
                }
            }

        }

        duration?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Gray,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .wrapContentSize()
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 8.dp)
                                .background(
                                    color = Gray,
                                    shape = RoundedCornerShape(3.dp)
                                )
                                .size(6.dp)
                        )

                        Text(
                            modifier = Modifier.padding(
                                top = 6.dp,
                                bottom = 6.dp,
                                end = 8.dp,
                                start = 6.dp
                            ),
                            text = it,
                            style = TextStyle(
                                fontSize = 10.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight(400),
                                color = Black,
                                textAlign = TextAlign.Center,
                            )
                        )
                    }
                }
            }
        }
    }
}

fun getColorForIndex(index: Int): Color {
    val colors = listOf(
        BrownRed,
        DarkRed,
        MaximumRed,
        KellyGreen,
        Melon,
        Gray,
        Green,
        LightGray,
        Green
    )
    val lastIndex = colors.size - 1
    val adjustedIndex = if (index > lastIndex) lastIndex - 1 else index
    return colors[adjustedIndex]
}

fun getProductColorForIndex(index: Int): Color {
    val colors = listOf(
        BrownRed,
        DarkRed,
        DarkRed,
        MaximumRed,
        KellyGreen,
        Melon,
        Gray,
    )
    val lastIndex = colors.size - 1
    val adjustedIndex = if (index > lastIndex) lastIndex - 1 else index
    return colors[adjustedIndex]
}

@Preview
@Composable
fun PieChartPreview() {
    val data = listOf(
        ChartItem(label = "Bank Transfer", value = 990.00),
        ChartItem(label = "Segment", value = 100.00),
        ChartItem(label = "Segment", value = 100.00),
        ChartItem(label = "Segment", value = 50.00),
        ChartItem(label = "Segment", value = 25.00),
    )

    PieChart(
        segments = data,
        modifier = Modifier
            .fillMaxWidth()
            .height(212.dp),
        showAsInt = false
    )
}