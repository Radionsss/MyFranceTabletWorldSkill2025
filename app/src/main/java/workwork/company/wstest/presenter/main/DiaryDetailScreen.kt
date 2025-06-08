package workwork.company.wstest.presenter.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import workwork.company.wstest.R
import workwork.company.wstest.domain.models.diaries.DiaryModel


@Composable
fun DiaryDetailScreen(
    diary: DiaryModel, onCloseClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean,
    ) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(diary.diaryImage)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .size(24.dp)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(4.dp)
                    .clickable { onCloseClick() }

            )

            Icon(
                painter = painterResource(
                    id = if (isFavorite) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark
                ),                contentDescription = "Favorite",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(4.dp)
                    .clickable { onFavoriteClick() }

            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = diary.diaryTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(R.drawable.ic_copy),
                contentDescription = "Copy Title",
                tint = Color.Gray,
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        clipboardManager.setText(AnnotatedString(diary.diaryTitle))
                        Toast
                            .makeText(context, "Заголовок скопирован", Toast.LENGTH_SHORT)
                            .show()
                    }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Published by ${diary.diaryUploadUsername}  ${diary.diaryUploadDatetime}",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Main Text from: ${diary.diaryMainText}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DiaryDetailScreenPreview() {
    DiaryDetailScreen(
        diary = DiaryModel(
            diaryId = "ID_PREVIEW",
            diaryTitle = "Preview: A Journey Through Kazakhstan",
            diaryMainText = "resources/preview_trip.json",
            diaryUploadDatetime = "2025-04-28 15:30:00",
            diaryImage = "resources/preview_image.jpg",
            diaryUploadUsername = "Preview User"
        ),
        onCloseClick = {}, onFavoriteClick = {}, isFavorite = true
    )
}