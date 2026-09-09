import re

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

call_old = """                            Screen.PickupScheduling -> PickupSchedulingScreen(
                                pickupSchedule = uiState.pickupSchedule,
                                onScheduleUpdated = { area, addr, date, slot, notes ->
                                    viewModel.updatePickupSchedule(area, addr, date, slot, notes)
                                },
                                totalCartCount = viewModel.totalCartBadgeCount,
                                totalPricePKR = viewModel.totalCartPricePKR,
                                isSubmitting = uiState.isSubmittingOrder,
                                onConfirmOrderClick = { viewModel.createAndSubmitOrder() },
                                onBackClick = { viewModel.navigateTo(Screen.CartCheckout) }
                            )"""

call_new = """                            Screen.PickupScheduling -> {
                                LaunchedEffect(Unit) {
                                    viewModel.fetchHubs()
                                }
                                PickupSchedulingScreen(
                                    pickupSchedule = uiState.pickupSchedule,
                                    availableHubs = uiState.availableHubs,
                                    selectedHub = uiState.selectedHub,
                                    onHubSelected = { viewModel.selectHub(it) },
                                    onScheduleUpdated = { area, addr, date, slot, notes ->
                                        viewModel.updatePickupSchedule(area, addr, date, slot, notes)
                                    },
                                    totalCartCount = viewModel.totalCartBadgeCount,
                                    totalPricePKR = viewModel.totalCartPricePKR,
                                    isSubmitting = uiState.isSubmittingOrder,
                                    onConfirmOrderClick = { viewModel.createAndSubmitOrder() },
                                    onBackClick = { viewModel.navigateTo(Screen.CartCheckout) }
                                )
                            }"""

content = content.replace(call_old, call_new)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
