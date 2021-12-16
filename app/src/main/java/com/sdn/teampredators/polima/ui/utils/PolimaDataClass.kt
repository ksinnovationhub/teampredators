package com.sdn.teampredators.polima.ui.utils

import java.lang.Exception

sealed class DataResult<out T> {
    data class Success<T>(val data: T?) : DataResult<T>()
    data class Error(val error: Throwable?) : DataResult<Nothing>()
    object Loading : DataResult<Nothing>()
}

sealed class ListResult<out E>{
    data class Success<E>(val list: List<E>?) : ListResult<E>()
    data class Error(val error: Throwable?): ListResult<Nothing>()
    object Loading : ListResult<Nothing>()

}