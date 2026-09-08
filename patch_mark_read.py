import re

with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "r") as f:
    content = f.read()

mark_read_code = """
    @POST("routes.php")
    suspend fun markMessagesAsRead(
        @Query("action") action: String = "mark_chat_read",
        @Body request: Map<String, Int> // pass order_id
    ): Response<com.example.data.model.ApiResponse<Any>>
"""

if "markMessagesAsRead" not in content:
    content = content.replace(
        "suspend fun sendChatMessage(",
        mark_read_code.strip() + "\n\n    @POST(\"routes.php\")\n    suspend fun sendChatMessage("
    )

with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "w") as f:
    f.write(content)
print("Added mark_chat_read API")
