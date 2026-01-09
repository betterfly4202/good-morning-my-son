package com.goodmorning.subtitle

import com.goodmorning.subtitle.model.SubtitleSegment

object SubtitleParser {

    fun parseXml(xml: String): List<SubtitleSegment> {
        val segments = mutableListOf<SubtitleSegment>()
        val pattern = "<text start=\"([^\"]+)\" dur=\"([^\"]+)\"[^>]*>([^<]*)</text>"
        val regex = Regex(pattern)

        regex.findAll(xml).forEach { match ->
            val start = match.groupValues[1].toDoubleOrNull() ?: 0.0
            val duration = match.groupValues[2].toDoubleOrNull() ?: 0.0
            val text = decodeHtmlEntities(match.groupValues[3].trim())

            if (text.isNotBlank()) {
                segments.add(SubtitleSegment(start, duration, text))
            }
        }

        return segments
    }

    private fun decodeHtmlEntities(text: String): String {
        return text
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&apos;", "'")
            .replace("\\n", " ")
            .replace("\n", " ")
    }
}
