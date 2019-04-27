/**
 * This class was adapted from a library by Mark Allison:
 * https://github.com/StylingAndroid/Rialto/
 */
package com.grivos.spanomatic

import android.text.SpannableStringBuilder
import android.text.SpannedString
import java.util.regex.Pattern

/**
 * A utility class that helps formatting string resources while preserving annotations
 */
internal object PlaceholderFormatter {
    private const val index = "(\\d+\\\$)"
    private const val width = "(\\d+)?"
    private const val precision = "(\\.\\d+)?"
    private const val general = "(-)?($width[bBhHsScC])"
    private const val character = "(-)?($width[cC])"
    private const val decimal = "(([-+ 0,(]*)?${width}d)"
    private const val integral = "(([-#+ 0(]*)?$width[oxX])"
    private const val floatingPoint = "(([-#+ 0(]*)?$width$precision[eEfgG])"
    private const val floatingPointHex = "(([-#+ ]*)?$width$precision[aA])"
    private const val dateTime = "(([-]*)?$width[tT][HIklMSLNpzZsQBbhAaCYyjmdeRTrDFc])"
    private const val percent = "(-?$width%)"
    private const val lineSeparator = "(n)"
    private val pattern = Pattern.compile(
        @Suppress("MaxLineLength")
        "%$index($general|$character|$decimal|$integral|$floatingPoint|$floatingPointHex|$dateTime|$percent|$lineSeparator)"
    )

    fun format(spanned: SpannedString, vararg formatArgs: Any?): CharSequence {
        val builder = SpannableStringBuilder(spanned)
        pattern.matcher(builder).apply {
            while (find()) {
                String.format(builder.substring(start() until end()), *formatArgs)
                    .also { replacement ->
                        builder.replace(start(), end(), replacement)
                    }
                reset(builder)
            }
        }
        return builder
    }
}