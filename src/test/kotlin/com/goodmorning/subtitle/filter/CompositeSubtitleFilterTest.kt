package com.goodmorning.subtitle.filter

import com.goodmorning.subtitle.model.SubtitleSegment
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("CompositeSubtitleFilter 테스트")
class CompositeSubtitleFilterTest {

    @Nested
    @DisplayName("필터 조합 동작")
    inner class CompositeFiltering {

        @Test
        @DisplayName("여러 필터 중 하나라도 매칭되면 필터링한다")
        fun shouldFilterWhenAnyMatches() {
            val filters = listOf(
                SubscriptionRequestFilter(),
                GreetingFilter(),
                ChannelPromotionFilter()
            )
            val composite = CompositeSubtitleFilter(filters)

            val subscriptionSegment = SubtitleSegment(0.0, 1.0, "구독 눌러주세요")
            assertTrue(composite.shouldFilter(subscriptionSegment))

            val greetingSegment = SubtitleSegment(0.0, 1.0, "안녕하세요 여러분")
            assertTrue(composite.shouldFilter(greetingSegment))

            val promotionSegment = SubtitleSegment(0.0, 1.0, "채널 방문해주세요")
            assertTrue(composite.shouldFilter(promotionSegment))
        }

        @Test
        @DisplayName("모든 필터에 매칭되지 않으면 필터링하지 않는다")
        fun shouldNotFilterWhenNoneMatches() {
            val filters = listOf(
                SubscriptionRequestFilter(),
                GreetingFilter(),
                ChannelPromotionFilter()
            )
            val composite = CompositeSubtitleFilter(filters)

            val normalSegment = SubtitleSegment(0.0, 1.0, "아이가 열이 날 때는 해열제를 복용하세요")
            assertFalse(composite.shouldFilter(normalSegment))
        }
    }

    @Nested
    @DisplayName("세그먼트 리스트 필터링")
    inner class FilterSegments {

        @Test
        @DisplayName("필터링 대상 세그먼트를 제거하고 정상 세그먼트만 반환한다")
        fun filterOutMatchingSegments() {
            val filters = listOf(
                SubscriptionRequestFilter(),
                GreetingFilter(),
                ChannelPromotionFilter()
            )
            val composite = CompositeSubtitleFilter(filters)

            val segments = listOf(
                SubtitleSegment(0.0, 1.0, "안녕하세요 여러분"),
                SubtitleSegment(1.0, 1.0, "오늘은 열에 대해 알아보겠습니다"),
                SubtitleSegment(2.0, 1.0, "구독과 좋아요 부탁드려요"),
                SubtitleSegment(3.0, 1.0, "해열제는 이렇게 복용합니다"),
                SubtitleSegment(4.0, 1.0, "시청해주셔서 감사합니다")
            )

            val filtered = composite.filterSegments(segments)

            assertEquals(2, filtered.size)
            assertEquals("오늘은 열에 대해 알아보겠습니다", filtered[0].text)
            assertEquals("해열제는 이렇게 복용합니다", filtered[1].text)
        }

        @Test
        @DisplayName("모든 세그먼트가 정상이면 그대로 반환한다")
        fun returnAllWhenNoneMatch() {
            val filters = listOf(
                SubscriptionRequestFilter(),
                GreetingFilter(),
                ChannelPromotionFilter()
            )
            val composite = CompositeSubtitleFilter(filters)

            val segments = listOf(
                SubtitleSegment(0.0, 1.0, "아이가 열이 날 때"),
                SubtitleSegment(1.0, 1.0, "해열제를 복용하세요")
            )

            val filtered = composite.filterSegments(segments)

            assertEquals(2, filtered.size)
            assertEquals(segments, filtered)
        }
    }
}
