with open('app/src/main/java/com/fuelone/smsgateway/server/ApiServer.kt', 'r') as f:
    content = f.read()

# Make sure OPTIONS is handled for all URIs
old = '''                method == Method.OPTIONS                  -> handleOptions()'''
new = '''                method == Method.OPTIONS                  -> handleOptions()
                method == Method.OPTIONS && uri == "/send"  -> handleOptions()
                method == Method.OPTIONS && uri == "/reply" -> handleOptions()'''

# Fix handleOptions to be more permissive
old_options = '''    private fun handleOptions(): Response {
        val response = newFixedLengthResponse(Response.Status.OK, JSON_MIME, "{}")
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type")
        response.addHeader("Access-Control-Max-Age", "3600")
        return response
    }'''

new_options = '''    private fun handleOptions(): Response {
        val response = newFixedLengthResponse(Response.Status.OK, "text/plain", "")
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        response.addHeader("Access-Control-Allow-Headers", "*")
        response.addHeader("Access-Control-Max-Age", "86400")
        response.addHeader("Access-Control-Allow-Credentials", "true")
        return response
    }'''

content = content.replace(old_options, new_options)

with open('app/src/main/java/com/fuelone/smsgateway/server/ApiServer.kt', 'w') as f:
    f.write(content)
print('Done!')
