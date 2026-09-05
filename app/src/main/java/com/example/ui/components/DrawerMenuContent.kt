package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.GradientAccentBlue
import com.example.ui.theme.LightBlueBorder
import com.example.ui.theme.SoftLightBlue

@Composable
fun DrawerMenuContent(
    currentRoute: String,
    isLoggedIn: Boolean = true,
    userName: String? = null,
    userPhone: String? = null,
    onNavigateHome: () -> Unit,
    onNavigateBookLaundry: () -> Unit,
    onNavigateProducts: () -> Unit,
    onNavigateTracking: () -> Unit,
    onNavigateHistory: () -> Unit,
    onNavigatePriceList: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onNavigateNotificationSettings: () -> Unit = {},
    onNavigateSupportWhatsApp: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onCloseDrawer: () -> Unit
) {
    val displayName = if (isLoggedIn && !userName.isNullOrBlank()) userName else "Guest User"
    val displayPhone = if (isLoggedIn && !userPhone.isNullOrBlank()) userPhone else "Not logged in"

    ModalDrawerSheet(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
            .testTag("drawer_menu_sheet"),
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(bottom = 16.dp)
        ) {
            // Header Profile Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(DeepBlue, GradientAccentBlue)
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AcUnit,
                                contentDescription = null,
                                tint = DeepBlue,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "SnoWhite",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "DRY CLEANERS & LAUNDRY",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.8f),
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .clickable {
                                if (!isLoggedIn) {
                                    onLoginClick()
                                } else {
                                    onNavigateProfile()
                                }
                                onCloseDrawer()
                            }
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = displayName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                            Text(
                                text = if (isLoggedIn) "$displayPhone • Tap to view Profile" else "Tap here to Log In / Register",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Navigation Items
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                DrawerMenuItem(
                    icon = Icons.Default.Home,
                    label = "Home",
                    isSelected = currentRoute == "home",
                    onClick = {
                        onNavigateHome()
                        onCloseDrawer()
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.LocalLaundryService,
                    label = "Book Laundry / Dry Cleaning",
                    isSelected = currentRoute == "book",
                    onClick = {
                        onNavigateBookLaundry()
                        onCloseDrawer()
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.ShoppingBag,
                    label = "Care Products Shop",
                    isSelected = currentRoute == "products",
                    onClick = {
                        onNavigateProducts()
                        onCloseDrawer()
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.TrackChanges,
                    label = "Live Order Tracking",
                    isSelected = currentRoute == "tracking",
                    onClick = {
                        onNavigateTracking()
                        onCloseDrawer()
                    }
                )

                DrawerMenuItem(
                    icon = Icons.AutoMirrored.Filled.ListAlt,
                    label = "My Order History",
                    isSelected = currentRoute == "history",
                    onClick = {
                        onNavigateHistory()
                        onCloseDrawer()
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = LightBlueBorder
                )

                DrawerMenuItem(
                    icon = Icons.Default.Payments,
                    label = "Price List & Fabric Care",
                    isSelected = currentRoute == "pricelist",
                    onClick = {
                        onNavigatePriceList()
                        onCloseDrawer()
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.NotificationsActive,
                    label = "Push Notification Settings",
                    isSelected = currentRoute == "notifications",
                    onClick = {
                        onNavigateNotificationSettings()
                        onCloseDrawer()
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.Call,
                    label = "WhatsApp Support (+92 301 8637011)",
                    isSelected = false,
                    onClick = {
                        onNavigateSupportWhatsApp()
                        onCloseDrawer()
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = LightBlueBorder
                )

                if (isLoggedIn) {
                    DrawerMenuItem(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        label = "Log Out",
                        isSelected = false,
                        onClick = {
                            onLogout()
                            onCloseDrawer()
                        }
                    )
                } else {
                    DrawerMenuItem(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        label = "Log In / Register",
                        isSelected = false,
                        onClick = {
                            onLoginClick()
                            onCloseDrawer()
                        }
                    )
                }
            }

            // Footer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Snowhite DRYCLEANERS v7.0 • Karachi",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun String?.isNull_or_blank_safe(): Boolean = this.isNullOrBlank()

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) DeepBlue else Color(0xFF64748B)
            )
        },
        label = {
            Text(
                text = label,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp,
                color = if (isSelected) DeepBlue else MaterialTheme.colorScheme.onBackground
            )
        },
        selected = isSelected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = SoftLightBlue,
            unselectedContainerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(vertical = 2.dp)
    )
}
