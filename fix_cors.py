with open('app/src/main/java/com/fuelone/smsgateway/server/ApiServer.kt', 'r') as f:
    content = f.read()

# Add CORS headers to all responses
old_ok = '''    private fun jsonOk(json: String): Response =
        newFixedLengthResponse(Response.Status.OK, JSON_MIME, json)'''

new_ok = '''    private fun jsonOk(json: String): Response {
        val response = newFixedLengthResponse(Response.Status.OK, JSON_MIME, json)
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type")
        return response
    }'''

old_error = '''    private fun jsonError(code: Int, message: String): Response =
        newFixedLengthResponse(
            Response.Status.lookup(code),
            JSON_MIME,
            """{"error":"$message"}"""
        )'''

new_error = '''    private fun jsonError(code: Int, message: String): Response {
        val response = newFixedLengthResponse(
            Response.Status.lookup(code),
            JSON_MIME,
            """{"error":"$message"}"""
        )
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type")
        return response
    }'''

# Add OPTIONS handler for preflight requests
old_router = '''        return try {
            when {
                method == Method.POST && uri == "/send"   -> handleSend(session)
                method == Method.GET  && uri == "/status" -> handleStatus()
                method == Method.GET  && uri == "/inbox"  -> handleInbox()
                method == Method.POST && uri == "/reply"  -> handleReply(session)
                method == Method.GET  && uri == "/"       -> handleRoot()
                else -> jsonError(404, "Not found")
            }'''

new_router = '''        return try {
            when {
                method == Method.OPTIONS                  -> handleOptions()
                method == Method.POST && uri == "/send"   -> handleSend(session)
                method == Method.GET  && uri == "/status" -> handleStatus()
                method == Method.GET  && uri == "/inbox"  -> handleInbox()
                method == Method.POST && uri == "/reply"  -> handleReply(session)
                method == Method.GET  && uri == "/"       -> handleRoot()
                else -> jsonError(404, "Not found")
            }'''

# Add OPTIONS handler function before handleRoot
old_root = '''    // ─── GET / ────────────────────────────────────────────────────────────────
    private fun handleRoot(): Response {'''

new_root = '''    // ─── OPTIONS (CORS Preflight) ─────────────────────────────────────────────
    private fun handleOptions(): Response {
        val response = newFixedLengthResponse(Response.Status.OK, JSON_MIME, "{}")
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type")
        response.addHeader("Access-Control-Max-Age", "3600")
        return response
    }

    // ─── GET / ────────────────────────────────────────────────────────────────
    private fun handleRoot(): Response {'''

content = content.replace(old_ok, new_ok)
content = content.replace(old_error, new_error)
content = content.replace(old_router, new_router)
content = content.replace(old_root, new_root)

# Fix unauthorized response too
content = content.replace(
    '''    private fun unauthorizedResponse(): Response =
        newFixedLengthResponse(
            Response.Status.UNAUTHORIZED,
            JSON_MIME,
            """{"error":"Unauthorized — invalid or missing Bearer token"}"""
        )''',
    '''    private fun unauthorizedResponse(): Response {
        val response = newFixedLengthResponse(
            Response.Status.UNAUTHORIZED,
            JSON_MIME,
            """{"error":"Unauthorized — invalid or missing Bearer token"}"""
        )
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type")
        return response
    }'''
)

with open('app/src/main/java/com/fuelone/smsgateway/server/ApiServer.kt', 'w') as f:
    f.write(content)
print('Done!')
