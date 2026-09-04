package com.example.data.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.example.ui.components.InvoiceData
import java.io.File
import java.io.FileOutputStream

object InvoiceGenerator {
    /**
     * Generates a structured PDF file for an order invoice using Android PdfDocument.
     * Returns the generated File object.
     */
    fun generatePdfInvoice(context: Context, invoice: InvoiceData): File? {
        return try {
            val pdfDocument = PdfDocument()
            // Standard A4 width = 595, height = 842 at 72 dpi
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas: Canvas = page.canvas

            val paint = Paint()
            val textPaint = Paint()

            // Canvas Background
            canvas.drawColor(Color.WHITE)

            // 1. Header Banner (Deep Blue)
            paint.color = Color.parseColor("#0F2C59")
            canvas.drawRect(0f, 0f, 595f, 100f, paint)

            // Header Text
            textPaint.color = Color.WHITE
            textPaint.textSize = 22f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("SNOWWHITE DRY CLEANERS", 30f, 42f, textPaint)

            textPaint.textSize = 10f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText("Premium Eco-Friendly Dry Cleaning & Steaming Care", 30f, 60f, textPaint)
            canvas.drawText("NTN: 2847291-3 • DHA Phase 6, Karachi • Helpdesk / WhatsApp: +92 301 8637011", 30f, 78f, textPaint)

            // Invoice Title Badge Right
            textPaint.color = Color.parseColor("#22C55E") // Success Green
            textPaint.textSize = 14f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("TAX INVOICE", 450f, 45f, textPaint)

            textPaint.color = Color.WHITE
            textPaint.textSize = 9f
            canvas.drawText(invoice.paymentStatus, 430f, 65f, textPaint)

            // 2. Meta Section Box (Soft Light Gray/Blue background)
            paint.color = Color.parseColor("#F1F5F9")
            canvas.drawRoundRect(30f, 115f, 565f, 185f, 12f, 12f, paint)

            // Meta Text Left
            textPaint.color = Color.parseColor("#64748B")
            textPaint.textSize = 9f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("INVOICE NUMBER", 45f, 135f, textPaint)
            canvas.drawText("DATE & TIME", 45f, 160f, textPaint)

            textPaint.color = Color.parseColor("#0F2C59")
            textPaint.textSize = 11f
            canvas.drawText(invoice.invoiceNumber, 150f, 135f, textPaint)
            canvas.drawText(invoice.invoiceDate, 150f, 160f, textPaint)

            // Meta Text Right
            textPaint.color = Color.parseColor("#64748B")
            textPaint.textSize = 9f
            canvas.drawText("ORDER REF #", 330f, 135f, textPaint)
            canvas.drawText("SERVICE TIER", 330f, 160f, textPaint)

            textPaint.color = Color.parseColor("#0F2C59")
            textPaint.textSize = 11f
            canvas.drawText("#${invoice.orderId}", 430f, 135f, textPaint)
            canvas.drawText(invoice.serviceTier, 430f, 160f, textPaint)

            // 3. Customer Billing Info
            textPaint.color = Color.parseColor("#64748B")
            textPaint.textSize = 10f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("BILLED TO & DELIVERED AT", 30f, 210f, textPaint)

            textPaint.color = Color.parseColor("#0F172A")
            textPaint.textSize = 12f
            canvas.drawText(invoice.customerName, 30f, 228f, textPaint)

            textPaint.textSize = 10f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText("Phone: ${invoice.customerPhone}", 30f, 244f, textPaint)
            canvas.drawText("Address: ${invoice.deliveryAddress}", 30f, 258f, textPaint)

            // 4. Items Table Header
            paint.color = Color.parseColor("#0F2C59")
            canvas.drawRect(30f, 280f, 565f, 302f, paint)

            textPaint.color = Color.WHITE
            textPaint.textSize = 10f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Item / Garment Description", 40f, 295f, textPaint)
            canvas.drawText("Qty", 340f, 295f, textPaint)
            canvas.drawText("Rate (PKR)", 400f, 295f, textPaint)
            canvas.drawText("Total (PKR)", 485f, 295f, textPaint)

            // Items Table Rows
            var currentY = 320f
            textPaint.color = Color.parseColor("#0F172A")
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

            val items = if (invoice.items.isNotEmpty()) invoice.items else listOf()
            items.forEachIndexed { index, item ->
                if (index % 2 == 1) {
                    paint.color = Color.parseColor("#F8FAFC")
                    canvas.drawRect(30f, currentY - 14f, 565f, currentY + 8f, paint)
                }

                canvas.drawText(item.description, 40f, currentY, textPaint)
                canvas.drawText("${item.quantity}", 345f, currentY, textPaint)
                canvas.drawText("Rs. ${item.unitPricePKR}", 400f, currentY, textPaint)

                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("Rs. ${item.totalPKR}", 485f, currentY, textPaint)
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

                currentY += 22f
            }

            // Divider Line
            paint.color = Color.parseColor("#CBD5E1")
            canvas.drawLine(30f, currentY + 5f, 565f, currentY + 5f, paint)
            currentY += 20f

            // 5. Totals Calculation Section
            textPaint.color = Color.parseColor("#64748B")
            textPaint.textSize = 10f
            canvas.drawText("Subtotal:", 340f, currentY, textPaint)
            textPaint.color = Color.parseColor("#0F172A")
            canvas.drawText("Rs. ${invoice.subtotalPKR} PKR", 470f, currentY, textPaint)
            currentY += 18f

            textPaint.color = Color.parseColor("#64748B")
            canvas.drawText("Doorstep Delivery:", 340f, currentY, textPaint)
            textPaint.color = if (invoice.deliveryFeePKR > 0) Color.parseColor("#0F172A") else Color.parseColor("#22C55E")
            val delText = if (invoice.deliveryFeePKR > 0) "Rs. ${invoice.deliveryFeePKR} PKR" else "FREE"
            canvas.drawText(delText, 470f, currentY, textPaint)
            currentY += 22f

            // Grand Total Box
            paint.color = Color.parseColor("#E0F2FE")
            canvas.drawRoundRect(330f, currentY, 565f, currentY + 36f, 8f, 8f, paint)

            textPaint.color = Color.parseColor("#0F2C59")
            textPaint.textSize = 11f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("GRAND TOTAL:", 340f, currentY + 22f, textPaint)

            textPaint.textSize = 14f
            canvas.drawText("Rs. ${invoice.grandTotalPKR} PKR", 450f, currentY + 23f, textPaint)

            currentY += 70f

            // 6. Barcode & Thank you footer
            textPaint.color = Color.parseColor("#334155")
            textPaint.textSize = 14f
            textPaint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
            canvas.drawText("||||| ||||||| |||| |||||||| ||||| |||||||", 180f, currentY, textPaint)

            currentY += 16f
            textPaint.textSize = 9f
            textPaint.color = Color.parseColor("#64748B")
            canvas.drawText("*INV-${invoice.invoiceNumber}*", 230f, currentY, textPaint)

            currentY += 25f
            textPaint.color = Color.parseColor("#0F2C59")
            textPaint.textSize = 11f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Thank you for choosing SnowWhite Dry Cleaners!", 160f, currentY, textPaint)

            pdfDocument.finishPage(page)

            // Save PDF File
            val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.cacheDir
            if (!downloadsDir.exists()) downloadsDir.mkdirs()
            val pdfFile = File(downloadsDir, "SnowWhite_Invoice_${invoice.orderId}.pdf")

            val fileOutputStream = FileOutputStream(pdfFile)
            pdfDocument.writeTo(fileOutputStream)
            fileOutputStream.close()
            pdfDocument.close()

            pdfFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
