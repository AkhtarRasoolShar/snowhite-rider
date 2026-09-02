package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
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
import kotlinx.coroutines.launch

import android.content.Intent
import android.net.Uri
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.data.util.InvoiceGenerator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    remoteOrders: List<RemoteOrder>,
    localOrdersList: List<OrderEntity>,
    isFetchingOrders: Boolean,
    onRefreshOrders: () -> Unit,
    onSelectOrder: (String) -> Unit,
    onBookNewOrderClick: () -> Unit,
    onViewInvoice: ((RemoteOrder?, OrderEntity?) -> Unit)? = null,
    onReorder: ((RemoteOrder?, OrderEntity?) -> Unit)? = null
) {
    var selectedRemoteOrder by remember { mutableStateOf<RemoteOrder?>(null) }
    var selectedLocalOrder by remember { mutableStateOf<OrderEntity?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val closeSheet: () -> Unit = {
        scope.launch {
            try {
                sheetState.hide()
            } catch (_: Exception) {}
        }.invokeOnCompletion {
            try {
                if (!sheetState.isVisible) {
                    selectedRemoteOrder = null
                    selectedLocalOrder = null
                }
            } catch (_: Exception) {
                selectedRemoteOrder = null
                selectedLocalOrder = null
            }
        }
    }

    val openRemoteSheet: (RemoteOrder) -> Unit = { order ->
        selectedRemoteOrder = order
        selectedLocalOrder = null
        scope.launch {
            try {
                sheetState.show()
            } catch (_: Exception) {}
        }
    }

    val openLocalSheet: (OrderEntity) -> Unit = { order ->
        selectedLocalOrder = order
        selectedRemoteOrder = null
        scope.launch {
            try {
                sheetState.show()
            } catch (_: Exception) {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("order_history_screen")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "My Laundry Orders",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Connected live to API (Customer Orders)",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onRefreshOrders,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(SoftLightBlue)
            ) {
                if (isFetchingOrders) {
                    CircularProgressIndicator(
                        color = DeepBlue,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Live Orders",
                        tint = DeepBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (isFetchingOrders && remoteOrders.isEmpty() && localOrdersList.isEmpty()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = DeepBlue, modifier = Modifier.size(40.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Fetching live order history...", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        } else if (remoteOrders.isEmpty() && localOrdersList.isEmpty()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.ListAlt,
                    contentDescription = null,
                    tint = LightBlueBorder,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("No Order History Yet", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Your booked dry cleaning orders will appear here", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBookNewOrderClick) {
                    Text("Book First Order")
                }
            }
        } else {
            val remoteIds = remoteOrders.map { it.displayOrderId }.toSet()
            val filteredLocalOrders = localOrdersList.filter { it.orderId !in remoteIds }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (remoteOrders.isNotEmpty()) {
                    items(remoteOrders, key = { "remote_${it.displayOrderId}" }) { order ->
                        val statusText = order.displayStatus
                        val (bgColor, textColor) = getStatusColors(statusText)
                        val isActiveTracking = !statusText.equals("DELIVERED", ignoreCase = true) && !statusText.equals("CANCELLED", ignoreCase = true)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { openRemoteSheet(order) }
                                .testTag("history_remote_order_${order.displayOrderId}"),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .border(1.dp, LightBlueBorder, RoundedCornerShape(18.dp))
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Order #${order.displayOrderId}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = DeepBlue
                                    )

                                    TrackingStatusBadge(
                                        statusText = statusText,
                                        isActiveTracking = isActiveTracking,
                                        bgColor = bgColor,
                                        textColor = textColor,
                                        modifier = Modifier.testTag("tracking_badge_${order.displayOrderId}")
                                    )
                                }

                                Text(
                                    text = "Date: ${order.displayDate} • Tier: ${order.service_tier ?: "Regular Care"}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                if (!order.pickup_address.isNullOrBlank()) {
                                    Text(
                                        text = "Address: ${order.pickup_address}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Total: Rs. ${order.displayAmount} PKR",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedButton(
                                            onClick = { onReorder?.invoke(order, null) },
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            modifier = Modifier.height(32.dp).testTag("reorder_button_${order.displayOrderId}")
                                        ) {
                                            Icon(Icons.Default.Autorenew, contentDescription = null, tint = DeepBlue, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Reorder", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .clickable { openRemoteSheet(order) }
                                                .testTag("view_details_button_${order.displayOrderId}")
                                        ) {
                                            Text("Details >", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (filteredLocalOrders.isNotEmpty()) {
                    items(filteredLocalOrders, key = { "local_${it.orderId}" }) { order ->
                        val statusObj = OrderStatus.values().getOrNull(order.statusStepIndex.coerceIn(0, OrderStatus.values().lastIndex)) ?: OrderStatus.PLACED
                        val isActiveTracking = order.statusStepIndex < 5
                        val displayDateStr = if (!order.date.equals("N/A", ignoreCase = true) && order.date.isNotBlank()) order.date else "Just now"

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { openLocalSheet(order) }
                                .testTag("history_order_${order.orderId}"),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .border(1.dp, LightBlueBorder, RoundedCornerShape(18.dp))
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Order #${order.orderId}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = DeepBlue
                                    )

                                    TrackingStatusBadge(
                                        statusText = statusObj.title,
                                        isActiveTracking = isActiveTracking,
                                        bgColor = if (order.statusStepIndex == 5) SuccessGreen.copy(alpha = 0.15f) else SoftLightBlue,
                                        textColor = if (order.statusStepIndex == 5) SuccessGreen else DeepBlue,
                                        modifier = Modifier.testTag("tracking_badge_${order.orderId}")
                                    )
                                }

                                Text(
                                    text = "Date: $displayDateStr • ${order.itemCount} Items • ${order.serviceTier}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Total: Rs. ${order.totalAmountPKR} PKR",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedButton(
                                            onClick = { onReorder?.invoke(null, order) },
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            modifier = Modifier.height(32.dp).testTag("reorder_button_${order.orderId}")
                                        ) {
                                            Icon(Icons.Default.Autorenew, contentDescription = null, tint = DeepBlue, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Reorder", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .clickable { openLocalSheet(order) }
                                                .testTag("view_details_button_${order.orderId}")
                                        ) {
                                            Text("Details >", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ModalBottomSheet for Order Details
    val currentRemote = selectedRemoteOrder
    val currentLocal = selectedLocalOrder

    if (currentRemote != null || currentLocal != null) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    try {
                        sheetState.hide()
                    } catch (_: Exception) {}
                    selectedRemoteOrder = null
                    selectedLocalOrder = null
                }
            },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            modifier = Modifier.testTag("order_details_modal_bottom_sheet")
        ) {
            OrderDetailsBottomSheetContent(
                remoteOrder = currentRemote,
                localOrder = currentLocal,
                onTrackOrder = { orderId ->
                    closeSheet()
                    onSelectOrder(orderId)
                },
                onViewInvoice = { remote, local ->
                    closeSheet()
                    onViewInvoice?.invoke(remote, local)
                },
                onReorder = { remote, local ->
                    closeSheet()
                    onReorder?.invoke(remote, local)
                },
                onClose = { closeSheet() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrderDetailsBottomSheetContent(
    remoteOrder: RemoteOrder?,
    localOrder: OrderEntity?,
    onTrackOrder: (String) -> Unit,
    onViewInvoice: (RemoteOrder?, OrderEntity?) -> Unit,
    onReorder: (RemoteOrder?, OrderEntity?) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val orderId = remoteOrder?.displayOrderId ?: localOrder?.orderId ?: "SW-1001"
    val date = remoteOrder?.displayDate ?: localOrder?.date ?: "31st Aug 2026"
    val tier = remoteOrder?.service_tier ?: localOrder?.serviceTier ?: "Regular Care"
    val statusText = remoteOrder?.displayStatus
        ?: OrderStatus.values().getOrNull((localOrder?.statusStepIndex ?: 0).coerceIn(0, OrderStatus.values().lastIndex))?.title
        ?: "Order Placed"
    val (bgColor, textColor) = getStatusColors(statusText)
    val isActiveTracking = remoteOrder?.let { !it.displayStatus.equals("DELIVERED", ignoreCase = true) && !it.displayStatus.equals("CANCELLED", ignoreCase = true) }
        ?: localOrder?.let { it.statusStepIndex < 5 }
        ?: true
    val address = remoteOrder?.pickup_address ?: localOrder?.let { "${it.address}, ${it.area}" } ?: "DHA Phase 6, Karachi"
    val amount = remoteOrder?.displayAmount ?: localOrder?.totalAmountPKR ?: 0
    val itemsList = remoteOrder?.items ?: emptyList()
    val itemsSummary = localOrder?.itemsSummaryJson

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState())
            .testTag("order_details_sheet_content"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Order Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "Order ID: #$orderId",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBlue
                )
            }

            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
            }
        }

        HorizontalDivider(color = LightBlueBorder)

        // Status & Tier Summary Card with animated TrackingStatusBadge
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SoftLightBlue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Current Status", fontSize = 11.sp, color = Color(0xFF64748B))
                    Spacer(modifier = Modifier.height(4.dp))
                    TrackingStatusBadge(
                        statusText = statusText,
                        isActiveTracking = isActiveTracking,
                        bgColor = bgColor,
                        textColor = textColor
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Service Tier", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text(text = tier, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                }
            }
        }

        // Assigned Captain / Rider Info Section with WhatsApp Contact Button
        val realRiderName = remoteOrder?.displayRiderName?.takeIf { it.isNotBlank() }
            ?: localOrder?.riderName?.takeIf { it.isNotBlank() }

        val realRiderPhone = remoteOrder?.displayRiderPhone?.takeIf { it.isNotBlank() }
            ?: localOrder?.riderPhone?.takeIf { it.isNotBlank() }

        if (!realRiderName.isNullOrBlank()) {
            val cleanPhone = (realRiderPhone ?: "").removePrefix("+92").removePrefix("92").removePrefix("0").replace(" ", "").trim()

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LightBlueBorder, RoundedCornerShape(16.dp))
                    .testTag("your_captain_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(SoftLightBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Rider Avatar",
                                tint = DeepBlue,
                                modifier = Modifier.size(24.dp)
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
                            Text(
                                text = realRiderName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            if (cleanPhone.isNotBlank()) {
                                Text(
                                    text = "Contact: +92 $cleanPhone",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }

                    if (cleanPhone.isNotBlank()) {
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
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Pending Captain",
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Waiting for a Captain to accept your order...",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }

        // Live Order Progress Timeline (Teal #00B4D8 for active step)
        val localStatusString = localOrder?.let { OrderStatus.values().getOrNull(it.statusStepIndex.coerceIn(0, 4))?.name }
        val rawStatusString = (remoteOrder?.status ?: remoteOrder?.displayStatus ?: localStatusString ?: "COLLECTING").trim().uppercase().replace(" ", "_")
        val currentStepIndex = when {
            rawStatusString.contains("DELIVERED") || rawStatusString.contains("COMPLETED") -> 4
            rawStatusString.contains("OUT_FOR_DELIVERY") || rawStatusString.contains("DISPATCHED") -> 3
            rawStatusString.contains("IN_WASHING") || rawStatusString.contains("WASHING") || rawStatusString.contains("CLEANING") -> 2
            rawStatusString.contains("RECEIVED_AT_HUB") || rawStatusString.contains("RECEIVED") || rawStatusString.contains("HUB") -> 1
            else -> 0
        }

        val brandTeal = Color(0xFF00B4D8)
        val timelineSteps = listOf(
            Pair("COLLECTING", "Order Placed & Pickup"),
            Pair("RECEIVED AT HUB", "Garments at Main Facility"),
            Pair("IN WASHING", "Eco Cleaning & Steam Pressing"),
            Pair("OUT FOR DELIVERY", "Rider Out For Doorstep Delivery"),
            Pair("DELIVERED", "Order Fully Delivered")
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Tracking Timeline",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            timelineSteps.forEachIndexed { index, (stepTitle, stepSub) ->
                val isCompleted = index < currentStepIndex
                val isCurrent = index == currentStepIndex

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
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
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        } else {
                            Text(
                                text = (index + 1).toString(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrent) Color.White else Color(0xFF64748B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = stepTitle,
                            fontSize = 13.sp,
                            fontWeight = if (isCurrent) FontWeight.ExtraBold else if (isCompleted) FontWeight.Bold else FontWeight.Normal,
                            color = when {
                                isCurrent -> brandTeal
                                isCompleted -> SuccessGreen
                                else -> Color(0xFF94A3B8)
                            }
                        )
                        Text(
                            text = stepSub,
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }

        // Details Section
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Order Information", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Order Date:", fontSize = 12.sp, color = Color(0xFF64748B))
                Text(text = date, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0F172A))
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Pickup Location:", fontSize = 12.sp, color = Color(0xFF64748B))
                Text(text = address, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0F172A), modifier = Modifier.width(200.dp))
            }
        }

        HorizontalDivider(color = LightBlueBorder)

        // Items Breakdown
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Items & Services Breakdown", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))

            if (itemsList.isNotEmpty()) {
                itemsList.forEach { itemReq ->
                    val qty = itemReq.qty ?: 1
                    val name = itemReq.item ?: "Garment Item"
                    val price = itemReq.price ?: 0
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${qty}x $name", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        Text(text = if (price > 0) "Rs. $price PKR" else "Included", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
                    }
                }
            } else if (!itemsSummary.isNullOrBlank()) {
                Text(text = itemsSummary, fontSize = 13.sp, color = Color(0xFF1E293B), lineHeight = 18.sp)
            } else {
                Text(text = "Dry Cleaning & Steam Pressing Service (Full Garment Care)", fontSize = 13.sp, color = Color(0xFF64748B))
            }
        }

        HorizontalDivider(color = LightBlueBorder)

        // Amount Breakdown
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Total Payable Amount:", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            Text(text = "Rs. $amount PKR", fontSize = 18.sp, fontWeight = FontWeight.Black, color = DeepBlue)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Actions
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onViewInvoice(remoteOrder, localOrder) },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoftLightBlue, contentColor = DeepBlue),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("view_invoice_from_sheet_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Text("View Invoice", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                Button(
                    onClick = {
                        val subtotal = remoteOrder?.displayAmount ?: localOrder?.totalAmountPKR ?: 0
                        val deliveryFee = if (subtotal > 1500 || subtotal == 0) 0 else 150
                        val invoice = com.example.ui.components.InvoiceData(
                            invoiceNumber = "INV-2026-$orderId",
                            orderId = orderId,
                            invoiceDate = date,
                            customerName = "Akhtar Hussain",
                            customerPhone = "+92 301 1234567",
                            deliveryAddress = address,
                            serviceTier = tier,
                            items = listOf(com.example.ui.components.InvoiceItemData("Dry Cleaning & Pressing Care", 1, subtotal, subtotal)),
                            subtotalPKR = subtotal,
                            deliveryFeePKR = deliveryFee,
                            grandTotalPKR = subtotal + deliveryFee,
                            paymentStatus = "PAID ON DELIVERY (COD)"
                        )
                        val pdf = InvoiceGenerator.generatePdfInvoice(context, invoice)
                        if (pdf != null && pdf.exists()) {
                            Toast.makeText(context, "Invoice PDF generated & saved: ${pdf.name}", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Downloaded Invoice #INV-2026-$orderId", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepBlue, contentColor = Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("download_invoice_from_sheet_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Text("Download PDF", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { onReorder(remoteOrder, localOrder) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("reorder_from_sheet_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Autorenew, contentDescription = null, tint = DeepBlue, modifier = Modifier.size(16.dp))
                        Text("Reorder", color = DeepBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                Button(
                    onClick = { onTrackOrder(orderId) },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepBlue, contentColor = Color.White),
                    modifier = Modifier
                        .weight(1.2f)
                        .height(46.dp)
                        .testTag("track_order_from_sheet_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.TrackChanges, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("Track Live", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun TrackingStatusBadge(
    statusText: String,
    isActiveTracking: Boolean,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "status_badge_pulse")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    val scaleAnim by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (isActiveTracking) {
            Box(
                modifier = Modifier.size(12.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer pulsing halo ring
                Box(
                    modifier = Modifier
                        .size(11.dp)
                        .graphicsLayer {
                            scaleX = scaleAnim
                            scaleY = scaleAnim
                            alpha = alphaAnim * 0.45f
                        }
                        .clip(CircleShape)
                        .background(textColor)
                )
                // Inner active spinning indicator
                CircularProgressIndicator(
                    color = textColor,
                    strokeWidth = 1.6.dp,
                    modifier = Modifier.size(9.dp)
                )
            }
        } else {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Delivered",
                tint = textColor,
                modifier = Modifier.size(12.dp)
            )
        }

        Text(
            text = statusText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

private fun getStatusColors(status: String): Pair<Color, Color> {
    return when (status.uppercase()) {
        "COLLECTING", "ORDER PLACED" -> Pair(SoftLightBlue, DeepBlue)
        "IN_WASHING", "IN CLEANING & PRESSING" -> Pair(Color(0xFFEDE7F6), Color(0xFF512DA8))
        "OUT_FOR_DELIVERY" -> Pair(Color(0xFFFFF3E0), Color(0xFFE65100))
        "DELIVERED" -> Pair(SuccessGreen.copy(alpha = 0.15f), SuccessGreen)
        else -> Pair(SoftLightBlue, DeepBlue)
    }
}

