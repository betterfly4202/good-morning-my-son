package com.goodmorning.subtitle.filter

import com.goodmorning.subtitle.model.SubtitleSegment
import org.springframework.stereotype.Component

/**
 * 구독/좋아요/알림 요청 패턴 필터
 */
@Component
class SubscriptionRequestFilter : SubtitleFilter {
    private val patterns = listOf(
        "구독.*눌러.*주세요",
        "구독.*부탁.*드립니다",
        "좋아요.*눌러.*주세요",
        "좋아요.*부탁.*드립니다",
        "알림.*설정.*해주세요",
        "알림.*설정.*부탁",
        "구독.*좋아요",
        "좋아요.*구독",
        "구독.*알림",
        "알림.*구독"
    ).map { it.toRegex(RegexOption.IGNORE_CASE) }

    override fun shouldFilter(segment: SubtitleSegment): Boolean {
        return patterns.any { it.containsMatchIn(segment.text) }
    }
}
