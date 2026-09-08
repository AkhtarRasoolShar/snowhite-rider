with open("app/src/main/java/com/example/ui/components/DualServiceGrid.kt", "r") as f:
    content = f.read()
    
content = content.replace("Icons.Default.DirectionsBike", "Icons.AutoMirrored.Filled.DirectionsBike")
content = content.replace("import androidx.compose.material.icons.filled.DirectionsBike", "import androidx.compose.material.icons.automirrored.filled.DirectionsBike")

with open("app/src/main/java/com/example/ui/components/DualServiceGrid.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/service/SnowWhiteMessagingService.kt", "r") as f:
    content = f.read()

content = content.replace(
    "override fun onDeletedMessages() {",
    "@Deprecated(\"Deprecated in Java\")\n    override fun onDeletedMessages() {"
)

with open("app/src/main/java/com/example/service/SnowWhiteMessagingService.kt", "w") as f:
    f.write(content)
