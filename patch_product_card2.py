import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

start_pattern = r"@Composable\s+private fun ProductCardItem\("
end_pattern = r"@Composable\s+private fun BentoCategoryTile\("

match = re.search(f"{start_pattern}.*?(?={end_pattern})", content, re.DOTALL)
if match:
    old_product_card = match.group(0)
    
    new_product_card = """@Composable
private fun ProductCardItem(
    product: Product,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    val primaryBrandColor = Color(0xFF00B4D8)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Premium Product Image Layout
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
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = product.name ?: "Product",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!product.description.isNullOrBlank()) {
                    Text(
                        text = product.description,
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 16.sp
                    )
                }
                Text(
                    text = "Rs. ${product.price.toInt()} PKR",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBrandColor
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            
            // +/- Quantity Selector with Brand Colors
            if (quantity > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(primaryBrandColor.copy(alpha = 0.1f))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove",
                            tint = primaryBrandColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "$quantity",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryBrandColor,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    IconButton(
                        onClick = onAdd,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = primaryBrandColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            } else {
                Button(
                    onClick = onAdd,
                    colors = ButtonDefaults.buttonColors(containerColor = primaryBrandColor.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = primaryBrandColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "ADD",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryBrandColor
                        )
                    }
                }
            }
        }
    }
}

"""
    
    content = content.replace(old_product_card, new_product_card)
    print("Replaced!")
else:
    print("Failed to find old product card block")

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
