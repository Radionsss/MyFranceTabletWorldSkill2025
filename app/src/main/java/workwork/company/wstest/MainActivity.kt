package workwork.company.wstest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint
import workwork.company.wstest.domain.models.diaries.DiaryModel
import workwork.company.wstest.presenter.auth.AuthScreen
import workwork.company.wstest.presenter.auth.AuthViewModel
import workwork.company.wstest.presenter.main.DiariesScreen
import workwork.company.wstest.presenter.main.DiariesScreenViewModel
import workwork.company.wstest.presenter.travel.TravelScreen
import workwork.company.wstest.ui.theme.WSTestTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WSTestTheme {
                TabletLayout()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
    }
}
@Composable
fun TabletLayout() {
    val diariesViewModel: DiariesScreenViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()
    var selectedItem by rememberSaveable { mutableStateOf("Home") }
    var selectedDiary = rememberSaveable { mutableStateOf<DiaryModel?>(null) }
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            color = colorResource(id = R.color.white),
            modifier = Modifier
                .width(250.dp)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.images),
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "My France",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                HorizontalDivider()

                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = selectedItem == "Home",
                    icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
                    onClick = {
                        diariesViewModel.clearData()
                        selectedItem = "Home"
                        selectedDiary .value= null
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Travel") },
                    selected = selectedItem == "Travel",
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_map),
                            contentDescription = null
                        )
                    },
                    onClick = {

                        selectedItem = "Travel"
                        selectedDiary.value = null
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Account") },
                    selected = selectedItem == "Account",
                    icon = {
                        Icon(
                            Icons.Outlined.AccountCircle,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        authViewModel.resetState()
                        selectedItem = "Account"
                        selectedDiary .value= null
                    }
                )

                Spacer(Modifier.height(24.dp))
            }
        }

        Surface(
            color = Color.White,
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.gray))
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                when (selectedItem) {
                    "Home" -> {
                        DiariesScreen(
                            selectedDiary=selectedDiary,
                        )
                    }
                    "Travel" -> {
                        TravelScreen()
                    }
                    "Account" -> {
                        AuthScreen()
                    }
                }
            }
        }
    }
}