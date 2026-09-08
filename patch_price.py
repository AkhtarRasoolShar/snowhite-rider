import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

old_price_calc = "val adjustedPrice = (product.price.toDoubleOrNull() ?: 0.0) * priceMultiplier"
new_price_calc = "val adjustedPrice = product.price * priceMultiplier"

content = content.replace(old_price_calc, new_price_calc)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
