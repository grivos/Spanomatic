package com.grivos.spanomatic

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned


object Spanomatic {

    private val annotationHandlers = mutableMapOf<String, (annotationValue: String, context: Context) -> Any>()
    @JvmStatic
    var typefaceProvider: TypefaceProvider? = null

    init {
        setAnnotationSpanHandler(ANNOTATION_KEY_CLICK, clickHandler)
        setAnnotationSpanHandler(ANNOTATION_KEY_FOREGROUND_COLOR, fgColorHandler)
        setAnnotationSpanHandler(ANNOTATION_KEY_BACKGROUND_COLOR, bgColorHandler)
        setAnnotationSpanHandler(ANNOTATION_KEY_DRAWABLE, drawableHandler)
        setAnnotationSpanHandler(ANNOTATION_KEY_TEXT_SIZE_RELATIVE, relativeTextSizeHandler)
        setAnnotationSpanHandler(ANNOTATION_KEY_TEXT_SIZE_ABSOLUTE, absoluteTextSizeHandler)
        setAnnotationSpanHandler(ANNOTATION_KEY_FORMAT, formatHandler)
        setAnnotationSpanHandler(ANNOTATION_KEY_QUOTE, quoteHandler)
        setAnnotationSpanHandler(ANNOTATION_KEY_BULLET, bulletHandler)
        setAnnotationSpanHandler(ANNOTATION_KEY_URL, urlHandler)
        setAnnotationSpanHandler(ANNOTATION_KEY_TYPEFACE, typefaceHandler)
        setAnnotationSpanHandler(ANNOTATION_KEY_LEADING_MARGIN, leadingMarginHandler)
    }

    @JvmStatic
    fun addSpansFromAnnotations(charSequence: CharSequence?, context: Context): CharSequence? {
        if (charSequence !is Spanned) return charSequence
        val annotations = charSequence.getSpans(0, charSequence.length, android.text.Annotation::class.java)
        val spannableString = SpannableString(charSequence)
        for (annotation in annotations) {
            annotationHandlers[annotation.key]
                ?.invoke(annotation.value, context)
                ?.let { span ->
                    spannableString.setSpan(
                        span,
                        charSequence.getSpanStart(annotation),
                        charSequence.getSpanEnd(annotation),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
        }
        return spannableString
    }

    @JvmStatic
    fun setAnnotationSpanHandler(
        annotationKey: String,
        handler: (annotationValue: String, context: Context) -> Any
    ): Spanomatic {
        return apply {
            annotationHandlers[annotationKey] = handler
        }
    }
}

private const val ANNOTATION_KEY_CLICK = "click"
private const val ANNOTATION_KEY_FOREGROUND_COLOR = "fgColor"
private const val ANNOTATION_KEY_BACKGROUND_COLOR = "bgColor"
private const val ANNOTATION_KEY_DRAWABLE = "drawable"
private const val ANNOTATION_KEY_TEXT_SIZE_RELATIVE = "relativeTextSize"
private const val ANNOTATION_KEY_TEXT_SIZE_ABSOLUTE = "absoluteTextSize"
private const val ANNOTATION_KEY_FORMAT = "format"
private const val ANNOTATION_KEY_QUOTE = "quote"
private const val ANNOTATION_KEY_BULLET = "bullet"
private const val ANNOTATION_KEY_URL = "url"
private const val ANNOTATION_KEY_TYPEFACE = "typeface"
private const val ANNOTATION_KEY_LEADING_MARGIN = "leadingMargin"

