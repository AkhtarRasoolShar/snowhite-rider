import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

if "val availableHubs" not in content:
    content = content.replace("val activePromos: List<com.example.data.model.Promo> = emptyList(),", "val activePromos: List<com.example.data.model.Promo> = emptyList(),\n    val availableHubs: List<com.example.data.model.Hub> = emptyList(),\n    val selectedHub: com.example.data.model.Hub? = null,")

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
