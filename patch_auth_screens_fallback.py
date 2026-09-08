import re
with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "r") as f:
    content = f.read()

snippet_template = """val customFallbackUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRSDAJkXtsNkzYsDhu_BhNUwLD82d47UMkHFx2JCjoZFw&s"
TARGET_INDENTval targetLogoUrl = logoUrl?.takeIf { it.isNotBlank() } ?: customFallbackUrl

TARGET_INDENTcoil.compose.AsyncImage(
TARGET_INDENT    model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
TARGET_INDENT        .data(targetLogoUrl)
TARGET_INDENT        .crossfade(true)
TARGET_INDENT        .build(),
TARGET_INDENT    contentDescription = "App Logo",
TARGET_INDENT    modifier = Modifier.size(TARGET_SIZE.dp).clip(CircleShape).background(Color.White),
TARGET_INDENT    contentScale = ContentScale.Fit
TARGET_INDENT)"""

# Splash
splash_target = """                Icon(
                    imageVector = Icons.Default.AcUnit,
                    contentDescription = "SnoWhite Logo",
                    tint = DeepBlue,
                    modifier = Modifier.size(52.dp)
                )"""
content = content.replace(splash_target, snippet_template.replace("TARGET_INDENT", "                ").replace("TARGET_SIZE", "60"))

# Login
login_target = """                            if (!logoUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(logoUrl)
                                        .crossfade(true)
                                        .build(),
                                    placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                                    error = painterResource(id = R.drawable.ic_launcher_foreground),
                                    contentDescription = "SnoWhite Logo",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(60.dp).clip(CircleShape).background(Color.White)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AcUnit,
                                    contentDescription = "SnoWhite Snowflake Logo",
                                    tint = Color.White,
                                    modifier = Modifier.size(38.dp)
                                )
                            }"""
content = content.replace(login_target, snippet_template.replace("TARGET_INDENT", "                            ").replace("TARGET_SIZE", "60"))

# SignUp
signup_target = """                        if (!logoUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(logoUrl)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                                error = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = "SnoWhite Logo",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.White)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AcUnit,
                                contentDescription = "SnoWhite Logo",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }"""
content = content.replace(signup_target, snippet_template.replace("TARGET_INDENT", "                        ").replace("TARGET_SIZE", "56"))

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "w") as f:
    f.write(content)
