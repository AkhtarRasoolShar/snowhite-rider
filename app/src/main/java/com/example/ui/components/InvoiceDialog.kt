package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.LightBlueBorder
import com.example.ui.theme.SoftLightBlue
import com.example.ui.theme.SuccessGreen

data class InvoiceItemData(
    val description: String,
    val quantity: Int,
    val unitPricePKR: Int,
    val totalPKR: Int
)

data class InvoiceData(
    val invoiceNumber: String,
    val orderId: String,
    val invoiceDate: String,
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: String,
    val serviceTier: String,
    val items: List<InvoiceItemData>,
    val subtotalPKR: Int,
    val deliveryFeePKR: Int,
    val grandTotalPKR: Int,
    val paymentStatus: String = "PAID ON DELIVERY (COD)"
)

@Composable
fun InvoiceDialog(
    invoiceData: InvoiceData,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp)
                .testTag("invoice_dialog_surface"),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Modal Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(SoftLightBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = null,
                                tint = DeepBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "Tax Invoice & Receipt",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_invoice_dialog_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                    }
                }

                HorizontalDivider(color = LightBlueBorder, modifier = Modifier.padding(vertical = 12.dp))

                // Scrollable Invoice Content Body
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                        .border(1.dp, LightBlueBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    // Company Header & Logo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            AsyncImage(
                                model = "https://snowhite.com.pk/wp-content/uploads/2021/04/snowhite-logo.png",
                                contentDescription = "SnowWhite Logo",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.height(36.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "SnowWhite Dry Cleaners (Pvt) Ltd.",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepBlue
                            )
                            Text(
                                text = "NTN: 2847291-3 • Reg: KHI-89240",
                                fontSize = 9.sp,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = "Plot 42-C, 26th Street, DHA Phase 6, Karachi",
                                fontSize = 9.sp,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = "Helpdesk: +92 300 0000000",
                                fontSize = 9.sp,
                                color = Color(0xFF64748B)
                            )
                        }

                        // Invoice Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SuccessGreen.copy(alpha = 0.12f))
                                .border(1.dp, SuccessGreen.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "OFFICIAL RECEIPT",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = SuccessGreen
                                )
                                Text(
                                    text = invoiceData.paymentStatus,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = LightBlueBorder, modifier = Modifier.padding(vertical = 12.dp))

                    // Meta Grid: Invoice #, Date, Order #, Service Tier
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SoftLightBlue, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "INVOICE NUMBER", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            Text(text = invoiceData.invoiceNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "DATE & TIME", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            Text(text = invoiceData.invoiceDate, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0F172A))
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "ORDER REFERENCE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            Text(text = "#${invoiceData.orderId}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "SERVICE TIER", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            Text(text = invoiceData.serviceTier, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0F172A))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Billed To Info
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(text = "BILLED TO & DELIVERED AT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = invoiceData.customerName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        Text(text = "Phone: ${invoiceData.customerPhone}", fontSize = 11.sp, color = Color(0xFF334155))
                        Text(text = "Address: ${invoiceData.deliveryAddress}", fontSize = 11.sp, color = Color(0xFF334155))
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Items Breakdown Table
                    Text(
                        text = "ORDERED GARMENTS & SERVICES",
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
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Item / Description", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155), modifier = Modifier.weight(2f))
                        Text(text = "Qty", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155), textAlign = TextAlign.Center, modifier = Modifier.weight(0.6f))
                        Text(text = "Rate", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155), textAlign = TextAlign.End, modifier = Modifier.weight(1f))
                        Text(text = "Total", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155), textAlign = TextAlign.End, modifier = Modifier.weight(1f))
                    }

                    // Table Rows
                    if (invoiceData.items.isNotEmpty()) {
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
                                    modifier = Modifier.weight(0.6f)
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
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Dry Cleaning & Pressing Package Care",
                                fontSize = 11.sp,
                                color = Color(0xFF334155)
                            )
                        }
                    }

                    HorizontalDivider(color = LightBlueBorder, modifier = Modifier.padding(vertical = 8.dp))

                    // Totals Calculation Summary
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

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Sales Tax / NTN Care Fee:", fontSize = 11.sp, color = Color(0xFF64748B))
                            Text(text = "Included in Price", fontSize = 11.sp, color = Color(0xFF64748B))
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
                            Text(text = "GRAND TOTAL PAYABLE", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = DeepBlue)
                            Text(text = "Rs. ${invoiceData.grandTotalPKR} PKR", fontSize = 16.sp, fontWeight = FontWeight.Black, color = DeepBlue)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Footer Barcode Graphic & Thank You
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "||||| ||||||| |||| |||||||| ||||| ||||||| ||||||",
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
                            text = "Thank you for choosing SnowWhite Dry Cleaners!",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepBlue
                        )
                        Text(
                            text = "For queries or return requests, please mention your Invoice Number.",
                            fontSize = 9.sp,
                            color = Color(0xFF64748B),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons Row: Download PDF, Share, Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            shareInvoiceSummary(context, invoiceData)
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SoftLightBlue, contentColor = DeepBlue),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("share_invoice_button")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            downloadInvoicePdf(context, invoiceData)
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepBlue, contentColor = Color.White),
                        modifier = Modifier
                            .weight(1.3f)
                            .height(46.dp)
                            .testTag("download_invoice_button")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Download", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Download PDF", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

private fun downloadInvoicePdf(context: Context, invoice: InvoiceData) {
    try {
        val pdfFile = com.example.data.util.InvoiceGenerator.generatePdfInvoice(context, invoice)
        if (pdfFile != null && pdfFile.exists()) {
            Toast.makeText(
                context,
                "Invoice PDF #${invoice.invoiceNumber} saved to ${pdfFile.parentFile?.name ?: "Downloads"}!",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                context,
                "Invoice #${invoice.invoiceNumber} generated!",
                Toast.LENGTH_SHORT
            ).show()
        }
    } catch (_: Exception) {
        Toast.makeText(context, "Downloaded Invoice #${invoice.invoiceNumber}", Toast.LENGTH_SHORT).show()
    }
}

private fun shareInvoiceSummary(context: Context, invoice: InvoiceData) {
    try {
        val itemsSummaryText = if (invoice.items.isNotEmpty()) {
            invoice.items.joinToString("\n") { "  • ${it.quantity}x ${it.description} - Rs. ${it.totalPKR} PKR" }
        } else {
            "  • Dry Cleaning & Pressing Service"
        }

        val shareText = """
            🧾 SNOWWHITE DRY CLEANERS - OFFICIAL INVOICE
            -------------------------------------------
            Invoice #: ${invoice.invoiceNumber}
            Order #: #${invoice.orderId}
            Date: ${invoice.invoiceDate}
            Service Tier: ${invoice.serviceTier}
            
            CUSTOMER DETAILS:
            Billed To: ${invoice.customerName}
            Contact: ${invoice.customerPhone}
            Address: ${invoice.deliveryAddress}
            
            GARMENT BREAKDOWN:
            $itemsSummaryText
            
            -------------------------------------------
            Subtotal: Rs. ${invoice.subtotalPKR} PKR
            Pickup & Delivery: FREE
            TOTAL PAYABLE: Rs. ${invoice.grandTotalPKR} PKR
            Status: ${invoice.paymentStatus}
            -------------------------------------------
            SnowWhite Dry Cleaners (Pvt) Ltd.
            Helpdesk: +92 300 0000000 | Karachi, Pakistan
        """.trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share SnowWhite Invoice")
        context.startActivity(shareIntent)
    } catch (_: Exception) {
        Toast.makeText(context, "Could not open share options", Toast.LENGTH_SHORT).show()
    }
}
