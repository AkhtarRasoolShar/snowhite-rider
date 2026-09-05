import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

# Add chatMessages to UiState
if "val chatMessages: List<com.example.data.model.ChatMessage> = emptyList()," not in content:
    content = content.replace(
        "val remoteOrders: List<RemoteOrder> = emptyList(),",
        "val remoteOrders: List<RemoteOrder> = emptyList(),\n    val chatMessages: List<com.example.data.model.ChatMessage> = emptyList(),"
    )

# Add fetchMessages
fetch_messages_code = """
    fun fetchMessages(orderId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getChatMessages(
                    action = "get_chat_messages",
                    orderId = orderId
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    val list = body?.data ?: body?.messages ?: emptyList()
                    _uiState.update { currentState ->
                        if (currentState.chatMessages == list) {
                            currentState
                        } else {
                            currentState.copy(chatMessages = list)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("CHAT_DEBUG", "Failed to fetch chat messages: ${e.message}")
            }
        }
    }
"""

if "fun fetchMessages(" not in content:
    content = content.replace(
        "fun fetchOrders(",
        fetch_messages_code + "\n    fun fetchOrders("
    )

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
print("Patched ViewModel")
