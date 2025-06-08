package workwork.company.wstest.domain

import workwork.company.wstest.domain.models.diaries.GetAllDiariesResponse
import workwork.company.wstest.domain.models.favorite.GetAllFavoritesResponse
import workwork.company.wstest.domain.models.favorite.InsertFavoriteResponse
import workwork.company.wstest.domain.models.sign.SignInRequest
import workwork.company.wstest.domain.models.sign.SignInResponse

interface MainRepository {
    suspend fun signIn(request: SignInRequest): SignInResponse

    suspend fun getAllDiaries(): GetAllDiariesResponse

    suspend fun insertFavorite(authToken: String, diaryId: String): InsertFavoriteResponse

    suspend fun getMyAllFavorites(authToken: String): GetAllFavoritesResponse

    suspend fun getResourceFile(path: String): String

    suspend fun getUserAgreement(): String
}