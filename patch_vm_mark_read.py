import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

mark_read_code = """
    fun markMessagesAsRead(orderId: Int) {
        viewModelScope.launch {
            try {
                RetrofitClient.apiService.markMessagesAsRead(
                    action = "mark_chat_read",
                    request = mapOf("order_id" to orderId)
                )
                // Optionally re-fetch messages after marking them as read
                // fetchMessages(orderId) 
            } catch (e: Exception) {
                Log.e("CHAT_DEBUG", "Failed to mark messages as read: ${e.message}")
            }
        }
    }
"""

if "fun markMessagesAsRead" not in content:
    content = content.replace(
        "fun fetchMessages(",
        mark_read_code.strip() + "\n\n    fun fetchMessages("
    )

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
print("Added markMessagesAsRead to ViewModel")
