package com.goodmorning.subtitle.parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

@DisplayName("VttSubtitleParser")
class VttSubtitleParserTest {

    private lateinit var parser: VttSubtitleParser

    @TempDir
    lateinit var tempDir: File

    @BeforeEach
    fun setUp() {
        parser = VttSubtitleParser()
    }

    @Nested
    @DisplayName("canParse 메서드")
    inner class CanParse {

        @Nested
        @DisplayName("vtt 확장자인 경우")
        inner class WhenVttExtension {

            @Test
            @DisplayName("true를 반환한다")
            fun returnsTrue() {
                // Given
                val file = File(tempDir, "test.vtt")
                file.createNewFile()

                // When
                val result = parser.canParse(file)

                // Then
                assertTrue(result)
            }
        }

        @Nested
        @DisplayName("vtt 확장자가 아닌 경우")
        inner class WhenNotVttExtension {

            @Test
            @DisplayName("false를 반환한다")
            fun returnsFalse() {
                // Given
                val file = File(tempDir, "test.srt")
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
        @DisplayName("유효한 vtt 파일인 경우")
        inner class WhenValidVttFile {

            @Test
            @DisplayName("자막 세그먼트 목록을 반환한다")
            fun returnsSubtitleSegments() {
                // Given
                val file = File(tempDir, "test.vtt")
                file.writeText("""
                    WEBVTT

                    00:00:01.000 --> 00:00:03.000
                    안녕하세요

                    00:00:05.500 --> 00:00:08.000
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
        @DisplayName("HTML 태그가 포함된 경우")
        inner class WhenContainsHtmlTags {

            @Test
            @DisplayName("HTML 태그를 제거한다")
            fun removesHtmlTags() {
                // Given
                val file = File(tempDir, "test.vtt")
                file.writeText("""
                    WEBVTT

                    00:00:01.000 --> 00:00:03.000
                    <c.colorWhite>안녕하세요</c>
                """.trimIndent())

                // When
                val segments = parser.parse(file)

                // Then
                assertEquals(1, segments.size)
                assertEquals("안녕하세요", segments[0].text)
            }
        }
    }
}
