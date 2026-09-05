import re

with open("app/src/main/java/com/example/data/model/Models.kt", "r") as f:
    content = f.read()

bad_remote_order = """data class RemoteOrder(
    @SerializedName("id") val id: String? = null,
    @SerializedName("order_id") val order_id: String? = null,
    @SerializedName("orderId") val orderId: String? = null,
    @SerializedName("date") val date: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("order_date") val orderDate: String? = null,
    @SerializedName("service_tier") val service_tier: String? = null,
    @SerializedName("pickup_address") val pickup_address: String? = null,
    @SerializedName("delivery_address") val delivery_address: String? = null,
    @SerializedName("total_amount") val rawTotalAmount: Any? = null,
    @SerializedName("totalAmountPKR") val rawTotalAmountPKR: Any? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("items") val items: List<OrderItemRequest>? = null,
    @SerializedName("rider_name") val rider_name: String? = null,
    @SerializedName("rider_phone") val rider_phone: String? = null,
    @SerializedName("riderName") val riderNameAlt: String? = null,
    @SerializedName("riderPhone") val riderPhoneAlt: String? = null
) {
    val total_amount: Int
        get() = when(rawTotalAmount) {
            is Number -> rawTotalAmount.toInt()
            is String -> rawTotalAmount.toDoubleOrNull()?.toInt() ?: 0
            else -> 0
        }
    val totalAmountPKR: Int
        get() = when(rawTotalAmountPKR) {
            is Number -> rawTotalAmountPKR.toInt()
            is String -> rawTotalAmountPKR.toDoubleOrNull()?.toInt() ?: 0
            else -> total_amount
        }

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
        get() = if (total_amount != null && total_amount > 0) total_amount else (totalAmountPKR ?: 0)"""

fixed_remote_order = """data class RemoteOrder(
    @SerializedName("id") val id: String? = null,
    @SerializedName("order_id") val order_id: String? = null,
    @SerializedName("orderId") val orderId: String? = null,
    @SerializedName("date") val date: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("order_date") val orderDate: String? = null,
    @SerializedName("service_tier") val service_tier: String? = null,
    @SerializedName("pickup_address") val pickup_address: String? = null,
    @SerializedName("delivery_address") val delivery_address: String? = null,
    @SerializedName("total_amount") val total_amount: String? = null,
    @SerializedName("totalAmountPKR") val totalAmountPKR: String? = null,
    @SerializedName("status") val status: String? = null,
    // Removed items list due to PHP empty string parsing crashes. Can be safely restored when backend issues arrays.
    @SerializedName("rider_name") val rider_name: String? = null,
    @SerializedName("rider_phone") val rider_phone: String? = null,
    @SerializedName("riderName") val riderNameAlt: String? = null,
    @SerializedName("riderPhone") val riderPhoneAlt: String? = null
) {
    val items: List<OrderItemRequest> = emptyList()

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
        get() = (total_amount ?: totalAmountPKR)?.toDoubleOrNull()?.toInt() ?: 0"""

content = content.replace(bad_remote_order, fixed_remote_order)

with open("app/src/main/java/com/example/data/model/Models.kt", "w") as f:
    f.write(content)
print("Replaced RemoteOrder")
