package com.goodmorning.controller

import com.goodmorning.subtitle.YouTubeSubtitleFetcher
import com.goodmorning.subtitle.model.VideoSubtitle
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/subtitles")
class SubtitleController(
    private val fetcher: YouTubeSubtitleFetcher
) {
    private val logger = LoggerFactory.getLogger(SubtitleController::class.java)

    @GetMapping("/{videoId}")
    fun getSubtitle(@PathVariable videoId: String): ResponseEntity<VideoSubtitle> {
        logger.info("Fetching subtitle for videoId: $videoId")

        val subtitle = fetcher.fetch(videoId)

        return if (subtitle != null) {
            logger.info("Subtitle found for videoId: $videoId")
            ResponseEntity.ok(subtitle)
        } else {
            logger.warn("Subtitle not found for videoId: $videoId")
            ResponseEntity.notFound().build()
        }
    }
}
