import re

with open("app/src/main/java/com/example/ui/screens/PickupSchedulingScreen.kt", "r") as f:
    content = f.read()

# Fix the import syntax error
content = content.replace("import com.example.data.model.Hub\n\n@OptIn", "@OptIn")
content = content.replace("import com.example.data.model.PickupSchedule", "import com.example.data.model.PickupSchedule\nimport com.example.data.model.Hub")

with open("app/src/main/java/com/example/ui/screens/PickupSchedulingScreen.kt", "w") as f:
    f.write(content)
