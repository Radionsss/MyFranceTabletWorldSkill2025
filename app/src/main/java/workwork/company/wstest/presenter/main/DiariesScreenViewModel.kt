package workwork.company.wstest.presenter.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import workwork.company.wstest.core.MyResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import workwork.company.wstest.domain.SharedPreferencesManager
import workwork.company.wstest.domain.models.diaries.DiaryModel
import workwork.company.wstest.domain.models.favorite.FavoriteData
import workwork.company.wstest.presenter.main.usecase.AddFavoriteUseCase
import workwork.company.wstest.presenter.main.usecase.GetDiariesUseCase

@HiltViewModel
class DiariesScreenViewModel @Inject constructor(
    private val getDiariesUseCase: GetDiariesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager,
) : ViewModel() {

    private val _diaryModelState = MutableStateFlow<MyResult<List<DiaryModel>>>(MyResult.Default)
    val diaryModelState: StateFlow<MyResult<List<DiaryModel>>> = _diaryModelState

    private val _insertFavoriteState = MutableStateFlow<MyResult<FavoriteData>>(MyResult.Default)
    val insertFavoriteState: StateFlow<MyResult<FavoriteData>> = _insertFavoriteState

    private val _favoriteIds = MutableStateFlow<List<String>>(emptyList())
    val favoriteIds: StateFlow<List<String>> = _favoriteIds

    init {
        loadFavoriteIds()
    }
    private fun loadFavoriteIds() {
        val ids = sharedPreferencesManager.getFavoriteIds()
        _favoriteIds.value = ids
    }
    fun getAllDiaries() {
        viewModelScope.launch {
            getDiariesUseCase.getAllDiaries().collect {
                _diaryModelState.value = it
            }
        }
    }
    fun insertFavorite(diaryId: String) {
        viewModelScope.launch {
            addFavoriteUseCase.insertFavorite(diaryId).collect {result ->
                _insertFavoriteState.value = result
                if (result is MyResult.Success) {
                    val updated = _favoriteIds.value.toMutableList()
                    if (!updated.contains(diaryId)) {
                        updated.add(diaryId)
                        _favoriteIds.value = updated
                        sharedPreferencesManager.saveFavoriteIds(updated)
                    }
                }
            }
        }
    }
    fun clearData() {
        _diaryModelState.value = MyResult.Default
        _insertFavoriteState.value = MyResult.Default
        _favoriteIds.value = emptyList()
    }
}