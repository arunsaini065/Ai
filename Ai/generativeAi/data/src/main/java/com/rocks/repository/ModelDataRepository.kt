package com.rocks.repository

import com.rocks.model.ApiOutput
import com.rocks.model.ModelListData
import com.rocks.model.SchedulerList
import com.rocks.uistate.ModelUiState
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

interface ModelDataRepository {


    fun getModelIdBaseData(requestBody: RequestBody):Flow<ModelUiState<ApiOutput>>

    fun getModelListData(requestBody: RequestBody):Flow<ModelUiState<ModelListData>>

    fun getSchedulerList(requestBody: RequestBody):Flow<ModelUiState<SchedulerList>>

}