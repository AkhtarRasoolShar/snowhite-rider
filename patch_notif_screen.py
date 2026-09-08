import re

with open("app/src/main/java/com/example/ui/screens/NotificationsListScreen.kt", "r") as f:
    content = f.read()

new_composable = """import androidx.compose.foundation.lazy.items
import com.example.ui.viewmodel.NotificationItemModel

@Composable
fun NotificationsListScreen(
    notifications: List<NotificationItemModel>,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DeepBlue
                    )
                }
                Text(
                    text = "Notifications",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                )
            }
        }
        
        if (notifications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No new notifications right now",
                    color = Color(0xFF64748B),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notifications) { notif ->
                    NotificationItem(
                        icon = notif.icon,
                        title = notif.title,
                        message = notif.message,
                        time = notif.time,
                        isUnread = notif.isUnread
                    )
                }
            }
        }
    }
}
"""

content = re.sub(r"@Composable\s*fun NotificationsListScreen.*?(?=@Composable\s*fun NotificationItem)", new_composable, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/ui/screens/NotificationsListScreen.kt", "w") as f:
    f.write(content)
