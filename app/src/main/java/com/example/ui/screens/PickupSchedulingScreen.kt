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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.repository.CatalogData
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.LightBlueBorder
import com.example.ui.theme.SoftLightBlue

import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.ui.components.GoogleMapAddressPickerModal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupSchedulingScreen(
    pickupSchedule: PickupSchedule,
    onScheduleUpdated: (String?, String?, String?, String?, String?) -> Unit,
    totalCartCount: Int,
    totalPricePKR: Int,
    isSubmitting: Boolean,
    onConfirmOrderClick: () -> Unit
) {
    var isAreaDropdownExpanded by remember { mutableStateOf(false) }
    var isMapPickerVisible by remember { mutableStateOf(false) }

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
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("pickup_scheduling_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Schedule Pickup & Address",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "SnowWhite rider will collect your garments from your doorstep in Karachi",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Section 1: Address & Karachi Area Picker
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
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = pickupSchedule.area,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Karachi Area / Sector") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                modifier = Modifier.clickable { isAreaDropdownExpanded = true }
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeepBlue,
                            unfocusedBorderColor = LightBlueBorder
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isAreaDropdownExpanded = true }
                            .testTag("area_picker_dropdown")
                    )

                    DropdownMenu(
                        expanded = isAreaDropdownExpanded,
                        onDismissRequest = { isAreaDropdownExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.85f)
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
            }
        }

        // Section 2: Date Selector
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

        // Section 3: Time Slot Selector
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

        // Section 4: Special Instructions
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

        // Order Total & Submit CTA Button
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
