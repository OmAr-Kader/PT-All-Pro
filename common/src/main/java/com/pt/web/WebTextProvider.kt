package com.pt.web

import com.pt.common.BuildConfig.*

open class WebTextProvider(
    private val ctx: android.content.Context,
    private val webListener: WebListener
) {

    suspend fun webTitleProvider(link: String, time: Long, table: String) {
        var titleWeb: String? = null
        var image: String? = null
        var titleWebTwo: String? = null
        var imageTwo: String? = null
        com.pt.common.stable.withBackDef(Unit) {
            ctx.runCatching {
                org.jsoup.Jsoup.connect(link).run {
                    userAgent(MOZ_ILLA).get()
                }.apply {
                    select(META_PRO).forEach {
                        if (it?.attr(PROP_PER) == OG_IMG) {
                            image = it.attr(CONT_ENT)
                        } else if (it?.attr(PROP_PER) == OG_TITLE) {
                            titleWeb = it.attr(CONT_ENT)
                        }
                    }
                }
            }
        }
        com.pt.common.stable.withBackDef(Unit) {
            kotlin.runCatching {
                org.jsoup.Jsoup.connect(link).get().apply {
                    titleWebTwo = select("meta").attr("description").let {
                        if (it.isEmpty() || it.isBlank()) null else it
                    }
                    select("link").also {
                        for (metaElem in it) {
                            val name: String = metaElem.attr("rel")
                            if (name.contains("icon")) {
                                metaElem.attr("href").let { str ->
                                    if (!str.contains(".svg")) imageTwo = str
                                }
                                return@withBackDef
                            }
                        }
                    }
                }
            }
        }
        com.pt.common.stable.withMain {
            if (titleWeb != null || titleWebTwo != null) {
                val fImage = image?.validLink ?: imageTwo?.validLink
                val fTitle = if ((titleWeb?.length ?: 0) > 200) {
                    titleWeb?.toDefString?.invoke(200)
                } else {
                    titleWeb
                }.let {
                    if ((titleWeb == null && titleWebTwo != null) ||
                        (titleWeb != null &&
                                titleWeb!!.contains("https:") &&
                                titleWebTwo != null)
                    ) {
                        if ((titleWebTwo?.length ?: 0) > 200) {
                            titleWebTwo?.toDefString?.invoke(200)
                        } else {
                            titleWebTwo
                        }
                    } else {
                        titleWeb
                    }.let {
                        if (fImage != null) it ?: "" else it
                    }
                }
                webListener.run {
                    ctx.onLoadFinish(
                        table = table,
                        time = time,
                        title = fTitle,
                        img = fImage
                    )
                }
            } else {
                webViewProvider(link, time, table)
            }
        }

    }

    private inline val String.toDefString: (Int) -> String
        get() = {
            kotlin.runCatching {
                substring(0, kotlin.math.min(it, length))
            }.getOrDefault(this)
        }

    private inline val String.validLink: String?
        get() {
            return android.util.Patterns.WEB_URL.matcher(this@validLink).matches().let {
                if (it) this@validLink else null
            }
        }


    internal fun jsonTwoProvider(link: String, a: (String?, String) -> Unit) {
        kotlin.runCatching {
            org.jsoup.Jsoup.connect(link).get().apply {
                select("meta").attr("description").let { des ->
                    select("link").also {
                        for (metaElem in it) {
                            val name: String = metaElem.attr("rel")
                            if (name.contains(".png") || name.contains("icon")) {
                                metaElem.attr("href").let { str ->
                                    a.invoke(str, des)
                                    return
                                }
                            }
                        }
                    }
                }
            }
        }.getOrNull()
    }

    private suspend fun webViewProvider(link: String, time: Long, table: String) {
        kotlinx.coroutines.coroutineScope {
            ctx.runCatching {
                android.webkit.WebView(ctx).run {
                    android.webkit.WebView.setWebContentsDebuggingEnabled(false)
                    loadUrl(link)
                    settings.cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE
                    webChromeClient = object : android.webkit.WebChromeClient() {
                        override fun onReceivedTitle(
                            view: android.webkit.WebView?,
                            title: String?
                        ) {
                            super.onReceivedTitle(view, title)
                            if (title != WEB_NOT) {
                                jsonTwoProvider(link) { it, _ ->
                                    val img = if (it?.contains(".svg") == false) it else null
                                    webListener.run {
                                        ctx.onLoadFinish(
                                            table = table,
                                            time = time,
                                            title = title,
                                            img = img
                                        )
                                    }
                                }
                                this@run.clearFormData()
                                this@run.clearCache(true)
                                @Suppress("LongLine")
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    this@run.webChromeClient = null
                                }
                            }
                        }
                    }
                    webViewClient = object : android.webkit.WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: android.webkit.WebView?,
                            request: android.webkit.WebResourceRequest?
                        ): Boolean {
                            return false
                        }
                    }
                }
            }
        }
    }

    @FunctionalInterface
    fun interface WebListener {
        fun android.content.Context?.onLoadFinish(
            table: String,
            time: Long,
            title: String?,
            img: String?
        )
    }
}