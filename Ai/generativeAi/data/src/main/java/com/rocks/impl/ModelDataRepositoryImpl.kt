package com.rocks.impl

import com.rocks.BodyDataHandler
import com.rocks.api.Api
import com.rocks.api.ApiInterface
import com.rocks.model.ApiOutput
import com.rocks.model.ModelListDataItem
import com.rocks.model.SchedulerList
import com.rocks.model.UploadImage
import com.rocks.repository.ModelDataRepository
import com.rocks.uistate.ModelUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody

class ModelDataRepositoryImpl(private val apiInterface: ApiInterface?) : ModelDataRepository {

    override fun getModelIdBaseData(bodyDataHandler: BodyDataHandler): Flow<ModelUiState<ApiOutput>> = flow<ModelUiState<ApiOutput>> {


            emit(ModelUiState.Loading())

            runCatching {

                val result = if (bodyDataHandler.uploadImage==null) {

                    apiInterface?.getModelIdData(Api.getBodyForModel(bodyDataHandler))

                }else{

                    apiInterface?.getModelIdDataImg2Img(Api.getBodyForModel(bodyDataHandler))

                }


                if (result?.status.equals("error")){

                    emit(ModelUiState.Error(result?.message?:"error"))

                } else if (result?.status.equals("processing")) {

                    emit(ModelUiState.Processing(result))

                    delay(10000)


                    runCatching {

                        val newResult = result?.let {
                            apiInterface?.getProcessingData(
                                bodyDataHandler,
                                it.id.toString()
                            )
                        }

                        if (newResult?.output.isNullOrEmpty().not()) {

                            result?.output = newResult?.output!!

                        } else {

                            result?.output = result?.future_links!!

                        }
                    }.onFailure {

                        result?.output = result?.future_links!!

                    }

                    emit(ModelUiState.Success(result))

                } else {

                    emit(ModelUiState.Success(result))

                }

            }.onFailure {

                emit(ModelUiState.Error(""+it))



            }



    }.flowOn(Dispatchers.IO)




    override fun getModelListData(requestBody: RequestBody): Flow<ModelUiState<MutableList<ModelListDataItem>>>  = flow<ModelUiState<MutableList<ModelListDataItem>>> {


        emit(ModelUiState.Loading())

        runCatching {

            val result = apiInterface?.getModelIdList(requestBody)

            emit(ModelUiState.Success(result))

        }.onFailure {

            emit(ModelUiState.Error("error"))
        }



    }.flowOn(Dispatchers.IO)

    override fun getSchedulerList(requestBody: RequestBody): Flow<ModelUiState<SchedulerList>>  = flow<ModelUiState<SchedulerList>> {


        emit(ModelUiState.Loading())

        runCatching {

            val result = apiInterface?.getSchedulerList(requestBody)

            emit(ModelUiState.Success(result))

        }.onFailure {

            emit(ModelUiState.Error("error"))
        }



    }.flowOn(Dispatchers.IO)

    override fun uploadImage(requestBody: RequestBody)=flow<ModelUiState<UploadImage>> {


        emit(ModelUiState.Loading())

        runCatching {

            val result = apiInterface?.uploadImage(requestBody)

            emit(ModelUiState.Success(result))

        }.onFailure {

            emit(ModelUiState.Error(it.message?:"",null))
        }



    }.flowOn(Dispatchers.IO)

}