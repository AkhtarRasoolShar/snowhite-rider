package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PickupSchedule
import com.example.data.model.Hub

import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import com.example.data.repository.CatalogData
import com.example.ui.components.GoogleMapAddressPickerModal
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.LightBlueBorder
import com.example.ui.theme.SoftLightBlue

typealias PickupScheduleEntity = PickupSchedule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupSchedulingScreen(
    pickupSchedule: PickupSchedule,
    availableHubs: List<Hub>,
    selectedHub: Hub?,
    onHubSelected: (Hub) -> Unit,
    onScheduleUpdated: (String?, String?, String?, String?, String?) -> Unit,
    totalCartCount: Int,
    totalPricePKR: Int,
    isSubmitting: Boolean,
    onConfirmOrderClick: () -> Unit,
    onBackClick: () -> Unit = {}
) {
    var isAreaDropdownExpanded by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var isMapPickerVisible by remember { mutableStateOf(false) }

    // Auto-Select Hub Based on Location (Area)
    LaunchedEffect(pickupSchedule.area, availableHubs) {
        val currentArea = pickupSchedule.area.trim()
        if (currentArea.isNotBlank() && availableHubs.isNotEmpty()) {
            val matchedHub = availableHubs.firstOrNull { hub ->
                val hubName = hub.name.orEmpty().trim()
                val hubAddress = hub.address.orEmpty().trim()
                if (hubName.isBlank()) {
                    false
                } else {
                    // Check if hub name matches or is contained in the area text, or vice versa (ignore case)
                    currentArea.contains(hubName, ignoreCase = true) ||
                    hubName.contains(currentArea, ignoreCase = true) ||
                    // Check individual meaningful location tokens (e.g. Clifton, DHA, Gulshan, PECHS, Nazimabad, Malir, Bahria, Saddar)
                    currentArea.split(" ", ",", "/", "-", "(", ")")
                        .map { it.trim().lowercase() }
                        .filter { it.length > 2 && it !in listOf("karachi", "scheme", "block", "blocks", "phase", "road", "near", "sector") }
                        .any { word ->
                            hubName.lowercase().contains(word) || hubAddress.lowercase().contains(word)
                        }
                }
            }
            if (matchedHub != null && matchedHub.id != selectedHub?.id) {
                onHubSelected(matchedHub)
            }
        }
    }

    val datesList = listOf(
        "Today, 31st Aug",
        "Tomorrow, 1st Sept",
        "Wednesday, 2nd Sept",
        "Thursday, 3rd Sept"
    )

    val timeSlotsList = listOf(
        "Morning (8:00 AM - 12:00 PM)",
        "Afternoon (12:00 PM - 4:00 PM)",
        "Evening (4:00 PM - 8:00 PM)",
        "Night (8:00 PM - 10:00 PM)"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .navigationBarsPadding()
            .testTag("pickup_scheduling_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with Back Button
            item(key = "header") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("checkout_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Schedule Pickup & Address",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "SnoWhite rider will collect your garments from your doorstep in Karachi",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Section 1: Address & Karachi Area Picker
            item(key = "address_section") {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, LightBlueBorder, RoundedCornerShape(20.dp))
                ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = DeepBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pickup & Delivery Location",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(SoftLightBlue)
                            .clickable { isMapPickerVisible = true }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                            .testTag("pin_on_map_header_badge")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Map, contentDescription = null, tint = DeepBlue, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Map Pin", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
                        }
                    }
                }

                // Google Maps Interactive Location Pin Banner Card
                Card(
                    onClick = { isMapPickerVisible = true },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBlueBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("google_map_pin_card_button")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(DeepBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "Google Maps Pin",
                                tint = Color(0xFFEF4444), // Red Google Maps Pin
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Pin Location on Google Maps",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepBlue
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.MyLocation, contentDescription = null, tint = DeepBlue, modifier = Modifier.size(12.dp))
                            }
                            Text(
                                text = if (pickupSchedule.streetAddress.isNotBlank())
                                    "${pickupSchedule.streetAddress}, ${pickupSchedule.area}"
                                else
                                    "Tap to select exact location pin on Karachi map",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )
                        }

                        Button(
                            onClick = { isMapPickerVisible = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepBlue, contentColor = Color.White),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("Open Map", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Area Picker Dropdown
                ExposedDropdownMenuBox(
                    expanded = isAreaDropdownExpanded,
                    onExpandedChange = { isAreaDropdownExpanded = !isAreaDropdownExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = pickupSchedule.area,
                        onValueChange = { newArea ->
                            onScheduleUpdated(newArea, null, null, null, null)
                        },
                        readOnly = false,
                        label = { Text("Karachi Area / Sector") },
                        placeholder = { Text("Type or select (e.g. Clifton, DHA)") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAreaDropdownExpanded)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeepBlue,
                            unfocusedBorderColor = LightBlueBorder
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("area_picker_dropdown")
                    )

                    DropdownMenu(
                        expanded = isAreaDropdownExpanded,
                        onDismissRequest = { isAreaDropdownExpanded = false },
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth(0.9f)
                    ) {
                        CatalogData.karachiAreas.forEach { area ->
                            DropdownMenuItem(
                                text = { Text(area, fontSize = 13.sp) },
                                onClick = {
                                    onScheduleUpdated(area, null, null, null, null)
                                    isAreaDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Street Address Field
                OutlinedTextField(
                    value = pickupSchedule.streetAddress,
                    onValueChange = { onScheduleUpdated(null, it, null, null, null) },
                    label = { Text("House / Apartment / Street Address") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = DeepBlue) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepBlue,
                        unfocusedBorderColor = LightBlueBorder
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("street_address_textfield")
                )
                
                // Select Nearest Hub Dropdown (ExposedDropdownMenuBox)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedHub?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Nearest Hub *") },
                        placeholder = { Text("Select Nearest Hub") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeepBlue,
                            unfocusedBorderColor = LightBlueBorder
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("hub_picker_dropdown")
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Color.White)
                    ) {
                        if (availableHubs.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Loading hubs...", fontSize = 13.sp, color = Color.Gray) },
                                onClick = { expanded = false }
                            )
                        } else {
                            availableHubs.forEach { hub ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = hub.name ?: "Hub",
                                                    fontSize = 13.sp,
                                                    fontWeight = if (hub.id == selectedHub?.id) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (hub.id == selectedHub?.id) DeepBlue else Color(0xFF0F172A)
                                                )
                                                if (hub.id == selectedHub?.id) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = "Selected",
                                                        tint = DeepBlue,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }
                                            if (!hub.address.isNullOrBlank()) {
                                                Text(
                                                    text = "${hub.address}${if (!hub.city.isNullOrBlank()) ", ${hub.city}" else ""}",
                                                    fontSize = 11.sp,
                                                    color = Color(0xFF64748B)
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        onHubSelected(hub)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                if (selectedHub != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Assigned Hub: ${selectedHub.name}",
                            fontSize = 11.sp,
                            color = Color(0xFF059669),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

            // Section 2: Date Selector
            item(key = "date_section") {
                Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LightBlueBorder, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = DeepBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Select Pickup Date",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    datesList.forEach { date ->
                        val isSelected = pickupSchedule.date == date
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) SoftLightBlue else Color(0xFFF8FAFC))
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) DeepBlue else LightBlueBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { onScheduleUpdated(null, null, date, null, null) }
                                .padding(12.dp)
                                .testTag("date_slot_$date")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = date,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) DeepBlue else MaterialTheme.colorScheme.onBackground,
                                    fontSize = 13.sp
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = DeepBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

            // Section 3: Time Slot Selector
            item(key = "time_section") {
                Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LightBlueBorder, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = DeepBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Preferred Time Slot",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    timeSlotsList.forEach { slot ->
                        val isSelected = pickupSchedule.timeSlot == slot
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) SoftLightBlue else Color(0xFFF8FAFC))
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) DeepBlue else LightBlueBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { onScheduleUpdated(null, null, null, slot, null) }
                                .padding(12.dp)
                                .testTag("time_slot_$slot")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = slot,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) DeepBlue else MaterialTheme.colorScheme.onBackground,
                                    fontSize = 13.sp
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = DeepBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

            // Section 4: Special Instructions
            item(key = "special_instructions_section") {
                OutlinedTextField(
                    value = pickupSchedule.specialNotes,
                    onValueChange = { onScheduleUpdated(null, null, null, null, it) },
                    label = { Text("Special Garment Notes (e.g. Collar stain, Extra Starch)") },
                    leadingIcon = { Icon(Icons.Default.EditNote, contentDescription = null, tint = DeepBlue) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepBlue,
                        unfocusedBorderColor = LightBlueBorder,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("special_instructions_textfield")
                )
            }
        }

        // Order Total & Submit Sticky Bottom CTA Button (Outside the LazyColumn)
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = onConfirmOrderClick,
                    enabled = !isSubmitting && totalCartCount > 0,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("confirm_and_book_order_button")
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Confirm Order • Rs. $totalPricePKR PKR",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        // Google Maps Address Picker Modal
        if (isMapPickerVisible) {
            GoogleMapAddressPickerModal(
                initialArea = pickupSchedule.area,
                initialStreetAddress = pickupSchedule.streetAddress,
                onLocationConfirmed = { selectedArea, selectedStreet ->
                    onScheduleUpdated(selectedArea, selectedStreet, null, null, null)
                    isMapPickerVisible = false
                },
                onDismiss = { isMapPickerVisible = false }
            )
        }
    }
}

/**
 * AddressScreen:
 * Alias providing the exact same robust single LazyColumn layout with weight(1f)
 * and sticky bottom button outside LazyColumn for checkout address scheduling.
 */
@Composable
fun AddressScreen(
    totalCartCount: Int,
    totalPricePKR: Int,
    pickupSchedule: PickupSchedule,
    isSubmitting: Boolean = false,
    availableHubs: List<Hub> = emptyList(),
    selectedHub: Hub? = null,
    onHubSelected: (Hub) -> Unit = {},
    onScheduleUpdated: (area: String?, streetAddress: String?, date: String?, timeSlot: String?, specialNotes: String?) -> Unit,
    onConfirmOrderClick: () -> Unit,
    onBackClick: () -> Unit
) {
    PickupSchedulingScreen(
        totalCartCount = totalCartCount,
        totalPricePKR = totalPricePKR,
        pickupSchedule = pickupSchedule,
        isSubmitting = isSubmitting,
        availableHubs = availableHubs,
        selectedHub = selectedHub,
        onHubSelected = onHubSelected,
        onScheduleUpdated = onScheduleUpdated,
        onConfirmOrderClick = onConfirmOrderClick,
        onBackClick = onBackClick
    )
}
