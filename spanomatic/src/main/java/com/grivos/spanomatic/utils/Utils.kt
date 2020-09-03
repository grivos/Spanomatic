package com.grivos.spanomatic.utils

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import android.util.TypedValue

fun parseAnnotationColor(colorValue: String, context: Context): Int {
    return when {
        colorValue.startsWith(ANNOTATION_VALUE_COLOR_RES_PREFIX) -> {
            val id = context.resources.getIdentifier(colorValue, null, context.packageName)
            ContextCompat.getColor(context, id)
        }
        else -> Color.parseColor(colorValue)
    }
}

fun parseAnnotationDimension(rawSize: String, context: Context): Int {
    return when {
        rawSize.endsWith(ANNOTATION_VALUE_DIMEN_DP_SUFFIX) -> {
            val size = rawSize.substringBefore(ANNOTATION_VALUE_DIMEN_DP_SUFFIX).toFloat()
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.resources.displayMetrics).toInt()
        }
        rawSize.endsWith(ANNOTATION_VALUE_DIMEN_SP_SUFFIX) -> {
            val size = rawSize.substringBefore(ANNOTATION_VALUE_DIMEN_SP_SUFFIX).toFloat()
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, context.resources.displayMetrics).toInt()
        }
        rawSize.endsWith(ANNOTATION_VALUE_DIMEN_PT_SUFFIX) -> {
            val size = rawSize.substringBefore(ANNOTATION_VALUE_DIMEN_PT_SUFFIX).toFloat()
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, size, context.resources.displayMetrics).toInt()
        }
        rawSize.endsWith(ANNOTATION_VALUE_DIMEN_PX_SUFFIX) -> {
            rawSize.substringBefore(ANNOTATION_VALUE_DIMEN_PX_SUFFIX).toInt()
        }
        rawSize.endsWith(ANNOTATION_VALUE_DIMEN_MM_SUFFIX) -> {
            val size = rawSize.substringBefore(ANNOTATION_VALUE_DIMEN_MM_SUFFIX).toFloat()
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, size, context.resources.displayMetrics).toInt()
        }
        rawSize.endsWith(ANNOTATION_VALUE_DIMEN_IN_SUFFIX) -> {
            val size = rawSize.substringBefore(ANNOTATION_VALUE_DIMEN_IN_SUFFIX).toFloat()
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, size, context.resources.displayMetrics).toInt()
        }
        rawSize.startsWith(ANNOTATION_VALUE_DIMEN_RES_PREFIX) -> {
            val id = context.resources.getIdentifier(rawSize, null, context.packageName)
            context.resources.getDimensionPixelSize(id)
        }
        else -> throw IllegalAccessException("Unknown size: $rawSize")
    }
}

private const val ANNOTATION_VALUE_COLOR_RES_PREFIX = "@color"
private const val ANNOTATION_VALUE_DIMEN_RES_PREFIX = "@dimen"

private const val ANNOTATION_VALUE_DIMEN_DP_SUFFIX = "dp"
private const val ANNOTATION_VALUE_DIMEN_SP_SUFFIX = "sp"
private const val ANNOTATION_VALUE_DIMEN_PT_SUFFIX = "pt"
private const val ANNOTATION_VALUE_DIMEN_PX_SUFFIX = "px"
private const val ANNOTATION_VALUE_DIMEN_MM_SUFFIX = "mm"
private const val ANNOTATION_VALUE_DIMEN_IN_SUFFIX = "in"