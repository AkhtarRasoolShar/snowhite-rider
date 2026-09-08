import re

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "r") as f:
    content = f.read()

email_block = """
                    // Email Input
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Email Address",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                if (emailError != null) emailError = null
                            },
                            isError = emailError != null,
                            placeholder = { Text("e.g. you@example.com") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = BrightBlue)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrightBlue,
                                unfocusedBorderColor = LightBlueBorder,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        if (emailError != null) {
                            Text(
                                text = emailError!!,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                    
                    // Password Input"""

# We want to replace the second "// Password Input" which is inside SignUpScreen
parts = content.split("// Password Input")
if len(parts) >= 3:
    new_content = parts[0] + "// Password Input" + parts[1] + email_block + parts[2]
    # Rejoin any remaining parts
    for i in range(3, len(parts)):
        new_content += "// Password Input" + parts[i]
        
    with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "w") as f:
        f.write(new_content)
    print("Success")
else:
    print("Not enough parts")
