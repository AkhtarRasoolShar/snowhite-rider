import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

fetch_logic = """
    fun fetchPromos() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getPromos()
                if (response.isSuccessful && response.body()?.success == true) {
                    val promos = response.body()?.promos ?: emptyList()
                    _uiState.update { it.copy(activePromos = promos) }
                    generateNotifications()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun generateNotifications() {
        val state = _uiState.value
        val notifications = mutableListOf<NotificationItemModel>()
        
        // Add promos (up to 2)
        state.activePromos.take(2).forEach { promo ->
            notifications.add(
                NotificationItemModel(
                    icon = androidx.compose.material.icons.Icons.Default.LocalOffer,
                    title = "Flash Sale!",
                    message = "Use code ${promo.code} for ${promo.discountPercent}% off! ${promo.description ?: ""}",
                    time = "Just now",
                    isUnread = false,
                    isPromo = true
                )
            )
        }
        
        // Add recent orders (up to 5)
        state.remoteOrders.sortedByDescending { it.id }.take(5).forEach { order ->
            notifications.add(
                NotificationItemModel(
                    icon = androidx.compose.material.icons.Icons.Default.LocalLaundryService,
                    title = "Order Status Update",
                    message = "Order #${order.id} is now ${order.status.replace("_", " ")}.",
                    time = order.date,
                    isUnread = true,
                    isPromo = false
                )
            )
        }
        
        _uiState.update { it.copy(notifications = notifications, notificationCount = notifications.count { n -> n.isUnread }) }
    }
"""

# Let's insert it before the closing brace of SnowWhiteViewModel
content = re.sub(r"}\s*$", fetch_logic + "\n}", content)

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
