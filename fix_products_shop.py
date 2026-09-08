with open("app/src/main/java/com/example/ui/screens/ProductsShopScreen.kt", "r") as f:
    content = f.read()

content = content.replace("product.price?.toString()?.toDoubleOrNull()?.toInt() ?: 0", "product.price.toInt()")

with open("app/src/main/java/com/example/ui/screens/ProductsShopScreen.kt", "w") as f:
    f.write(content)
