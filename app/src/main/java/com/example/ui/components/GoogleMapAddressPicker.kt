package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.LightBlueBorder
import com.example.ui.theme.SoftLightBlue
import com.example.ui.theme.SuccessGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class KarachiMapLocation(
    val id: String,
    val areaName: String,
    val fullAddress: String,
    val lat: Double,
    val lng: Double,
    val offsetX: Float,
    val offsetY: Float
)

object KarachiPresetLocations {
    val presetList = listOf(
        KarachiMapLocation(
            id = "dha6",
            areaName = "DHA Phase 6, Karachi",
            fullAddress = "Plot 42-C, Street 15, Khayaban-e-Seher, DHA Phase 6",
            lat = 24.8214,
            lng = 67.0673,
            offsetX = 0f,
            offsetY = 120f
        ),
        KarachiMapLocation(
            id = "clifton",
            areaName = "Clifton (Blocks 1-9), Karachi",
            fullAddress = "Apt 204, Ocean Tower Lane, Block 2, Clifton",
            lat = 24.8283,
            lng = 67.0311,
            offsetX = -150f,
            offsetY = 80f
        ),
        KarachiMapLocation(
            id = "pechs",
            areaName = "PECHS (Blocks 1-6), Karachi",
            fullAddress = "House 18/2, Tariq Road, Block 3, PECHS",
            lat = 24.8719,
            lng = 67.0594,
            offsetX = 20f,
            offsetY = -100f
        ),
        KarachiMapLocation(
            id = "gulshan",
            areaName = "Gulshan-e-Iqbal, Karachi",
            fullAddress = "Flat A-5, Main University Road, Block 13-D, Gulshan-e-Iqbal",
            lat = 24.9180,
            lng = 67.0971,
            offsetX = 180f,
            offsetY = -220f
        ),
        KarachiMapLocation(
            id = "nazimabad",
            areaName = "North Nazimabad, Karachi",
            fullAddress = "House B-102, Shahrah-e-Jahangir, Block H, North Nazimabad",
            lat = 24.9389,
            lng = 67.0350,
            offsetX = -110f,
            offsetY = -280f
        ),
        KarachiMapLocation(
            id = "bahria",
            areaName = "Bahria Town, Karachi",
            fullAddress = "Villa 145, Precinct 1, Main Jinnah Avenue, Bahria Town",
            lat = 25.0125,
            lng = 67.3110,
            offsetX = 320f,
            offsetY = -400f
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleMapAddressPickerModal(
    initialArea: String,
    initialStreetAddress: String,
    onLocationConfirmed: (selectedArea: String, selectedStreetAddress: String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier.testTag("google_map_address_picker_sheet")
    ) {
        GoogleMapPickerContent(
            initialArea = initialArea,
            initialStreetAddress = initialStreetAddress,
            onConfirm = { area, street ->
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onLocationConfirmed(area, street)
                    }
                }
            },
            onClose = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onDismiss()
                    }
                }
            }
        )
    }
}

@Composable
fun GoogleMapPickerContent(
    initialArea: String,
    initialStreetAddress: String,
    onConfirm: (selectedArea: String, selectedStreetAddress: String) -> Unit,
    onClose: () -> Unit
) {
    var isSatelliteMode by remember { mutableStateOf(false) }
    var zoomLevel by remember { mutableFloatStateOf(1.0f) }
    var mapOffsetX by remember { mutableFloatStateOf(0f) }
    var mapOffsetY by remember { mutableFloatStateOf(100f) }

    var selectedAreaName by remember { mutableStateOf(if (initialArea.isNotBlank()) initialArea else "DHA Phase 1-8, Karachi") }
    var selectedStreetAddress by remember { mutableStateOf(if (initialStreetAddress.isNotBlank()) initialStreetAddress else "Plot 42-C, Street 15, Khayaban-e-Seher, DHA Phase 6") }
    var currentLat by remember { mutableStateOf(24.8214) }
    var currentLng by remember { mutableStateOf(67.0673) }

    var isLocatingGps by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Reverse geocode calculation when dragging map
    fun updateGeocodeFromOffset(offsetX: Float, offsetY: Float) {
        val nearPreset = KarachiPresetLocations.presetList.minByOrNull { loc ->
            val dx = loc.offsetX - offsetX
            val dy = loc.offsetY - offsetY
            dx * dx + dy * dy
        } ?: KarachiPresetLocations.presetList.first()

        selectedAreaName = nearPreset.areaName
        currentLat = nearPreset.lat + (offsetY / 10000.0)
        currentLng = nearPreset.lng + (offsetX / 10000.0)

        val houseNum = (10 + (kotlin.math.abs(offsetX.toInt()) % 90))
        val stNum = (1 + (kotlin.math.abs(offsetY.toInt()) % 30))
        val shortAreaName = nearPreset.areaName.substringBefore(",")
        selectedStreetAddress = "House $houseNum-B, Street $stNum, $shortAreaName"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .testTag("google_map_picker_content"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Top Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = DeepBlue,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Google Maps Location Picker",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A)
                    )
                }
                Text(
                    text = "Drag pin or search Karachi neighborhood",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }

            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
            }
        }

        // Search Bar for Karachi Areas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    val match = KarachiPresetLocations.presetList.firstOrNull {
                        it.areaName.contains(query, ignoreCase = true) || it.fullAddress.contains(query, ignoreCase = true)
                    }
                    if (match != null) {
                        mapOffsetX = match.offsetX
                        mapOffsetY = match.offsetY
                        selectedAreaName = match.areaName
                        selectedStreetAddress = match.fullAddress
                        currentLat = match.lat
                        currentLng = match.lng
                    }
                },
                placeholder = { Text("Search DHA, Clifton, PECHS, Gulshan...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DeepBlue) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepBlue,
                    unfocusedBorderColor = LightBlueBorder,
                    focusedContainerColor = Color(0xFFF8FAFC),
                    unfocusedContainerColor = Color(0xFFF8FAFC)
                ),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("map_search_textfield")
            )
        }

        // Quick Preset Area Chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
        ) {
            items(KarachiPresetLocations.presetList) { preset ->
                val isSelected = selectedAreaName == preset.areaName
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (isSelected) DeepBlue else SoftLightBlue)
                        .border(1.dp, if (isSelected) DeepBlue else LightBlueBorder, RoundedCornerShape(50))
                        .clickable {
                            mapOffsetX = preset.offsetX
                            mapOffsetY = preset.offsetY
                            selectedAreaName = preset.areaName
                            selectedStreetAddress = preset.fullAddress
                            currentLat = preset.lat
                            currentLng = preset.lng
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("preset_chip_${preset.id}")
                ) {
                    Text(
                        text = preset.areaName.substringBefore(","),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else DeepBlue
                    )
                }
            }
        }

        // Interactive Map Box Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, LightBlueBorder, RoundedCornerShape(20.dp))
                .background(if (isSatelliteMode) Color(0xFF1E293B) else Color(0xFFE2E8F0))
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        mapOffsetX += dragAmount.x / zoomLevel
                        mapOffsetY += dragAmount.y / zoomLevel
                        updateGeocodeFromOffset(mapOffsetX, mapOffsetY)
                    }
                }
                .testTag("interactive_karachi_map_canvas")
        ) {
            // Map Canvas Drawing (Karachi Roads, Coastline, Grid)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                val centerX = canvasWidth / 2f + mapOffsetX * zoomLevel
                val centerY = canvasHeight / 2f + mapOffsetY * zoomLevel

                if (!isSatelliteMode) {
                    // Standard Vector Map Background
                    drawRect(color = Color(0xFFE0F2FE)) // Light land/sea base

                    // Arabian Sea & Clifton / Defense Coastline
                    val seaPath = Path().apply {
                        moveTo(0f, centerY + 180f * zoomLevel)
                        quadraticTo(
                            centerX - 100f * zoomLevel, centerY + 220f * zoomLevel,
                            centerX + 250f * zoomLevel, centerY + 350f * zoomLevel
                        )
                        lineTo(canvasWidth, canvasHeight)
                        lineTo(0f, canvasHeight)
                        close()
                    }
                    drawPath(path = seaPath, color = Color(0xFF38BDF8).copy(alpha = 0.65f))

                    // Major Roads / Highways Grid
                    val roadColor = Color.White
                    val majorRoadColor = Color(0xFFFDE047) // Shahrah-e-Faisal / Expressways

                    // Shahrah-e-Faisal Expressway (Diagonal Main Arterial)
                    drawLine(
                        color = majorRoadColor,
                        start = Offset(centerX - 350f * zoomLevel, centerY - 150f * zoomLevel),
                        end = Offset(centerX + 400f * zoomLevel, centerY + 50f * zoomLevel),
                        strokeWidth = 14f * zoomLevel
                    )

                    // Sunset Boulevard & Khayaban-e-Ittehad
                    drawLine(
                        color = roadColor,
                        start = Offset(centerX - 250f * zoomLevel, centerY + 80f * zoomLevel),
                        end = Offset(centerX + 300f * zoomLevel, centerY + 180f * zoomLevel),
                        strokeWidth = 10f * zoomLevel
                    )

                    // Grid Street Lines
                    for (i in -5..5) {
                        drawLine(
                            color = roadColor.copy(alpha = 0.8f),
                            start = Offset(centerX + (i * 90f) * zoomLevel, 0f),
                            end = Offset(centerX + (i * 90f) * zoomLevel, canvasHeight),
                            strokeWidth = 5f * zoomLevel
                        )
                        drawLine(
                            color = roadColor.copy(alpha = 0.8f),
                            start = Offset(0f, centerY + (i * 90f) * zoomLevel),
                            end = Offset(canvasWidth, centerY + (i * 90f) * zoomLevel),
                            strokeWidth = 5f * zoomLevel
                        )
                    }

                    // Green Parks
                    drawRoundRect(
                        color = Color(0xFF86EFAC).copy(alpha = 0.6f),
                        topLeft = Offset(centerX - 120f * zoomLevel, centerY + 40f * zoomLevel),
                        size = Size(80f * zoomLevel, 60f * zoomLevel),
                        cornerRadius = CornerRadius(16f, 16f)
                    )
                } else {
                    // Satellite Aerial Mode
                    drawRect(color = Color(0xFF0F172A)) // Dark satellite ground

                    val seaPath = Path().apply {
                        moveTo(0f, centerY + 180f * zoomLevel)
                        quadraticTo(
                            centerX - 100f * zoomLevel, centerY + 220f * zoomLevel,
                            centerX + 250f * zoomLevel, centerY + 350f * zoomLevel
                        )
                        lineTo(canvasWidth, canvasHeight)
                        lineTo(0f, canvasHeight)
                        close()
                    }
                    drawPath(path = seaPath, color = Color(0xFF0284C7).copy(alpha = 0.7f))

                    // Roads on Satellite View
                    drawLine(
                        color = Color(0xFF94A3B8).copy(alpha = 0.7f),
                        start = Offset(centerX - 350f * zoomLevel, centerY - 150f * zoomLevel),
                        end = Offset(centerX + 400f * zoomLevel, centerY + 50f * zoomLevel),
                        strokeWidth = 10f * zoomLevel
                    )
                }

                // Preset Landmark Markers on the Map Canvas
                KarachiPresetLocations.presetList.forEach { loc ->
                    val posX = centerX + (loc.offsetX - mapOffsetX) * zoomLevel
                    val posY = centerY + (loc.offsetY - mapOffsetY) * zoomLevel

                    if (posX in -50f..(canvasWidth + 50f) && posY in -50f..(canvasHeight + 50f)) {
                        drawCircle(
                            color = DeepBlue.copy(alpha = 0.25f),
                            radius = 18f * zoomLevel,
                            center = Offset(posX, posY)
                        )
                        drawCircle(
                            color = DeepBlue,
                            radius = 8f * zoomLevel,
                            center = Offset(posX, posY)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 3.5f * zoomLevel,
                            center = Offset(posX, posY)
                        )
                    }
                }
            }

            // Fixed Center Pin Overlay (Google Maps Drop Pin Style)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 32.dp) // Offset pin tip to exact center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 4.dp
                    ) {
                        Text(
                            text = "Set Pickup Here",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepBlue,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Pin",
                        tint = Color(0xFFDC2626), // Google Maps Red
                        modifier = Modifier
                            .size(44.dp)
                            .shadow(8.dp, CircleShape)
                    )
                }
            }

            // Central Pin Shadow Target Dot
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.35f))
            )

            // Top Overlay: Satellite / Layer Toggle & Watermark
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    onClick = { isSatelliteMode = !isSatelliteMode },
                    color = Color.White.copy(alpha = 0.92f),
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Layers, contentDescription = null, tint = DeepBlue, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isSatelliteMode) "Satellite" else "Map View",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepBlue
                        )
                    }
                }
            }

            // Bottom-Right Controls: Zoom & GPS Locate
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // GPS Locate Button
                Surface(
                    onClick = {
                        isLocatingGps = true
                        mapOffsetX = 0f
                        mapOffsetY = 120f
                        val loc = KarachiPresetLocations.presetList.first()
                        selectedAreaName = loc.areaName
                        selectedStreetAddress = loc.fullAddress
                        currentLat = loc.lat
                        currentLng = loc.lng
                    },
                    color = Color.White,
                    shape = CircleShape,
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isLocatingGps) {
                            CircularProgressIndicator(color = DeepBlue, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
                        } else {
                            Icon(Icons.Default.GpsFixed, contentDescription = "GPS", tint = DeepBlue, modifier = Modifier.size(20.dp))
                        }
                    }
                }

                // Zoom Controls
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 4.dp
                ) {
                    Column {
                        IconButton(
                            onClick = { zoomLevel = (zoomLevel + 0.25f).coerceAtMost(2.5f) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = Color(0xFF0F172A))
                        }
                        IconButton(
                            onClick = { zoomLevel = (zoomLevel - 0.25f).coerceAtLeast(0.75f) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Zoom Out", tint = Color(0xFF0F172A))
                        }
                    }
                }
            }

            // Reset GPS Locating State
            LaunchedEffect(isLocatingGps) {
                if (isLocatingGps) {
                    delay(600)
                    isLocatingGps = false
                }
            }
        }

        // Pinned Location Summary Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SoftLightBlue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pinned Google Maps Location",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepBlue
                        )
                    }

                    Text(
                        text = "GPS: ${"%.4f".format(currentLat)}° N, ${"%.4f".format(currentLng)}° E",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B)
                    )
                }

                // Editable Street Address Input
                OutlinedTextField(
                    value = selectedStreetAddress,
                    onValueChange = { selectedStreetAddress = it },
                    label = { Text("House / Building / Street Address") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepBlue,
                        unfocusedBorderColor = LightBlueBorder,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("pinned_address_input_field")
                )
            }
        }

        // Confirm Button
        Button(
            onClick = {
                onConfirm(selectedAreaName, selectedStreetAddress)
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DeepBlue, contentColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 16.dp)
                .testTag("confirm_pinned_location_button")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
                Text(
                    text = "Confirm & Save Pinned Address",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
