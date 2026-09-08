import re
with open("app/src/main/java/com/example/ui/screens/ProductsShopScreen.kt", "r") as f:
    content = f.read()

content = content.replace("import com.example.data.model.CareProduct", "import com.example.data.model.Product")
content = content.replace("onAddProduct: (CareProduct) -> Unit", "onAddProduct: (Product) -> Unit, retailProducts: List<Product>")
content = content.replace("onRemoveProduct: (CareProduct) -> Unit", "onRemoveProduct: (Product) -> Unit")
content = content.replace("getProductQuantity: (String) -> Int", "getProductQuantity: (Int?) -> Int")
content = content.replace("items(CatalogData.careProducts, key = { it.id })", "items(retailProducts, key = { it.id ?: 0 })")
content = content.replace("product.imageRes", "null") # Assuming no local images
content = content.replace("product.pricePKR", "product.price?.toDoubleOrNull()?.toInt() ?: 0")

with open("app/src/main/java/com/example/ui/screens/ProductsShopScreen.kt", "w") as f:
    f.write(content)
