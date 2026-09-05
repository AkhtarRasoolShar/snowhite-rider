import re

with open("app/src/main/java/com/example/data/model/Models.kt", "r") as f:
    content = f.read()

content = content.replace(
"""data class ChatMessage(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("order_id") val orderId: Int? = null,
    @SerializedName("sender_type") val senderType: String? = null, // "customer" or "rider"
    @SerializedName("sender_id") val senderId: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)""",
"""data class ChatMessage(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("order_id") val orderId: Int? = null,
    @SerializedName("sender_type") val senderType: String? = null, // "customer" or "rider"
    @SerializedName("sender_id") val senderId: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("is_read") val isRead: String? = "0"
)"""
)

with open("app/src/main/java/com/example/data/model/Models.kt", "w") as f:
    f.write(content)
print("Patched ChatMessage")
