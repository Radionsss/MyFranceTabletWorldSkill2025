package workwork.company.wstest.data.remote

import kotlinx.coroutines.delay
import retrofit2.Response
import workwork.company.wstest.domain.models.diaries.*
import workwork.company.wstest.domain.models.favorite.*
import workwork.company.wstest.domain.models.sign.*
import javax.inject.Inject

class FakeApiService @Inject constructor(): ApiService {
    private val DEFAULT_IMAGE_URL = "https://media.istockphoto.com/id/2162521331/photo/aerial-view-of-the-town-at-night-and-the-baiterek-tower-in-astana-kazakhstan.jpg?s=1024x1024&w=is&k=20&c=h51DZn9wVUA8jcR-HsUzAT6E04kA6T4IJkeJrHRGrFU="

    override suspend fun signIn(request: SignInRequest): Response<SignInResponse> {
        delay(3000)
        return Response.success(
            SignInResponse(
                msg = "Sign in successful",
                data = SignInData(
                    auth_token = "FAKE_AUTH_TOKEN_123456"
                )
            )
        )
    }
    override suspend fun getAllDiaries(): Response<GetAllDiariesResponse> {
        delay(3000)
        return Response.success(
            GetAllDiariesResponse(
                msg = "Success",
                data = listOf(
                    DiaryModel(
                        diaryId = "ID1",
                        diaryTitle = "A Trip to Astana",
                        diaryMainText = "resources/trip1.json",
                        diaryUploadDatetime = "2025-04-26 12:00:00",
                        diaryImage = DEFAULT_IMAGE_URL,
                        diaryUploadUsername = "Fake User 1"
                    ),
                    DiaryModel(
                        diaryId = "ID2",
                        diaryTitle = "Exploring Baiterek Tower",
                        diaryMainText = "resources/trip2.json",
                        diaryUploadDatetime = "2025-04-25 09:30:00",
                        diaryImage = DEFAULT_IMAGE_URL,
                        diaryUploadUsername = "Fake User 2"
                    ),
                    DiaryModel(
                        diaryId = "ID3",
                        diaryTitle = "Sunset at Nur-Sultan",
                        diaryMainText = "resources/trip3.json",
                        diaryUploadDatetime = "2025-04-24 18:45:00",
                        diaryImage = DEFAULT_IMAGE_URL,
                        diaryUploadUsername = "Fake User 3"
                    ),
                    DiaryModel(
                        diaryId = "ID4",
                        diaryTitle = "Walk Through Khan Shatyr",
                        diaryMainText = "resources/trip4.json",
                        diaryUploadDatetime = "2025-04-23 14:20:00",
                        diaryImage = DEFAULT_IMAGE_URL,
                        diaryUploadUsername = "Fake User 4"
                    ),
                    DiaryModel(
                        diaryId = "ID5",
                        diaryTitle = "Discovering EXPO 2017 Sites",
                        diaryMainText = "resources/trip5.json",
                        diaryUploadDatetime = "2025-04-22 11:10:00",
                        diaryImage = DEFAULT_IMAGE_URL,
                        diaryUploadUsername = "Fake User 5"
                    ),
                    DiaryModel(
                        diaryId = "ID6",
                        diaryTitle = "National Museum Tour",
                        diaryMainText = "resources/trip6.json",
                        diaryUploadDatetime = "2025-04-21 15:55:00",
                        diaryImage = DEFAULT_IMAGE_URL,
                        diaryUploadUsername = "Fake User 6"
                    ),
                    DiaryModel(
                        diaryId = "ID7",
                        diaryTitle = "Peace Pyramid Adventure",
                        diaryMainText = "resources/trip7.json",
                        diaryUploadDatetime = "2025-04-20 10:00:00",
                        diaryImage = DEFAULT_IMAGE_URL,
                        diaryUploadUsername = "Fake User 7"
                    ),
                    DiaryModel(
                        diaryId = "ID8",
                        diaryTitle = "Astana Opera House Visit",
                        diaryMainText = "resources/trip8.json",
                        diaryUploadDatetime = "2025-04-19 13:30:00",
                        diaryImage = DEFAULT_IMAGE_URL,
                        diaryUploadUsername = "Fake User 8"
                    )
                )
            )
        )
    }

    override suspend fun insertFavorite(authToken: String, diaryId: String): Response<InsertFavoriteResponse> {
        delay(3000)
        return Response.success(
            InsertFavoriteResponse(
                msg = "Success",
                data = FavoriteData(
                    diary_id = diaryId,
                    favorite_datetime = "2025-04-26 15:00:00"
                )
            )
        )
    }

    override suspend fun getMyAllFavorites(authToken: String): Response<GetAllFavoritesResponse> {
        delay(3000)

        val allDiaries = listOf(
            DiaryModel(
                diaryId = "ID1",
                diaryTitle = "A Trip to Astana",
                diaryMainText = "resources/trip1.json",
                diaryUploadDatetime = "2025-04-26 12:00:00",
                diaryImage = DEFAULT_IMAGE_URL,
                diaryUploadUsername = "Fake User 1"
            ),
            DiaryModel(
                diaryId = "ID2",
                diaryTitle = "Exploring Baiterek Tower",
                diaryMainText = "resources/trip2.json",
                diaryUploadDatetime = "2025-04-25 09:30:00",
                diaryImage = DEFAULT_IMAGE_URL,
                diaryUploadUsername = "Fake User 2"
            ),
            DiaryModel(
                diaryId = "ID3",
                diaryTitle = "Sunset at Nur-Sultan",
                diaryMainText = "resources/trip3.json",
                diaryUploadDatetime = "2025-04-24 18:45:00",
                diaryImage = DEFAULT_IMAGE_URL,
                diaryUploadUsername = "Fake User 3"
            ),
            DiaryModel(
                diaryId = "ID4",
                diaryTitle = "Walk Through Khan Shatyr",
                diaryMainText = "resources/trip4.json",
                diaryUploadDatetime = "2025-04-23 14:20:00",
                diaryImage = DEFAULT_IMAGE_URL,
                diaryUploadUsername = "Fake User 4"
            ),
            DiaryModel(
                diaryId = "ID5",
                diaryTitle = "Discovering EXPO 2017 Sites",
                diaryMainText = "resources/trip5.json",
                diaryUploadDatetime = "2025-04-22 11:10:00",
                diaryImage = DEFAULT_IMAGE_URL,
                diaryUploadUsername = "Fake User 5"
            ),
            DiaryModel(
                diaryId = "ID6",
                diaryTitle = "National Museum Tour",
                diaryMainText = "resources/trip6.json",
                diaryUploadDatetime = "2025-04-21 15:55:00",
                diaryImage = DEFAULT_IMAGE_URL,
                diaryUploadUsername = "Fake User 6"
            ),
            DiaryModel(
                diaryId = "ID7",
                diaryTitle = "Peace Pyramid Adventure",
                diaryMainText = "resources/trip7.json",
                diaryUploadDatetime = "2025-04-20 10:00:00",
                diaryImage = DEFAULT_IMAGE_URL,
                diaryUploadUsername = "Fake User 7"
            ),
            DiaryModel(
                diaryId = "ID8",
                diaryTitle = "Astana Opera House Visit",
                diaryMainText = "resources/trip8.json",
                diaryUploadDatetime = "2025-04-19 13:30:00",
                diaryImage = DEFAULT_IMAGE_URL,
                diaryUploadUsername = "Fake User 8"
            )
        )

        val favorites = allDiaries.map { diary ->
            FavoriteItem(
                diary_id = diary.diaryId,
                favorite_datetime = diary.diaryUploadDatetime
            )
        }

        return Response.success(
            GetAllFavoritesResponse(
                msg = "Success",
                data = favorites
            )
        )
    }

    override suspend fun getResourceFile(path: String): Response<String> {
        delay(3000)
        return Response.success("{ \"content\": \"This is fake resource content for $path\" }")
    }

    override suspend fun getUserAgreement(): Response<String> {
        delay(3000)
        return Response.success("Fake User Agreement Text.")
    }

}