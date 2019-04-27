package com.grivos.spanomatic

import android.widget.TextView
import io.github.inflationx.viewpump.InflateResult
import io.github.inflationx.viewpump.Interceptor

class SpanomaticInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): InflateResult {
        val result = chain.proceed(chain.request())
        val view = result.view()
        if (view is TextView) {
            view.text = Spanomatic.addSpansFromAnnotations(view.text, view.context)
        }
        return result
    }

}