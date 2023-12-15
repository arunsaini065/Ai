package com.rocks.ui

import androidx.lifecycle.ViewModel
import com.rocks.BodyDataHandler
import kotlinx.coroutines.flow.MutableStateFlow

class AiUiViewModel : ViewModel() {


    val ratioUpdate = MutableStateFlow<BodyDataHandler?>(null)

    val modelUpdate = MutableStateFlow<BodyDataHandler?>(null)

    val bodyDataHandler by lazy { BodyDataHandler() }


}