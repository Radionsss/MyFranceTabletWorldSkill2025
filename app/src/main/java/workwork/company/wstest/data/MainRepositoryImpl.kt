package workwork.company.wstest.data

import workwork.company.wstest.data.remote.ApiService
import workwork.company.wstest.data.remote.FakeApiService
import workwork.company.wstest.domain.MainRepository
import javax.inject.Inject
import workwork.company.wstest.domain.models.diaries.GetAllDiariesResponse
import workwork.company.wstest.domain.models.favorite.GetAllFavoritesResponse
import workwork.company.wstest.domain.models.favorite.InsertFavoriteResponse
import workwork.company.wstest.domain.models.sign.SignInRequest
import workwork.company.wstest.domain.models.sign.SignInResponse
class MainRepositoryImpl @Inject constructor(
   // private val apiService: ApiService,
    private val apiService: FakeApiService,
) : MainRepository {

    override suspend fun signIn(request: SignInRequest): SignInResponse {
        val response = apiService.signIn(request)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty body on signIn")
        } else {
            throw Exception("SignIn failed: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun getAllDiaries(): GetAllDiariesResponse {
        val response = apiService.getAllDiaries()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty body on getAllDiaries")
        } else {
            throw Exception("GetAllDiaries failed: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun insertFavorite(authToken: String, diaryId: String): InsertFavoriteResponse {
        val response = apiService.insertFavorite(authToken, diaryId)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty body on insertFavorite")
        } else {
            throw Exception("InsertFavorite failed: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun getMyAllFavorites(authToken: String): GetAllFavoritesResponse {
        val response = apiService.getMyAllFavorites(authToken)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty body on getMyAllFavorites")
        } else {
            throw Exception("GetMyAllFavorites failed: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun getResourceFile(path: String): String {
        val response = apiService.getResourceFile(path)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty body on getResourceFile")
        } else {
            throw Exception("GetResourceFile failed: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun getUserAgreement(): String {
        val response = apiService.getUserAgreement()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty body on getUserAgreement")
        } else {
            throw Exception("GetUserAgreement failed: ${response.errorBody()?.string()}")
        }
    }
}