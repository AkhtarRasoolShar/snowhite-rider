import re

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

old_profile = """                            Screen.Profile -> CustomerProfileScreen(
                                userName = uiState.userProfileName,
                                userPhone = uiState.userProfilePhone,
                                savedAddress = uiState.userAddress,
                                savedDeliveryAddress = uiState.userDeliveryAddress,
                                onSaveAddress = { viewModel.saveUserAddress(it) },
                                onSaveDeliveryAddress = { viewModel.saveUserDeliveryAddress(it) },
                                onOpenMapPicker = { viewModel.navigateTo(Screen.MapPicker) },
                                onOpenNotificationSettings = { viewModel.navigateTo(Screen.NotificationSettings) },
                                onWhatsAppSupportClick = { launchWhatsAppSupport() },
                                onLogoutClick = { viewModel.logoutUser() },
                                onBackClick = { viewModel.navigateTo(Screen.Home) }
                            )"""

new_profile = """                            Screen.Profile -> CustomerProfileScreen(
                                name = uiState.userProfileName,
                                phone = uiState.userProfilePhone,
                                email = uiState.userProfileEmail,
                                onBackClick = { viewModel.navigateTo(Screen.Home) },
                                onUpdateProfile = { newPhone, newEmail -> 
                                    // TODO: Trigger OTP via ViewModel
                                    viewModel.updateProfileParams(newPhone, newEmail) 
                                },
                                onResetPassword = {
                                    viewModel.navigateTo(Screen.ForgotPassword)
                                }
                            )"""

# Fallback regex in case it was partially modified
content = re.sub(r"Screen\.Profile -> CustomerProfileScreen\([^)]+\)", new_profile, content, flags=re.MULTILINE|re.DOTALL)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
