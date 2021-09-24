package com.challenge.app.utils

object Utils {
    fun correctWebUrl(url: String) = if (
        !url.startsWith("http://")
        && !url.startsWith("https://")
    ) "http://$url"
    else url
}
