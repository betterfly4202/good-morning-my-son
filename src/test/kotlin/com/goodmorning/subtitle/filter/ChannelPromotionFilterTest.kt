package com.goodmorning.subtitle.filter

import com.goodmorning.subtitle.model.SubtitleSegment
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("ChannelPromotionFilter 테스트")
class ChannelPromotionFilterTest {

    private val filter = ChannelPromotionFilter()

    @Nested
    @DisplayName("채널 방문 요청 패턴")
    inner class ChannelVisitPatterns {

        @Test
        @DisplayName("채널 방문 요청 패턴을 필터링한다")
        fun filterChannelVisit() {
            val segment = SubtitleSegment(0.0, 1.0, "채널에 방문해주세요")
            assertTrue(filter.shouldFilter(segment))
        }

        @Test
        @DisplayName("채널 소개 패턴을 필터링한다")
        fun filterChannelIntro() {
            val segment = SubtitleSegment(0.0, 1.0, "제 채널 소개를 해드릴게요")
            assertTrue(filter.shouldFilter(segment))
        }
    }

    @Nested
    @DisplayName("영상 홍보 패턴")
    inner class VideoPromotionPatterns {

        @Test
        @DisplayName("다른 영상 안내 패턴을 필터링한다")
        fun filterOtherVideos() {
            val segment = SubtitleSegment(0.0, 1.0, "다른 영상을 보시려면 채널을 확인해주세요")
            assertTrue(filter.shouldFilter(segment))
        }

        @Test
        @DisplayName("더 많은 영상 패턴을 필터링한다")
        fun filterMoreVideos() {
            val segment = SubtitleSegment(0.0, 1.0, "더 많은 영상은 채널에 있습니다")
            assertTrue(filter.shouldFilter(segment))
        }
    }

    @Nested
    @DisplayName("설명란 안내 패턴")
    inner class DescriptionPatterns {

        @Test
        @DisplayName("링크 설명란 패턴을 필터링한다")
        fun filterDescriptionLink() {
            val segment = SubtitleSegment(0.0, 1.0, "링크는 설명란에 있습니다")
            assertTrue(filter.shouldFilter(segment))
        }

        @Test
        @DisplayName("설명란 확인 요청 패턴을 필터링한다")
        fun filterCheckDescription() {
            val segment = SubtitleSegment(0.0, 1.0, "설명란을 확인해주세요")
            assertTrue(filter.shouldFilter(segment))
        }
    }

    @Nested
    @DisplayName("일반 내용")
    inner class NormalContent {

        @Test
        @DisplayName("일반 내용은 필터링하지 않는다")
        fun doNotFilterNormalContent() {
            val segment = SubtitleSegment(0.0, 1.0, "아이의 건강을 지키는 방법")
            assertFalse(filter.shouldFilter(segment))
        }
    }
}
