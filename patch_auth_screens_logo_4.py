import re
with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "r") as f:
    content = f.read()

async_splash = """                            if (!logoUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(logoUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "SnoWhite Logo",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(60.dp).clip(CircleShape)
                                )"""
async_splash_updated = """                            if (!logoUrl.isNullOrBlank()) {
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
content = content.replace(async_splash, async_splash_updated)

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "w") as f:
    f.write(content)
