package com.golvia.smartflowtechandroidassessment.ui.inventory.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.golvia.smartflowtechandroidassessment.R

/**
 * davidsunday
 */

@Composable
fun AnalyticsEmptyField(
    modifier: Modifier,
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .background(color = White)
        .border(
            width = 0.5.dp,
            color = LightGray,
            shape = RoundedCornerShape(10.dp)
        ),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Image(
        modifier = Modifier,
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = null
    )
    Text(
        modifier = Modifier
            .padding(12.dp),
        text = "No data",
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 18.sp,
            fontFamily = FontFamily(),
            fontWeight = FontWeight(500),
            color = Black
        )
    )
    Text(
        text = "There is currently no data to display",
        style = TextStyle(
            fontSize = 14.sp,
            lineHeight = 18.sp,
            fontFamily = FontFamily(),
            fontWeight = FontWeight(400),
            color = Black
        )
    )
}