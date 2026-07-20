package com.kodex.guide.presentation.details.parallaxScreen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun MapScreen(
    destinationLat: Double,
    destinationLng: Double,
    destinationTitle: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var userLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var routePoints by remember { mutableStateOf<List<GeoPoint>>(emptyList()) }

    // Инициализация osmdroid
    LaunchedEffect(Unit) {
        Configuration.getInstance().apply {
            userAgentValue = context.packageName
            osmdroidBasePath = context.filesDir
            osmdroidTileCache = context.cacheDir
        }
    }

    // Получение текущей позиции пользователя
    LaunchedEffect(Unit) {
        userLocation = getUserLocation(context)
    }

    // Построение маршрута при изменении userLocation
    LaunchedEffect(userLocation) {
        if (userLocation != null) {
            val route = fetchRoute(
                startLat = userLocation!!.latitude,
                startLng = userLocation!!.longitude,
                endLat = destinationLat,
                endLng = destinationLng
            )
            routePoints = route
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Маршрут до: $destinationTitle") },
        text = {
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    factory = { ctx ->
                        createMapView(ctx, userLocation, destinationLat, destinationLng, destinationTitle, routePoints)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
            }
        }
    )
}

private fun createMapView(
    context: Context,
    userLocation: GeoPoint?,
    destLat: Double,
    destLng: Double,
    destTitle: String,
    routePoints: List<GeoPoint>
): MapView {
    val mapView = MapView(context).apply {
        setTileSource(TileSourceFactory.MAPNIK)
        setMultiTouchControls(true)
        isClickable = true
        isFocusable = true
    }

    // ✅ Исправление 1: используем zoomToBoundingBox вместо inflateByLatitude
    if (userLocation != null) {
        val boundingBox = BoundingBox.fromGeoPoints(
            listOf(userLocation, GeoPoint(destLat, destLng))
        )
        // Автоматически подбирает зум и центр, чтобы оба маркера были видны
        mapView.post {
            mapView.zoomToBoundingBox(boundingBox, true, 100) // 100 — padding в пикселях
        }
    } else {
        mapView.controller.setCenter(GeoPoint(destLat, destLng))
        mapView.controller.setZoom(15.0)
    }

    // Маркер назначения (адрес книги)
    val destMarker = Marker(mapView).apply {
        position = GeoPoint(destLat, destLng)
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        title = destTitle
        // Используем стандартную иконку маркера osmdroid
        setIcon(ContextCompat.getDrawable(context, android.R.drawable.ic_menu_mylocation))
    }
    destMarker.setOnMarkerClickListener { _, _ -> true }
    mapView.overlays.add(destMarker)

    // Маркер пользователя
    userLocation?.let { userPoint ->
        val userMarker = Marker(mapView).apply {
            position = userPoint
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            title = "Вы здесь"
            setIcon(ContextCompat.getDrawable(context, android.R.drawable.ic_menu_myplaces))
        }
        mapView.overlays.add(userMarker)
    }

    // Линия маршрута
    if (routePoints.isNotEmpty()) {
        val routeLine = Polyline().apply {
            setPoints(routePoints)
            outlinePaint.apply {
                color = Color.BLUE
                strokeWidth = 12f
                style = Paint.Style.STROKE
            }
        }
        mapView.overlays.add(routeLine)
    }

    // ✅ Исправление 2: используем setFollowLocationEnabled вместо свойства
    val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
    locationOverlay.enableMyLocation()
    locationOverlay.enableFollowLocation() // ← правильный метод
    mapView.overlays.add(locationOverlay)

    return mapView
}

private fun getUserLocation(context: Context): GeoPoint? {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    if (ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        Log.w("MapScreen", "Нет разрешения на доступ к геолокации")
        return null
    }

    val providers = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
    for (provider in providers) {
        try {
            val location: Location? = locationManager.getLastKnownLocation(provider)
            if (location != null) {
                return GeoPoint(location.latitude, location.longitude)
            }
        } catch (e: SecurityException) {
            Log.e("MapScreen", "No permission for $provider")
        }
    }
    return null
}

private suspend fun fetchRoute(
    startLat: Double,
    startLng: Double,
    endLat: Double,
    endLng: Double
): List<GeoPoint> {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val url = "https://router.project-osrm.org/route/v1/driving/" +
                    "$startLng,$startLat;$endLng,$endLat?overview=full&geometries=geojson"

            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: return@withContext emptyList()

            val json = JsonParser.parseString(responseBody).asJsonObject
            val routes = json.getAsJsonArray("routes")
            if (routes.size() == 0) return@withContext emptyList()

            val route = routes[0].asJsonObject
            val geometry = route.getAsJsonObject("geometry")
            val coordinates = geometry.getAsJsonArray("coordinates")

            coordinates.map { coord ->
                val lng = coord.asJsonArray[0].asDouble
                val lat = coord.asJsonArray[1].asDouble
                GeoPoint(lat, lng)
            }
        } catch (e: Exception) {
            Log.e("MapScreen", "Route fetch error: ${e.message}")
            emptyList()
        }
    }
}