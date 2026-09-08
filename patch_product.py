import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# 1. Update filteredProducts
old_filtered = """    val filteredProducts = baseFilteredProducts.filter { product ->
        when (selectedServiceTab) {
            "Wash & Fold" -> product.name?.contains("wash", ignoreCase = true) == true || product.description?.contains("wash", ignoreCase = true) == true
            "Steam Ironing" -> product.name?.contains("press", ignoreCase = true) == true || product.description?.contains("press", ignoreCase = true) == true || product.name?.contains("iron", ignoreCase = true) == true
            else -> true
        }
    }.ifEmpty { baseFilteredProducts }"""

new_filtered = """    val filteredProducts = baseFilteredProducts"""
content = content.replace(old_filtered, new_filtered)

# 2. Update ProductCardItem call
old_call = """                        ProductCardItem(
                            product = product,
                            quantity = qty,
                            onAdd = { onAddProduct(product) },
                            onRemove = { onRemoveProduct(product) }
                        )"""

new_call = """                        ProductCardItem(
                            product = product,
                            quantity = qty,
                            selectedServiceTab = selectedServiceTab,
                            onAdd = { onAddProduct(product) },
                            onRemove = { onRemoveProduct(product) }
                        )"""
content = content.replace(old_call, new_call)

# 3. Update ProductCardItem definition
old_def = """@Composable
private fun ProductCardItem(
    product: Product,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {"""

new_def = """@Composable
private fun ProductCardItem(
    product: Product,
    quantity: Int,
    selectedServiceTab: String,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {"""
content = content.replace(old_def, new_def)

# 4. Update ProductCardItem price
old_price = """                Text(
                    text = "Rs. ${product.price.toInt()} PKR",
                    fontSize = 14.sp,"""

new_price = """                val priceMultiplier = when (selectedServiceTab) {
                    "Wash & Fold" -> 0.6
                    "Steam Ironing" -> 0.4
                    else -> 1.0
                }
                val adjustedPrice = (product.price.toDoubleOrNull() ?: 0.0) * priceMultiplier

                Text(
                    text = "Rs. ${adjustedPrice.toInt()} PKR",
                    fontSize = 14.sp,"""
content = content.replace(old_price, new_price)

# 5. Exact Image implementation
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
                model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                    .data(imgUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = product.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )"""
content = content.replace(old_image, new_image)


with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)

