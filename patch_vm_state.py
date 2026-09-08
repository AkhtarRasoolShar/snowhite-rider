import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

content = content.replace("val userProfilePhone: String = \"+92 301 1234567\",", "val userProfilePhone: String = \"+92 301 1234567\",\n    val userProfileEmail: String = \"\",")

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
