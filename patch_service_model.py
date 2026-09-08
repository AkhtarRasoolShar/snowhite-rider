import re
with open("app/src/main/java/com/example/data/model/Models.kt", "r") as f:
    content = f.read()

content = content.replace(
    "@SerializedName(\"is_active\") val is_active: Int? = null",
    "@SerializedName(\"is_active\") val is_active: Int? = null,\n    @SerializedName(\"pricing_type\") val pricing_type: String? = null"
)

with open("app/src/main/java/com/example/data/model/Models.kt", "w") as f:
    f.write(content)
