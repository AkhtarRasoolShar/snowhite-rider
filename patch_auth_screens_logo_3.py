import re
with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "r") as f:
    content = f.read()

# Add R import
if "import com.example.R" not in content:
    content = content.replace("import coil.compose.AsyncImage", "import coil.compose.AsyncImage\nimport com.example.R")

async_login = """                            if (!logoUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(logoUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "SnoWhite Logo",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(60.dp).clip(CircleShape)
                                )"""
async_login_updated = """                            if (!logoUrl.isNullOrBlank()) {
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
                                )"""
content = content.replace(async_login, async_login_updated)

async_signup = """                        if (!logoUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(logoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "SnoWhite Logo",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(56.dp).clip(CircleShape)
                            )"""
async_signup_updated = """                        if (!logoUrl.isNullOrBlank()) {
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
                            )"""
content = content.replace(async_signup, async_signup_updated)

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "w") as f:
    f.write(content)
