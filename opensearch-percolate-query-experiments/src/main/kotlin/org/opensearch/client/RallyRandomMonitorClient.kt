package org.opensearch.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.BufferedReader
import java.io.FileReader
import kotlin.random.Random

class RallyRandomMonitorClient {
    private val monitorSkeletonStr = "{\"type\":\"monitor\",\"monitor_type\":\"doc_level_monitor\",\"name\":\"iad-monitor4\",\"enabled\":true,\"createdBy\":\"chip\",\"schedule\":{\"period\":{\"interval\":120,\"unit\":\"MINUTES\"}},\"inputs\":[{\"doc_level_input\":{\"description\":\"windows-powershell\",\"indices\":[\"http-logs\"],\"queries\":[]}}],\"triggers\":[]}"

    fun createMonitorType1() {
        val mapper = ObjectMapper()

        val statuses = ArrayList<Int>()
        val statusSet = HashSet<Int>()

        val clientips = ArrayList<String>()
        val clientipSet = HashSet<String>()

        var counter = 0

        val path = RallyTrackDataClient::class.java.classLoader.getResource("documents-181998.json")?.path

        if (path != null) {
            val reader = BufferedReader(FileReader(path))
            var line = reader.readLine()

            while (line != null && counter < 100000) {
                val record = mapper.readValue<Map<String, Any>>(line)

                statuses.add(record["status"].toString().toInt())
                statusSet.add(record["status"].toString().toInt())

                clientips.add(record["clientip"].toString())
                clientipSet.add(record["clientip"].toString())
                line = reader.readLine()
                counter += 1
            }

            for (j in 1..25) {
                val monitor = mapper.readValue<Map<String, Any>>(monitorSkeletonStr)
                val queries = ArrayList<Map<String, Any>>()
                for (i in 1..1000) {
                    val idx1 = Random.nextInt(statuses.size)
                    val idx2 = Random.nextInt(clientips.size)

                    val queryStr = "status:${statuses[idx1]} AND clientip:\"${clientips[idx2]}\""

                    val id = java.util.UUID.randomUUID().toString()
                    val query = HashMap<String, Any>()
                    query["id"] = id
                    query["name"] = id
                    query["query"] = queryStr
                    query["severity"] = "5"
                    query["tags"] = listOf("MITRE:8500")

                    queries.add(query)
                }
                (((monitor["inputs"] as ArrayList<Map<String, Any>>)[0]["doc_level_input"] as Map<String, Any>)["queries"] as ArrayList<Map<String, Any>>).addAll(
                    queries
                )
//            println(mapper.writeValueAsString(monitor))

                val response = khttp.post(
                    "http://localhost:9200/_plugins/_alerting/monitors",
                    headers = mapOf(pair = Pair("Content-Type", "application/json")),
                    data = mapper.writeValueAsString(monitor)
                )
                println(response.statusCode)
                println(response.headers["Location"])
                Thread.sleep(5000)
            }
        }
    }

    fun createMonitorType2() {
        val mapper = ObjectMapper()

        val sizes = ArrayList<Int>()
        val sizesSet = HashSet<Int>()

        val clientips = ArrayList<String>()
        val clientipSet = HashSet<String>()

        var counter = 0

        val path = RallyTrackDataClient::class.java.classLoader.getResource("documents-181998.json")?.path

        if (path != null) {
            val reader = BufferedReader(FileReader(path))
            var line = reader.readLine()

            while (line != null && counter < 100000) {
                val record = mapper.readValue<Map<String, Any>>(line)

                sizes.add(record["size"].toString().toInt())
                sizesSet.add(record["size"].toString().toInt())

                clientips.add(record["clientip"].toString())
                clientipSet.add(record["clientip"].toString())
                line = reader.readLine()
                counter += 1
            }

            for (j in 1..25) {
                val monitor = mapper.readValue<Map<String, Any>>(monitorSkeletonStr)
                val queries = ArrayList<Map<String, Any>>()
                for (i in 1..1000) {
                    val idx1 = Random.nextInt(sizes.size)
                    val idx2 = Random.nextInt(clientips.size)

                    val queryStr = "size:${sizes[idx1]} AND clientip:\"${clientips[idx2]}\""

                    val id = java.util.UUID.randomUUID().toString()
                    val query = HashMap<String, Any>()
                    query["id"] = id
                    query["name"] = id
                    query["query"] = queryStr
                    query["severity"] = "5"
                    query["tags"] = listOf("MITRE:8500")

                    queries.add(query)
                }
                (((monitor["inputs"] as ArrayList<Map<String, Any>>)[0]["doc_level_input"] as Map<String, Any>)["queries"] as ArrayList<Map<String, Any>>).addAll(
                    queries
                )
                val response = khttp.post(
                    "http://localhost:9200/_plugins/_alerting/monitors",
                    headers = mapOf(pair = Pair("Content-Type", "application/json")),
                    data = mapper.writeValueAsString(monitor)
                )
                println(response.statusCode)
                println(response.headers["Location"])
                Thread.sleep(5000)
//                println(mapper.writeValueAsString(monitor))
            }
        }
    }

    fun createMonitorType4() {
        val mapper = ObjectMapper()

        val sizes = ArrayList<Int>()
        val sizesSet = HashSet<Int>()

        val requests = ArrayList<String>()
        val requestSet = HashSet<String>()

        var counter = 0

        val path = RallyTrackDataClient::class.java.classLoader.getResource("documents-181998.json")?.path

        if (path != null) {
            val reader = BufferedReader(FileReader(path))
            var line = reader.readLine()

            while (line != null && counter < 100000) {
                val record = mapper.readValue<Map<String, Any>>(line)

                sizes.add(record["size"].toString().toInt())
                sizesSet.add(record["size"].toString().toInt())

                requests.add(record["request"].toString())
                requestSet.add(record["request"].toString())
                line = reader.readLine()
                counter += 1
            }

            for (j in 1..25) {
                val monitor = mapper.readValue<Map<String, Any>>(monitorSkeletonStr)
                val queries = ArrayList<Map<String, Any>>()
                for (i in 1..1000) {
                    val idx1 = Random.nextInt(sizes.size)
                    val idx2 = Random.nextInt(requests.size)

                    val queryStr = "size:${sizes[idx1]} AND request:\"${requests[idx2]}\""

                    val id = java.util.UUID.randomUUID().toString()
                    val query = HashMap<String, Any>()
                    query["id"] = id
                    query["name"] = id
                    query["query"] = queryStr
                    query["severity"] = "5"
                    query["tags"] = listOf("MITRE:8500")

                    queries.add(query)
                }
                (((monitor["inputs"] as ArrayList<Map<String, Any>>)[0]["doc_level_input"] as Map<String, Any>)["queries"] as ArrayList<Map<String, Any>>).addAll(
                    queries
                )
                val response = khttp.post(
                    "http://localhost:9200/_plugins/_alerting/monitors",
                    headers = mapOf(pair = Pair("Content-Type", "application/json")),
                    data = mapper.writeValueAsString(monitor)
                )
                println(response.statusCode)
                println(response.headers["Location"])
                Thread.sleep(5000)
//                println(mapper.writeValueAsString(monitor))
            }
        }
    }

    fun createMonitorType5() {
        val mapper = ObjectMapper()

        val statuses = ArrayList<Int>()
        val statusSet = HashSet<Int>()

        val sizes = ArrayList<Int>()
        val sizesSet = HashSet<Int>()

        val clientips = ArrayList<String>()
        val clientipSet = HashSet<String>()

        var counter = 0

        val path = RallyTrackDataClient::class.java.classLoader.getResource("documents-181998.json")?.path

        if (path != null) {
            val reader = BufferedReader(FileReader(path))
            var line = reader.readLine()

            while (line != null && counter < 100000) {
                val record = mapper.readValue<Map<String, Any>>(line)

                statuses.add(record["status"].toString().toInt())
                statusSet.add(record["status"].toString().toInt())

                sizes.add(record["size"].toString().toInt())
                sizesSet.add(record["size"].toString().toInt())

                clientips.add(record["clientip"].toString())
                clientipSet.add(record["clientip"].toString())
                line = reader.readLine()
                counter += 1
            }

            for (j in 1..25) {
                val monitor = mapper.readValue<Map<String, Any>>(monitorSkeletonStr)
                val queries = ArrayList<Map<String, Any>>()
                for (i in 1..1000) {
                    val idx1 = Random.nextInt(sizes.size)
                    val idx2 = Random.nextInt(clientips.size)
                    val idx4 = Random.nextInt(statuses.size)

                    val queryStr =
                        "size:${sizes[idx1]} AND clientip:\"${clientips[idx2]}\" AND status:${statuses[idx4]}"

                    val id = java.util.UUID.randomUUID().toString()
                    val query = HashMap<String, Any>()
                    query["id"] = id
                    query["name"] = id
                    query["query"] = queryStr
                    query["severity"] = "5"
                    query["tags"] = listOf("MITRE:8500")

                    queries.add(query)
                }
                (((monitor["inputs"] as ArrayList<Map<String, Any>>)[0]["doc_level_input"] as Map<String, Any>)["queries"] as ArrayList<Map<String, Any>>).addAll(
                    queries
                )
                val response = khttp.post(
                    "http://localhost:9200/_plugins/_alerting/monitors",
                    headers = mapOf(pair = Pair("Content-Type", "application/json")),
                    data = mapper.writeValueAsString(monitor)
                )
                println(response.statusCode)
                println(response.headers["Location"])
                Thread.sleep(5000)
//                println(mapper.writeValueAsString(monitor))
            }
        }
    }
}

fun main(args: Array<String>) {
    val monitorClient = RallyRandomMonitorClient()
    monitorClient.createMonitorType5()
}
