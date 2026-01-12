package com.goodmorning.subtitle

import com.goodmorning.subtitle.client.YtDlpClient
import com.goodmorning.subtitle.filter.CompositeSubtitleFilter
import com.goodmorning.subtitle.model.VideoSubtitle
import com.goodmorning.subtitle.parser.SubtitleParser
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import kotlin.io.path.createTempDirectory

@Component
class SubtitleFetchPipeline(
    private val ytDlpClient: YtDlpClient,
    private val fileFinder: SubtitleFileFinder,
    private val parsers: List<SubtitleParser>,
    private val subtitleFilter: CompositeSubtitleFilter,
    @Value("\${subtitle.channel-name:하정훈의 삐뽀삐뽀 119 소아과}")
    private val channelName: String
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun fetch(videoId: String): VideoSubtitle? {
        logger.info("Fetching subtitle for videoId: {}", videoId)
        val tempDir = createTempDirectory("yt-subtitle-").toFile()

        try {
            val downloadResult = ytDlpClient.downloadSubtitle(videoId, tempDir)
            if (!downloadResult.success) {
                logger.warn("Download failed for videoId: {}", videoId)
                return null
            }

            val subtitleFile = fileFinder.find(tempDir, videoId)
            if (subtitleFile == null) {
                logger.warn("No subtitle file found for videoId: {}", videoId)
                return null
            }

            val parser = parsers.find { it.canParse(subtitleFile) }
            if (parser == null) {
                logger.warn("No parser found for file: {}", subtitleFile.name)
                return null
            }

            val segments = parser.parse(subtitleFile)
            if (segments.isEmpty()) {
                logger.warn("No segments parsed from file: {}", subtitleFile.name)
                return null
            }

            val filteredSegments = subtitleFilter.filterSegments(segments)
            logger.info("Filtered {} segments from {} total for videoId: {}",
                segments.size - filteredSegments.size, segments.size, videoId)

            if (filteredSegments.isEmpty()) {
                logger.warn("No segments remaining after filtering for videoId: {}", videoId)
                return null
            }

            val title = ytDlpClient.getVideoTitle(videoId) ?: "Unknown Title"

            logger.info("Successfully fetched {} segments for videoId: {}", filteredSegments.size, videoId)

            return VideoSubtitle(
                videoId = videoId,
                title = title,
                channelName = channelName,
                collectedAt = Instant.now().toString(),
                segments = filteredSegments,
                fullText = filteredSegments.joinToString(" ") { it.text }
            )
        } finally {
            tempDir.deleteRecursively()
        }
    }
}
