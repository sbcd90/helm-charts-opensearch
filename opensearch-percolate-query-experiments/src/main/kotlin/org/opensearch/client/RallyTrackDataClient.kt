package org.opensearch.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.http.HttpHost
import org.opensearch.action.index.IndexRequest
import org.opensearch.client.indices.CreateIndexRequest
import org.opensearch.common.settings.Settings
import org.opensearch.common.xcontent.XContentType
import java.io.BufferedReader
import java.io.FileReader

class RallyTrackDataClient(host: String, port: Int, indexName: String, mappingFile: String, dataFile: String) {
    private val esIndex = indexName
    private val esMappingFile = mappingFile
    private val esDataLoc = dataFile

    private val client = RestHighLevelClient(RestClient.builder(HttpHost(host, port, "http")))

    companion object {
        fun indexMappings(mappingFile: String): String {
            return RallyTrackDataClient::class.java.classLoader.getResource(mappingFile).readText()
        }
    }

    fun createIndex(noOfShards: Int, noOfReplicas: Int): Boolean {
        val request = CreateIndexRequest(esIndex)

        var settings = Settings.builder()
            .put("index.number_of_shards", noOfShards)
            .put("index.number_of_replicas", noOfReplicas)
            .put("index.hidden", true)

        request.settings(settings)

        request.mapping(indexMappings(esMappingFile), XContentType.JSON)

        val response = client.indices().create(request, RequestOptions.DEFAULT)
        return response.index().equals(esIndex)
    }

    fun readDataFromFileAndSend(): Boolean {
        val path = RallyTrackDataClient::class.java.classLoader.getResource(esDataLoc)?.path

        if (path != null) {
            val reader = BufferedReader(FileReader(path))
            var line = reader.readLine()

            while (line != null) {
                println(line)
                line = reader.readLine()
                val mapper = ObjectMapper()

                val indexRequest = IndexRequest(esIndex).id(java.util.UUID.randomUUID().toString())
                indexRequest.source(mapper.readValue<Map<String, Any>>(line))

                try {
                    client.index(indexRequest, RequestOptions.DEFAULT)
                } catch (ex: Exception) {

                }

            }
            reader.close()
        }
        return true
    }

    fun close() {
        client.close()
    }
}

fun main(args: Array<String>) {
    val trackDataClient = RallyTrackDataClient("127.0.0.1", 9200, "http-logs", "http-logs.json", "documents-181998.json")
    trackDataClient.createIndex(2, 2)
    trackDataClient.readDataFromFileAndSend()
    trackDataClient.close()
}