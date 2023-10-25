package com.rocks.usecase

import com.rocks.BodyDataHandler
import com.rocks.repository.ModelDataRepository
import okhttp3.RequestBody

class ModelUseCase(private val modelDataRepository: ModelDataRepository) {


    fun fetchIdModelBaseData(bodyDataHandler: BodyDataHandler) = modelDataRepository.getModelIdBaseData(bodyDataHandler)

    fun getAllModelList(requestBody: RequestBody) = modelDataRepository.getModelListData(requestBody)


    fun getSchedulerList(requestBody: RequestBody) = modelDataRepository.getSchedulerList(requestBody)

    fun uploadImage(requestBody: RequestBody) = modelDataRepository.uploadImage(requestBody)

}