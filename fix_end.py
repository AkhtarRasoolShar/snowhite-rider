import re
with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# Replace everything from the end of the `else` block up to `@Composable private fun ProductCardItem`
content = re.sub(
    r"            \} else \{\s*items\(filteredProducts, key = \{ it\.id \?: 0 \}\) \{ product ->\s*val qty = getProductQuantity\(product\.id\)\s*Box\(modifier = Modifier\.padding\(horizontal = 16\.dp, vertical = 2\.dp\)\) \{\s*ProductCardItem\(\s*product = product,\s*quantity = qty,\s*onAdd = \{ onAddProduct\(product\) \},\s*onRemove = \{ onRemoveProduct\(product\) \}\s*\)\s*\}\s*\}\s*\}.*?@Composable\s*private fun ProductCardItem",
    """            } else {
                items(filteredProducts, key = { it.id ?: 0 }) { product ->
                    val qty = getProductQuantity(product.id)
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)) {
                        ProductCardItem(
                            product = product,
                            quantity = qty,
                            onAdd = { onAddProduct(product) },
                            onRemove = { onRemoveProduct(product) }
                        )
                    }
                }
            }
        }
    }
}
}

@Composable
private fun ProductCardItem""",
    content,
    flags=re.DOTALL
)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
