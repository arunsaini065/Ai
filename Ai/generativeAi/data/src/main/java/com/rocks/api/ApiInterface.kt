package com.rocks.api

import com.rocks.model.ApiOutput
import com.rocks.model.ModelListData
import com.rocks.model.ProcessingOutput
import com.rocks.model.ModelListDataItem
import com.rocks.model.SchedulerList
import com.rocks.model.UploadImage
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {

    @POST("/api/v4/dreambooth")
    suspend fun getModelIdData(@Body  params: RequestBody): ApiOutput?

    @POST("/api/v4/dreambooth/img2img")
    suspend fun getModelIdDataImg2Img(@Body  params: RequestBody): ApiOutput?

    @POST("/api/v4/dreambooth/{id}")
    suspend fun getProcessingData(@Body params: RequestBody,@Path("id") id: String): ProcessingOutput?


    @POST("/api/v4/dreambooth/model_list")
    suspend fun getModelIdList(@Body  params: RequestBody): MutableList<ModelListDataItem>?

    @POST("/api/v1/enterprise/schedulers_list")
    suspend fun getSchedulerList(@Body  params: RequestBody): SchedulerList?

    @POST("/api/v3/base64_crop")
    suspend fun uploadImage(@Body  params: RequestBody): UploadImage?

}