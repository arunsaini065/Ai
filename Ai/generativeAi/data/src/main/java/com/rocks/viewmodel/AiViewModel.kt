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


    fun getLocalLoRAId(): ModelUiState<MutableList<ModelListDataItem>> {
        val listOfLoRAid = mutableListOf<ModelListDataItem>()
        val listOf = Api.listOfLoRAModel
        val dummyUrl = "https://fastly.picsum.photos/id/278/536/354.jpg?hmac=B3RGgunW6oirJoQEgt80to9HNb7oZqLut-4fFVVc9NM"
        listOf.forEach {
            listOfLoRAid.add(
                ModelListDataItem(
                    "", "",
                    "", "",
                    "", "", "", it, "", dummyUrl, ""
                )
            )

        }

        return ModelUiState.Success(listOfLoRAid)
    }


    fun getAllInspiration(context: Context) = flow {

        runCatching {

            val fileString = readJsonFromAssets(context)



             emit(parseJsonToModel(fileString))

        }

    }.flowOn(Dispatchers.IO)

    private fun readJsonFromAssets(context: Context): String {

        return context.assets.open("inspirations.json").bufferedReader().use { it.readText() }

    }

    private fun parseJsonToModel(jsonString: String): MutableList<InspirationData> {

        val gson = Gson()

        return gson.fromJson(jsonString, object : TypeToken<MutableList<InspirationData>>() {}.type)

    }

}