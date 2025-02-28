package com.uwaisalqadri.mangaku.android.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.uwaisalqadri.mangaku.data.souce.remote.response.ApiException
import com.uwaisalqadri.mangaku.domain.base.ApiError

@Composable
fun ComposableObserver(observe: @Composable () -> Unit) {
    observe()
}

@Composable
inline fun <reified T> ComposableObserver(
    state: State<Result<T>?>,
    noinline onLoading: @Composable ((Boolean) -> Unit)? = null,
    crossinline onSuccess: @Composable (T) -> Unit,
    noinline onFailure: @Composable ((ApiError) -> Unit)? = null
) {
    ComposableObserver {
        when (state.value) {
            is Result.Loading -> {
                onLoading?.invoke(true)
            }
            is Result.Success -> {
                onLoading?.invoke(false)
                getValue(state.value)?.let { onSuccess.invoke(it) }
            }
            is Result.Failure -> {
                onLoading?.invoke(false)
                onFailure?.invoke(observeApiError(state.value))
            }
            else -> {}
        }
    }
}

inline fun <reified T> getValue(resource: Result<T>?): T? {
    return if (resource is Result.Success) resource.data else null
}

inline fun <reified T> Result<T>.isLoading(): Boolean {
    return this is Result.Loading
}

inline fun <reified T> Result<T>.isEmpty(): Boolean {
    return (this is Result.Empty || this is Result.Default)
}

fun <T> observeApiError(resource: Result<T>?): ApiError {
    val result = resource as Result.Failure<T>

    return when (result.throwable) {
        is ApiException -> {
            result.throwable.map()
        }
        else -> {
            ApiError(
                errorTitle = "Error",
                errorMessage = "Something's wrong"
            )
        }
    }
}
