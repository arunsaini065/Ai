package com.rocks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocks.model.ApiOutput
import com.rocks.model.ModelListData
import com.rocks.model.SchedulerList
import com.rocks.uistate.ModelUiState
import com.rocks.usecase.ModelUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody


class AiViewModel (private val modelUseCase: ModelUseCase): ViewModel() {

    private val _stateflowAiModel:MutableStateFlow<ModelUiState<ApiOutput>> = MutableStateFlow(ModelUiState.Success(null))

    private val _stateflowAiModelList:MutableStateFlow<ModelUiState<ModelListData>> = MutableStateFlow(ModelUiState.Success(null))

    private val _stateflowAiModelSchedulerList:MutableStateFlow<ModelUiState<SchedulerList>> = MutableStateFlow(ModelUiState.Success(null))

    val stateflowAiModel = _stateflowAiModel.asStateFlow()

    val stateflowAiModelList = _stateflowAiModelList.asStateFlow()

    val stateflowAiModelSchedulerList = _stateflowAiModelSchedulerList.asStateFlow()


    fun postModelIdBase(requestBody: RequestBody)  {

         viewModelScope.launch {

             /* modelUseCase.fetchIdModelBaseData(requestBody).collect {

                 _stateflowAiModel.value = it

             }*/

         }

    }

    fun postModelIdsList(requestBody: RequestBody)  {

         viewModelScope.launch {

              modelUseCase.getAllModelList(requestBody).collect {

                  _stateflowAiModelList.value = it

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

}