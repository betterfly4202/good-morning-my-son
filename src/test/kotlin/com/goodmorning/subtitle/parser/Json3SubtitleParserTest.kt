package com.goodmorning.subtitle.parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

@DisplayName("Json3SubtitleParser")
class Json3SubtitleParserTest {

    private lateinit var parser: Json3SubtitleParser

    @TempDir
    lateinit var tempDir: File

    @BeforeEach
    fun setUp() {
        parser = Json3SubtitleParser()
    }

    @Nested
    @DisplayName("canParse 메서드")
    inner class CanParse {

        @Nested
        @DisplayName("json3 확장자인 경우")
        inner class WhenJson3Extension {

            @Test
            @DisplayName("true를 반환한다")
            fun returnsTrue() {
                // Given
                val file = File(tempDir, "test.json3")
                file.createNewFile()

                // When
                val result = parser.canParse(file)

                // Then
                assertTrue(result)
            }
        }

        @Nested
        @DisplayName("json3 확장자가 아닌 경우")
        inner class WhenNotJson3Extension {

            @Test
            @DisplayName("false를 반환한다")
            fun returnsFalse() {
                // Given
                val file = File(tempDir, "test.vtt")
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
        @DisplayName("유효한 json3 파일인 경우")
        inner class WhenValidJson3File {

            @Test
            @DisplayName("자막 세그먼트 목록을 반환한다")
            fun returnsSubtitleSegments() {
                // Given
                val file = File(tempDir, "test.json3")
                file.writeText("""
                    {
                      "events": [
                        {
                          "tStartMs": 1000,
                          "dDurationMs": 2000,
                          "segs": [{"utf8": "안녕하세요"}]
                        },
                        {
                          "tStartMs": 3000,
                          "dDurationMs": 1500,
                          "segs": [{"utf8": "반갑습니다"}]
                        }
                      ]
                    }
                """.trimIndent())

                // When
                val segments = parser.parse(file)

                // Then
                assertEquals(2, segments.size)
                assertEquals(1.0, segments[0].start)
                assertEquals(2.0, segments[0].duration)
                assertEquals("안녕하세요", segments[0].text)
                assertEquals(3.0, segments[1].start)
                assertEquals("반갑습니다", segments[1].text)
            }
        }

        @Nested
        @DisplayName("유효하지 않은 JSON인 경우")
        inner class WhenInvalidJson {

            @Test
            @DisplayName("빈 목록을 반환한다")
            fun returnsEmptyList() {
                // Given
                val file = File(tempDir, "test.json3")
                file.writeText("not valid json")

                // When
                val segments = parser.parse(file)

                // Then
                assertTrue(segments.isEmpty())
            }
        }

        @Nested
        @DisplayName("공백 텍스트가 포함된 이벤트가 있는 경우")
        inner class WhenEventsWithBlankText {

            @Test
            @DisplayName("공백 텍스트 이벤트를 건너뛴다")
            fun skipsBlankTextEvents() {
                // Given
                val file = File(tempDir, "test.json3")
                file.writeText("""
                    {
                      "events": [
                        {
                          "tStartMs": 1000,
                          "dDurationMs": 2000,
                          "segs": [{"utf8": "  "}]
                        },
                        {
                          "tStartMs": 3000,
                          "dDurationMs": 1500,
                          "segs": [{"utf8": "유효한 텍스트"}]
                        }
                      ]
                    }
                """.trimIndent())

                // When
                val segments = parser.parse(file)

                // Then
                assertEquals(1, segments.size)
                assertEquals("유효한 텍스트", segments[0].text)
            }
        }
    }
}
