package workwork.company.wstest.presenter.main

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import workwork.company.wstest.core.MyResult
import coil.compose.AsyncImage
import workwork.company.wstest.R
import workwork.company.wstest.domain.models.diaries.DiaryModel
import workwork.company.wstest.presenter.commons.LoadingDialog
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import android.content.ClipData
import android.content.ClipDescription
import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiariesScreen(
    selectedDiary: MutableState<DiaryModel?>
) {
    val viewModel: DiariesScreenViewModel = hiltViewModel()
    var showLoadingDialog by remember { mutableStateOf(false) }

    val diaryState by viewModel.diaryModelState.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val context = LocalContext.current
    val insertFavoriteState by viewModel.insertFavoriteState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getAllDiaries()
    }
    var isSwapped by remember { mutableStateOf(false) }
    LaunchedEffect(insertFavoriteState) {
        if (insertFavoriteState is MyResult.Failure) {
            showLoadingDialog = false
            val errorMessage =
                (insertFavoriteState as MyResult.Failure).exception.message ?: "Неизвестная ошибка"
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
        if (insertFavoriteState is MyResult.Loading) {
            showLoadingDialog = true
        }
        if (insertFavoriteState is MyResult.Success) {
            showLoadingDialog = false
        }
    }
    if (showLoadingDialog) {
        LoadingDialog()
    }
    LaunchedEffect(Unit) {
        viewModel.getAllDiaries()
    }

    Row {
        if (!isSwapped) {
            DiariesListColumn(
                diaryState = diaryState,
                selectedDiary = selectedDiary,
                onDiarySelected = { selectedDiary.value = it },
                modifier = Modifier.weight(1f)
            )
            DetailsSurface(
                selectedDiary = selectedDiary,
                favoriteIds = favoriteIds,
                onFavoriteClick = { diaryId -> viewModel.insertFavorite(diaryId) },
                onDrop = { isSwapped = true },
                modifier = Modifier.weight(1f)
            )
        } else {
            DetailsSurface(
                selectedDiary = selectedDiary,
                favoriteIds = favoriteIds,
                onFavoriteClick = { diaryId -> viewModel.insertFavorite(diaryId) },
                onDrop = { isSwapped = false },
                modifier = Modifier.weight(1f)
            )
            DiariesListColumn(
                diaryState = diaryState,
                selectedDiary = selectedDiary,
                onDiarySelected = { selectedDiary.value = it },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DiariesListColumn(
    selectedDiary: MutableState<DiaryModel?>,
    onDiarySelected: (DiaryModel) -> Unit,
    modifier: Modifier = Modifier,
    diaryState: MyResult<List<DiaryModel>>
) {
    Column(
        modifier = modifier

            //.fillMaxSize()
            .padding(8.dp)
            .dragAndDropSource {
                detectTapGestures(
                    onLongPress = {
                        startTransfer(
                            transferData = DragAndDropTransferData(
                                clipData = ClipData.newPlainText(
                                    "diaryId",
                                    "feojf"
                                )
                            )
                        )
                    }
                )
            }
    ) {
        Text(
            text = "Diaries",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (diaryState) {
            is MyResult.Default -> {
                Text("No diaries loaded.", modifier = Modifier.padding(8.dp))
            }

            is MyResult.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is MyResult.Failure -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Failed to load diaries", color = Color.Red)
                }
            }

            is MyResult.Success -> {
                val diaries = (diaryState as MyResult.Success<List<DiaryModel?>>).data
                if (diaries.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Нет доступных дневников",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(diaries) { diary ->
                            if (diary != null) {
                                Column(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .background(
                                            color = if (diary.diaryId == selectedDiary.value?.diaryId)
                                                Color.Red.copy(alpha = 0.2f)
                                            else
                                                Color.Transparent
                                        )
                                        .padding(8.dp)
                                        .clickable { onDiarySelected(diary) }
                                ) {
                                    AsyncImage(
                                        model = diary.diaryImage,
                                        contentDescription = null,
                                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp),
                                        placeholder = painterResource(id = R.drawable.images),
                                        error = painterResource(id = R.drawable.images)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = diary.diaryTitle,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "By ${diary.diaryUploadUsername}",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Start
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DetailsSurface(
    selectedDiary: MutableState<DiaryModel?>,
    favoriteIds: List<String>,
    onFavoriteClick: (String) -> Unit,
    onDrop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        modifier = modifier
            .fillMaxHeight()
            .dragAndDropTarget(
                shouldStartDragAndDrop = { event ->
                    event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                },
                target = remember {
                    object : DragAndDropTarget {
                        override fun onDrop(event: DragAndDropEvent): Boolean {
                            onDrop()
                            return true
                        }
                    }
                }
            )
    ) {
        if (selectedDiary .value!= null) {
            DiaryDetailScreen(
                diary = selectedDiary.value!!,
                isFavorite = favoriteIds.contains(selectedDiary.value!!.diaryId),
                onCloseClick = { selectedDiary.value =null},
                onFavoriteClick = { onFavoriteClick(selectedDiary.value!!.diaryId) }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.images),
                    contentDescription = "Welcome image",
                    modifier = Modifier.size(150.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Welcome to France",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}