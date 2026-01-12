package com.goodmorning.subtitle.chunker

import com.goodmorning.subtitle.model.SubtitleSegment
import com.goodmorning.subtitle.model.VideoSubtitle
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DisplayName("TimeBasedChunkingStrategy 테스트")
class TimeBasedChunkingStrategyTest {

    @Nested
    @DisplayName("빈 세그먼트")
    inner class EmptySegments {

        @Test
        @DisplayName("빈 리스트를 반환한다")
        fun returnEmptyList() {
            val strategy = TimeBasedChunkingStrategy(intervalSeconds = 30.0)
            val videoSubtitle = VideoSubtitle(
                videoId = "test",
                title = "Test",
                channelName = "Test",
                collectedAt = "2024-01-01",
                segments = emptyList(),
                fullText = ""
            )

            val chunks = strategy.chunk(videoSubtitle)

            assertTrue(chunks.isEmpty())
        }
    }

    @Nested
    @DisplayName("30초 간격 분할")
    inner class ThirtySecondInterval {

        @Test
        @DisplayName("시간 구간별로 세그먼트를 청크로 묶는다")
        fun chunkByTimeInterval() {
            val strategy = TimeBasedChunkingStrategy(intervalSeconds = 30.0)
            val segments = listOf(
                SubtitleSegment(start = 0.0, duration = 5.0, text = "첫 번째"),
                SubtitleSegment(start = 5.0, duration = 5.0, text = "두 번째"),
                SubtitleSegment(start = 35.0, duration = 5.0, text = "세 번째"),
                SubtitleSegment(start = 40.0, duration = 5.0, text = "네 번째")
            )
            val videoSubtitle = VideoSubtitle(
                videoId = "test",
                title = "Test",
                channelName = "Test",
                collectedAt = "2024-01-01",
                segments = segments,
                fullText = ""
            )

            val chunks = strategy.chunk(videoSubtitle)

            assertEquals(2, chunks.size)
            assertEquals("첫 번째 두 번째", chunks[0].text)
            assertEquals(0.0, chunks[0].startTime)
            assertEquals(10.0, chunks[0].endTime)
            assertEquals(2, chunks[0].segmentCount)

            assertEquals("세 번째 네 번째", chunks[1].text)
            assertEquals(35.0, chunks[1].startTime)
            assertEquals(45.0, chunks[1].endTime)
            assertEquals(2, chunks[1].segmentCount)
        }
    }

    @Nested
    @DisplayName("단일 세그먼트")
    inner class SingleSegment {

        @Test
        @DisplayName("하나의 청크로 변환한다")
        fun convertToSingleChunk() {
            val strategy = TimeBasedChunkingStrategy(intervalSeconds = 30.0)
            val segments = listOf(
                SubtitleSegment(start = 0.0, duration = 5.0, text = "단일 세그먼트")
            )
            val videoSubtitle = VideoSubtitle(
                videoId = "test",
                title = "Test",
                channelName = "Test",
                collectedAt = "2024-01-01",
                segments = segments,
                fullText = ""
            )

            val chunks = strategy.chunk(videoSubtitle)

            assertEquals(1, chunks.size)
            assertEquals("단일 세그먼트", chunks[0].text)
            assertEquals(1, chunks[0].segmentCount)
        }
    }
}
