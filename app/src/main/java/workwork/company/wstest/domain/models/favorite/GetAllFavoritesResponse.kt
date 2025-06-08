package workwork.company.wstest.domain.models.favorite

data class GetAllFavoritesResponse(
    val msg: String,
    val data: List<FavoriteItem>
)