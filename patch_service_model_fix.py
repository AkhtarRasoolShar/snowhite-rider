import re
with open("app/src/main/java/com/example/data/model/Models.kt", "r") as f:
    content = f.read()

# Remove the previously added fields
content = content.replace(
    "@SerializedName(\"id\") val id: Int? = null,\n    @SerializedName(\"service_code\") val service_code: String? = null,\n    @SerializedName(\"is_active\") val is_active: Int? = null,",
    "@SerializedName(\"id\") val id: Int? = null,"
)

# Add them at the end of ServiceItem
content = content.replace(
    "@SerializedName(\"category\") val category: String? = null",
    "@SerializedName(\"category\") val category: String? = null,\n    @SerializedName(\"service_code\") val service_code: String? = null,\n    @SerializedName(\"is_active\") val is_active: Int? = null"
)

with open("app/src/main/java/com/example/data/model/Models.kt", "w") as f:
    f.write(content)
