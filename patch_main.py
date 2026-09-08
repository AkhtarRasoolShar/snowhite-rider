with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

content = content.replace(
"""                            Screen.ProductsShop -> ProductsShopScreen(
                                getProductQuantity = { viewModel.getProductQuantity(it) },""",
"""                            Screen.ProductsShop -> ProductsShopScreen(
                                retailProducts = uiState.products.filter { it.category_name?.contains("Premium") == true || it.category_name?.contains("Product") == true },
                                getProductQuantity = { viewModel.getProductQuantity(it) },"""
)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
