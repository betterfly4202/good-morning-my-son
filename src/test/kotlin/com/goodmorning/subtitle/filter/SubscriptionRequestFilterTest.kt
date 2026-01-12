package com.goodmorning.subtitle.filter

import com.goodmorning.subtitle.model.SubtitleSegment
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("SubscriptionRequestFilter 테스트")
class SubscriptionRequestFilterTest {

    private val filter = SubscriptionRequestFilter()

    @Nested
    @DisplayName("구독 요청 패턴")
    inner class SubscriptionPatterns {

        @Test
        @DisplayName("구독 + 눌러주세요 패턴을 필터링한다")
        fun filterSubscriptionRequest() {
            val segment = SubtitleSegment(0.0, 1.0, "구독 버튼을 눌러 주세요")
            assertTrue(filter.shouldFilter(segment))
        }

        @Test
        @DisplayName("구독 + 부탁드립니다 패턴을 필터링한다")
        fun filterSubscriptionPlease() {
            val segment = SubtitleSegment(0.0, 1.0, "구독 부탁 드립니다")
            assertTrue(filter.shouldFilter(segment))
        }
    }

    @Nested
    @DisplayName("좋아요 요청 패턴")
    inner class LikePatterns {

        @Test
        @DisplayName("좋아요 + 눌러주세요 패턴을 필터링한다")
        fun filterLikeRequest() {
            val segment = SubtitleSegment(0.0, 1.0, "좋아요 버튼을 눌러주세요")
            assertTrue(filter.shouldFilter(segment))
        }

        @Test
        @DisplayName("좋아요 + 구독 조합 패턴을 필터링한다")
        fun filterLikeAndSubscription() {
            val segment = SubtitleSegment(0.0, 1.0, "좋아요와 구독 부탁드려요")
            assertTrue(filter.shouldFilter(segment))
        }
    }

    @Nested
    @DisplayName("알림 설정 요청 패턴")
    inner class NotificationPatterns {

        @Test
        @DisplayName("알림 설정 요청 패턴을 필터링한다")
        fun filterNotificationRequest() {
            val segment = SubtitleSegment(0.0, 1.0, "알림 설정을 해주세요")
            assertTrue(filter.shouldFilter(segment))
        }

        @Test
        @DisplayName("알림 + 구독 조합 패턴을 필터링한다")
        fun filterNotificationAndSubscription() {
            val segment = SubtitleSegment(0.0, 1.0, "구독과 알림 설정 부탁드립니다")
            assertTrue(filter.shouldFilter(segment))
        }
    }

    @Nested
    @DisplayName("일반 내용")
    inner class NormalContent {

        @Test
        @DisplayName("일반 내용은 필터링하지 않는다")
        fun doNotFilterNormalContent() {
            val segment = SubtitleSegment(0.0, 1.0, "아이가 열이 날 때는 해열제를 복용하세요")
            assertFalse(filter.shouldFilter(segment))
        }
    }
}
