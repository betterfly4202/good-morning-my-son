package com.goodmorning.subtitle.model

import kotlinx.serialization.Serializable

@Serializable
data class SubtitleSegment(
    val start: Double,
    val duration: Double,
    val text: String
)

@Serializable
data class VideoSubtitle(
    val videoId: String,
    val title: String,
    val channelName: String,
    val collectedAt: String,
    val segments: List<SubtitleSegment>,
    val fullText: String
)
