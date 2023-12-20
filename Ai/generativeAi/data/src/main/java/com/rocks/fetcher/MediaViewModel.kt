package com.rocks.fetcher

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaViewModel(application: Application) : AndroidViewModel(application) {

    private val _listOfImage = MutableStateFlow<MutableList<MediaStoreData>>(mutableListOf())

    val listOfImage = _listOfImage.asStateFlow()

    private val _listOfFolder = MutableStateFlow<MutableList<AlbumModel>>(mutableListOf())

    val listOfFolder = _listOfFolder.asStateFlow()

    fun fetchAllData(){

        viewModelScope.launch(Dispatchers.IO) {

            val result = FetchMediaImageAsync(getApplication(),null).queryImages()

            _listOfImage.value = result

        }

        viewModelScope.launch(Dispatchers.IO) {

            val result = FetchAlbumsAsync(getApplication(),true).allAlbumDetails

            _listOfFolder.value = result

        }

    }

    fun fetchDataByBuket(id: String){

        viewModelScope.launch(Dispatchers.IO) {

            val result = FetchMediaImageAsync(getApplication(), arrayOf(id)).queryImages()

            _listOfImage.value = result

        }
    }

}