package workwork.company.wstest.presenter.main.usecase

import android.util.Log
import workwork.company.wstest.core.MyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import workwork.company.wstest.domain.MainRepository
import workwork.company.wstest.domain.SharedPreferencesManager
import workwork.company.wstest.domain.models.favorite.FavoriteData
import javax.inject.Inject
class AddFavoriteUseCase @Inject constructor(
    private val repository: MainRepository,
    private val sharedPreferencesManager: SharedPreferencesManager,
) {
    fun insertFavorite(diaryId: String): Flow<MyResult<FavoriteData>> = flow {
        emit(MyResult.Loading)
        try {
            val authToken = sharedPreferencesManager.getAuthToken()

            if (authToken.isNullOrEmpty()) {
                throw IllegalStateException("Токен авторизации отсутствует")
            }

            val diariesResult = repository.insertFavorite(authToken, diaryId).data
            sharedPreferencesManager.addFavoriteId(diariesResult.diary_id)
            emit(MyResult.Success(diariesResult))
        } catch (e: Exception) {
            Log.d("AddFavoriteUseCase", "Ошибка: ${e.message}")
            emit(MyResult.Failure(e))
        }
    }
}