package com.goodmorning.subtitle.filter

import com.goodmorning.subtitle.model.SubtitleSegment
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("GreetingFilter 테스트")
class GreetingFilterTest {

    private val filter = GreetingFilter()

    @Nested
    @DisplayName("인사 시작 패턴")
    inner class GreetingStart {

        @Test
        @DisplayName("안녕하세요로 시작하는 패턴을 필터링한다")
        fun filterHello() {
            val segment = SubtitleSegment(0.0, 1.0, "안녕하세요 여러분")
            assertTrue(filter.shouldFilter(segment))
        }

        @Test
        @DisplayName("여러분 안녕하세요 패턴을 필터링한다")
        fun filterHelloEveryone() {
            val segment = SubtitleSegment(0.0, 1.0, "여러분 안녕하세요")
            assertTrue(filter.shouldFilter(segment))
        }
    }

    @Nested
    @DisplayName("감사 인사 패턴")
    inner class ThankYouPatterns {

        @Test
        @DisplayName("감사합니다로 끝나는 패턴을 필터링한다")
        fun filterThankYou() {
            val segment = SubtitleSegment(0.0, 1.0, "시청해주셔서 감사합니다")
            assertTrue(filter.shouldFilter(segment))
        }

        @Test
        @DisplayName("고맙습니다로 끝나는 패턴을 필터링한다")
        fun filterThankYouPolite() {
            val segment = SubtitleSegment(0.0, 1.0, "봐주셔서 고맙습니다")
            assertTrue(filter.shouldFilter(segment))
        }

        @Test
        @DisplayName("시청 감사 패턴을 필터링한다")
        fun filterWatchingThankYou() {
            val segment = SubtitleSegment(0.0, 1.0, "시청 감사 드립니다")
            assertTrue(filter.shouldFilter(segment))
        }
    }

    @Nested
    @DisplayName("일반 내용")
    inner class NormalContent {

        @Test
        @DisplayName("중간에 안녕하세요가 있는 경우 필터링하지 않는다")
        fun doNotFilterMiddleHello() {
            val segment = SubtitleSegment(0.0, 1.0, "아이가 안녕하세요 라고 인사했어요")
            assertFalse(filter.shouldFilter(segment))
        }

        @Test
        @DisplayName("일반 내용은 필터링하지 않는다")
        fun doNotFilterNormalContent() {
            val segment = SubtitleSegment(0.0, 1.0, "오늘은 열에 대해 알아보겠습니다")
            assertFalse(filter.shouldFilter(segment))
        }
    }
}
