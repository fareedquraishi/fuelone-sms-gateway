with open('app/src/main/java/com/fuelone/smsgateway/server/ApiServer.kt', 'r') as f:
    content = f.read()

# Replace the send handler to accept both field name formats
old_send = '''        val req  = try {
            gson.fromJson(body, SendRequest::class.java)
        } catch (e: Exception) {
            return jsonError(400, "Invalid JSON: \${e.message}")
        }

        if (req.phone.isNullOrBlank() || req.message.isNullOrBlank()) {
            return jsonError(400, "phone and message are required")
        }'''

new_send = '''        val req = try {
            val jsonObj = com.google.gson.JsonParser.parseString(body).asJsonObject
            val phone = when {
                jsonObj.has("phone") -> jsonObj.get("phone").asString
                jsonObj.has("customer_mobile") -> jsonObj.get("customer_mobile").asString
                else -> ""
            }
            val message = when {
                jsonObj.has("message") -> jsonObj.get("message").asString
                jsonObj.has("message_text") -> jsonObj.get("message_text").asString
                else -> ""
            }
            val messageId = when {
                jsonObj.has("message_id") -> jsonObj.get("message_id").asString
                jsonObj.has("entry_id") -> jsonObj.get("entry_id").asString
                else -> java.util.UUID.randomUUID().toString()
            }
            val customerName = when {
                jsonObj.has("customer_name") -> jsonObj.get("customer_name").asString
                else -> ""
            }
            SendRequest(phone = phone, message = message, messageId = messageId, customerName = customerName)
        } catch (e: Exception) {
            return jsonError(400, "Invalid JSON: \${e.message}")
        }

        if (req.phone.isNullOrBlank() || req.message.isNullOrBlank()) {
            return jsonError(400, "phone and message are required")
        }'''

content = content.replace(old_send, new_send)

with open('app/src/main/java/com/fuelone/smsgateway/server/ApiServer.kt', 'w') as f:
    f.write(content)
print('Done!')
