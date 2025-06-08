package workwork.company.wstest.presenter.auth.authStates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import workwork.company.wstest.core.MyResult
import workwork.company.wstest.R
import workwork.company.wstest.presenter.auth.AuthViewModel
import workwork.company.wstest.presenter.auth.data.FavoriteDiaryModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun AccountScreen(
    email: String,
    onSignOutClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val favoritesState by viewModel.favoritesState.collectAsState()
    val username = email.substringBefore("@")

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            color = Color.White,
            modifier = Modifier
                .width(250.dp)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_account),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = username,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { onSignOutClick() }) {
                    Text(text = "Sign out")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "to change the other account",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = Color.LightGray.copy(alpha = 0.2f),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (favoritesState) {
                            is MyResult.Loading -> {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                            is MyResult.Success -> {
                                val favorites = (favoritesState as MyResult.Success<List<FavoriteDiaryModel>>).data
                                Text(text = favorites.size.toString(), fontSize = 24.sp)
                            }
                            else -> {
                                Text(text = "-", fontSize = 24.sp)
                            }
                        }
                        Text(text = "Favorites", fontSize = 14.sp)
                    }
                }
            }
        }

        Surface(
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "My Favorites",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                when (favoritesState) {
                    is MyResult.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is MyResult.Success -> {
                        val favorites = (favoritesState as MyResult.Success<List<FavoriteDiaryModel>>).data

                        if (favorites.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No favorites yet",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Gray
                                )
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(favorites.sortedByDescending { it.favoriteDateTime }) { favorite ->
                                    FavoriteDiaryItem(favorite)
                                }
                            }
                        }
                    }
                    is MyResult.Failure -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Error loading favorites")
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
}

@Composable
fun FavoriteDiaryItem(
    item: FavoriteDiaryModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.1f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.diaryImage,
            contentDescription = "Image",
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.diaryTitle,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Published by ${item.username} • ${item.diaryUploadDateTime}",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.diaryMainText,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
        }
    }
}