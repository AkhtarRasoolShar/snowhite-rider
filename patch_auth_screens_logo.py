import re

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "r") as f:
    content = f.read()

# Add imports
if "coil.compose.AsyncImage" not in content:
    content = content.replace(
        "import androidx.compose.ui.unit.sp",
        "import androidx.compose.ui.unit.sp\nimport coil.compose.AsyncImage\nimport androidx.compose.material.icons.filled.LocalLaundryService"
    )

if "logoUrl: String? = null," not in content:
    content = content.replace("appName: String = \"SnowWhite\",\n", "appName: String = \"SnowWhite\",\n    logoUrl: String? = null,\n")

logo_logic_splash = """            if (!logoUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = logoUrl,
                    contentDescription = "App Logo",
                    modifier = Modifier.size(52.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.LocalLaundryService,
                    contentDescription = "App Logo",
                    tint = DeepBlue,
                    modifier = Modifier.size(52.dp)
                )
            }"""

content = content.replace(
"""            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AcUnit,
                    contentDescription = "SnoWhite Logo",
                    tint = DeepBlue,
                    modifier = Modifier.size(52.dp)
                )
            }""",
f"""            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {{
{logo_logic_splash}
            }}"""
)


logo_logic_login = """                        if (!logoUrl.isNullOrEmpty()) {
                            AsyncImage(
                                model = logoUrl,
                                contentDescription = "App Logo",
                                modifier = Modifier.size(38.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.LocalLaundryService,
                                contentDescription = "App Logo",
                                tint = Color.White,
                                modifier = Modifier.size(38.dp)
                            )
                        }"""

content = content.replace(
"""                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color.White.copy(alpha = 0.2f), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AcUnit,
                                contentDescription = "SnoWhite Snowflake Logo",
                                tint = Color.White,
                                modifier = Modifier.size(38.dp)
                            )
                        }""",
f"""                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color.White.copy(alpha = 0.2f), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {{
{logo_logic_login}
                        }}"""
)


logo_logic_signup = """                    if (!logoUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = logoUrl,
                            contentDescription = "App Logo",
                            modifier = Modifier.size(32.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.LocalLaundryService,
                            contentDescription = "App Logo",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }"""

content = content.replace(
"""                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.White.copy(alpha = 0.2f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AcUnit,
                            contentDescription = "SnoWhite Logo",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }""",
f"""                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.White.copy(alpha = 0.2f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {{
{logo_logic_signup}
                    }}"""
)

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "w") as f:
    f.write(content)
