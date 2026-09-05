import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

import_statement = "import androidx.compose.runtime.LaunchedEffect\n"
if "import androidx.compose.runtime.LaunchedEffect" not in content:
    content = content.replace("import androidx.compose.runtime.Composable", "import androidx.compose.runtime.Composable\n" + import_statement)

old_start = """fun HomeScreen(
    activeOrder: OrderEntity?,
    categories: List<Category>,
    products: List<Product>,"""

new_start = """fun HomeScreen(
    activeOrder: OrderEntity?,
    categories: List<Category>,
    products: List<Product>,"""

old_box = """    val filteredProducts = products.filter { product ->
        selectedCategoryId == null || product.category_id == selectedCategoryId
    }

    Box("""

new_box = """    val filteredProducts = products.filter { product ->
        selectedCategoryId == null || product.category_id == selectedCategoryId
    }

    LaunchedEffect(Unit) {
        onRefresh()
    }

    Box("""

content = content.replace(old_box, new_box)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
print("Patched HomeScreen.kt")
