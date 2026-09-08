import re

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

# Add setValue
if "import androidx.compose.runtime.setValue" not in content:
    content = content.replace("import androidx.compose.runtime.getValue", "import androidx.compose.runtime.getValue\nimport androidx.compose.runtime.setValue")

# Remove one trailing brace
# Let's count them at the end.
end_braces = re.search(r"(\s*\}\s*)+$", content)
if end_braces:
    # Just trim one `}` from the end
    # We will replace the last } with nothing
    content = content.rsplit("}", 1)[0]

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
