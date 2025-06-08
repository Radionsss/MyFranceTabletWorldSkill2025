package workwork.company.wstest.domain.models.diaries

data class GetAllDiariesResponse(
    val msg: String,
    val data: List<DiaryModel>
)
