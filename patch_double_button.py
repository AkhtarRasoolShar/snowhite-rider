import re

with open("app/src/main/java/com/example/ui/screens/LiveOrderTrackingScreen.kt", "r") as f:
    content = f.read()

content = content.replace("Button(\n                        Button(", "Button(")

with open("app/src/main/java/com/example/ui/screens/LiveOrderTrackingScreen.kt", "w") as f:
    f.write(content)
print("Fixed double button")
