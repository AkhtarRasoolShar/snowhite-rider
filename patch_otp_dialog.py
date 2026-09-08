import re

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

otp_dialog = """
        if (uiState.isProfileOtpDialogVisible) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { viewModel.dismissProfileOtpDialog() },
                title = { androidx.compose.material3.Text("Enter OTP") },
                text = {
                    var otpValue by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
                    androidx.compose.foundation.layout.Column {
                        androidx.compose.material3.Text("Please enter the OTP sent to your new email/phone.")
                        androidx.compose.material3.OutlinedTextField(
                            value = otpValue,
                            onValueChange = { otpValue = it },
                            label = { androidx.compose.material3.Text("OTP") }
                        )
                        androidx.compose.material3.Button(
                            onClick = { viewModel.verifyProfileOtp(otpValue) },
                            modifier = androidx.compose.ui.Modifier.padding(top = 16.dp)
                        ) {
                            androidx.compose.material3.Text("Verify")
                        }
                    }
                },
                confirmButton = {}
            )
        }
    }
}"""

# Insert right before the last closing brace of MainContainer
parts = content.rsplit("    }\n}", 1)
if len(parts) == 2:
    new_content = parts[0] + otp_dialog
    with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
        f.write(new_content)
