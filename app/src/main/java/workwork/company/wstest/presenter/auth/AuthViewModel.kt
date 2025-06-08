package workwork.company.wstest.presenter.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import workwork.company.wstest.core.MyResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import workwork.company.wstest.domain.models.sign.SignInRequest
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import workwork.company.wstest.domain.SharedPreferencesManager
import workwork.company.wstest.presenter.auth.data.FavoriteDiaryModel
import workwork.company.wstest.presenter.auth.usecase.AuthUseCase
import workwork.company.wstest.presenter.auth.usecase.GetFavoritesUseCase

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager,
) : ViewModel() {

    private val _authState = MutableStateFlow<MyResult<Unit>>(MyResult.Default)
    val authState: StateFlow<MyResult<Unit>> = _authState

    private val _favoritesState = MutableStateFlow<MyResult<List<FavoriteDiaryModel>>>(MyResult.Default)
    val favoritesState: StateFlow<MyResult<List<FavoriteDiaryModel>>> = _favoritesState
    private val _isUserSignedIn = MutableStateFlow(checkIfUserSignedIn())
    val isUserSignedIn: StateFlow<Boolean> = _isUserSignedIn
    private val _email = MutableStateFlow(sharedPreferencesManager.getUserName()?:"")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private fun checkIfUserSignedIn(): Boolean {
        return sharedPreferencesManager.getAuthToken()?.isNotEmpty() == true
    }
    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }
    fun signIn() {
        viewModelScope.launch {
            val currentEmail = email.value
            val currentPassword = password.value
            authUseCase.signIn(SignInRequest(currentEmail, currentPassword)).collect {
                if (it is MyResult.Success){
                    _isUserSignedIn.value = true
                }
                _authState.value = it
            }
        }
    }

    fun getFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase.getFavorites().collect {
                _favoritesState.value = it
            }
        }
    }
    fun signOut() {
        sharedPreferencesManager.clearAuthToken()
        _authState.value = MyResult.Default
        _isUserSignedIn.value = false

    }
    fun resetState() {
        _authState.value = MyResult.Default
    }
}