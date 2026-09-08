with open("app/src/main/java/com/example/ui/components/DrawerMenuContent.kt", "r") as f:
    content = f.read()

# Update UI logic
target_ui = """                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AcUnit,
                                contentDescription = null,
                                tint = DeepBlue,
                                modifier = Modifier.size(26.dp)
                            )
                        }"""

replacement_ui = """                        val customFallbackUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRSDAJkXtsNkzYsDhu_BhNUwLD82d47UMkHFx2JCjoZFw&s"
                        val targetLogoUrl = logoUrl?.takeIf { it.isNotBlank() } ?: customFallbackUrl

                        coil.compose.AsyncImage(
                            model = coil.request.ImageRequest.Builder(LocalContext.current)
                                .data(targetLogoUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Drawer Logo",
                            modifier = Modifier.size(64.dp).clip(CircleShape).background(Color.White).padding(8.dp),
                            contentScale = ContentScale.Fit
                        )"""

content = content.replace(target_ui, replacement_ui)

with open("app/src/main/java/com/example/ui/components/DrawerMenuContent.kt", "w") as f:
    f.write(content)
