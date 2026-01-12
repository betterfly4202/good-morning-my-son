package com.goodmorning.subtitle.parser

import com.goodmorning.subtitle.model.SubtitleSegment
import kotlinx.serialization.json.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File

@Component
class Json3SubtitleParser(
    private val json: Json = Json { ignoreUnknownKeys = true }
) : SubtitleParser {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun canParse(file: File): Boolean = file.extension == "json3"

    override fun parse(file: File): List<SubtitleSegment> {
        val content = file.readText()
        logger.debug("Parsing json3 file: {} ({} chars)", file.name, content.length)

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
            logger.error("Error parsing json3: {}", e.message)
            emptyList()
        }
    }
}
