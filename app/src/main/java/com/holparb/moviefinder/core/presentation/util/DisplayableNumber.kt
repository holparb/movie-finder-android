package com.holparb.moviefinder.core.presentation.util

import android.icu.text.NumberFormat
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class DisplayableNumber(
    val value: Double,
    val formattedValue: String
): Parcelable

fun Double.toDisplayableNumber(): DisplayableNumber {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 1
    }
    return DisplayableNumber(
        value = this,
        formattedValue = formatter.format(this)
    )
}