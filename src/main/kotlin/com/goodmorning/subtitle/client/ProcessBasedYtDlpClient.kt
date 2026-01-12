package com.goodmorning.subtitle.client

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File
import java.util.concurrent.TimeUnit

@Component
class ProcessBasedYtDlpClient : YtDlpClient {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun downloadSubtitle(videoId: String, outputDir: File): YtDlpResult {
        val url = "https://www.youtube.com/watch?v=$videoId"

        val command = listOf(
            "yt-dlp",
            "--skip-download",
            "--write-auto-sub",
            "--write-sub",
            "--sub-lang", "ko",
            "--sub-format", "json3",
            "--output", "${outputDir.absolutePath}/$videoId.%(ext)s",
            url
        )

        logger.debug("Running yt-dlp command for videoId: {}", videoId)

        val process = ProcessBuilder(command)
            .redirectErrorStream(true)
            .start()

        val output = process.inputStream.bufferedReader().readText()
        val exitCode = process.waitFor()

        logger.debug("yt-dlp exit code: {}", exitCode)

        return if (exitCode == 0) {
            YtDlpResult(success = true, outputDir = outputDir)
        } else {
            logger.warn("yt-dlp failed: {}", output)
            YtDlpResult(success = false, outputDir = outputDir, message = output)
        }
    }

    override fun getVideoTitle(videoId: String): String? {
        val command = listOf(
            "yt-dlp",
            "--get-title",
            "--no-warnings",
            "https://www.youtube.com/watch?v=$videoId"
        )

        logger.debug("Fetching title for videoId: {}", videoId)

        val process = ProcessBuilder(command).start()
        val output = process.inputStream.bufferedReader().readText().trim()
        process.waitFor(30, TimeUnit.SECONDS)

        return output.lines()
            .lastOrNull { it.isNotBlank() && !it.startsWith("WARNING") && !it.startsWith("ERROR") }
    }
}
