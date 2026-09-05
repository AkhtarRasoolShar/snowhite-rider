import re

with open("app/src/main/java/com/example/ui/screens/OrderChatScreen.kt", "r") as f:
    content = f.read()

if "import androidx.compose.material.icons.filled.Check" not in content:
    content = content.replace(
        "import androidx.compose.material.icons.filled.DoneAll",
        "import androidx.compose.material.icons.filled.DoneAll\nimport androidx.compose.material.icons.filled.Check"
    )

old_ticks = """                    if (isMe) {
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Delivered",
                            tint = Color(0xFF90CAF9),
                            modifier = Modifier.size(13.dp)
                        )
                    }"""

new_ticks = """                    if (isMe) {
                        Spacer(modifier = Modifier.width(3.dp))
                        val isMessageRead = message.isRead == "1" || message.isRead == "true"
                        Icon(
                            imageVector = if (isMessageRead) Icons.Default.DoneAll else Icons.Default.Check,
                            contentDescription = if (isMessageRead) "Read" else "Sent",
                            tint = if (isMessageRead) Color(0xFF64B5F6) else Color(0xFFB0BEC5),
                            modifier = Modifier.size(13.dp)
                        )
                    }"""

content = content.replace(old_ticks, new_ticks)

with open("app/src/main/java/com/example/ui/screens/OrderChatScreen.kt", "w") as f:
    f.write(content)
print("Patched UI ticks")
