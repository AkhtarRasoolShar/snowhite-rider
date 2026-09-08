import re

with open("app/src/main/java/com/example/ui/components/TopAppBarHeader.kt", "r") as f:
    content = f.read()

# Add imports
if "coil.compose.AsyncImage" not in content:
    content = content.replace(
        "import androidx.compose.ui.unit.sp",
        "import androidx.compose.ui.unit.sp\nimport coil.compose.AsyncImage\nimport androidx.compose.material.icons.filled.LocalLaundryService"
    )

if "logoUrl: String? = null," not in content:
    content = content.replace(
        "appName: String = \"SnowWhite\",",
        "appName: String = \"SnowWhite\",\n    logoUrl: String? = null,"
    )

logo_logic = """                    if (!logoUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = logoUrl,
                            contentDescription = "App Logo",
                            modifier = Modifier.size(32.dp).clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    } else {
                        Icon(
                            imageVector = Icons.Default.LocalLaundryService,
                            contentDescription = "App Logo",
                            tint = DeepBlue,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text("""

content = content.replace(
    "                    Text(",
    logo_logic,
    1
)

with open("app/src/main/java/com/example/ui/components/TopAppBarHeader.kt", "w") as f:
    f.write(content)
