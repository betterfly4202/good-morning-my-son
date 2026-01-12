package com.goodmorning.subtitle.parser

import com.goodmorning.subtitle.model.SubtitleSegment
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File

@Component
class SrtSubtitleParser : SubtitleParser {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun canParse(file: File): Boolean = file.extension == "srt"

    override fun parse(file: File): List<SubtitleSegment> {
        logger.debug("Parsing srt file: {}", file.name)
        val segments = mutableListOf<SubtitleSegment>()
        val lines = file.readLines()
        var i = 0

        while (i < lines.size) {
            val line = lines[i]
            if (line.contains("-->")) {
                val times = line.split("-->").map { it.trim() }
                if (times.size == 2) {
                    val start = parseTime(times[0])
                    val end = parseTime(times[1])

                    val textLines = mutableListOf<String>()
                    i++
                    while (i < lines.size && lines[i].isNotBlank()) {
                        textLines.add(lines[i])
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

        logger.debug("Parsed {} segments from srt", segments.size)
        return segments
    }

    private fun parseTime(time: String): Double {
        val parts = time.replace(",", ".").split(":", ".")
        return when (parts.size) {
            4 -> parts[0].toDouble() * 3600 + parts[1].toDouble() * 60 + parts[2].toDouble() + parts[3].toDouble() / 1000
            3 -> parts[0].toDouble() * 60 + parts[1].toDouble() + parts[2].toDouble() / 1000
            else -> 0.0
        }
    }
}
