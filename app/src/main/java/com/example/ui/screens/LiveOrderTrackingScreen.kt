package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.OrderEntity
import com.example.data.model.OrderStatus
import com.example.data.model.RemoteOrder
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.LightBlueBorder
import com.example.ui.theme.SoftLightBlue
import com.example.ui.theme.SuccessGreen

import androidx.compose.material.icons.automirrored.filled.ArrowBack

@Composable
fun LiveOrderTrackingScreen(
    order: OrderEntity? = null,
    remoteOrder: RemoteOrder? = null,
    onBackToHomeClick: () -> Unit
) {
    if (order == null && remoteOrder == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.LocalLaundryService, contentDescription = null, modifier = Modifier.size(64.dp), tint = DeepBlue)
            Spacer(modifier = Modifier.height(16.dp))
            Text("No Active Order Found", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Book a new laundry order to view live tracking", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBackToHomeClick) {
                Text("Go to Home")
            }
        }
        return
    }

    val displayOrderId = remoteOrder?.displayOrderId ?: order?.orderId ?: "SW-1001"
    val displayTrackingCode = remoteOrder?.displayOrderId ?: order?.trackingCode ?: "SW-1001"
    val displayServiceTier = remoteOrder?.service_tier ?: order?.serviceTier ?: "Standard Care"
    val displayEstimatedDelivery = remoteOrder?.displayDate ?: order?.estimatedDelivery ?: "Today"
    val displayTotalAmount = remoteOrder?.displayAmount ?: order?.totalAmountPKR ?: 0

    val riderName = (remoteOrder?.rider_name ?: remoteOrder?.riderNameAlt ?: order?.riderName)?.takeIf { it.isNotBlank() }

    val riderPhone = (remoteOrder?.rider_phone ?: remoteOrder?.riderPhoneAlt ?: order?.riderPhone)?.takeIf { it.isNotBlank() }

    val addressText = remoteOrder?.pickup_address?.takeIf { it.isNotBlank() }
        ?: order?.address?.takeIf { it.isNotBlank() }
        ?: "DHA Phase 6, Karachi"

    val itemsText = remoteOrder?.items?.takeIf { it.isNotEmpty() }?.joinToString("\n") {
        "${it.qty ?: 1}x ${it.item ?: "Garment Item"} (${it.price ?: 0} PKR)"
    } ?: order?.itemsSummaryJson?.takeIf { it.isNotBlank() } ?: "1x Dry Cleaning & Steam Pressing Care"

    val statusStr = (remoteOrder?.status ?: order?.let { OrderStatus.values().getOrNull(it.statusStepIndex.coerceIn(0, 4))?.name } ?: "COLLECTING").uppercase().replace(" ", "_").trim()
    val currentStep = when {
        statusStr.contains("DELIVERED") || statusStr == "4" || statusStr.contains("COMPLETED") -> 4
        statusStr.contains("OUT_FOR_DELIVERY") || statusStr == "3" || statusStr.contains("DISPATCHED") -> 3
        statusStr.contains("IN_WASHING") || statusStr == "2" || statusStr.contains("WASHING") || statusStr.contains("CLEANING") -> 2
        statusStr.contains("RECEIVED_AT_HUB") || statusStr == "1" || statusStr.contains("RECEIVED") || statusStr.contains("HUB") -> 1
        else -> 0
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("live_order_tracking_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header Info Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DeepBlue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = onBackToHomeClick,
                            modifier = Modifier.testTag("order_tracking_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Column {
                            Text(
                                text = "Order #$displayOrderId",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "Tracking: $displayTrackingCode",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = displayServiceTier,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Est. Delivery: $displayEstimatedDelivery",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "Rs. $displayTotalAmount PKR",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }

        // Assigned Captain / Rider Details Section
        if (!riderName.isNullOrBlank()) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LightBlueBorder, RoundedCornerShape(20.dp))
                    .testTag("your_captain_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(SoftLightBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Driver Avatar",
                                tint = DeepBlue,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Your Captain",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = riderName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Verified, contentDescription = "Verified Rider", tint = DeepBlue, modifier = Modifier.size(14.dp))
                            }
                            Text(
                                text = "SnoWhite Delivery Captain",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // WhatsApp Contact Button
                    val cleanPhone = (riderPhone ?: "")
                        .removePrefix("+92")
                        .removePrefix("92")
                        .removePrefix("0")
                        .replace(" ", "")
                        .trim()

                    Button(
                        onClick = {
                            val whatsappUrl = "https://wa.me/92$cleanPhone"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl))
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                Toast.makeText(context, "Opening WhatsApp for +92 $cleanPhone", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366), contentColor = Color.White),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("whatsapp_captain_button")
                    ) {
                        Text("WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Pending Captain",
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Waiting for a Captain to accept the order",
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }

        // Live Step Progress Bar (Strict Status Sync)
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LightBlueBorder, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Live Order Progress",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                val brandTeal = Color(0xFF00B4D8)

                OrderStatus.values().forEach { status ->
                    val isCompleted = status.stepIndex < currentStep
                    val isCurrent = status.stepIndex == currentStep

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Step Indicator Icon Circle
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isCurrent -> brandTeal
                                        isCompleted -> SuccessGreen
                                        else -> Color(0xFFE2E8F0)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            } else {
                                Text(
                                    text = (status.stepIndex + 1).toString(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent) Color.White else Color(0xFF64748B)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = status.title,
                                fontSize = 14.sp,
                                fontWeight = if (isCurrent || isCompleted) FontWeight.Bold else FontWeight.Medium,
                                color = when {
                                    isCurrent -> brandTeal
                                    isCompleted -> SuccessGreen
                                    else -> Color(0xFF64748B)
                                }
                            )
                            Text(
                                text = status.subtitle,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Address & Items Summary Card
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Address & Items Breakdown",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Home, contentDescription = null, tint = DeepBlue, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = addressText,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = LightBlueBorder)

                Text(
                    text = "Items Breakdown:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = itemsText,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp
                )
            }
        }

        Button(
            onClick = onBackToHomeClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DeepBlue, contentColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("back_to_home_button")
        ) {
            Text("Return to Dashboard", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}
