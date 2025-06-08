package workwork.company.wstest.domain.models.sign

data class SignInRequest(
    val userEmailAddress: String,
    val userPassword: String
)
