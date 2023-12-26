package com.rocks

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Html
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import pub.devrel.easypermissions.EasyPermissions
import java.util.Random


fun String.stripHtml(): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        @Suppress("DEPRECATION")
        return Html.fromHtml(this).toString()
    }
}

const val UNKOWN: String = "Unknown"
const val INTERNAL_STORAGE: String = "Internal storage"

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
fun is33():Boolean {

    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

}


fun Context.checkPermission(): Boolean {
    return if (is33()) {

        EasyPermissions.hasPermissions(this, *READ_AND_WRITE_EXTERNAL_STORAGE_33)

    } else {

        EasyPermissions.hasPermissions(this, *READ_AND_WRITE_EXTERNAL_STORAGE)

    }
}


fun Context.getActivity(): Activity?{
    return this as Activity?
}

var READ_AND_WRITE_EXTERNAL_STORAGE =
    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
var READ_AND_WRITE_EXTERNAL_STORAGE_33 =
    arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)

const val RC_READ_EXTERNAL_STORAGE = 120

val readExtrenal = "Please allow read access of files."

fun Context.requestPermissions() {
    if (is33()){
        getActivity()?.let {
            EasyPermissions.requestPermissions(
                it,
                readExtrenal,
                RC_READ_EXTERNAL_STORAGE,
                *READ_AND_WRITE_EXTERNAL_STORAGE_33
            )
        }
    }else {
        getActivity()?.let {
            EasyPermissions.requestPermissions(
                it,
                readExtrenal,
                RC_READ_EXTERNAL_STORAGE,
                *READ_AND_WRITE_EXTERNAL_STORAGE
            )
        }
    }
}


fun seeds(): Int {
    val min = 0
    val max = 199999
    return Random().nextInt((max - min) + 1) + min
}