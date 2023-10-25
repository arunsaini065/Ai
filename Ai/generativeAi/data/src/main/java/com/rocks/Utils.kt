package com.rocks

import android.os.Build
import android.text.Html


fun String.stripHtml(): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        @Suppress("DEPRECATION")
        return Html.fromHtml(this).toString()
    }
}