package com.goodmorning

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GoodMorningApplication

fun main(args: Array<String>) {
    runApplication<GoodMorningApplication>(*args)
}
