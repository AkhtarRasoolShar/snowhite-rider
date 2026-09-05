package com.example.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.data.model.SendChatMessageRequest
import com.example.data.remote.RetrofitClient
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.GradientAccentBlue
import com.example.ui.theme.LightBlueBorder
import com.example.ui.theme.OffWhiteBg
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SoftLightBlue
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryMuted
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Reusable Order Chat Screen for both Customer and Rider applications.
 *
 * @param orderId Unique numeric ID of the order
 * @param mySenderType "customer" or "rider"
 * @param mySenderId The logged-in customer_id or rider_id
 * @param orderCode Friendly display code (e.g. "SW-84920")
 * @param otherPartyName Name of the person being chatted with
 * @param onBackClick Navigation back callback
 */
@Composable
fun OrderChatScreen(
    orderId: Int,
    mySenderType: String,
    mySenderId: Int,
    orderCode: String = "SW-$orderId",
    otherPartyName: String = if (mySenderType.equals("customer", ignoreCase = true)) "Delivery Captain" else "Customer",
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val messages = remember { mutableStateListOf<ChatMessage>() }
    var inputMessage by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    var isInitialLoading by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }

    val isCustomerView = mySenderType.equals("customer", ignoreCase = true)
    val otherPartyRole = if (isCustomerView) "SnoWhite Delivery Captain" else "SnoWhite Customer"

    // Real-time polling function: fetches messages for orderId
    suspend fun fetchMessages(silent: Boolean = false) {
        if (!silent && messages.isEmpty()) {
            isInitialLoading = true
        }
        try {
            val response = RetrofitClient.apiService.getChatMessages(
                action = "get_chat_messages",
                orderId = orderId
            )
            if (response.isSuccessful) {
                val body = response.body()
                val list = body?.data ?: body?.messages ?: emptyList()
                if (list.isNotEmpty()) {
                    // Update list only if there are new items or differences to prevent unnecessary UI jitter
                    if (list.size != messages.size || list.lastOrNull()?.id != messages.lastOrNull()?.id) {
                        messages.clear()
                        messages.addAll(list)
                        coroutineScope.launch {
                            delay(100)
                            if (messages.isNotEmpty()) {
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        }
                    }
                }
            }
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Throwable) {
            Log.e("CHAT_DEBUG", "Failed to fetch chat messages: ${e.localizedMessage}")
        } finally {
            isInitialLoading = false
            isRefreshing = false
        }
    }

    // Send a message
    fun sendMessage(textToSend: String) {
        val cleanText = textToSend.trim()
        if (cleanText.isBlank() || isSending) return

        inputMessage = ""
        isSending = true

        // Optimistic UI insertion for responsive feel
        val localMsg = ChatMessage(
            id = (messages.maxOfOrNull { it.id ?: 0 } ?: 0) + 1,
            orderId = orderId,
            senderType = mySenderType.lowercase(),
            senderId = mySenderId,
            message = cleanText,
            createdAt = "Just now"
        )
        messages.add(localMsg)

        coroutineScope.launch {
            try {
                if (messages.isNotEmpty()) {
                    listState.animateScrollToItem(messages.size - 1)
                }

                val req = SendChatMessageRequest(
                    order_id = orderId,
                    sender_type = mySenderType.lowercase(),
                    sender_id = mySenderId,
                    message = cleanText
                )
                val response = RetrofitClient.apiService.sendChatMessage("send_chat_message", req)
                if (!response.isSuccessful) {
                    Log.w("CHAT_DEBUG", "Send chat API failed with code: ${response.code()}")
                }
                // Refresh to sync exact server timestamp and IDs
                fetchMessages(silent = true)
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Throwable) {
                Log.e("CHAT_DEBUG", "Error sending chat message: ${e.localizedMessage}")
            } finally {
                isSending = false
            }
        }
    }

    // Polling effect every 3 seconds to fetch new messages in real-time
    LaunchedEffect(orderId) {
        fetchMessages(silent = false)
        while (isActive) {
            delay(3000L)
            fetchMessages(silent = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhiteBg)
            .statusBarsPadding()
            .imePadding()
            .navigationBarsPadding()
            .testTag("order_chat_screen")
    ) {
        // WhatsApp-like Modern Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = PureWhite,
            shadowElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(40.dp).testTag("chat_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DeepBlue
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Avatar with online status indicator
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(DeepBlue, GradientAccentBlue)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isCustomerView) Icons.Default.TwoWheeler else Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = PureWhite,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Green Active Indicator Dot
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(PureWhite)
                            .padding(1.5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(SuccessGreen)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = otherPartyName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimaryDark,
                            maxLines = 1
                        )
                    }
                    Text(
                        text = "$otherPartyRole • $orderCode",
                        fontSize = 11.sp,
                        color = TextSecondaryMuted,
                        maxLines = 1
                    )
                }

                // Refresh action button
                IconButton(
                    onClick = {
                        isRefreshing = true
                        coroutineScope.launch { fetchMessages(silent = false) }
                    },
                    modifier = Modifier.size(36.dp).testTag("chat_refresh_button")
                ) {
                    if (isRefreshing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = DeepBlue
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = DeepBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Security Notice Banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SoftLightBlue)
                .border(1.dp, LightBlueBorder)
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = DeepBlue,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Order $orderCode private chat. Messages are encrypted & logged.",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = DeepBlue,
                textAlign = TextAlign.Center
            )
        }

        // Main Message Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (isInitialLoading && messages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = DeepBlue, strokeWidth = 3.dp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Connecting to Order Live Chat...",
                            fontSize = 13.sp,
                            color = TextSecondaryMuted
                        )
                    }
                }
            } else if (messages.isEmpty()) {
                // Empty state with quick suggestions
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(SoftLightBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = null,
                            tint = DeepBlue,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Messages Yet",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isCustomerView)
                            "Have special fabric requests or pickup instructions? Send a message to your delivery captain!"
                        else
                            "Contact the customer regarding gate access, timing, or order verification.",
                        fontSize = 13.sp,
                        color = TextSecondaryMuted,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Quick Prompts:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DeepBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val quickPrompts = if (isCustomerView) {
                        listOf(
                            "Hi! Please call when you reach.",
                            "Please handle the embroidery garments with care.",
                            "I'm at home, ready for pickup.",
                            "What is the estimated delivery time?"
                        )
                    } else {
                        listOf(
                            "Assalam-o-Alaikum! I am on my way.",
                            "I have arrived at your building entrance.",
                            "Your laundry has been collected safely.",
                            "Order dispatched for delivery!"
                        )
                    }

                    quickPrompts.forEach { prompt ->
                        Surface(
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { sendMessage(prompt) },
                            color = PureWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, LightBlueBorder)
                        ) {
                            Text(
                                text = "💬  \"$prompt\"",
                                fontSize = 12.sp,
                                color = DeepBlue,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(messages, key = { it.id ?: it.hashCode() }) { msg ->
                        val isMe = (msg.senderType?.lowercase() == mySenderType.lowercase()) ||
                                (msg.senderId == mySenderId)

                        ChatBubbleItem(
                            message = msg,
                            isMe = isMe
                        )
                    }
                }
            }
        }

        // Quick Suggestion Chips (above input bar when chat has messages)
        if (messages.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(OffWhiteBg)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val chips = if (isCustomerView) {
                    listOf("I'm at the gate", "Call when arrived", "Thanks!")
                } else {
                    listOf("On my way", "Arrived at location", "All collected")
                }
                chips.forEach { chip ->
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { sendMessage(chip) },
                        color = PureWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, LightBlueBorder)
                    ) {
                        Text(
                            text = chip,
                            fontSize = 11.sp,
                            color = DeepBlue,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        // Bottom WhatsApp-style Input Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = PureWhite,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputMessage,
                    onValueChange = { inputMessage = it },
                    placeholder = {
                        Text(
                            "Type a message...",
                            fontSize = 14.sp,
                            color = TextSecondaryMuted
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = OffWhiteBg,
                        unfocusedContainerColor = OffWhiteBg,
                        focusedBorderColor = DeepBlue,
                        unfocusedBorderColor = LightBlueBorder
                    ),
                    maxLines = 4,
                    singleLine = false,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = { sendMessage(inputMessage) }
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Send Button
                val canSend = inputMessage.isNotBlank() && !isSending
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            if (canSend) Brush.linearGradient(listOf(DeepBlue, GradientAccentBlue))
                            else Brush.linearGradient(listOf(Color(0xFFCBD5E1), Color(0xFF94A3B8)))
                        )
                        .clickable(enabled = canSend) {
                            sendMessage(inputMessage)
                        }
                        .testTag("chat_send_button"),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSending) {
                        CircularProgressIndicator(
                            color = PureWhite,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = PureWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Message Bubble Composable:
 * - "My Messages": Right-aligned, primary blue bubble, white text, read receipt ticks.
 * - "Other's Messages": Left-aligned, crisp white bubble with subtle border, dark text.
 */
@Composable
private fun ChatBubbleItem(
    message: ChatMessage,
    isMe: Boolean
) {
    val bubbleShape = if (isMe) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 2.dp)
    } else {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 2.dp, bottomEnd = 16.dp)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        // Sender Label for other party
        if (!isMe) {
            val label = if (message.senderType.equals("rider", ignoreCase = true)) "Delivery Captain" else "Customer"
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBlue,
                modifier = Modifier.padding(start = 6.dp, bottom = 2.dp)
            )
        }

        Surface(
            shape = bubbleShape,
            color = if (isMe) DeepBlue else PureWhite,
            border = if (isMe) null else androidx.compose.foundation.BorderStroke(1.dp, LightBlueBorder),
            shadowElevation = if (isMe) 2.dp else 1.dp,
            modifier = Modifier
                .widthIn(min = 60.dp, max = 290.dp)
                .testTag(if (isMe) "chat_bubble_me" else "chat_bubble_other")
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = message.message ?: "",
                    fontSize = 14.sp,
                    color = if (isMe) PureWhite else TextPrimaryDark,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.formattedTime,
                        fontSize = 10.sp,
                        color = if (isMe) Color(0xFFD0E8FF) else TextSecondaryMuted
                    )

                    if (isMe) {
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Delivered",
                            tint = Color(0xFF90CAF9),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}
