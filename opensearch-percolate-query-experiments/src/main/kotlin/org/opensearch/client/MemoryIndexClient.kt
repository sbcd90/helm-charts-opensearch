package org.opensearch.client

import org.apache.lucene.analysis.core.SimpleAnalyzer
import org.apache.lucene.index.memory.MemoryIndex
import org.apache.lucene.queryparser.classic.QueryParser

class MemoryIndexClient {
    constructor() {
        val analyzer = SimpleAnalyzer()
        val index = MemoryIndex()

        val text = "Readings about Salmons and other select Alaska fishing Manuals".repeat(1000)
        val name = "Tales of James".repeat(1000)
        for (i in 1..1000000) {
            index.addField("content", text, analyzer)
            index.addField("author", name, analyzer)
        }

        val parser = QueryParser("content", analyzer)
        val score = index.search(parser.parse("+author:james +salmon~ +fish* manual~"))
        if (score > 0.0f) {
            println("hit")
        } else {
            println("not hit")
        }
        println(index.toString())
    }
}

fun main(args: Array<String>) {
    Thread.sleep(120000)
    MemoryIndexClient()
}