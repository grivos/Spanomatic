package com.grivos.spanomatic

import android.content.Context
import android.graphics.Typeface
import android.text.style.*
import com.grivos.spanomatic.spans.CustomTypefaceSpan
import com.grivos.spanomatic.spans.ListenableClickableSpan
import com.grivos.spanomatic.utils.parseAnnotationColor
import com.grivos.spanomatic.utils.parseAnnotationDimension

internal val clickHandler = { annotationValue: String, _: Context ->
    ListenableClickableSpan(annotationValue)
}

internal val fgColorHandler = { annotationValue: String, context: Context ->
    val color = parseAnnotationColor(annotationValue, context)
    ForegroundColorSpan(color)
}

internal val bgColorHandler = { annotationValue: String, context: Context ->
    val color = parseAnnotationColor(annotationValue, context)
    BackgroundColorSpan(color)
}

internal val drawableHandler = { annotationValue: String, context: Context ->
    val split = annotationValue.split(ANNOTATION_VALUE_DELIMITER)
    val verticalAlignment = if (split.size == 2) {
        when (val alignmentKey = split[1]) {
            KEY_DRAWABLE_ALIGN_BASELINE -> ImageSpan.ALIGN_BASELINE
            KEY_DRAWABLE_ALIGN_BOTTOM -> ImageSpan.ALIGN_BOTTOM
            else -> throw IllegalAccessException("Unknown vertical alignment: $alignmentKey")
        }
    } else {
        // default value
        ImageSpan.ALIGN_BASELINE
    }
    val drawableResId = context.resources.getIdentifier(split[0], null, context.packageName)
    ImageSpan(
        context, drawableResId,
        verticalAlignment
    )
}

internal val relativeTextSizeHandler = { annotationValue: String, _: Context ->
    RelativeSizeSpan(annotationValue.toFloat())
}

internal val absoluteTextSizeHandler = { annotationValue: String, context: Context ->
    val pixelSize = parseAnnotationDimension(annotationValue, context)
    AbsoluteSizeSpan(pixelSize)
}

@Suppress("IMPLICIT_CAST_TO_ANY")
internal val formatHandler = { annotationValue: String, _: Context ->
    when (annotationValue) {
        KEY_FORMAT_STRIKETHROUGH -> StrikethroughSpan()
        KEY_FORMAT_BOLD -> StyleSpan(Typeface.BOLD)
        KEY_FORMAT_ITALIC -> StyleSpan(Typeface.ITALIC)
        KEY_FORMAT_BOLD_ITALIC -> StyleSpan(Typeface.BOLD_ITALIC)
        KEY_FORMAT_SUPERSCRIPT -> SuperscriptSpan()
        KEY_FORMAT_SUBSCRIPT -> SubscriptSpan()
        KEY_FORMAT_UNDERLINE -> UnderlineSpan()
        else -> throw IllegalAccessException("Unknown format: $annotationValue")
    }
}

internal val quoteHandler = { annotationValue: String, context: Context ->
    if (annotationValue.isEmpty()) {
        QuoteSpan()
    } else {
        val color = parseAnnotationColor(annotationValue, context)
        QuoteSpan(color)
    }
}

internal val bulletHandler = { annotationValue: String, context: Context ->
    if (annotationValue.isEmpty()) {
        BulletSpan()
    } else {
        val split = annotationValue.split(ANNOTATION_VALUE_DELIMITER)
        val gapWidth = parseAnnotationDimension(split[0], context)
        if (split.size == 2) {
            val color = parseAnnotationColor(split[1], context)
            BulletSpan(gapWidth, color)
        } else {
            BulletSpan(gapWidth)
        }
    }
}

internal val urlHandler = { annotationValue: String, _: Context ->
    URLSpan(annotationValue)
}

internal val typefaceHandler = { annotationValue: String, context: Context ->
    val provider = Spanomatic.typefaceProvider
    if (provider == null) {
        throw IllegalAccessException("No typeface provider found")
    } else {
        val typefaceKey = if (annotationValue.startsWith(ANNOTATION_VALUE_STRING_RES_PREFIX)) {
            val id = context.resources.getIdentifier(annotationValue, null, context.packageName)
            context.getString(id)
        } else {
            annotationValue
        }
        val typeface = provider.getTypeface(typefaceKey)
        if (typeface == null) {
            throw IllegalAccessException("Unknown typeface: $annotationValue")
        } else {
            CustomTypefaceSpan(typeface)
        }
    }
}

internal val leadingMarginHandler = { annotationValue: String, context: Context ->
    val pixelSize = parseAnnotationDimension(annotationValue, context)
    LeadingMarginSpan.Standard(pixelSize)
}

private const val ANNOTATION_VALUE_DELIMITER = "|"
private const val ANNOTATION_VALUE_STRING_RES_PREFIX = "@string"

private const val KEY_FORMAT_STRIKETHROUGH = "strikethrough"
private const val KEY_FORMAT_BOLD = "bold"
private const val KEY_FORMAT_ITALIC = "italic"
private const val KEY_FORMAT_BOLD_ITALIC = "boldItalic"
private const val KEY_FORMAT_SUPERSCRIPT = "superscript"
private const val KEY_FORMAT_SUBSCRIPT = "subscript"
private const val KEY_FORMAT_UNDERLINE = "underline"

private const val KEY_DRAWABLE_ALIGN_BASELINE = "baseline"
private const val KEY_DRAWABLE_ALIGN_BOTTOM = "bottom"