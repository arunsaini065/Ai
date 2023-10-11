package com.rocks.api

import com.rocks.model.ApiOutput
import com.rocks.model.ModelListDataItem
import com.rocks.model.SchedulerList
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("/api/v4/dreambooth")
    suspend fun getModelIdData(@Body  params: RequestBody): ApiOutput?


    @POST("/api/v4/dreambooth/model_list")
    suspend fun getModelIdList(@Body  params: RequestBody): MutableList<ModelListDataItem>?

    @POST("/api/v1/enterprise/schedulers_list")
    suspend fun getSchedulerList(@Body  params: RequestBody): SchedulerList?

}