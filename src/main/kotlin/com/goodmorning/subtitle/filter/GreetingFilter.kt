package com.goodmorning.subtitle.filter

import com.goodmorning.subtitle.model.SubtitleSegment
import org.springframework.stereotype.Component

/**
 * 인사말 패턴 필터
 */
@Component
class GreetingFilter : SubtitleFilter {
    private val patterns = listOf(
        "^안녕하세요",
        "^여러분.*안녕하세요",
        "감사합니다$",
        "고맙습니다$",
        "시청.*감사.*드립니다",
        "시청.*해주셔서.*감사",
        "봐주셔서.*감사",
        "찾아.*주셔서.*감사"
    ).map { it.toRegex(RegexOption.IGNORE_CASE) }

    override fun shouldFilter(segment: SubtitleSegment): Boolean {
        val trimmedText = segment.text.trim()
        return patterns.any { it.containsMatchIn(trimmedText) }
    }
}
