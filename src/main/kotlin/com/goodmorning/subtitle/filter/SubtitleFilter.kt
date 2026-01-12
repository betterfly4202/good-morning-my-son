package com.goodmorning.subtitle.filter

import com.goodmorning.subtitle.model.SubtitleSegment

/**
 * 자막 필터링 인터페이스
 */
interface SubtitleFilter {
    /**
     * 자막 세그먼트가 필터링 대상인지 판단
     * @return true면 필터링 대상 (제거), false면 유지
     */
    fun shouldFilter(segment: SubtitleSegment): Boolean
}
