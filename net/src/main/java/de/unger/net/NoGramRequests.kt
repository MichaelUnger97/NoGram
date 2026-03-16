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
            .build().start()
}