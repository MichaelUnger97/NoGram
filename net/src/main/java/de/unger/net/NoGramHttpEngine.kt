package de.unger.net

import android.content.Context
import android.net.http.HttpEngine
import android.net.http.HttpEngine.Builder.HTTP_CACHE_DISK
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class NoGramHttpEngine(context: Context) {
    val httpExecutor: Executor = Executors.newSingleThreadExecutor()
    private val cacheDir = File(context.cacheDir, "http_cache").apply {
        if (!exists()) {
            mkdirs()
        }
    }

    val engine = HttpEngine.Builder(context)
        .setStoragePath(cacheDir.absolutePath)
        .setEnableHttpCache(HTTP_CACHE_DISK, 1024 * 1024 * 512) // 512 MB
        .build()
}
