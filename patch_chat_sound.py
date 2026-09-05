import re

with open("app/src/main/java/com/example/ui/screens/OrderChatScreen.kt", "r") as f:
    content = f.read()

# Add import
if "import androidx.compose.runtime.mutableIntStateOf" not in content:
    content = content.replace(
        "import androidx.compose.runtime.mutableStateOf",
        "import androidx.compose.runtime.mutableStateOf\nimport androidx.compose.runtime.mutableIntStateOf"
    )

old_sync_effect = """    // Sync external messages from ViewModel State
    LaunchedEffect(chatMessages) {
        if (chatMessages.isNotEmpty()) {
            if (chatMessages.size != messages.size || chatMessages.lastOrNull()?.id != messages.lastOrNull()?.id) {
                messages.clear()
                messages.addAll(chatMessages)
                delay(100)
                if (messages.isNotEmpty()) {
                    listState.animateScrollToItem(messages.size - 1)
                }
            }
            isInitialLoading = false
            isRefreshing = false
        }
    }"""

new_sync_effect = """    var previousCount by remember { mutableIntStateOf(0) }

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
    }"""

content = content.replace(old_sync_effect, new_sync_effect)

with open("app/src/main/java/com/example/ui/screens/OrderChatScreen.kt", "w") as f:
    f.write(content)
print("Patched sound")
