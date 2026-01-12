package com.goodmorning.subtitle.chunker

/**
 * RAG 검색을 위한 자막 청크 단위
 */
data class SubtitleChunk(
    val text: String,
    val startTime: Double,
    val endTime: Double,
    val segmentCount: Int
)
