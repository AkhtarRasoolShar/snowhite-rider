import re

with open("app/src/main/java/com/example/data/model/Models.kt", "r") as f:
    content = f.read()

# Add hub_name to CreateOrderRequest
content = content.replace('@SerializedName("service_tier") val service_tier: String? = null,', '@SerializedName("service_tier") val service_tier: String? = null,\n    @SerializedName("hub_name") val hub_name: String? = null,')

if "data class Hub" not in content:
    content += """

data class Hub(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("is_active") val isActive: Int? = null
)

data class GetHubsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<Hub>? = null,
    @SerializedName("hubs") val hubs: List<Hub>? = null
)
"""

with open("app/src/main/java/com/example/data/model/Models.kt", "w") as f:
    f.write(content)
