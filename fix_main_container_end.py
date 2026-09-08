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

# Fix the misplaced if block
bad_end = """            }
        }
        if (uiState.isProfileOtpDialogVisible) {"""

good_end = """            }
        }
    } // Close when (val screen = uiState.currentScreen)
    
    if (uiState.isProfileOtpDialogVisible) {"""

content = content.replace(bad_end, good_end)

# Also we need to remove one trailing `}` because we effectively moved it.
# The previous `parts = content.rsplit("    }\n}", 1)` added the dialog before the last `}\n}`.
# Wait, let's just count the braces at the end.
# If I change `bad_end` to `good_end`, the file will have:
#     if (...) {
#         AlertDialog(...)
#     }
# } // This closes the function MainContainer
# } // Wait, is there an extra }?

# Let's just find the `if` and the very end of the file.
