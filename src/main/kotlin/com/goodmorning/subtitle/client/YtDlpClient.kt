package com.goodmorning.subtitle.client

import java.io.File

interface YtDlpClient {
    fun downloadSubtitle(videoId: String, outputDir: File): YtDlpResult
    fun getVideoTitle(videoId: String): String?
}
