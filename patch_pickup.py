import re

with open("app/src/main/java/com/example/ui/screens/PickupSchedulingScreen.kt", "r") as f:
    content = f.read()

# Add availableHubs and selectedHub to signature
signature_old = """@Composable
fun PickupSchedulingScreen(
    pickupSchedule: PickupSchedule,
    onScheduleUpdated: (String?, String?, String?, String?, String?) -> Unit,
    totalCartCount: Int,
    totalPricePKR: Int,
    isSubmitting: Boolean,
    onConfirmOrderClick: () -> Unit,
    onBackClick: () -> Unit = {}
) {"""

signature_new = """import com.example.data.model.Hub

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupSchedulingScreen(
    pickupSchedule: PickupSchedule,
    availableHubs: List<Hub>,
    selectedHub: Hub?,
    onHubSelected: (Hub) -> Unit,
    onScheduleUpdated: (String?, String?, String?, String?, String?) -> Unit,
    totalCartCount: Int,
    totalPricePKR: Int,
    isSubmitting: Boolean,
    onConfirmOrderClick: () -> Unit,
    onBackClick: () -> Unit = {}
) {"""
content = content.replace(signature_old.replace("@OptIn(ExperimentalMaterial3Api::class)\n", ""), signature_new)

# Add dropdown
hub_dropdown = """                // Street Address Field
                OutlinedTextField(
                    value = pickupSchedule.streetAddress,
                    onValueChange = { onScheduleUpdated(null, it, null, null, null) },
                    label = { Text("House / Apartment / Street Address") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = DeepBlue) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepBlue,
                        unfocusedBorderColor = LightBlueBorder
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("street_address_textfield")
                )
                
                // Select Nearest Hub Dropdown
                var isHubDropdownExpanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedHub?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Nearest Hub *") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                modifier = Modifier.clickable { isHubDropdownExpanded = true }
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeepBlue,
                            unfocusedBorderColor = LightBlueBorder
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isHubDropdownExpanded = true }
                            .testTag("hub_picker_dropdown")
                    )

                    DropdownMenu(
                        expanded = isHubDropdownExpanded,
                        onDismissRequest = { isHubDropdownExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.85f)
                    ) {
                        availableHubs.forEach { hub ->
                            DropdownMenuItem(
                                text = { Text("${hub.name} - ${hub.city ?: ""}", fontSize = 13.sp) },
                                onClick = {
                                    onHubSelected(hub)
                                    isHubDropdownExpanded = false
                                }
                            )
                        }
                    }
                }"""
content = re.sub(r"// Street Address Field.*?\.testTag\(\"street_address_textfield\"\)\s*\n\s*\)", hub_dropdown, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/ui/screens/PickupSchedulingScreen.kt", "w") as f:
    f.write(content)
