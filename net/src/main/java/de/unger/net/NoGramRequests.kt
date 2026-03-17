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
            .setHttpMethod("GET").addHeader(
                "User-Agent",
                "Mozilla/5.0 (Android 14; Mobile; rv:128.0) Gecko/128.0 Firefox/128.0"
            )
            .build().start()
}