package com.uwaisalqadri.mangaku.android.utils

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

suspend fun <U> collectFlow(outputLiveData: MutableLiveData<Result<U>>, block: suspend () -> Flow<U>) {
    block.invoke().catch { cause: Throwable ->
        outputLiveData.value = Result.fail(cause, cause.message)
    }.collect {
        outputLiveData.value = Result.success(it)
    }
}

suspend fun <U> collectFlow(outputLiveData: MutableStateFlow<Result<U>>, block: suspend () -> Flow<U>) {
    block.invoke().catch { cause: Throwable ->
        outputLiveData.emit(Result.fail(cause, cause.message))
    }.collect {
        if (it is List<*>) {
            if (it.isEmpty()) outputLiveData.emit(Result.empty())
            else outputLiveData.emit(Result.success(it))
        }

        if (it != null) {
            outputLiveData.emit(Result.success(it))
        }

        if (it == null) {
            outputLiveData.emit(Result.empty())
        }
    }
}