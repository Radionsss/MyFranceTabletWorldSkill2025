package workwork.company.wstest.presenter.auth.data

data class FavoriteDiaryModel(
    val diaryId: String,
    val diaryTitle: String,
    val diaryMainText: String,
    val diaryUploadDateTime: String,
    val diaryImage: String,
    val username: String,
    val favoriteDateTime: String
)