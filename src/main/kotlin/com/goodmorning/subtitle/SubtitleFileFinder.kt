package com.goodmorning.subtitle

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File

@Component
class SubtitleFileFinder {

    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        val SUPPORTED_EXTENSIONS = listOf("json3", "vtt", "srt")
    }

    fun find(directory: File, videoId: String): File? {
        logger.debug("Searching for subtitle file in: {}", directory.absolutePath)

        val found = directory.listFiles()
            ?.filter { it.name.contains(videoId) && it.extension in SUPPORTED_EXTENSIONS }
            ?.sortedBy { SUPPORTED_EXTENSIONS.indexOf(it.extension) }
            ?.firstOrNull()

        if (found != null) {
            logger.debug("Found subtitle file: {}", found.name)
        } else {
            logger.debug("No subtitle file found for videoId: {}", videoId)
        }

        return found
    }
}
