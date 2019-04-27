package com.grivos.spanomatic.spans

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class ListenableClickableSpan(val clickName: String) : ClickableSpan() {

    var listener: Listener? = null

    override fun onClick(widget: View) {
        listener?.onClick(clickName)
    }

    override fun updateDrawState(ds: TextPaint) {
        // don't decorate the link
        // if a user wants to add underline he can wrap the "click" annotation with a "format" annotation
    }

    interface Listener {
        fun onClick(clickName: String)
    }

}