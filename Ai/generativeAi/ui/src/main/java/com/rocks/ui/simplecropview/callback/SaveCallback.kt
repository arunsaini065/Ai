package com.rocks.ui.simplecropview.callback

import android.net.Uri

interface SaveCallback : Callback {
    fun onSuccess(uri: Uri?)
}
