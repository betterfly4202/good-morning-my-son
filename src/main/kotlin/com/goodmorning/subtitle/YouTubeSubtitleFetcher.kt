package com.goodmorning.subtitle

import com.goodmorning.subtitle.model.SubtitleSegment
import com.goodmorning.subtitle.model.VideoSubtitle
import kotlinx.serialization.json.*
import java.io.File
import java.time.Instant
import java.util.concurrent.TimeUnit
import kotlin.io.path.createTempDirectory

class YouTubeSubtitleFetcher(
    private val channelName: String = "하정훈의 삐뽀삐뽀 119 소아과"
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun fetch(videoId: String, debug: Boolean = false): VideoSubtitle? {
        val tempDir = createTempDirectory("yt-subtitle-").toFile()

        try {
            // yt-dlp로 자막 다운로드
            val result = downloadSubtitle(videoId, tempDir, debug)
            if (!result) {
                if (debug) println("DEBUG: yt-dlp failed to download subtitle")
                return null
            }

            // 다운로드된 자막 파일 찾기
            val subtitleFile = findSubtitleFile(tempDir, videoId)
            if (subtitleFile == null) {
                if (debug) println("DEBUG: No subtitle file found")
                return null
            }

            if (debug) println("DEBUG: Found subtitle file: ${subtitleFile.name}")

            // 영상 정보 가져오기
            val title = getVideoTitle(videoId) ?: "Unknown Title"
            if (debug) println("DEBUG: Title = $title")

            // 자막 파싱
            val segments = parseSubtitleFile(subtitleFile, debug)
            if (debug) println("DEBUG: Parsed ${segments.size} segments")

            return VideoSubtitle(
                videoId = videoId,
                title = title,
                channelName = channelName,
                collectedAt = Instant.now().toString(),
                segments = segments,
                fullText = segments.joinToString(" ") { it.text }
            )
        } finally {
            tempDir.deleteRecursively()
        }
    }

    private fun downloadSubtitle(videoId: String, outputDir: File, debug: Boolean): Boolean {
        val url = "https://www.youtube.com/watch?v=$videoId"

        val command = listOf(
            "yt-dlp",
            "--skip-download",
            "--write-auto-sub",
            "--write-sub",
            "--sub-lang", "ko",
            "--sub-format", "json3",
            "--output", "${outputDir.absolutePath}/$videoId.%(ext)s",
            url
        )

        if (debug) println("DEBUG: Running command: ${command.joinToString(" ")}")

        val process = ProcessBuilder(command)
            .redirectErrorStream(true)
            .start()

        val output = process.inputStream.bufferedReader().readText()
        val exitCode = process.waitFor()

        if (debug) {
            println("DEBUG: yt-dlp exit code = $exitCode")
            println("DEBUG: yt-dlp output = $output")
        }

        return exitCode == 0
    }

    private fun findSubtitleFile(dir: File, videoId: String): File? {
        return dir.listFiles()
            ?.filter { it.name.contains(videoId) && (it.extension == "json3" || it.extension == "vtt" || it.extension == "srt") }
            ?.firstOrNull()
    }

    private fun getVideoTitle(videoId: String): String? {
        val command = listOf(
            "yt-dlp",
            "--get-title",
            "--no-warnings",
            "https://www.youtube.com/watch?v=$videoId"
        )

        val process = ProcessBuilder(command)
            .start()

        val output = process.inputStream.bufferedReader().readText().trim()
        process.waitFor(30, TimeUnit.SECONDS)

        return output.lines()
            .lastOrNull { it.isNotBlank() && !it.startsWith("WARNING") && !it.startsWith("ERROR") }
    }

    private fun parseSubtitleFile(file: File, debug: Boolean): List<SubtitleSegment> {
        return when (file.extension) {
            "json3" -> parseJson3(file, debug)
            "vtt" -> parseVtt(file)
            "srt" -> parseSrt(file)
            else -> emptyList()
        }
    }

    private fun parseJson3(file: File, debug: Boolean): List<SubtitleSegment> {
        val content = file.readText()
        if (debug) println("DEBUG: json3 content length = ${content.length}")

        return try {
            val jsonObj = json.parseToJsonElement(content).jsonObject
            val events = jsonObj["events"]?.jsonArray ?: return emptyList()

            events.mapNotNull { event ->
                val eventObj = event.jsonObject
                val start = eventObj["tStartMs"]?.jsonPrimitive?.longOrNull ?: return@mapNotNull null
                val duration = eventObj["dDurationMs"]?.jsonPrimitive?.longOrNull ?: 0L
                val segs = eventObj["segs"]?.jsonArray ?: return@mapNotNull null

                val text = segs.mapNotNull { seg ->
                    seg.jsonObject["utf8"]?.jsonPrimitive?.contentOrNull
                }.joinToString("").trim()

                if (text.isBlank()) return@mapNotNull null

                SubtitleSegment(
                    start = start / 1000.0,
                    duration = duration / 1000.0,
                    text = text
                )
            }
        } catch (e: Exception) {
            if (debug) println("DEBUG: Error parsing json3: ${e.message}")
            emptyList()
        }
    }

    private fun parseVtt(file: File): List<SubtitleSegment> {
        val segments = mutableListOf<SubtitleSegment>()
        val lines = file.readLines()
        var i = 0

        while (i < lines.size) {
            val line = lines[i]
            // VTT timestamp pattern: 00:00:00.000 --> 00:00:00.000
            if (line.contains("-->")) {
                val times = line.split("-->").map { it.trim() }
                if (times.size == 2) {
                    val start = parseVttTime(times[0])
                    val end = parseVttTime(times[1])

                    // 다음 줄들이 자막 텍스트
                    val textLines = mutableListOf<String>()
                    i++
                    while (i < lines.size && lines[i].isNotBlank()) {
                        textLines.add(lines[i].replace(Regex("<[^>]+>"), ""))
                        i++
                    }

                    val text = textLines.joinToString(" ").trim()
                    if (text.isNotBlank()) {
                        segments.add(SubtitleSegment(start, end - start, text))
                    }
                }
            }
            i++
        }
        return segments
    }

    private fun parseVttTime(time: String): Double {
        val parts = time.split(":", ".")
        return when (parts.size) {
            4 -> parts[0].toDouble() * 3600 + parts[1].toDouble() * 60 + parts[2].toDouble() + parts[3].toDouble() / 1000
            3 -> parts[0].toDouble() * 60 + parts[1].toDouble() + parts[2].toDouble() / 1000
            else -> 0.0
        }
    }

    private fun parseSrt(file: File): List<SubtitleSegment> {
        // SRT는 VTT와 유사하게 처리
        return parseVtt(file)
    }
}
