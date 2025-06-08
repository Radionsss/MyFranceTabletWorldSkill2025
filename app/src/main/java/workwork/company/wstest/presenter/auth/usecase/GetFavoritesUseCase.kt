package workwork.company.wstest.presenter.auth.usecase

import android.util.Log
import workwork.company.wstest.core.MyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import workwork.company.wstest.domain.MainRepository
import workwork.company.wstest.domain.SharedPreferencesManager
import workwork.company.wstest.presenter.auth.data.FavoriteDiaryModel
import javax.inject.Inject
class GetFavoritesUseCase @Inject constructor(
    private val repository: MainRepository,
    private val sharedPreferencesManager: SharedPreferencesManager,
) {
    fun getFavorites(): Flow<MyResult<List<FavoriteDiaryModel>>> = flow {
        emit(MyResult.Loading)
        try {
            // Получаем избранные id
            val token = sharedPreferencesManager.getAuthToken() ?: ""
            val favoritesResult = repository.getMyAllFavorites(token)
            val favoriteItems = favoritesResult.data

            val diariesResult = repository.getAllDiaries()
            val allDiaries = diariesResult.data
            val favoriteDiaryModels = favoriteItems.mapNotNull { favoriteItem ->
                val diary = allDiaries.find { it.diaryId == favoriteItem.diary_id }
                diary?.let {
                    FavoriteDiaryModel(
                        diaryId = it.diaryId,
                        diaryTitle = it.diaryTitle,
                        diaryMainText = it.diaryMainText,
                        diaryUploadDateTime = it.diaryUploadDatetime,
                        diaryImage = it.diaryImage,
                        username = it.diaryUploadUsername.substringBefore("@"),
                        favoriteDateTime = favoriteItem.favorite_datetime
                    )
                }
            }.sortedByDescending { it.favoriteDateTime }

            emit(MyResult.Success(favoriteDiaryModels))
        } catch (e: Exception) {
            Log.e("GetFavoritesUseCase", "Ошибка при загрузке избранного: ${e.message}", e)
            emit(MyResult.Failure(e))
        }
    }
}