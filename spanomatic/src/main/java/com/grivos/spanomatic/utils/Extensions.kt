package com.grivos.spanomatic.utils

import android.content.Context
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.text.SpannableString
import android.text.SpannedString
import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.grivos.spanomatic.PlaceholderFormatter
import com.grivos.spanomatic.Spanomatic.addSpansFromAnnotations
import com.grivos.spanomatic.spans.ListenableClickableSpan

fun TextView.addSpanClickListener(
    clickName: String,
    addLinkMovementMethod: Boolean = true,
    callback: (clickName: String) -> Unit
): TextView {
    return apply {
        if (movementMethod == null && addLinkMovementMethod) {
            movementMethod = LinkMovementMethod.getInstance()
        }
        val text = text
        if (text is SpannableString) {
            text.getSpans(0, text.length, ListenableClickableSpan::class.java)
                .first { clickableSpan -> clickableSpan.clickName == clickName }
                .let { clickableSpan ->
                    clickableSpan.listener = object : ListenableClickableSpan.Listener {
                        override fun onClick(clickName: String) {
                            callback(clickName)
                        }

                    }
                }
        }
    }
}

fun Context.addSpansFromAnnotations(@StringRes id: Int): CharSequence? = addSpansFromAnnotations(getText(id), this)

fun Fragment.addSpansFromAnnotations(@StringRes id: Int): CharSequence? = context?.addSpansFromAnnotations(id)

fun Context.addSpansFromAnnotations(@StringRes id: Int, vararg formatArgs: Any): CharSequence? {
    val text = getText(id)
    return if (text is SpannedString) {
        val replaced = PlaceholderFormatter.format(text, *formatArgs)
        addSpansFromAnnotations(replaced, this)
    } else text
}

fun Fragment.addSpansFromAnnotations(@StringRes id: Int, vararg formatArgs: Any): CharSequence? =
    context?.addSpansFromAnnotations(id, formatArgs)