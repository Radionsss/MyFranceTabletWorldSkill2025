package workwork.company.wstest.presenter.travel

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import workwork.company.wstest.R
import workwork.company.wstest.databinding.TravelScreenBinding
import java.io.OutputStream

@Composable
fun TravelScreen() {
    val context = LocalContext.current
    val attractions = remember { loadAttractions(context) }
    val mapViewHolder = remember { mutableStateOf<MapView?>(null) }
    var selectedAttraction by remember { mutableStateOf<AttractionModel?>(null) }
    var isDrawingMode by remember { mutableStateOf(false) }
    val drawnPoints = remember { mutableStateListOf<Point>() }
    var polylineObject by remember { mutableStateOf<PolylineMapObject?>(null) }
    if (selectedAttraction != null) {
        AttractionDialog(
            attraction = selectedAttraction!!,
            onDismiss = { selectedAttraction = null }
        )
    }
    val placemarkListeners = remember { mutableStateListOf<MapObjectTapListener>() }

    val mapInputListener = remember {
        object : InputListener {
            override fun onMapTap(map: Map, point: Point) {
                if (isDrawingMode) {
                    drawnPoints.add(point)

                    polylineObject?.let { map.mapObjects.remove(it) }
                    polylineObject = map.mapObjects.addPolyline(Polyline(drawnPoints)).apply {
                        setStrokeColor(0xFFFF0000.toInt())
                        strokeWidth = 4f
                    }
                }
            }

            override fun onMapLongTap(map: Map, point: Point) {
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { context ->

                val binding = TravelScreenBinding.inflate(
                    android.view.LayoutInflater.from(context)
                )
                val mapView = binding.mapView
                mapViewHolder.value = mapView
                mapView.mapWindow.map.isZoomGesturesEnabled = true
                mapView.mapWindow.map.isScrollGesturesEnabled = true
                mapView.mapWindow.map.isTiltGesturesEnabled = true
                mapView.mapWindow.map.isRotateGesturesEnabled = true

                mapView.mapWindow.map.move(
                        CameraPosition(
                            attractions.firstOrNull()?.location ?: Point(55.751244, 37.618423),
                            14.0f, 0.0f, 0.0f
                        ),
                        Animation(Animation.Type.SMOOTH, 0f),
                        null
                    )


                    attractions.forEach { attraction ->
                        val placemark = mapView.mapWindow.map.mapObjects.addPlacemark(
                            attraction.location,
                            getResizedMarker(context, R.drawable.ic_marker_map, 64, 64)
                        )

                        val tapListener = MapObjectTapListener { _, _ ->
                            selectedAttraction = attraction
                            true
                        }

                        placemark.addTapListener(tapListener)
                        placemarkListeners.add(tapListener)
                    }
                mapView.mapWindow.map.addInputListener(mapInputListener)
                binding.root

            },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    mapViewHolder.value?.let { mapView ->
                        val currentPosition = mapView.mapWindow.map.cameraPosition
                        mapView.mapWindow.map.move(
                            CameraPosition(
                                currentPosition.target,
                                currentPosition.zoom + 1f,
                                currentPosition.azimuth,
                                currentPosition.tilt
                            ),
                            Animation(Animation.Type.SMOOTH, 0.3f),
                            null
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    painterResource(R.drawable.ic_plus),
                    contentDescription = "Zoom In",
                    tint = Color.White
                )
            }

            FloatingActionButton(
                onClick = {
                    mapViewHolder.value?.let { mapView ->
                        val currentPosition = mapView.mapWindow.map.cameraPosition
                        mapView.mapWindow.map.move(
                            CameraPosition(
                                currentPosition.target,
                                currentPosition.zoom - 1f,
                                currentPosition.azimuth,
                                currentPosition.tilt
                            ),
                            Animation(Animation.Type.SMOOTH, 0.3f),
                            null
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    painterResource(R.drawable.ic_minus),
                    contentDescription = "Zoom Out",
                    tint = Color.White
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FloatingActionButton(
                onClick = {isDrawingMode=true },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    painterResource(R.drawable.ic_draw),
                    contentDescription = "Change map style",
                    tint = Color.White
                )
            }
            FloatingActionButton(
                onClick = {
                    mapViewHolder.value?.let { mapView ->
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                val bitmap = captureMapViewBitmap(mapView)
                                saveBitmapToGallery(context, bitmap)
                                Toast.makeText(context, "Saved to Gallery", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error saving screenshot: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }                },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    painterResource(R.drawable.ic_download),
                    contentDescription = "Download",
                    tint = Color.White
                )
            }
        }
    }
}
suspend fun captureMapViewBitmap(mapView: MapView): Bitmap = withContext(Dispatchers.Main) {
    val screenshot = mapView.screenshot
    screenshot
        ?: throw IllegalStateException("Failed to capture screenshot. Try setting app:movable=\"true\".")
}

fun getResizedMarker(context: Context, resId: Int, width: Int, height: Int): ImageProvider {
    val drawable = ContextCompat.getDrawable(context, resId)
    val bitmap = when (drawable) {
        is BitmapDrawable -> drawable.bitmap
        is VectorDrawable -> drawable.toBitmap(width = width, height = height)
        else -> throw IllegalArgumentException("Unsupported drawable type or resource not found.")
    }
    return ImageProvider.fromBitmap(bitmap)
}

fun loadAttractions(context: Context): List<AttractionModel> {
    val jsonString =
        context.assets.open("attractions.json").bufferedReader().use { it.readText() }
    val jsonArray = JSONArray(jsonString)

    val attractions = mutableListOf<AttractionModel>()

    for (i in 0 until jsonArray.length()) {
        val item = jsonArray.getJSONObject(i)

        val name = item.getString("name")
        val description = item.getString("description")
        val imageName = item.getString("image")
        val latitude = item.getDouble("latitude")
        val longitude = item.getDouble("longitude")

        attractions.add(
            AttractionModel(
                name = name,
                description = description,
                image = imageName,
                location = Point(latitude, longitude)
            )
        )
    }
    return attractions
}
@Composable
fun AttractionDialog(attraction: AttractionModel, onDismiss: () -> Unit) {
    val context = LocalContext.current
    var isFullImageDialogOpen by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { },
        confirmButton = { },
        dismissButton = { },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable { onDismiss() }
                        .padding(8.dp)
                        .size(24.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = attraction.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = attraction.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                val uri = Uri.parse("geo:${attraction.location.latitude},${attraction.location.longitude}?q=${attraction.name}")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Go Now!",
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = attraction.image,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(Color.Gray)
                                .clickable {
                                    isFullImageDialogOpen = true
                                }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AndroidView(
                            factory = { context ->
                                MapView(context).apply {
                                    mapWindow.map.move(
                                        CameraPosition(
                                            attraction.location,
                                            16.0f,
                                            0.0f,
                                            0.0f
                                        )
                                    )

                                    post {
                                        mapWindow.map.mapObjects.addPlacemark(
                                            attraction.location,
                                            getResizedMarker(context, R.drawable.ic_marker_map, 48, 48)
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.5f)
                        )
                    }
                }
            }
        }
    )

    if (isFullImageDialogOpen) {
        Dialog(
            onDismissRequest = { isFullImageDialogOpen = false }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = attraction.image,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Image",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(32.dp)
                        .clickable { isFullImageDialogOpen = false },
                    tint = Color.White
                )
            }
        }
    }
}
suspend fun saveBitmapToGallery(context: Context, bitmap: Bitmap) {
    withContext(Dispatchers.IO) {
        val filename = "map_snapshot_${System.currentTimeMillis()}.png"
        val resolver = context.contentResolver
        val contentValues = android.content.ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Maps/")
        }
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            val outputStream: OutputStream? = resolver.openOutputStream(it)
            outputStream?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }
        }
    }
}
data class AttractionModel(
    val name: String,
    val description: String,
    val image: String,
    val location: Point
)