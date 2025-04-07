package com.golvia.smartflowtechandroidassessment.data

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class NetworkInterceptor @Inject constructor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // 1. Check if the device is offline.
        if (!isInternetConnected()) {
            // Return a 503 Service Unavailable.
            return createErrorResponse(
                chain,
                statusCode = 503,
                message = "No Internet Connection"
            )
        }

        return try {
            // 2. Attempt the network request.
            chain.proceed(chain.request())
        } catch (e: IOException) {
            // 3. Distinguish between different IOException subtypes.
            val (code, message) = when (e) {
                // For timeouts, HTTP 408 (Request Timeout) is often used.
                is SocketTimeoutException -> 408 to "Request timed out: ${e.localizedMessage}"

                // For unknown host (DNS), you can reuse 503 or use something else.
                is UnknownHostException -> 503 to "Unknown host error: ${e.localizedMessage}"

                // For SSL-related errors, you can handle them separately if desired.
                // is SSLException -> 495 to "SSL Error: ${e.localizedMessage}"  // 4xx or 5xx, your choice.

                // Default/fallback for other IO issues:
                else -> 500 to "Server Error: ${e.localizedMessage}"
            }

            // 4. Create and return a custom error response.
            return createErrorResponse(chain, code, message)
        }
    }

    @SuppressLint("MissingPermission")
    private fun isInternetConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return false

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun createErrorResponse(
        chain: Interceptor.Chain,
        statusCode: Int,
        message: String
    ): Response {
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val errorBody = """{"error":"$message"}""".toResponseBody(mediaType)

        return Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(statusCode)
            .message(message)
            .body(errorBody)
            .build()
    }
}
