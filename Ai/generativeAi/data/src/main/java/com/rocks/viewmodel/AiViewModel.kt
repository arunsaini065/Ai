package com.rocks.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rocks.BodyDataHandler
import com.rocks.InspirationData
import com.rocks.api.Api
import com.rocks.model.ApiOutput
import com.rocks.model.LoraModel
import com.rocks.model.ModelListDataItem
import com.rocks.model.SchedulerList
import com.rocks.model.UploadImage
import com.rocks.uistate.ModelUiState
import com.rocks.usecase.ModelUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.RequestBody


class AiViewModel (private val modelUseCase: ModelUseCase): ViewModel() {

    private val _stateflowAiModel:MutableStateFlow<ModelUiState<ApiOutput>> = MutableStateFlow(ModelUiState.Success(null))

    private val _stateflowAiModelList:MutableStateFlow<ModelUiState<MutableList<ModelListDataItem>>> = MutableStateFlow(ModelUiState.Success(null))

    private val _stateflowAiModelSchedulerList:MutableStateFlow<ModelUiState<SchedulerList>> = MutableStateFlow(ModelUiState.Success(null))

    private val _stateflowUploadImage:MutableStateFlow<ModelUiState<UploadImage>> = MutableStateFlow(ModelUiState.Success(null))

    val stateflowAiModel = _stateflowAiModel.asStateFlow()

    val stateflowAiModelList = _stateflowAiModelList.asStateFlow()

    val stateflowUploadImage = _stateflowUploadImage.asStateFlow()

    val stateflowAiModelSchedulerList = _stateflowAiModelSchedulerList.asStateFlow()


    fun postModelIdBase(bodyDataHandler: BodyDataHandler)  {

         viewModelScope.launch {

              modelUseCase.fetchIdModelBaseData(bodyDataHandler).collect {

                 _stateflowAiModel.value = it

             }
         }

    }

    fun postModelIdsList(requestBody: RequestBody)  {

         viewModelScope.launch {

              modelUseCase.getAllModelList(requestBody).collect {

                  _stateflowAiModelList.value = it

             }

         }

    }



    fun uploadImage(requestBody: RequestBody){

        viewModelScope.launch {

            modelUseCase.uploadImage(requestBody).collect{

                _stateflowUploadImage.value =it

            }

        }
    }




    fun postSchedulerList(requestBody: RequestBody)  {

         viewModelScope.launch {

              modelUseCase.getSchedulerList(requestBody).collect {

                  _stateflowAiModelSchedulerList.value = it

             }

         }

    }


    fun getLocalLoRAId(context: Context) = flow {

        runCatching {

            val fileString = readJsonFromAssets(context, "lora_model.json")

            val listOf  = parseJsonToModelLoraModel(fileString).map { it.toModelListDataItem() }.toMutableList()

            emit(ModelUiState.Success(listOf))
        }
    }.flowOn(Dispatchers.IO)


    fun getAllInspiration(context: Context) = flow {

        runCatching {

            val fileString = readJsonFromAssets(context,"inspirations.json")



             emit(parseJsonToModelInspirationData(fileString))

        }

    }.flowOn(Dispatchers.IO)

    private fun readJsonFromAssets(context: Context,name:String): String {

        return context.assets.open(name).bufferedReader().use { it.readText() }

    }

    private fun parseJsonToModelInspirationData(jsonString: String): MutableList<InspirationData> {

        val gson = Gson()

        return gson.fromJson(jsonString, object : TypeToken<MutableList<InspirationData>>() {}.type)

    }

    private fun parseJsonToModelLoraModel(jsonString: String): MutableList<LoraModel> {

        val gson = Gson()

        return gson.fromJson(jsonString, object : TypeToken<MutableList<LoraModel>>() {}.type)

    }

}