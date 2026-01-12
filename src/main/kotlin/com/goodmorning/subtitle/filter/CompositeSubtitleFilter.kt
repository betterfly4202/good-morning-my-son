package com.goodmorning.subtitle.filter

import com.goodmorning.subtitle.model.SubtitleSegment
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * 여러 필터를 조합하는 복합 필터
 */
@Component
class CompositeSubtitleFilter(
    private val filters: List<SubtitleFilter>
) : SubtitleFilter {

    private val logger = LoggerFactory.getLogger(CompositeSubtitleFilter::class.java)

    override fun shouldFilter(segment: SubtitleSegment): Boolean {
        val matchedFilter = filters.firstOrNull { it.shouldFilter(segment) }
        if (matchedFilter != null) {
            logger.debug("Segment filtered by {}: '{}'", matchedFilter::class.simpleName, segment.text.take(50))
            return true
        }
        return false
    }

    /**
     * 필터링되지 않은 세그먼트만 반환
     */
    fun filterSegments(segments: List<SubtitleSegment>): List<SubtitleSegment> {
        return segments.filterNot { shouldFilter(it) }
    }
}
