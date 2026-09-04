package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.request.ImageRequest
import coil.compose.AsyncImage
import com.example.data.util.InvoiceGenerator
import com.example.ui.components.InvoiceData
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.LightBlueBorder
import com.example.ui.theme.SoftLightBlue
import com.example.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceScreen(
    invoiceData: InvoiceData,
    isPreOrderQuote: Boolean = false,
    userEmail: String = "akhtarhussain1452@gmail.com",
    onBackClick: () -> Unit,
    onSendEmail: (email: String, orderId: String, invoiceNumber: String, callback: (Boolean, String, String?) -> Unit) -> Unit,
    onPrimaryAction: () -> Unit
) {
    val context = LocalContext.current
    var isSendingEmail by remember { mutableStateOf(false) }
    var emailStatusMessage by remember { mutableStateOf<String?>(null) }
    var generatedDeepLink by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isPreOrderQuote) "Pre-Order Price Estimate" else "Tax Invoice & Receipt",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (isPreOrderQuote) "Review charges before placing order" else "Invoice #${invoiceData.invoiceNumber}",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("invoice_screen_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DeepBlue
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val pdfFile = InvoiceGenerator.generatePdfInvoice(context, invoiceData)
                            if (pdfFile != null && pdfFile.exists()) {
                                Toast.makeText(
                                    context,
                                    "Invoice PDF generated: ${pdfFile.name}",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(context, "Downloaded Invoice #${invoiceData.invoiceNumber}", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.testTag("invoice_screen_top_download_button")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Download PDF", tint = DeepBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Notice Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isPreOrderQuote) SoftLightBlue else SuccessGreen.copy(alpha = 0.1f)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isPreOrderQuote) LightBlueBorder else SuccessGreen.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = if (isPreOrderQuote) Icons.Default.Info else Icons.Default.Receipt,
                        contentDescription = null,
                        tint = if (isPreOrderQuote) DeepBlue else SuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isPreOrderQuote) "PRE-ORDER COST BREAKDOWN" else "OFFICIAL TAX INVOICE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isPreOrderQuote) DeepBlue else SuccessGreen
                        )
                        Text(
                            text = if (isPreOrderQuote)
                                "This itemized quote reflects your selected garments, service tier multiplier, and doorstep delivery fee."
                            else
                                "Payment logged for Order #${invoiceData.orderId}. Keep this receipt for your records.",
                            fontSize = 11.sp,
                            color = Color(0xFF334155)
                        )
                    }
                }
            }

            // Main Paper Invoice Container
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("invoice_screen_paper_surface"),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, LightBlueBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Header Logo & Company Info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data("https://snowhite.com.pk/wp-content/uploads/2021/04/snowhite-logo.png")
                                    .allowHardware(false)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "SnoWhite Logo",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.height(38.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "SnoWhite Dry Cleaners (Pvt) Ltd.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepBlue
                            )
                            Text(text = "NTN: 2847291-3 • Reg: KHI-89240", fontSize = 10.sp, color = Color(0xFF64748B))
                            Text(text = "Plot 42-C, 26th St, DHA Phase 6, Karachi", fontSize = 10.sp, color = Color(0xFF64748B))
                            Text(text = "Helpdesk (WhatsApp): +92 301 8637011", fontSize = 10.sp, color = Color(0xFF64748B))
                        }

                        // Badge Right
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isPreOrderQuote) SoftLightBlue else SuccessGreen.copy(alpha = 0.15f))
                                .border(1.dp, if (isPreOrderQuote) LightBlueBorder else SuccessGreen.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (isPreOrderQuote) "ESTIMATE" else "PAID",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isPreOrderQuote) DeepBlue else SuccessGreen
                            )
                        }
                    }

                    HorizontalDivider(color = LightBlueBorder, modifier = Modifier.padding(vertical = 14.dp))

                    // Meta Summary Grid
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SoftLightBlue, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "INVOICE NO", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            Text(text = invoiceData.invoiceNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "DATE / SCHEDULE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            Text(text = invoiceData.invoiceDate, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0F172A))
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "ORDER REF", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            Text(text = "#${invoiceData.orderId}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "SERVICE TIER", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            Text(text = invoiceData.serviceTier, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0F172A))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Billed To Info
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp)
                    ) {
                        Text(text = "BILLED TO & DELIVERED AT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = invoiceData.customerName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        Text(text = "Phone: ${invoiceData.customerPhone}", fontSize = 11.sp, color = Color(0xFF334155))
                        Text(text = "Address: ${invoiceData.deliveryAddress}", fontSize = 11.sp, color = Color(0xFF334155))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Itemized Table
                    Text(
                        text = "ITEMIZED CHARGES BREAKDOWN",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE2E8F0), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Garment / Service Description", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155), modifier = Modifier.weight(2f))
                        Text(text = "Qty", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155), textAlign = TextAlign.Center, modifier = Modifier.weight(0.5f))
                        Text(text = "Rate", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155), textAlign = TextAlign.End, modifier = Modifier.weight(1f))
                        Text(text = "Total", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155), textAlign = TextAlign.End, modifier = Modifier.weight(1f))
                    }

                    // Table Items
                    invoiceData.items.forEachIndexed { index, item ->
                        val rowBg = if (index % 2 == 0) Color.White else Color(0xFFF8FAFC)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(rowBg)
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.description,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF0F172A),
                                modifier = Modifier.weight(2f)
                            )
                            Text(
                                text = "${item.quantity}",
                                fontSize = 11.sp,
                                color = Color(0xFF334155),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.5f)
                            )
                            Text(
                                text = "Rs. ${item.unitPricePKR}",
                                fontSize = 11.sp,
                                color = Color(0xFF334155),
                                textAlign = TextAlign.End,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Rs. ${item.totalPKR}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepBlue,
                                textAlign = TextAlign.End,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    HorizontalDivider(color = LightBlueBorder, modifier = Modifier.padding(vertical = 10.dp))

                    // Totals
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Subtotal:", fontSize = 11.sp, color = Color(0xFF64748B))
                            Text(text = "Rs. ${invoiceData.subtotalPKR} PKR", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0F172A))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Doorstep Pickup & Delivery:", fontSize = 11.sp, color = Color(0xFF64748B))
                            Text(
                                text = if (invoiceData.deliveryFeePKR > 0) "Rs. ${invoiceData.deliveryFeePKR} PKR" else "FREE (Complimentary)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (invoiceData.deliveryFeePKR > 0) Color(0xFF0F172A) else SuccessGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DeepBlue.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "TOTAL AMOUNT PAYABLE", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = DeepBlue)
                            Text(text = "Rs. ${invoiceData.grandTotalPKR} PKR", fontSize = 16.sp, fontWeight = FontWeight.Black, color = DeepBlue)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Barcode & Footer
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "||||| ||||||| |||| |||||||| ||||| |||||||",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF334155),
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "*INV-${invoiceData.invoiceNumber}*",
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Thank you for choosing SnoWhite Dry Cleaners!",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepBlue
                        )
                    }
                }
            }

            // Email & Share Deep Link Action Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, LightBlueBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "EMAIL & SHARE INVOICE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Text(
                        text = "Send receipt to $userEmail or copy shareable link.",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                isSendingEmail = true
                                onSendEmail(userEmail, invoiceData.orderId, invoiceData.invoiceNumber) { success, msg, link ->
                                    isSendingEmail = false
                                    emailStatusMessage = msg
                                    generatedDeepLink = link
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SoftLightBlue, contentColor = DeepBlue),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("email_invoice_screen_button")
                        ) {
                            Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Email Invoice", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val pdfFile = InvoiceGenerator.generatePdfInvoice(context, invoiceData)
                                if (pdfFile != null && pdfFile.exists()) {
                                    Toast.makeText(
                                        context,
                                        "Saved PDF: ${pdfFile.absolutePath}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(context, "Invoice PDF saved to Downloads!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepBlue, contentColor = Color.White),
                            modifier = Modifier
                                .weight(1.2f)
                                .height(44.dp)
                                .testTag("download_pdf_screen_button")
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Download PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    emailStatusMessage?.let { status ->
                        Text(
                            text = status,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }

                    generatedDeepLink?.let { deepLink ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SoftLightBlue, RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = deepLink,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = DeepBlue,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, "SnoWhite Invoice Deep Link: $deepLink")
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share Deep Link"))
                                }
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "Share Link", tint = DeepBlue, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            // Bottom Primary Navigation Action Button
            Button(
                onClick = onPrimaryAction,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepBlue, contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("invoice_screen_primary_action_button")
            ) {
                Text(
                    text = if (isPreOrderQuote) "Proceed to Confirm Booking ➔" else "View Order History ➔",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
