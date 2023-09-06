package com.godzuche.recircu.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface RecircuResult<out T> {
    data class Success<T>(val data: T) : RecircuResult<T>
    data class Error(val exception: Throwable? = null) : RecircuResult<Nothing>
    object Loading : RecircuResult<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<RecircuResult<T>> {
    return this
        .map<T, RecircuResult<T>> {
            RecircuResult.Success(it)
        }
        .onStart { emit(RecircuResult.Loading) }
        .catch { emit(RecircuResult.Error(it)) }
}
