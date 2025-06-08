package workwork.company.wstest.presenter.main.usecase

import android.util.Log
import workwork.company.wstest.core.MyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import workwork.company.wstest.domain.MainRepository
import workwork.company.wstest.domain.models.diaries.DiaryModel
import javax.inject.Inject

class GetDiariesUseCase @Inject constructor(
    private val repository: MainRepository,
) {
    fun getAllDiaries(
    ): Flow<MyResult<List<DiaryModel>>> = flow {
        emit(MyResult.Loading)
        try {
            val diariesResult = repository.getAllDiaries().data

            emit(MyResult.Success(diariesResult))
        } catch (e: Exception) {
            Log.d("GetFavoritesUseCase", "Ошибка: ${e.message}")
            emit(MyResult.Failure(e))
        }
    }
}