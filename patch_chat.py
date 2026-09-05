import re

with open("app/src/main/java/com/example/ui/screens/OrderChatScreen.kt", "r") as f:
    content = f.read()

# 1. Add imports
imports = """import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.RowScope
"""
content = content.replace("import androidx.compose.material3.Text\n", "import androidx.compose.material3.Text\n" + imports)
content = content.replace("@Composable\nfun OrderChatScreen", "@OptIn(ExperimentalMaterial3Api::class)\n@Composable\nfun OrderChatScreen")

# 2. Add isAgentTyping state
content = content.replace("var isRefreshing by remember { mutableStateOf(false) }", "var isRefreshing by remember { mutableStateOf(false) }\n    var isAgentTyping by remember { mutableStateOf(false) }\n    val pullRefreshState = rememberPullToRefreshState()")

# 3. Add to sendMessage
send_msg_old = """                val response = RetrofitClient.apiService.sendChatMessage("send_chat_message", req)
                if (!response.isSuccessful) {
                    Log.w("CHAT_DEBUG", "Send chat API failed with code: ${response.code()}")
                } else {
                    fetchMessages(silent = true)
                }"""
send_msg_new = """                val response = RetrofitClient.apiService.sendChatMessage("send_chat_message", req)
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
                }"""
content = content.replace(send_msg_old, send_msg_new)

# 4. Wrap the Box content in PullToRefreshBox
old_box_start = """        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {"""

new_box_start = """        Box(
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
            ) {"""
content = content.replace(old_box_start, new_box_start)

# 5. Add closing brace for PullToRefreshBox and the TypingIndicator inside LazyColumn
lazy_col_end = """                        ChatBubbleItem(
                            message = msg,
                            isMe = isMe
                        )
                    }
                }
            }
        }

        // Bottom WhatsApp-style Input Bar"""

new_lazy_col_end = """                        ChatBubbleItem(
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

        // Bottom WhatsApp-style Input Bar"""
content = content.replace(lazy_col_end, new_lazy_col_end)

# 6. Add TypingIndicatorBubble Composable
typing_indicator_code = """
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
"""

with open("app/src/main/java/com/example/ui/screens/OrderChatScreen.kt", "w") as f:
    f.write(content + typing_indicator_code)

print("Patched!")
