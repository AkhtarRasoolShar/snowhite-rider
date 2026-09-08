import re

with open("app/src/main/java/com/example/ui/screens/CustomerProfileScreen.kt", "r") as f:
    content = f.read()

new_ui = """        Spacer(modifier = Modifier.height(24.dp))
        
        // Addresses Section
        Text(
            text = "Addresses",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = DeepBlue)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Home", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("123 Main Street, Phase 5, DHA", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        // Account Security Section
        Text(
            text = "Account Security",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Keep your account secure by regularly updating your password. Use a strong and unique password.",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                OutlinedButton(
                    onClick = onResetPassword,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepBlue)
                ) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Change Password", fontWeight = FontWeight.Bold)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))"""

# find the part starting with `Spacer(modifier = Modifier.height(24.dp))` and `Card` with `onResetPassword`
# and replace it.
content = re.sub(r"Spacer\(modifier = Modifier\.height\(24\.dp\)\)\s*Card\([\s\S]*?Spacer\(modifier = Modifier\.height\(32\.dp\)\)", new_ui, content)

# ensure we import icons
if "import androidx.compose.material.icons.filled.LocationOn" not in content:
    content = content.replace("import androidx.compose.material.icons.filled.Email", "import androidx.compose.material.icons.filled.Email\nimport androidx.compose.material.icons.filled.LocationOn\nimport androidx.compose.material.icons.filled.Lock")
    
if "import androidx.compose.material3.OutlinedButton" not in content:
    content = content.replace("import androidx.compose.material3.TextButton", "import androidx.compose.material3.TextButton\nimport androidx.compose.material3.OutlinedButton")

if "import androidx.compose.material3.ButtonDefaults" not in content:
    content = content.replace("import androidx.compose.material3.OutlinedButton", "import androidx.compose.material3.OutlinedButton\nimport androidx.compose.material3.ButtonDefaults")

with open("app/src/main/java/com/example/ui/screens/CustomerProfileScreen.kt", "w") as f:
    f.write(content)
