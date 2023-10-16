package com.rocks.repository

import com.rocks.BodyDataHandler
import com.rocks.model.ApiOutput
import com.rocks.model.ModelListDataItem
import com.rocks.model.SchedulerList
import com.rocks.model.UploadImage
import com.rocks.uistate.ModelUiState
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

interface ModelDataRepository {


    fun getModelIdBaseData(bodyDataHandler: BodyDataHandler):Flow<ModelUiState<ApiOutput>>

    fun getModelListData(requestBody: RequestBody):Flow<ModelUiState<MutableList<ModelListDataItem>>>

    fun getSchedulerList(requestBody: RequestBody):Flow<ModelUiState<SchedulerList>>

    fun uploadImage(requestBody: RequestBody) : Flow<ModelUiState<UploadImage>>

}