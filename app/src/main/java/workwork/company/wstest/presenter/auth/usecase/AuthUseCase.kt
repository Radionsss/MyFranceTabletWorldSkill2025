package workwork.company.wstest.presenter.auth.usecase

import android.util.Log
import workwork.company.wstest.core.MyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import workwork.company.wstest.domain.MainRepository
import workwork.company.wstest.domain.SharedPreferencesManager
import workwork.company.wstest.domain.models.sign.SignInRequest
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: MainRepository,
    private val sharedPreferencesManager: SharedPreferencesManager,
) {
    fun signIn(
        request: SignInRequest
    ): Flow<MyResult<Unit>> = flow {
        emit(MyResult.Loading)
        try {
            val result = repository.signIn(
                request = request,
            )
            val username = request.userEmailAddress.substringBefore("@")
            sharedPreferencesManager.saveUserName(username)
            sharedPreferencesManager.saveAuthToken(result.data.auth_token)
            emit(MyResult.Success(data = Unit))
        } catch (e: Exception) {
            Log.d("createClass", "Ошибка: ${e.message}")
            emit(MyResult.Failure(e))
        }
    }
}