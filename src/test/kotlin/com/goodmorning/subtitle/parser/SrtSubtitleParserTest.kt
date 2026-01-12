package com.goodmorning.subtitle.parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

@DisplayName("SrtSubtitleParser")
class SrtSubtitleParserTest {

    private lateinit var parser: SrtSubtitleParser

    @TempDir
    lateinit var tempDir: File

    @BeforeEach
    fun setUp() {
        parser = SrtSubtitleParser()
    }

    @Nested
    @DisplayName("canParse 메서드")
    inner class CanParse {

        @Nested
        @DisplayName("srt 확장자인 경우")
        inner class WhenSrtExtension {

            @Test
            @DisplayName("true를 반환한다")
            fun returnsTrue() {
                // Given
                val file = File(tempDir, "test.srt")
                file.createNewFile()

                // When
                val result = parser.canParse(file)

                // Then
                assertTrue(result)
            }
        }

        @Nested
        @DisplayName("srt 확장자가 아닌 경우")
        inner class WhenNotSrtExtension {

            @Test
            @DisplayName("false를 반환한다")
            fun returnsFalse() {
                // Given
                val file = File(tempDir, "test.json3")
                file.createNewFile()

                // When
                val result = parser.canParse(file)

                // Then
                assertFalse(result)
            }
        }
    }

    @Nested
    @DisplayName("parse 메서드")
    inner class Parse {

        @Nested
        @DisplayName("유효한 srt 파일인 경우")
        inner class WhenValidSrtFile {

            @Test
            @DisplayName("자막 세그먼트 목록을 반환한다")
            fun returnsSubtitleSegments() {
                // Given
                val file = File(tempDir, "test.srt")
                file.writeText("""
                    1
                    00:00:01,000 --> 00:00:03,000
                    안녕하세요

                    2
                    00:00:05,500 --> 00:00:08,000
                    반갑습니다
                """.trimIndent())

                // When
                val segments = parser.parse(file)

                // Then
                assertEquals(2, segments.size)
                assertEquals(1.0, segments[0].start)
                assertEquals(2.0, segments[0].duration)
                assertEquals("안녕하세요", segments[0].text)
                assertEquals(5.5, segments[1].start)
                assertEquals("반갑습니다", segments[1].text)
            }
        }

        @Nested
        @DisplayName("쉼표를 소수점 구분자로 사용하는 경우")
        inner class WhenCommaAsDecimalSeparator {

            @Test
            @DisplayName("시간을 올바르게 파싱한다")
            fun parsesTimeCorrectly() {
                // Given
                val file = File(tempDir, "test.srt")
                file.writeText("""
                    1
                    00:01:30,500 --> 00:01:35,000
                    시간 테스트
                """.trimIndent())

                // When
                val segments = parser.parse(file)

                // Then
                assertEquals(1, segments.size)
                assertEquals(90.5, segments[0].start)
                assertEquals(4.5, segments[0].duration)
            }
        }
    }
}
