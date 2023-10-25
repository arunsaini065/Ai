package com.rocks.uistate

sealed class ModelUiState<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null): ModelUiState<T>(data)
    class Processing<T>(data: T? = null): ModelUiState<T>(data)
    class Success<T>(data: T?): ModelUiState<T>(data)
    class Error<T>(message: String, data: T? = null): ModelUiState<T>(data, message)
}
