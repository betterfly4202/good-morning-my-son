package com.goodmorning.subtitle.chunker

import com.goodmorning.subtitle.model.VideoSubtitle
import org.slf4j.LoggerFactory

/**
 * 시간 기반 청크 분할 전략
 * 지정된 시간 간격으로 세그먼트를 묶어 청크 생성
 */
class TimeBasedChunkingStrategy(
    private val intervalSeconds: Double = 30.0
) : ChunkingStrategy {

    private val logger = LoggerFactory.getLogger(javaClass)

    private data class ChunkAccumulator(
        val completedChunks: List<SubtitleChunk> = emptyList(),
        val currentSegments: List<String> = emptyList(),
        val chunkStartTime: Double = 0.0,
        val chunkEndTime: Double = 0.0,
        val segmentCount: Int = 0
    )

    override fun chunk(videoSubtitle: VideoSubtitle): List<SubtitleChunk> {
        if (videoSubtitle.segments.isEmpty()) {
            logger.debug("No segments to chunk")
            return emptyList()
        }

        logger.debug("Starting time-based chunking with interval: {}s, segments: {}", intervalSeconds, videoSubtitle.segments.size)

        val firstSegment = videoSubtitle.segments.first()
        val initialAcc = ChunkAccumulator(chunkStartTime = firstSegment.start, chunkEndTime = firstSegment.start)

        val finalAcc = videoSubtitle.segments.fold(initialAcc) { acc, segment ->
            val segmentEndTime = segment.start + segment.duration

            if (segment.start >= acc.chunkStartTime + intervalSeconds && acc.currentSegments.isNotEmpty()) {
                val completedChunk = SubtitleChunk(
                    text = acc.currentSegments.joinToString(" "),
                    startTime = acc.chunkStartTime,
                    endTime = acc.chunkEndTime,
                    segmentCount = acc.segmentCount
                )
                ChunkAccumulator(
                    completedChunks = acc.completedChunks + completedChunk,
                    currentSegments = listOf(segment.text),
                    chunkStartTime = segment.start,
                    chunkEndTime = segmentEndTime,
                    segmentCount = 1
                )
            } else {
                acc.copy(
                    currentSegments = acc.currentSegments + segment.text,
                    chunkEndTime = segmentEndTime,
                    segmentCount = acc.segmentCount + 1
                )
            }
        }

        val result = if (finalAcc.currentSegments.isNotEmpty()) {
            finalAcc.completedChunks + SubtitleChunk(
                text = finalAcc.currentSegments.joinToString(" "),
                startTime = finalAcc.chunkStartTime,
                endTime = finalAcc.chunkEndTime,
                segmentCount = finalAcc.segmentCount
            )
        } else {
            finalAcc.completedChunks
        }

        logger.debug("Completed chunking: {} chunks created", result.size)
        return result
    }
}
