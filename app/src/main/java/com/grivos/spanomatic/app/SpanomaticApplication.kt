package com.grivos.spanomatic.app

import android.app.Application
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import com.grivos.spanomatic.Spanomatic
import com.grivos.spanomatic.SpanomaticInterceptor
import com.grivos.spanomatic.TypefaceProvider
import io.github.inflationx.viewpump.ViewPump


class SpanomaticApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    SpanomaticInterceptor()
                )
                .build()
        )
        val latoRegular = ResourcesCompat.getFont(this, R.font.lato_regular)
        val latoLight = ResourcesCompat.getFont(this, R.font.lato_light)
        val latoBold = ResourcesCompat.getFont(this, R.font.lato_bold)
        Spanomatic.typefaceProvider = object : TypefaceProvider {

            override fun getTypeface(name: String): Typeface? {
                return when (name) {
                    KEY_TYPEFACE_LATO_REGULAR -> latoRegular
                    KEY_TYPEFACE_LATO_LIGHT -> latoLight
                    KEY_TYPEFACE_LATO_BOLD -> latoBold
                    else -> null
                }
            }

        }
    }
}

private const val KEY_TYPEFACE_LATO_REGULAR = "latoRegular"
private const val KEY_TYPEFACE_LATO_LIGHT = "latoLight"
private const val KEY_TYPEFACE_LATO_BOLD = "latoBold"