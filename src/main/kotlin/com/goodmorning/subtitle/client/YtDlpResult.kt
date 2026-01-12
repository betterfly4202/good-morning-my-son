package com.goodmorning.subtitle.client

import java.io.File

data class YtDlpResult(
    val success: Boolean,
    val outputDir: File,
    val message: String? = null
)
