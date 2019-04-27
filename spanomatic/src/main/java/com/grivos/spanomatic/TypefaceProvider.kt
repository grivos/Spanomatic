package com.grivos.spanomatic

import android.graphics.Typeface

interface TypefaceProvider {
    fun getTypeface(name: String): Typeface?
}