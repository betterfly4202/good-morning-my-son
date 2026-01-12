package com.goodmorning.subtitle.parser

import com.goodmorning.subtitle.model.SubtitleSegment
import java.io.File

interface SubtitleParser {
    fun canParse(file: File): Boolean
    fun parse(file: File): List<SubtitleSegment>
}
