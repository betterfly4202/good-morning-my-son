package com.goodmorning.subtitle.chunker

import com.goodmorning.subtitle.model.SubtitleSegment
import com.goodmorning.subtitle.model.VideoSubtitle
import org.slf4j.LoggerFactory

/**
 * 크기 기반 청크 분할 전략
 *
 * @param targetSize 목표 청크 크기 (글자 수, 양수 필수)
 * @param overlapSegments 오버랩할 세그먼트 개수 (0 이상)
 *
 * 동작 규칙:
 * - 단일 세그먼트가 targetSize를 초과해도 분할하지 않음 (세그먼트 무결성 유지)
 * - overlapSegments가 청크 내 세그먼트 수보다 크면, 전체 세그먼트를 오버랩으로 유지
 * - 오버랩은 세그먼트 단위로 유지하여 시간/구조 정보 보존
 */
class SizeBasedChunkingStrategy(
    private val targetSize: Int = 500,
    private val overlapSegments: Int = 0
) : ChunkingStrategy {

    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        require(targetSize > 0) { "targetSize must be positive, got: $targetSize" }
        require(overlapSegments >= 0) { "overlapSegments must be non-negative, got: $overlapSegments" }
    }

    override fun chunk(videoSubtitle: VideoSubtitle): List<SubtitleChunk> {
        if (videoSubtitle.segments.isEmpty()) {
            logger.debug("No segments to chunk")
            return emptyList()
        }

        logger.debug(
            "Starting size-based chunking with targetSize: {}, overlapSegments: {}, segments: {}",
            targetSize,
            overlapSegments,
            videoSubtitle.segments.size
        )

        val chunks = mutableListOf<SubtitleChunk>()
        var currentSegments = mutableListOf<SubtitleSegment>()
        var currentLength = 0

        for (segment in videoSubtitle.segments) {
            val segmentTextLength = segment.text.length
            val spaceLength = if (currentSegments.isNotEmpty()) 1 else 0

            // 목표 크기 초과 시 청크 생성
            if (currentLength + spaceLength + segmentTextLength > targetSize && currentSegments.isNotEmpty()) {
                chunks.add(createChunk(currentSegments))

                // 오버랩 처리: 마지막 N개 세그먼트를 다음 청크로 유지
                currentSegments = if (overlapSegments > 0) {
                    val overlapStart = (currentSegments.size - overlapSegments).coerceAtLeast(0)
                    currentSegments.subList(overlapStart, currentSegments.size).toMutableList()
                } else {
                    mutableListOf()
                }
                currentLength = calculateLength(currentSegments)
            }

            currentSegments.add(segment)
            currentLength = calculateLength(currentSegments)
        }

        // 마지막 청크 추가
        if (currentSegments.isNotEmpty()) {
            chunks.add(createChunk(currentSegments))
        }

        logger.debug("Completed chunking: {} chunks created", chunks.size)
        return chunks
    }

    private fun createChunk(segments: List<SubtitleSegment>): SubtitleChunk {
        return SubtitleChunk(
            text = segments.joinToString(" ") { it.text },
            startTime = segments.first().start,
            endTime = segments.last().start + segments.last().duration,
            segmentCount = segments.size
        )
    }

    private fun calculateLength(segments: List<SubtitleSegment>): Int {
        if (segments.isEmpty()) return 0
        return segments.sumOf { it.text.length } + (segments.size - 1) // 텍스트 + 공백
    }
}
