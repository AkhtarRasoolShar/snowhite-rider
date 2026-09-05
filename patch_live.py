import re

with open("app/src/main/java/com/example/ui/screens/LiveOrderTrackingScreen.kt", "r") as f:
    content = f.read()

bad_block = """        // Assigned Captain / Rider Details Section
        val displayRider = if (!riderName.isNullOrBlank()) riderName else "SnoWhite Captain"
        val displayPhone = if (!riderPhone.isNullOrBlank()) riderPhone else "+92 301 8637011"
        
        Card(
            shape = RoundedCornerShape(20.dp),"""

fixed_block = """        // Assigned Captain / Rider Details Section
        val displayRider = if (!riderName.isNullOrBlank()) riderName else "SnoWhite Captain"
        val displayPhone = if (!riderPhone.isNullOrBlank()) riderPhone else "+92 301 8637011"
        
        Card(
            shape = RoundedCornerShape(20.dp),"""

content = content.replace("            }        }        // Live Step Progress Bar", "            }        // Live Step Progress Bar")

with open("app/src/main/java/com/example/ui/screens/LiveOrderTrackingScreen.kt", "w") as f:
    f.write(content)
print("Fixed brackets")
