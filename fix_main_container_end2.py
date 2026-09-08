import re

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

# Fix the delegation error by adding imports
imports = """
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
"""
if "import androidx.compose.runtime.getValue" not in content:
    content = content.replace("import androidx.compose.runtime.Composable", "import androidx.compose.runtime.Composable\n" + imports)

# We want to replace `} \n if (uiState.isProfileOtpDialogVisible)` with `} \n } \n if (...)`
content = re.sub(r"\}\s*if \(uiState\.isProfileOtpDialogVisible\)", r"}\n    }\n    if (uiState.isProfileOtpDialogVisible)", content)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
