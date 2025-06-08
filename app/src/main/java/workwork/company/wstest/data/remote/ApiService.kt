package workwork.company.wstest.data.remote
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import workwork.company.wstest.domain.models.diaries.GetAllDiariesResponse
import workwork.company.wstest.domain.models.favorite.GetAllFavoritesResponse
import workwork.company.wstest.domain.models.favorite.InsertFavoriteResponse
import workwork.company.wstest.domain.models.sign.SignInRequest
import workwork.company.wstest.domain.models.sign.SignInResponse

interface ApiService {
    @POST("/api/users/signin")
    suspend fun signIn(
        @Body request: SignInRequest
    ): Response<SignInResponse>

    @GET("/api/diary")
    suspend fun getAllDiaries(): Response<GetAllDiariesResponse>

    @FormUrlEncoded
    @PUT("/api/diary/collection")
    suspend fun insertFavorite(
        @Header("auth_token") authToken: String,
        @Field("diary_id") diaryId: String
    ): Response<InsertFavoriteResponse>

    @GET("/api/diary/collection")
    suspend fun getMyAllFavorites(
        @Header("auth_token") authToken: String
    ): Response<GetAllFavoritesResponse>

    @GET("/api/{path}")
    suspend fun getResourceFile(
        @Path(value = "path", encoded = true) path: String
    ): Response<String>

    @GET("/api/user-agreement")
    suspend fun getUserAgreement(): Response<String>
}