package de.unger.net

import android.net.http.HttpException
import android.net.http.UrlRequest
import android.net.http.UrlResponseInfo
import android.util.Log
import java.nio.ByteBuffer
import java.nio.charset.Charset

class NoGramCallback(
    private val onSuccess: (String) -> Unit,
    private val onError: (String) -> Unit
) :
    UrlRequest.Callback {
    private val resultStream = java.io.ByteArrayOutputStream(1024 * 64)
    private var responseCharset: Charset = Charsets.UTF_8
    override fun onCanceled(
        request: UrlRequest,
        info: UrlResponseInfo?
    ) {
        Log.d("NoGramCallback", "cancelled: $request, $info")
        onError("Request cancelled")
    }

    override fun onFailed(
        request: UrlRequest,
        info: UrlResponseInfo?,
        error: HttpException
    ) {
        Log.e("NoGramCallback", "failed: $request, $info", error)
        onError(error.message ?: "Request failed")
    }

    override fun onReadCompleted(
        request: UrlRequest,
        info: UrlResponseInfo,
        byteBuffer: ByteBuffer
    ) {
        byteBuffer.flip()

        if (byteBuffer.hasRemaining()) {
            val bytes = ByteArray(byteBuffer.remaining())
            byteBuffer.get(bytes)
            resultStream.write(bytes)
        }

        byteBuffer.clear()
        request.read(byteBuffer)
    }

    override fun onRedirectReceived(
        request: UrlRequest,
        info: UrlResponseInfo,
        newLocationUrl: String
    ) {
        request.followRedirect()
    }

    override fun onResponseStarted(
        request: UrlRequest,
        info: UrlResponseInfo
    ) {
        resultStream.reset()
        responseCharset = extractCharset(info) ?: Charsets.UTF_8
        request.read(ByteBuffer.allocateDirect(64 * 1024))
    }

    override fun onSucceeded(
        request: UrlRequest,
        info: UrlResponseInfo
    ) {
        val htmlContent = resultStream.toString(responseCharset.name())
        onSuccess(htmlContent)
    }

    private fun extractCharset(info: UrlResponseInfo): Charset? {
        val contentType = info.headers.asMap["Content-Type"]?.firstOrNull() ?: return null
        val charsetName = contentType
            .substringAfter("charset=", "")
            .trim()
            .ifEmpty { return null }

        return runCatching { Charset.forName(charsetName) }.getOrNull()
    }

}