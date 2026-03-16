package de.unger.net

import android.net.http.HttpException
import android.net.http.UrlRequest
import android.net.http.UrlResponseInfo
import java.nio.ByteBuffer

class NoGramCallback(private val onSuccess: (HashMap<UrlResponseInfo, ByteArray>) -> Unit) :
    UrlRequest.Callback {
    val currentBuffers = HashMap<UrlResponseInfo, ByteArray>()
    override fun onCanceled(
        p0: UrlRequest,
        p1: UrlResponseInfo?
    ) {
        print("cancelled: $p0, $p1")
    }

    override fun onFailed(
        p0: UrlRequest,
        p1: UrlResponseInfo?,
        p2: HttpException
    ) {
        print("failed: $p0, $p1, $p2")
    }

    override fun onReadCompleted(
        request: UrlRequest,
        info: UrlResponseInfo,
        byteBuffer: ByteBuffer
    ) {
        // 1. Flip the buffer to prepare it for reading
        byteBuffer.flip()

        // 2. Extract bytes (e.g., into a ByteArray)
        val bytes = ByteArray(byteBuffer.remaining())
        byteBuffer.get(bytes)
        info.headers.asList.forEach {
            println("${it.component1()}: ${it.component2()}")
        }
        // Example: Append these bytes to a global output stream or list
        // myAccumulatedData.write(bytes)
        currentBuffers[info] = bytes
    }

    override fun onRedirectReceived(
        p0: UrlRequest,
        p1: UrlResponseInfo,
        p2: String
    ) {
        p0.followRedirect()
    }

    override fun onResponseStarted(
        p0: UrlRequest,
        p1: UrlResponseInfo
    ) {
        p0.read(ByteBuffer.allocateDirect(1024 * 1024 * 16))
    }

    override fun onSucceeded(
        p0: UrlRequest,
        p1: UrlResponseInfo
    ) {
        print("success:$p0, $p1")
        onSuccess(currentBuffers)
        currentBuffers.clear()
    }

}