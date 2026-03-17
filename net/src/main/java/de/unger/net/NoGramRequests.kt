package de.unger.net


class NoGramRequests(
    private val engine: NoGramHttpEngine,
    private val nogramCallback: NoGramCallback
) {
    fun home() =
        engine.engine.newUrlRequestBuilder(
            "https://www.instagram.com/",
            engine.httpExecutor,
            nogramCallback
        )
            .setHttpMethod("GET")
            .addHeader(
                "ACCEPT",
                "text/html, application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"
            ).addHeader(
                "ACCEPT-ENCODING", "gzip, deflate, br, zstd"
            )
            .addHeader(
                "ACCEPT-LANGUAGE", "de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7"
            )
            .addHeader("DEVICE-MEMORY", "2")
            .addHeader("DOWNLINK", "1")
            .addHeader("DPR", "2.625")
            .addHeader("ECT", "3g")
            .addHeader("HOST", "www.instagram.com")
            .addHeader("PRIORITY", "u=0, i")
            .addHeader("REFERER", "https://www.instagram.com/")
            .addHeader("RTT", "350")
            .addHeader("SEC-CH-PREFERS-COLOR-SCHEME", "light")
            .addHeader("SEC-CH-PREFERS-REDUCED-MOTION", "reduce")
            .addHeader(
                "SEC-CH-UA",
                "\"Not(A:Brand\";v=\"99\", \"Google Chrome\";v=\"133\", \"Chromium\";v=\"133\""
            )
            //.addHeader("SEC-CH-UA-FULL-VERSION,", "\"133.0.6943.137\"")
            .addHeader("SEC-CH-UA-MOBILE", "?1")
            .addHeader("SEC-CH-UA-MODEL", "\"sdk_gphone64_x86_64\"")
            .addHeader("SEC-CH-UA-PLATFORM", "\"Android\"")
            .addHeader("SEC-CH-UA-PLATFORM-VERSION", "\"16.0.0\"")
            .addHeader("SEC-FETCH-DEST", "document")
            .addHeader("SEC-FETCH-MODE", "navigate")
            .addHeader("SEC-FETCH-SITE", "same-origin")
            .addHeader("SEC-FETCH-USER", "?1")
            .addHeader("UPGRADE-INSECURE-REQUESTS", "1")
            .addHeader(
                "USER-AGENT",
                "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Mobile Safari/537.36"
            )
            .addHeader(
                "VIEWPORT-WIDTH", "980"
            )
            .build().start()
}