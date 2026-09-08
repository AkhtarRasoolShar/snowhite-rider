import re
with open("app/src/main/java/com/example/data/model/Models.kt", "r") as f:
    content = f.read()

content = content.replace(
    "@SerializedName(\"type\") val type: String? = null",
    "@SerializedName(\"type\") val type: String? = null,\n    @SerializedName(\"is_active\") val is_active: Int? = null"
)

with open("app/src/main/java/com/example/data/model/Models.kt", "w") as f:
    f.write(content)
