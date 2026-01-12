package com.goodmorning.subtitle.chunker

import com.goodmorning.subtitle.model.SubtitleSegment
import com.goodmorning.subtitle.model.VideoSubtitle
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DisplayName("SizeBasedChunkingStrategy 테스트")
class SizeBasedChunkingStrategyTest {

    @Nested
    @DisplayName("빈 세그먼트")
    inner class EmptySegments {

        @Test
        @DisplayName("빈 리스트를 반환한다")
        fun returnEmptyList() {
            val strategy = SizeBasedChunkingStrategy(targetSize = 50)
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
    @DisplayName("목표 크기 기반 분할")
    inner class TargetSizeSplit {

        @Test
        @DisplayName("텍스트 길이가 목표 크기를 초과하면 청크를 분할한다")
        fun splitWhenExceedingTargetSize() {
            val strategy = SizeBasedChunkingStrategy(targetSize = 20)
            val segments = listOf(
                SubtitleSegment(start = 0.0, duration = 5.0, text = "짧은 텍스트"),
                SubtitleSegment(start = 5.0, duration = 5.0, text = "또 다른 짧은 텍스트"),
                SubtitleSegment(start = 10.0, duration = 5.0, text = "추가 텍스트")
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
            assertEquals("짧은 텍스트 또 다른 짧은 텍스트", chunks[0].text)
            assertEquals("추가 텍스트", chunks[1].text)
        }
    }

    @Nested
    @DisplayName("오버랩 옵션")
    inner class OverlapOption {

        @Test
        @DisplayName("마지막 N개 세그먼트를 다음 청크에 포함한다")
        fun applyOverlapBetweenChunks() {
            val strategy = SizeBasedChunkingStrategy(targetSize = 10, overlapSegments = 1)
            val segments = listOf(
                SubtitleSegment(start = 0.0, duration = 5.0, text = "첫번째"),
                SubtitleSegment(start = 5.0, duration = 5.0, text = "두번째"),
                SubtitleSegment(start = 10.0, duration = 5.0, text = "세번째")
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
            // 첫 번째 청크: "첫번째 두번째"
            assertEquals("첫번째 두번째", chunks[0].text)
            assertEquals(0.0, chunks[0].startTime)
            assertEquals(10.0, chunks[0].endTime)
            assertEquals(2, chunks[0].segmentCount)
            // 두 번째 청크: "두번째 세번째" (오버랩으로 "두번째" 포함)
            assertEquals("두번째 세번째", chunks[1].text)
            assertEquals(5.0, chunks[1].startTime)  // 오버랩 세그먼트의 시작 시간
            assertEquals(15.0, chunks[1].endTime)
            assertEquals(2, chunks[1].segmentCount)
        }
    }

    @Nested
    @DisplayName("단일 세그먼트")
    inner class SingleSegment {

        @Test
        @DisplayName("하나의 청크로 변환한다")
        fun convertToSingleChunk() {
            val strategy = SizeBasedChunkingStrategy(targetSize = 100)
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

    @Nested
    @DisplayName("파라미터 검증")
    inner class ParameterValidation {

        @Test
        @DisplayName("targetSize가 0이면 예외 발생")
        fun throwExceptionWhenTargetSizeIsZero() {
            assertThrows<IllegalArgumentException> {
                SizeBasedChunkingStrategy(targetSize = 0)
            }
        }

        @Test
        @DisplayName("targetSize가 음수면 예외 발생")
        fun throwExceptionWhenTargetSizeIsNegative() {
            assertThrows<IllegalArgumentException> {
                SizeBasedChunkingStrategy(targetSize = -1)
            }
        }

        @Test
        @DisplayName("overlapSegments가 음수면 예외 발생")
        fun throwExceptionWhenOverlapSegmentsIsNegative() {
            assertThrows<IllegalArgumentException> {
                SizeBasedChunkingStrategy(targetSize = 100, overlapSegments = -1)
            }
        }
    }

    @Nested
    @DisplayName("엣지 케이스")
    inner class EdgeCases {

        @Test
        @DisplayName("단일 세그먼트가 targetSize를 초과해도 분할하지 않는다")
        fun singleSegmentExceedingTargetSizeNotSplit() {
            val strategy = SizeBasedChunkingStrategy(targetSize = 5)
            val segments = listOf(
                SubtitleSegment(start = 0.0, duration = 10.0, text = "이것은매우긴텍스트입니다")
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
            assertEquals("이것은매우긴텍스트입니다", chunks[0].text)
            assertEquals(1, chunks[0].segmentCount)
        }

        @Test
        @DisplayName("overlapSegments가 청크 세그먼트 수보다 크면 전체 유지")
        fun overlapSegmentsLargerThanChunkSize() {
            val strategy = SizeBasedChunkingStrategy(targetSize = 10, overlapSegments = 5)
            val segments = listOf(
                SubtitleSegment(start = 0.0, duration = 5.0, text = "첫번째"),
                SubtitleSegment(start = 5.0, duration = 5.0, text = "두번째"),
                SubtitleSegment(start = 10.0, duration = 5.0, text = "세번째")
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
            // 두 번째 청크는 오버랩으로 전체 세그먼트(첫번째, 두번째)를 유지
            assertEquals("첫번째 두번째 세번째", chunks[1].text)
        }
    }
}
