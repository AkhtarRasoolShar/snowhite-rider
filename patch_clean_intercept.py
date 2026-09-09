import re

with open("app/src/main/java/com/example/data/remote/RetrofitClient.kt", "r") as f:
    content = f.read()

# Let's just fix the whole MockOrderInterceptor method
# First, I'll extract everything between 'private class MockOrderInterceptor : Interceptor {' and 'private val gson = GsonBuilder()'

start_idx = content.find("private class MockOrderInterceptor : Interceptor {")
end_idx = content.find("private val gson = GsonBuilder()")

if start_idx != -1 and end_idx != -1:
    interceptor_code = content[start_idx:end_idx]
    
    # Remove the `realResponse?.close()` from the top
    interceptor_code = interceptor_code.replace("            // If we are going to use a mock, we must close the realResponse\n            realResponse?.close()\n", "")
    
    # In each `return Response.Builder()`, we need to prepend `realResponse?.close()`
    interceptor_code = interceptor_code.replace("                return Response.Builder()", "                realResponse?.close()\n                return Response.Builder()")
    
    # Fix the bottom
    old_bottom = """            // If we get here, it means no mock matched.
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
    }"""
    
    new_bottom = """            // If we get here, it means no mock matched.
            if (realResponse != null) {
                return realResponse
            }
            
            return Response.Builder()
                .code(503)
                .message("Service Unavailable")
                .protocol(Protocol.HTTP_1_1)
                .request(request)
                .body("{}".toResponseBody("application/json".toMediaType()))
                .build()
        }
    }"""
    interceptor_code = interceptor_code.replace(old_bottom, new_bottom)
    
    new_content = content[:start_idx] + interceptor_code + content[end_idx:]
    with open("app/src/main/java/com/example/data/remote/RetrofitClient.kt", "w") as f:
        f.write(new_content)

