import re
with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "r") as f:
    content = f.read()

# Add imports
imports = """import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
"""
content = content.replace("import androidx.compose.ui.unit.sp", "import androidx.compose.ui.unit.sp\n" + imports)

# Splash Screen replacement
splash_icon = """                            Icon(
                                imageVector = Icons.Default.AcUnit,
                                contentDescription = "SnoWhite Logo",
                                tint = DeepBlue,
                                modifier = Modifier.size(52.dp)
                            )"""
splash_async = """                            if (!logoUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(logoUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "SnoWhite Logo",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(60.dp).clip(CircleShape)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AcUnit,
                                    contentDescription = "SnoWhite Logo",
                                    tint = DeepBlue,
                                    modifier = Modifier.size(52.dp)
                                )
                            }"""
content = content.replace(splash_icon, splash_async)

# Login Screen replacement
login_icon = """                            Icon(
                                imageVector = Icons.Default.AcUnit,
                                contentDescription = "SnoWhite Snowflake Logo",
                                tint = Color.White,
                                modifier = Modifier.size(38.dp)
                            )"""
login_async = """                            if (!logoUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(logoUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "SnoWhite Logo",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(60.dp).clip(CircleShape)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AcUnit,
                                    contentDescription = "SnoWhite Snowflake Logo",
                                    tint = Color.White,
                                    modifier = Modifier.size(38.dp)
                                )
                            }"""
content = content.replace(login_icon, login_async)

# SignUp Screen replacement
signup_icon = """                        Icon(
                            imageVector = Icons.Default.AcUnit,
                            contentDescription = "SnoWhite Logo",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )"""
signup_async = """                        if (!logoUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(logoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "SnoWhite Logo",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(56.dp).clip(CircleShape)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AcUnit,
                                contentDescription = "SnoWhite Logo",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }"""
content = content.replace(signup_icon, signup_async)

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "w") as f:
    f.write(content)
