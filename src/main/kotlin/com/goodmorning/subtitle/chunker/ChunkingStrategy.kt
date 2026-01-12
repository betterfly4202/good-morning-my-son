package com.goodmorning.subtitle.chunker

import com.goodmorning.subtitle.model.VideoSubtitle

/**
 * 자막 청크 분할 전략 인터페이스
 */
interface ChunkingStrategy {
    /**
     * VideoSubtitle을 청크 리스트로 분할
     */
    fun chunk(videoSubtitle: VideoSubtitle): List<SubtitleChunk>
}
