package com.example.data.model

import com.google.gson.annotations.SerializedName

enum class ServiceTierType(val id: String, val title: String, val deliveryTime: String, val priceMultiplier: Double, val badgeText: String, val description: String) {
    ECONO("econo", "Econo Service", "Standard 48 Hours", 1.0, "Budget Choice", "Economical care for everyday clothing. Washed, folded or ironed in 48 hours."),
    REGULAR("regular", "Regular Care", "Standard 24 Hours", 1.25, "Most Popular", "Premium dry cleaning and steam pressing delivered in 24 hours."),
    EXPRESS("express", "Express Same-Day", "Express 8 Hours", 1.75, "Super Fast", "Priority express washing, dry cleaning & custom pressing in 8 hours.")
}

enum class ItemCategory(val id: String, val displayName: String) {
    MEN("men", "Men"),
    WOMEN("women", "Women"),
    HOUSEHOLD("household", "Household")
}

data class GarmentItem(
    val id: String,
    val name: String,
    val category: ItemCategory,
    val basePricePKR: Int,
    val iconName: String,
    val description: String,
    val popularityBadge: String? = null
)

data class Category(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("type") val type: String? = null
)

data class Product(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("category_id") val category_id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("price") val rawPrice: Any? = null,
    @SerializedName("image_url") val image_url: String? = null
) {
    val price: Double
        get() {
            return when (rawPrice) {
                is Number -> rawPrice.toDouble()
                is String -> rawPrice.replace("Rs.", "").replace("PKR", "").trim().toDoubleOrNull() ?: 0.0
                else -> 0.0
            }
        }
}

data class CareProduct(
    val id: String,
    val name: String,
    val pricePKR: Int,
    val category: String,
    val description: String,
    val rating: Double,
    val volumeOrQty: String
)

data class CartItem(
    val garmentItem: GarmentItem? = null,
    val careProduct: CareProduct? = null,
    val product: Product? = null,
    var quantity: Int = 1,
    val serviceTier: ServiceTierType = ServiceTierType.REGULAR
) {
    val totalAmountPKR: Int
        get() {
            return if (garmentItem != null) {
                (garmentItem.basePricePKR * serviceTier.priceMultiplier * quantity).toInt()
            } else if (careProduct != null) {
                careProduct.pricePKR * quantity
            } else if (product != null) {
                ((product.price ?: 0.0) * quantity).toInt()
            } else 0
        }
}

data class PickupSchedule(
    val area: String = "DHA Phase 6, Karachi",
    val streetAddress: String = "Plot 42-C, 26th Street",
    val date: String = "Today, 31st Aug",
    val timeSlot: String = "Afternoon (12:00 PM - 4:00 PM)",
    val specialNotes: String = ""
)

enum class OrderStatus(val stepIndex: Int, val title: String, val subtitle: String) {
    COLLECTING(0, "Collecting", "Rider is scheduled for collecting garments"),
    RECEIVED_AT_HUB(1, "Received at Hub", "Garments received and logged at cleaning hub"),
    IN_WASHING(2, "In Washing", "Garments undergoing expert eco-cleaning & pressing"),
    OUT_FOR_DELIVERY(3, "Out for Delivery", "Rider is delivering crisp fresh clothes to your doorstep"),
    DELIVERED(4, "Delivered", "Order successfully fulfilled. Thank you for choosing SnoWhite!")
}

data class OrderItemRequest(
    @SerializedName("item") val item: String? = null,
    @SerializedName("qty") val qty: Int? = null,
    @SerializedName("price") val price: Int? = null
)

data class CreateOrderRequest(
    @SerializedName("customer_id") val customer_id: Int? = 1,
    @SerializedName("customer_name") val customer_name: String? = "Customer",
    @SerializedName("customer_phone") val customer_phone: String? = "+923000000000",
    @SerializedName("pickup_address") val pickup_address: String? = null,
    @SerializedName("delivery_address") val delivery_address: String? = null,
    @SerializedName("pickup_time_slot") val pickup_time_slot: String? = null,
    @SerializedName("service_tier") val service_tier: String? = null,
    @SerializedName("total_amount") val total_amount: Int? = null,
    @SerializedName("items") val items: List<OrderItemRequest>? = null
)

data class CreateOrderResponse(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("orderId") val orderId: String? = null,
    @SerializedName("order_id") val order_id: String? = null,
    @SerializedName("trackingCode") val trackingCode: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("estimatedDelivery") val estimatedDelivery: String? = null,
    @SerializedName("riderName") val riderName: String? = null,
    @SerializedName("riderPhone") val riderPhone: String? = null,
    @SerializedName("rider_name") val rider_name: String? = null,
    @SerializedName("rider_phone") val rider_phone: String? = null,
    @SerializedName("message") val message: String? = null
)

data class RemoteOrder(
    @SerializedName("id") val id: String? = null,
    @SerializedName("order_id") val order_id: String? = null,
    @SerializedName("orderId") val orderId: String? = null,
    @SerializedName("date") val date: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("order_date") val orderDate: String? = null,
    @SerializedName("service_tier") val service_tier: String? = null,
    @SerializedName("pickup_address") val pickup_address: String? = null,
    @SerializedName("delivery_address") val delivery_address: String? = null,
    @SerializedName("total_amount") val total_amount: Int? = 0,
    @SerializedName("totalAmountPKR") val totalAmountPKR: Int? = 0,
    @SerializedName("status") val status: String? = null,
    @SerializedName("items") val items: List<OrderItemRequest>? = null,
    @SerializedName("rider_name") val rider_name: String? = null,
    @SerializedName("rider_phone") val rider_phone: String? = null,
    @SerializedName("riderName") val riderNameAlt: String? = null,
    @SerializedName("riderPhone") val riderPhoneAlt: String? = null
) {
    val displayOrderId: String
        get() = order_id ?: orderId ?: id ?: "SW-1001"

    val displayDate: String
        get() {
            val raw = date ?: createdAt ?: orderDate
            return if (!raw.isNullOrBlank() && !raw.equals("N/A", ignoreCase = true)) {
                raw
            } else {
                "Just now"
            }
        }

    val displayAmount: Int
        get() = if (total_amount != null && total_amount > 0) total_amount else (totalAmountPKR ?: 0)

    val displayStatus: String
        get() = (status ?: "COLLECTING").replace("_", " ").uppercase()

    val displayRiderName: String?
        get() = rider_name ?: riderNameAlt

    val displayRiderPhone: String?
        get() = rider_phone ?: riderPhoneAlt

    val statusStepIndex: Int
        get() {
            val clean = (status ?: "COLLECTING").uppercase().replace(" ", "_").trim()
            return when {
                clean.contains("DELIVERED") || clean == "4" || clean.contains("COMPLETED") -> 4
                clean.contains("OUT_FOR_DELIVERY") || clean == "3" || clean.contains("DISPATCHED") -> 3
                clean.contains("IN_WASHING") || clean == "2" || clean.contains("WASHING") || clean.contains("CLEANING") -> 2
                clean.contains("RECEIVED_AT_HUB") || clean == "1" || clean.contains("RECEIVED") || clean.contains("HUB") -> 1
                else -> 0
            }
        }
}

typealias CustomerOrder = RemoteOrder

data class GetOrdersResponse(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("orders") val orders: List<RemoteOrder>? = null,
    @SerializedName("data") val data: List<RemoteOrder>? = null
)

data class CustomerReview(
    val id: String,
    val authorName: String,
    val city: String,
    val rating: Int,
    val comment: String,
    val dateAgo: String,
    val verifiedBuyer: Boolean = true
)

data class LoginRequest(
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("password") val password: String? = null
)

data class RegisterRequest(
    @SerializedName("name") val name: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("password") val password: String? = null
)

data class AuthResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("customer_id") val customer_id: Any? = null,
    @SerializedName("user_id") val user_id: Any? = null,
    @SerializedName("userId") val userIdAlt: Any? = null,
    @SerializedName("id") val id: Any? = null,
    @SerializedName("user") val user: Map<String, Any?>? = null,
    @SerializedName("data") val data: Map<String, Any?>? = null,
    @SerializedName("customer") val customer: Map<String, Any?>? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("customer_name") val customer_name: String? = null,
    @SerializedName("customer_phone") val customer_phone: String? = null,
    @SerializedName("error") val error: String? = null
) {
    fun extractUserId(): Int? {
        fun parseId(value: Any?): Int? {
            return when (value) {
                is Number -> value.toInt()
                is String -> value.toDoubleOrNull()?.toInt() ?: value.toIntOrNull()
                else -> null
            }
        }

        return parseId(user_id)
            ?: parseId(customer_id)
            ?: parseId(userIdAlt)
            ?: parseId(id)
            ?: parseId(user?.get("id") ?: user?.get("user_id") ?: user?.get("customer_id"))
            ?: parseId(data?.get("id") ?: data?.get("user_id") ?: data?.get("customer_id"))
            ?: parseId(customer?.get("id") ?: customer?.get("customer_id"))
    }

    fun extractUserName(): String? {
        val raw = name ?: customer_name
            ?: (user?.get("name") ?: user?.get("customer_name") ?: data?.get("name") ?: customer?.get("name")) as? String
        return raw?.takeIf { it.isNotBlank() }
    }

    fun extractUserPhone(): String? {
        val raw = phone ?: customer_phone
            ?: (user?.get("phone") ?: user?.get("customer_phone") ?: data?.get("phone") ?: customer?.get("phone")) as? String
        return raw?.takeIf { it.isNotBlank() }
    }
}

data class EmailInvoiceRequest(
    @SerializedName("order_id") val order_id: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("invoice_number") val invoice_number: String? = null
)

data class EmailInvoiceResponse(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("deep_link") val deep_link: String? = null
)

data class ServiceItem(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("base_fare") val base_fare: String? = null,
    @SerializedName("per_km_rate") val per_km_rate: String? = null,
    @SerializedName("price") val price: Double? = null,
    @SerializedName("category") val category: String? = null
)

data class ApiResponse<T>(
    @SerializedName("status") val status: String? = null,
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: T? = null
)

data class ChatMessage(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("order_id") val orderId: Int? = null,
    @SerializedName("sender_type") val senderType: String? = null, // "customer" or "rider"
    @SerializedName("sender_id") val senderId: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
) {
    val isFromCustomer: Boolean
        get() = senderType.equals("customer", ignoreCase = true)

    val isFromRider: Boolean
        get() = senderType.equals("rider", ignoreCase = true)

    val formattedTime: String
        get() {
            if (createdAt.isNullOrBlank()) return "Just now"
            return try {
                if (createdAt.contains(" ")) {
                    createdAt.substringAfter(" ").substringBeforeLast(":")
                } else if (createdAt.contains("T")) {
                    createdAt.substringAfter("T").substringBeforeLast(":")
                } else {
                    createdAt
                }
            } catch (_: Exception) {
                createdAt
            }
        }
}

data class SendChatMessageRequest(
    @SerializedName("order_id") val order_id: Int,
    @SerializedName("sender_type") val sender_type: String,
    @SerializedName("sender_id") val sender_id: Int,
    @SerializedName("message") val message: String
)

data class ChatMessagesResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: List<ChatMessage>? = null,
    @SerializedName("messages") val messages: List<ChatMessage>? = null
)

