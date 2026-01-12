package com.goodmorning.subtitle.filter

import com.goodmorning.subtitle.model.SubtitleSegment
import org.springframework.stereotype.Component

/**
 * 채널 홍보 문구 필터
 */
@Component
class ChannelPromotionFilter : SubtitleFilter {
    private val patterns = listOf(
        "채널.*방문.*해주세요",
        "채널.*소개",
        "다른.*영상.*보시려면",
        "링크.*설명란",
        "설명란.*확인.*해주세요",
        "커뮤니티.*공지",
        "더.*많은.*영상",
        "영상.*많이.*올리",
        "제.*채널에"
    ).map { it.toRegex(RegexOption.IGNORE_CASE) }

    override fun shouldFilter(segment: SubtitleSegment): Boolean {
        return patterns.any { it.containsMatchIn(segment.text) }
    }
}
