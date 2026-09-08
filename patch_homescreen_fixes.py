import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# 1. Move selectedServiceTab to top level
# Look for:
#    val filteredProducts = products.filter { product ->
#        selectedCategoryId == null || product.category_id == selectedCategoryId
#    }
# We will insert selectedServiceTab above it.

state_injection = """    var selectedServiceTab by remember { mutableStateOf("Dry Cleaning") }
    val servicesList = listOf("Dry Cleaning", "Wash & Fold", "Steam Ironing")

    val baseFilteredProducts = products.filter { product ->
        selectedCategoryId == null || product.category_id == selectedCategoryId
    }
    
    val filteredProducts = baseFilteredProducts.filter { product ->
        when (selectedServiceTab) {
            "Wash & Fold" -> product.name?.contains("wash", ignoreCase = true) == true || product.description?.contains("wash", ignoreCase = true) == true
            "Steam Ironing" -> product.name?.contains("press", ignoreCase = true) == true || product.description?.contains("press", ignoreCase = true) == true || product.name?.contains("iron", ignoreCase = true) == true
            else -> true
        }
    }.ifEmpty { baseFilteredProducts }"""

content = re.sub(
    r"    val filteredProducts = products\.filter \{ product ->\s*selectedCategoryId == null \|\| product\.category_id == selectedCategoryId\s*\}",
    state_injection,
    content
)

# 2. Update the sub-categories tab in the item block
old_subcategories = """                // Sub-Categories (Services)
                item {
                    val subServices = listOf("Dry Cleaning", "Wash & Fold", "Steam Ironing")
                    var selectedSubService by remember { mutableStateOf("Dry Cleaning") }
                    val primaryBrandColor = Color(0xFF00B4D8)
                    
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(subServices) { service ->
                            val isSelected = selectedSubService == service
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) primaryBrandColor else Color(0xFFF1F5F9))
                                    .clickable { selectedSubService = service }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = service,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }"""

new_subcategories = """                // Sub-Categories (Services)
                item {
                    val primaryBrandColor = Color(0xFF00B4D8)
                    
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(servicesList) { service ->
                            val isSelected = selectedServiceTab == service
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) primaryBrandColor else Color(0xFFF1F5F9))
                                    .clickable { selectedServiceTab = service }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = service,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }"""

content = content.replace(old_subcategories, new_subcategories)

# 3. Replace product image with exact user code
old_image_box = """            // Premium Product Image Layout
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC)),
                contentAlignment = Alignment.Center
            ) {
                val customFallbackUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRSDAJkXtsNkzYsDhu_BhNUwLD82d47UMkHFx2JCjoZFw&s"
                val targetLogoUrl = product.image_url?.takeIf { it.isNotBlank() } ?: customFallbackUrl

                AsyncImage(
                    model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                        .data(targetLogoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = product.name ?: "Product Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }"""

new_image_code = """            val customFallbackUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRSDAJkXtsNkzYsDhu_BhNUwLD82d47UMkHFx2JCjoZFw&s"
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

content = content.replace(old_image_box, new_image_code)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
