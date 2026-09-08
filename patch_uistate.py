import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

if "data class NotificationItemModel" not in content:
    notification_model = """
data class NotificationItemModel(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val message: String,
    val time: String,
    val isUnread: Boolean,
    val isPromo: Boolean = false
)
"""
    content = content.replace("data class UiState(", notification_model + "data class UiState(")

if "val activePromos: List<com.example.data.model.Promo> = emptyList()" not in content:
    content = content.replace("val notificationCount: Int = 3,", "val notificationCount: Int = 3,\n    val activePromos: List<com.example.data.model.Promo> = emptyList(),\n    val notifications: List<NotificationItemModel> = emptyList(),")

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
