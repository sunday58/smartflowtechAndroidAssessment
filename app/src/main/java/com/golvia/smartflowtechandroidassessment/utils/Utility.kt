package com.golvia.smartflowtechandroidassessment.utils

import androidx.compose.ui.text.AnnotatedString
import com.golvia.smartflowtechandroidassessment.data.ChartItem
import com.golvia.smartflowtechandroidassessment.data.InventoryResponse
import com.golvia.smartflowtechandroidassessment.data.InventoryResponseItem
import org.threeten.bp.format.DateTimeFormatter
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import org.threeten.bp.ZonedDateTime

/**
 * davidsunday
 */

fun Double.formatShortDescriptionWithDecimals(showAsInt: Boolean): String {
    val decimalFormat = DecimalFormat("##0.00")
    decimalFormat.roundingMode = RoundingMode.DOWN
    return when {
        this >= 1_000_000_000_000 -> {
            decimalFormat.format(this / 1_000_000_000_000) + "T"
        }
        this >= 1_000_000_000 -> {
            decimalFormat.format(this / 1_000_000_000) + "B"
        }
        this >= 1_000_000 -> {
            decimalFormat.format(this / 1_000_000) + "M"
        }
        this >= 1_000 -> {
            decimalFormat.format(this / 1_000) + "K"
        }
        else -> {
            if (showAsInt) {
                decimalFormat.format(this).dropLast(3)
            } else {
                decimalFormat.format(this)
            }
        }
    }
}

fun convertAnalyticsSendMoneyToChart(data: List<InventoryResponseItem>?): List<ChartItem>? {
    return data
        ?.filter { (it.price ?: 0.0) > 0 }
        ?.sortedByDescending { it.price }
        ?.take(9)
        ?.map {
            ChartItem(
                label = it.title?.take(15) ?: "Other",
                value = it.price ?: 0.0
            )
        }
}

fun convertSalesOverTimeToChart(
    item: List<InventoryResponseItem>,
): List<ChartItem>? {
    return item?.filter { (it.price ?: 0.0) > 0 }
        ?.sortedByDescending { it.price }
        ?.take(8)?.map {
        ChartItem(
            label = it.title?.take(5).orEmpty(),
            value = it.price ?: 0.0
        )
    }
}

fun AnnotatedString.formatDecimalText(): String {
    val value = this.text.toDoubleOrNull()
    return if (value != null) {
        (value / 100).formatWithComma()
    } else {
        "0.00"
    }
}

fun Double?.formatWithComma(): String {
    val numberFormat = DecimalFormat("#,##0.00")
    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ','
        decimalSeparator = '.'
    }
    numberFormat.decimalFormatSymbols = symbols
    return numberFormat.format(this ?: 0)
}

fun formatDate(dateString: String): String {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val zonedDateTime = ZonedDateTime.parse(dateString, formatter)

    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return zonedDateTime.format(outputFormatter)
}
