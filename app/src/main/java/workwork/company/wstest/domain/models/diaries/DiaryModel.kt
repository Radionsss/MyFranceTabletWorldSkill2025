package workwork.company.wstest.domain.models.diaries

data class DiaryModel(
    val diaryId: String,
    val diaryTitle: String,
    val diaryMainText: String,
    val diaryUploadDatetime: String,
    val diaryImage: String,
    val diaryUploadUsername: String
)