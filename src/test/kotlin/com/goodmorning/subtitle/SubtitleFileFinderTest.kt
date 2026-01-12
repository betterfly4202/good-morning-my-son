package com.goodmorning.subtitle

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

@DisplayName("SubtitleFileFinder")
class SubtitleFileFinderTest {

    private lateinit var finder: SubtitleFileFinder

    @TempDir
    lateinit var tempDir: File

    @BeforeEach
    fun setUp() {
        finder = SubtitleFileFinder()
    }

    @Nested
    @DisplayName("find 메서드")
    inner class Find {

        @Nested
        @DisplayName("json3 파일이 존재하는 경우")
        inner class WhenJson3FileExists {

            @Test
            @DisplayName("json3 파일을 반환한다")
            fun returnsJson3File() {
                // Given
                val videoId = "abc123"
                File(tempDir, "$videoId.ko.json3").createNewFile()

                // When
                val result = finder.find(tempDir, videoId)

                // Then
                assertNotNull(result)
                assertEquals("json3", result?.extension)
            }
        }

        @Nested
        @DisplayName("파일이 존재하지 않는 경우")
        inner class WhenNoFileExists {

            @Test
            @DisplayName("null을 반환한다")
            fun returnsNull() {
                // Given
                val videoId = "nonexistent"

                // When
                val result = finder.find(tempDir, videoId)

                // Then
                assertNull(result)
            }
        }

        @Nested
        @DisplayName("json3와 vtt 파일이 모두 존재하는 경우")
        inner class WhenBothJson3AndVttExist {

            @Test
            @DisplayName("json3 파일을 우선 반환한다")
            fun prefersJson3OverVtt() {
                // Given
                val videoId = "abc123"
                File(tempDir, "$videoId.ko.vtt").createNewFile()
                File(tempDir, "$videoId.ko.json3").createNewFile()

                // When
                val result = finder.find(tempDir, videoId)

                // Then
                assertNotNull(result)
                assertEquals("json3", result?.extension)
            }
        }

        @Nested
        @DisplayName("vtt 파일만 존재하는 경우")
        inner class WhenOnlyVttExists {

            @Test
            @DisplayName("vtt 파일을 반환한다")
            fun returnsVttFile() {
                // Given
                val videoId = "abc123"
                File(tempDir, "$videoId.ko.vtt").createNewFile()

                // When
                val result = finder.find(tempDir, videoId)

                // Then
                assertNotNull(result)
                assertEquals("vtt", result?.extension)
            }
        }
    }
}
