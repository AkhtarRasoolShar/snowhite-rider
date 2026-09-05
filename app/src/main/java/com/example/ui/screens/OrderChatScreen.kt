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
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderChatScreen(
    orderId: Int,
    mySenderType: String,
    mySenderId: Int,
    orderCode: String = "SW-$orderId",
    otherPartyName: String = if (mySenderType.equals("customer", ignoreCase = true)) "Delivery Captain" else "Customer",
    chatMessages: List<ChatMessage> = emptyList(),
    viewModel: com.example.ui.viewmodel.SnowWhiteViewModel,
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
    var isAgentTyping by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()

    val isCustomerView = mySenderType.equals("customer", ignoreCase = true)
    val otherPartyRole = if (isCustomerView) "SnoWhite Delivery Captain" else "SnoWhite Customer"

    var previousCount by remember { mutableIntStateOf(0) }

    // Sync external messages from ViewModel State & Play Tone
    LaunchedEffect(chatMessages.size) {
        val currentSize = chatMessages.size
        if (chatMessages.isNotEmpty()) {
            if (chatMessages.size != messages.size || chatMessages.lastOrNull()?.id != messages.lastOrNull()?.id) {
                messages.clear()
                messages.addAll(chatMessages)
                delay(100)
                
                // 1. Auto-Scroll to bottom
                if (currentSize > 0) {
                    listState.animateScrollToItem(currentSize - 1)
                }
                
                // 2. Play Notification Tone for NEW INCOMING messages
                if (currentSize > previousCount && previousCount > 0) {
                    val lastMessage = chatMessages.last()
                    val isIncoming = lastMessage.senderType?.equals(mySenderType, ignoreCase = true) != true
                    
                    if (isIncoming) {
                        try {
                            val uri = android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION)
                            val ringtone = android.media.RingtoneManager.getRingtone(context, uri)
                            ringtone.play()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            isInitialLoading = false
            isRefreshing = false
        }
        previousCount = currentSize
    }

    // Wrap the viewModel fetch in a convenient local function
    fun fetchMessages(silent: Boolean = false) {
        if (!silent && messages.isEmpty()) {
            isInitialLoading = true
        }
        viewModel.fetchMessages(orderId)
        
        // Timeout to disable loading spinners if it gets stuck
        coroutineScope.launch {
            delay(1500)
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
                } else {
                    fetchMessages(silent = true)
                    if (isCustomerView) {
                        coroutineScope.launch {
                            delay(500)
                            isAgentTyping = true
                            if (messages.isNotEmpty()) {
                                listState.animateScrollToItem(messages.size)
                            }
                            delay(3000)
                            isAgentTyping = false
                        }
                    }
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
        while(true) {
            viewModel.fetchMessages(orderId)
            kotlinx.coroutines.delay(3000) // Poll every 3 seconds
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
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { 
                    isRefreshing = true
                    coroutineScope.launch { fetchMessages(silent = false) }
                },
                modifier = Modifier.fillMaxSize()
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
                        // STRICT DIFFERENTIATION: Only match senderType to avoid crossing streams if senderIds happen to match
                        val isMe = msg.senderType?.equals(mySenderType, ignoreCase = true) == true

                        ChatBubbleItem(
                            message = msg,
                            isMe = isMe
                        )
                    }
                    if (isAgentTyping) {
                        item {
                            TypingIndicatorBubble(otherPartyRole)
                        }
                    }
                }
            }
            } // Close PullToRefreshBox
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
                        val isMessageRead = message.isRead == "1" || message.isRead == "true"
                        Icon(
                            imageVector = if (isMessageRead) Icons.Default.DoneAll else Icons.Default.Check,
                            contentDescription = if (isMessageRead) "Read" else "Sent",
                            tint = if (isMessageRead) Color(0xFF64B5F6) else Color(0xFFB0BEC5),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TypingIndicatorBubble(role: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = role,
                fontSize = 11.sp,
                color = TextSecondaryMuted,
                modifier = Modifier.padding(start = 12.dp, bottom = 2.dp)
            )
            Surface(
                shape = RoundedCornerShape(
                    topStart = 4.dp,
                    topEnd = 16.dp,
                    bottomEnd = 16.dp,
                    bottomStart = 16.dp
                ),
                color = PureWhite,
                shadowElevation = 1.dp,
                modifier = Modifier.widthIn(min = 60.dp, max = 100.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val transition = rememberInfiniteTransition(label = "typing")
                    for (i in 0 until 3) {
                        val alpha by transition.animateFloat(
                            initialValue = 0.3f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(durationMillis = 500, delayMillis = i * 150),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "dotAlpha"
                        )
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(DeepBlue.copy(alpha = alpha))
                        )
                    }
                }
            }
        }
    }
}
