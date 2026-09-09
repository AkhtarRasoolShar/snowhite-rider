import re

with open("app/src/main/java/com/example/ui/screens/PickupSchedulingScreen.kt", "r") as f:
    content = f.read()

# Fix the duplicate annotation
content = content.replace("@OptIn(ExperimentalMaterial3Api::class)\n@OptIn(ExperimentalMaterial3Api::class)", "@OptIn(ExperimentalMaterial3Api::class)")

with open("app/src/main/java/com/example/ui/screens/PickupSchedulingScreen.kt", "w") as f:
    f.write(content)
