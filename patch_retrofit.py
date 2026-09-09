import re

with open("app/src/main/java/com/example/data/remote/RetrofitClient.kt", "r") as f:
    content = f.read()

old_top = """    private class MockOrderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val url = request.url.toString()

            try {
                // Try real network call first
                val response = chain.proceed(request)
                if (response.isSuccessful) {
                    return response
                }
            } catch (e: Throwable) {
                // Fall back to simulated server response if network fails
            }"""

new_top = """    private class MockOrderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val url = request.url.toString()

            var realResponse: Response? = null
            try {
                // Try real network call first
                realResponse = chain.proceed(request)
                if (realResponse.isSuccessful) {
                    return realResponse
                }
            } catch (e: Throwable) {
                // Fall back to simulated server response if network fails
            }
            
            // If we are going to use a mock, we must close the realResponse
            realResponse?.close()"""

content = content.replace(old_top, new_top)

# Replace the final return statement
content = re.sub(r"return chain\.proceed\(request\)\s*}\s*}", """            // If we get here, it means no mock matched.
            if (realResponse != null) {
                // We closed it above, so we have to recreate an error response or just proceed again (since we closed it, proceeding again is legal in OkHttp, but wasteful).
                // Actually, proceeding again is fine, but it's better to just return the realResponse. Wait, we closed it!
                // Since we closed it, we MUST proceed again or return a new constructed response.
                return chain.proceed(request)
            }
            
            return Response.Builder()
                .code(503)
                .message("Service Unavailable")
                .protocol(Protocol.HTTP_1_1)
                .request(request)
                .body("{}".toResponseBody("application/json".toMediaType()))
                .build()
        }
    }""", content)

with open("app/src/main/java/com/example/data/remote/RetrofitClient.kt", "w") as f:
    f.write(content)
