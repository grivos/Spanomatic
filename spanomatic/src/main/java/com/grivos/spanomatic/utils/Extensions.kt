package com.grivos.spanomatic.utils

import android.content.Context
import android.text.SpannableString
import android.text.SpannedString
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.grivos.spanomatic.PlaceholderFormatter
import com.grivos.spanomatic.Spanomatic
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
        Spanomatic.addSpansFromAnnotations(replaced, this)
    } else {
        getString(id, formatArgs)
    }
}

fun Context.addSpansFromAnnotations(text: CharSequence, vararg formatArgs: Any): CharSequence? {
    return if (text is SpannedString) {
        val replaced = PlaceholderFormatter.format(text, *formatArgs)
        Spanomatic.addSpansFromAnnotations(replaced, this)
    } else {
        text.toString().format(*formatArgs)
    }
}

fun Fragment.addSpansFromAnnotations(@StringRes id: Int, vararg formatArgs: Any): CharSequence? =
    context?.addSpansFromAnnotations(id, *formatArgs)

fun Fragment.addSpansFromAnnotations(text: CharSequence, vararg formatArgs: Any): CharSequence? =
    context?.addSpansFromAnnotations(text, *formatArgs)