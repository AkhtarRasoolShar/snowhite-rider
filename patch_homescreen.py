import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# Add imports
imports = """import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
"""
content = content.replace("import androidx.compose.ui.Alignment", imports + "import androidx.compose.ui.Alignment")

# Replace categories tab row and add sub-categories
old_categories_block = """            // Dynamic ScrollableTabRow for Categories
            if (categories.isNotEmpty()) {
                item {
                    val selectedIndex = categories.indexOfFirst { it.id == selectedCategoryId }.let { if (it < 0) 0 else it }

                    ScrollableTabRow(
                        selectedTabIndex = selectedIndex,
                        containerColor = Color.White,
                        contentColor = DeepBlue,
                        edgePadding = 16.dp,
                        indicator = { tabPositions ->
                            if (selectedIndex in tabPositions.indices) {
                                TabRowDefaults.SecondaryIndicator(
                                    Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                                    height = 3.dp,
                                    color = DeepBlue
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        categories.forEach { category ->
                            val isSelected = category.id == selectedCategoryId
                            Tab(
                                selected = isSelected,
                                onClick = { category.id?.let { onCategorySelected(it) } },
                                text = {
                                    Text(
                                        text = category.name ?: "Category",
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) DeepBlue else Color(0xFF64748B),
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            )
                        }
                    }
                }
            }"""

new_categories_block = """            // Dynamic ScrollableTabRow for Categories
            if (categories.isNotEmpty()) {
                item {
                    val selectedIndex = categories.indexOfFirst { it.id == selectedCategoryId }.let { if (it < 0) 0 else it }
                    val primaryBrandColor = Color(0xFF00B4D8)

                    ScrollableTabRow(
                        selectedTabIndex = selectedIndex,
                        containerColor = Color.White,
                        contentColor = primaryBrandColor,
                        edgePadding = 16.dp,
                        indicator = { tabPositions ->
                            if (selectedIndex in tabPositions.indices) {
                                TabRowDefaults.SecondaryIndicator(
                                    Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                                    height = 3.dp,
                                    color = primaryBrandColor
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        categories.forEach { category ->
                            val isSelected = category.id == selectedCategoryId
                            Tab(
                                selected = isSelected,
                                onClick = { category.id?.let { onCategorySelected(it) } },
                                text = {
                                    Text(
                                        text = category.name ?: "Category",
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) primaryBrandColor else Color(0xFF64748B),
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            )
                        }
                    }
                }
                
                // Sub-Categories (Services)
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
                }
            }"""

if old_categories_block in content:
    content = content.replace(old_categories_block, new_categories_block)
else:
    print("Failed to find old categories block")

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
