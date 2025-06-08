package workwork.company.wstest.core

sealed class MyResult<out T> {
    data class Success<out T>(val data: T) : MyResult<T>()
    data class Failure(val exception: Exception) : MyResult<Nothing>()
    data object Loading : MyResult<Nothing>()
    data object Default : MyResult<Nothing>()
}
