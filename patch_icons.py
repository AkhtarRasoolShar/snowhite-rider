import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

content = content.replace("androidx.compose.material.icons.Icons.Default.LocalOffer", "androidx.compose.material.icons.Icons.Default.LocalOffer")
content = content.replace("androidx.compose.material.icons.Icons.Default.LocalLaundryService", "androidx.compose.material.icons.Icons.Default.LocalLaundryService")

# Actually, let's just add the imports at the top and use Icons.Default...
if "import androidx.compose.material.icons.Icons" not in content:
    content = content.replace("package com.example.ui.viewmodel", "package com.example.ui.viewmodel\nimport androidx.compose.material.icons.Icons\nimport androidx.compose.material.icons.filled.LocalOffer\nimport androidx.compose.material.icons.filled.LocalLaundryService")

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
