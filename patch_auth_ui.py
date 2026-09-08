import re

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "r") as f:
    content = f.read()

# We need to find the phone input column in SignUpScreen and add email input after it.
# We'll use a regex that looks for the phone column and the next column (password) and inserts between them.

phone_block_pattern = r"""(                    // Phone Input.*?                    Column\(verticalArrangement = Arrangement\.spacedBy\(6\.dp\)\) \{.*?                        Text\([^)]*?"Phone Number"[^)]*\).*?                        OutlinedTextField\(.*?                            value = phone,.*?                                \),.*?                                modifier = Modifier.*?                                    \.fillMaxWidth\(\).*?                            \)[\s\S]*?                    \})"""

# Let's verify we can find it.
match = re.search(phone_block_pattern, content)
if match:
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
                    }"""
    
    # We only want to replace the SECOND occurrence (which is in SignUpScreen, since LoginScreen also has phone).
    # Actually the pattern match above is general, but let's replace all matches that are inside SignUpScreen
    # Let's be safer by splitting by "fun SignUpScreen"
    
    parts = content.split("fun SignUpScreen")
    if len(parts) > 1:
        new_second_part = re.sub(phone_block_pattern, r"\1" + email_block, parts[1], count=1)
        content = parts[0] + "fun SignUpScreen" + new_second_part
        with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "w") as f:
            f.write(content)
        print("Success")
    else:
        print("SignUpScreen not found")
else:
    print("Phone block not found")
