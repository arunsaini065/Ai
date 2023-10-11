package com.rocks.impl

import android.util.Log
import com.rocks.api.ApiInterface
import com.rocks.model.ApiOutput
import com.rocks.model.ModelListDataItem
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

                val result = apiInterface?.getModelIdData(requestBody)

                Log.d("@Arun", "getModelIdBaseData: "+result?.status)

                Log.d("@Arun", "getModelIdBaseData  -msg-  : "+result?.message)

                if (result?.status.equals("error")){

                    emit(ModelUiState.Error(result?.message?:"error"))

                }else if (result?.status.equals("success")) {
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

}