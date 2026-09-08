with open("app/src/main/java/com/example/ui/components/DualServiceGrid.kt", "r") as f:
    content = f.read()

new_content = """
package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.GradientAccentBlue
import com.example.ui.theme.LightBlueBorder
import com.example.data.model.ServiceItem

@Composable
fun DualServiceGrid(
    services: List<ServiceItem> = emptyList(),
    onLaundryClick: () -> Unit,
    onProductsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Section Header
        Text(
            text = "Get Started",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Choose from our available services",
            fontSize = 11.sp,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(bottom = 12.dp, top = 2.dp)
        )
        
        val activeServices = services.filter { it.is_active == 1 }

        if (activeServices.isNotEmpty()) {
            val chunks = activeServices.chunked(2)
            for (rowItems in chunks) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (service in rowItems) {
                        val icon = when {
                            service.service_code?.contains("BIKE") == true -> Icons.Default.DirectionsBike
                            service.service_code?.contains("CAR") == true -> Icons.Default.LocalTaxi
                            service.service_code?.contains("COURIER") == true -> Icons.Default.LocalShipping
                            service.service_code?.contains("LAUNDRY") == true -> Icons.Default.LocalLaundryService
                            else -> Icons.Default.ShoppingBag
                        }
                        val color = if (service.service_code?.contains("LAUNDRY") == true) DeepBlue else GradientAccentBlue
                        
                        BentoServiceCard(
                            modifier = Modifier
                                .weight(1f)
                                .testTag("service_card_${service.id}"),
                            icon = icon,
                            iconBgColor = color,
                            title = service.name ?: "Service",
                            subtitle = if (service.service_code?.contains("LAUNDRY") == true) "Econo • Regular • Express" else "Base Fare: ${service.base_fare ?: 0}",
                            onClick = {
                                if (service.service_code?.contains("LAUNDRY") == true) onLaundryClick()
                                else onProductsClick()
                            }
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        } else {
            // Fallback to static if empty
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BentoServiceCard(
                    modifier = Modifier.weight(1f).testTag("laundry_service_card"),
                    icon = Icons.Default.LocalLaundryService,
                    iconBgColor = DeepBlue,
                    title = "Laundry / Dry Cleaning",
                    subtitle = "Econo • Regular • Express",
                    onClick = onLaundryClick
                )
                BentoServiceCard(
                    modifier = Modifier.weight(1f).testTag("products_service_card"),
                    icon = Icons.Default.ShoppingBag,
                    iconBgColor = GradientAccentBlue,
                    title = "Products",
                    subtitle = "Detergents & Care Kits",
                    onClick = onProductsClick
                )
            }
        }
    }
}

@Composable
private fun BentoServiceCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconBgColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    lineHeight = 18.sp
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}
"""

with open("app/src/main/java/com/example/ui/components/DualServiceGrid.kt", "w") as f:
    f.write(new_content.strip())
