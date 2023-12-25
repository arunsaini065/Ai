package com.rocks

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

fun String.convertBitmap(): Bitmap? {

    runCatching {

        val decodedBytes = Base64.decode(substring(indexOf(",") + 1), Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

    }

    return null

}


fun Bitmap.convertBase64(): String {

    runCatching {

        val outputStream = ByteArrayOutputStream()

        compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)

    }

    return ""
}

