package workwork.company.wstest.presenter.auth

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import workwork.company.wstest.core.MyResult
import workwork.company.wstest.R
import workwork.company.wstest.presenter.auth.authStates.AccountScreen
import workwork.company.wstest.presenter.commons.LoadingDialog
import java.util.regex.Pattern

@Composable
fun AuthScreen(

) {
    val viewModel: AuthViewModel = hiltViewModel()
    val authState by viewModel.authState.collectAsState()
    var showPassword by remember { mutableStateOf(false) }
    var showSignInForm by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    var showUserAgreement by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var showLoadingDialog by remember { mutableStateOf(false) }
    val isSignedIn by viewModel.isUserSignedIn.collectAsState()

    if (showUserAgreement) {
        UserAgreementDialog(onDismissRequest = { showUserAgreement = false })
    }
    LaunchedEffect(isSignedIn) {
        if (viewModel.isUserSignedIn.value){
            viewModel.getFavorites()
        }
    }
    LaunchedEffect(authState) {
        when (authState) {
            is MyResult.Loading -> {
                showLoadingDialog = true
            }

            is MyResult.Success -> {
                showLoadingDialog = false
                val mediaPlayer = MediaPlayer.create(context, R.raw.sign_in_success)
                mediaPlayer.start()
            }

            is MyResult.Failure -> {
                showLoadingDialog = false
                Toast.makeText(
                    context,
                    (authState as MyResult.Failure).exception.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> Unit
        }
    }

    if (showLoadingDialog) {
        LoadingDialog()
    }
    fun validateEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        )
        return emailPattern.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= 6 && password.any { it.isLetter() } && password.any { it.isDigit() }
    }
    if (isSignedIn) {
        AccountScreen(
            email = email.substringBefore("@"),
            onSignOutClick = {
                viewModel.signOut()
            }
        )
    } else {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFF5F5F5)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_account),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.White, shape = CircleShape)
                        .padding(20.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { showSignInForm = true },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = "Join us now")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "to discover more sights of France",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            if (showSignInForm) {
                Column(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight()
                        .padding(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Sign in/up",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(text = "Your Email Address", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    BasicTextField(
                        value = email,
                        onValueChange = {
                            viewModel.updateEmail(it)
                        },
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.LightGray.copy(alpha = 0.3f),
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(12.dp)
                    )
                    if (emailError) {
                        Text(
                            text = "Invalid Email",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Your Password", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.LightGray.copy(alpha = 0.3f),
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(12.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        BasicTextField(
                            value = password,
                            onValueChange = {
                                viewModel.updatePassword(it)
                            },
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Icon(
                            painter = painterResource(R.drawable.ic_visibility),
                            contentDescription = "Toggle Password Visibility",
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            showPassword = true
                                            tryAwaitRelease()
                                            showPassword = false
                                        }
                                    )
                                }
                        )
                    }
                    if (passwordError) {
                        Text(
                            text = "Invalid Password",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            emailError = !validateEmail(email)
                            passwordError = !validatePassword(password)

                            if (!emailError && !passwordError) {
                                viewModel.signIn()
                            } else if (passwordError) {
                                Toast.makeText(
                                    context,
                                    "Invalid Password: must contain letters and numbers and be at least 6 characters",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Sign in/up")
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "READ USER AGREEMENT",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.Blue,
                            fontSize = 12.sp
                        ),
                        modifier = Modifier.clickable { showUserAgreement = true }
                    )
                }
            }
        }
    }
}

@Composable
fun UserAgreementDialog(
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {},
        dismissButton = {},
        text = {
            Surface(modifier = Modifier.fillMaxSize()) {
                WebViewContent(onDismissRequest)
            }
        }
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewContent(
    onDismissRequest: () -> Unit
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()

                val customHtml = """
                    <html>
                    <head>
                        <style>
                            body { font-family: sans-serif; padding: 20px; }
                            button { 
                                background-color: #1976D2; 
                                color: white; 
                                padding: 10px 20px; 
                                border: none; 
                                border-radius: 8px; 
                                font-size: 16px;
                            }
                        </style>
                    </head>
                    <body>
                        <h2>User Agreement</h2>
                        <p>This is a fake user agreement text for testing purposes.</p>
                        <button onclick="window.Android.closeDialog()">Close</button>
                    </body>
                    </html>
                """.trimIndent()

                loadDataWithBaseURL(null, customHtml, "text/html", "UTF-8", null)

                addJavascriptInterface(
                    object {
                        @android.webkit.JavascriptInterface
                        fun closeDialog() {
                            onDismissRequest()
                        }
                    },
                    "Android"
                )
            }
        }
    )
}