package com.rocks.usecase

import com.rocks.repository.ModelDataRepository
import okhttp3.RequestBody

class ModelUseCase(private val modelDataRepository: ModelDataRepository) {


    fun fetchIdModelBaseData(requestBody: RequestBody) = modelDataRepository.getModelIdBaseData(requestBody)

    fun getAllModelList(requestBody: RequestBody) = modelDataRepository.getModelListData(requestBody)


    fun getSchedulerList(requestBody: RequestBody) = modelDataRepository.getSchedulerList(requestBody)

}