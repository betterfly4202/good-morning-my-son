package com.goodmorning

import com.goodmorning.subtitle.YouTubeSubtitleFetcher
import com.goodmorning.subtitle.model.VideoSubtitle
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

private val json = Json { prettyPrint = true }

fun main(args: Array<String>) {
    val videoIds = loadVideoIds(args)

    if (videoIds.isEmpty()) {
        println("Usage: ./gradlew run --args=\"VIDEO_ID [VIDEO_ID...]\"")
        println("   or: ./gradlew run --args=\"--file video_ids.txt\"")
        return
    }

    val fetcher = YouTubeSubtitleFetcher()
    val outputDir = File("output/subtitles").apply { mkdirs() }

    videoIds.forEachIndexed { index, videoId ->
        println("[${index + 1}/${videoIds.size}] Fetching: $videoId")

        val subtitle = fetcher.fetch(videoId)
        if (subtitle != null) {
            saveSubtitle(subtitle, outputDir)
            println("  -> Saved: ${subtitle.title}")
        } else {
            println("  -> Failed to fetch subtitle")
        }

        // Rate limiting
        if (index < videoIds.lastIndex) {
            Thread.sleep(1000)
        }
    }

    println("\nDone! Subtitles saved to: ${outputDir.absolutePath}")
}

private fun loadVideoIds(args: Array<String>): List<String> {
    if (args.isEmpty()) return emptyList()

    return if (args[0] == "--file" && args.size > 1) {
        File(args[1]).readLines()
            .map { it.trim() }
            .filter { it.isNotBlank() && !it.startsWith("#") }
    } else {
        args.toList()
    }
}

private fun saveSubtitle(subtitle: VideoSubtitle, outputDir: File) {
    val filename = "${subtitle.videoId}.json"
    val file = File(outputDir, filename)
    file.writeText(json.encodeToString(subtitle))
}
