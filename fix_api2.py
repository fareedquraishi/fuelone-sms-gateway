with open('app/src/main/java/com/fuelone/smsgateway/server/ApiServer.kt', 'r') as f:
    content = f.read()

old = '''        val body = readBody(session)
        val req  = try {
            gson.fromJson(body, SendRequest::class.java)
        } catch (e: Exception) {
            return jsonError(400, "Invalid JSON: ${e.message}")
        }

        if (req.phone.isNullOrBlank() || req.message.isNullOrBlank()) {
            return jsonError(400, "phone and message are required")
        }'''

new = '''        val body = readBody(session)
        val req = try {
            val jsonObj = com.google.gson.JsonParser.parseString(body).asJsonObject
            val phone = if (jsonObj.has("phone") && !jsonObj.get("phone").isJsonNull)
                jsonObj.get("phone").asString
                else if (jsonObj.has("customer_mobile") && !jsonObj.get("customer_mobile").isJsonNull)
                jsonObj.get("customer_mobile").asString
                else ""
            val message = if (jsonObj.has("message") && !jsonObj.get("message").isJsonNull)
                jsonObj.get("message").asString
                else if (jsonObj.has("message_text") && !jsonObj.get("message_text").isJsonNull)
                jsonObj.get("message_text").asString
                else ""
            val messageId = if (jsonObj.has("message_id") && !jsonObj.get("message_id").isJsonNull)
                jsonObj.get("message_id").asString
                else if (jsonObj.has("entry_id") && !jsonObj.get("entry_id").isJsonNull)
                jsonObj.get("entry_id").asString
                else java.util.UUID.randomUUID().toString()
            val customerName = if (jsonObj.has("customer_name") && !jsonObj.get("customer_name").isJsonNull)
                jsonObj.get("customer_name").asString
                else ""
            SendRequest(phone = phone, message = message, messageId = messageId, customerName = customerName)
        } catch (e: Exception) {
            return jsonError(400, "Invalid JSON: ${e.message}")
        }

        if (req.phone.isNullOrBlank() || req.message.isNullOrBlank()) {
            return jsonError(400, "phone and message are required")
        }'''

if old in content:
    content = content.replace(old, new)
    print('Replaced successfully!')
else:
    print('Pattern not found - showing current handleSend:')
    start = content.find('private fun handleSend')
    print(content[start:start+800])

with open('app/src/main/java/com/fuelone/smsgateway/server/ApiServer.kt', 'w') as f:
    f.write(content)
