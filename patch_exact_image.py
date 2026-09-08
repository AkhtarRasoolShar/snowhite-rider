import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# Replace selectedServiceTab with selectedService
content = content.replace("selectedServiceTab", "selectedService")

# Replace image rendering block
old_image = """            val customFallbackUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRSDAJkXtsNkzYsDhu_BhNUwLD82d47UMkHFx2JCjoZFw&s"
            val imageUrl = product.image_url?.takeIf { it.isNotBlank() } ?: customFallbackUrl

            coil.compose.AsyncImage(
                model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = product.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )"""

new_image = """            val defaultImg = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRSDAJkXtsNkzYsDhu_BhNUwLD82d47UMkHFx2JCjoZFw&s"
            val imgUrl = if (product.image_url.isNullOrBlank()) defaultImg else product.image_url
            coil.compose.AsyncImage(
                model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current).data(imgUrl).crossfade(true).build(),
                contentDescription = product.name,
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )"""

content = content.replace(old_image, new_image)

# Replace price multiplier
old_price = """                val priceMultiplier = when (selectedService) {
                    "Wash & Fold" -> 0.6
                    "Steam Ironing" -> 0.4
                    else -> 1.0
                }"""

new_price = """                val priceMultiplier = when (selectedService) {
                    "Wash & Fold" -> 0.6
                    "Steam Ironing" -> 0.4
                    else -> 1.0
                }"""

# ensure size of image container is correct
old_container = """            // Premium Product Image Layout
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC)),
                contentAlignment = Alignment.Center
            ) {"""

new_container = """            // Premium Product Image Layout
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC)),
                contentAlignment = Alignment.Center
            ) {"""

content = content.replace(old_container, new_container)


with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
