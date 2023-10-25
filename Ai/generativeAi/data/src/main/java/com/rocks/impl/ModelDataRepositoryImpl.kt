package com.rocks.impl

import android.util.Log
import com.rocks.api.ApiInterface
import com.rocks.model.ApiOutput
import com.rocks.model.ModelListData
import com.rocks.model.SchedulerList
import com.rocks.repository.ModelDataRepository
import com.rocks.uistate.ModelUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody

class ModelDataRepositoryImpl(private val apiInterface: ApiInterface?) : ModelDataRepository {

    override fun getModelIdBaseData(requestBody: RequestBody): Flow<ModelUiState<ApiOutput>> = flow<ModelUiState<ApiOutput>> {


            emit(ModelUiState.Loading())

            runCatching {

                var result = apiInterface?.getModelIdData(requestBody)

                if (result?.status.equals("error")){

                    emit(ModelUiState.Error(result?.message?:"error"))

                }else if(result?.status.equals("processing")){
//                    Log.d("@processing","old result  $result")
                    var newResult = result?.let { apiInterface?.getProcessingData(requestBody, it.id.toString()) }
//                    Log.d("@processing","middle result  $newResult")
                    result?.output = newResult?.output!!
                    emit(ModelUiState.Success(result))
//                    Log.d("@processing","new result  $result")
                }
                else {
                    emit(ModelUiState.Success(result))
                }

            }.onFailure {

                emit(ModelUiState.Error("error"))
            }



    }.flowOn(Dispatchers.IO)




    override fun getModelListData(requestBody: RequestBody): Flow<ModelUiState<ModelListData>>  = flow<ModelUiState<ModelListData>> {


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

}