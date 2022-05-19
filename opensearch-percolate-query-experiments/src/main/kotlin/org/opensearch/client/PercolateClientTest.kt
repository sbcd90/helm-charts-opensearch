package org.opensearch.client

import org.apache.http.HttpHost
import org.apache.http.message.BasicHeader
import org.apache.lucene.index.memory.MemoryIndex
import org.opensearch.action.bulk.BulkRequest
import org.opensearch.action.index.IndexRequest
import org.opensearch.action.search.MultiSearchRequest
import org.opensearch.action.search.MultiSearchResponse
import org.opensearch.action.search.SearchRequest
import org.opensearch.client.indices.CreateIndexRequest
import org.opensearch.client.indices.GetIndexRequest
import org.opensearch.client.indices.GetIndexResponse
import org.opensearch.client.indices.PutMappingRequest
import org.opensearch.common.bytes.BytesReference
import org.opensearch.common.settings.Settings
import org.opensearch.common.xcontent.XContentBuilder
import org.opensearch.common.xcontent.XContentFactory
import org.opensearch.common.xcontent.XContentType
import org.opensearch.index.query.BoolQueryBuilder
import org.opensearch.index.query.QueryBuilders
import org.opensearch.percolator.PercolateQueryBuilder
import org.opensearch.search.SearchHit
import org.opensearch.search.builder.SearchSourceBuilder
import java.util.Base64

class PercolateClientTest(host: String, port: Int,
                          credentials: Map<String, String>) {
    private val client = RestHighLevelClient(
        RestClient.builder(HttpHost(host, port, "http"))
            .setDefaultHeaders(arrayOf(BasicHeader("Authorization", "Basic " +
                    Base64.getEncoder().encodeToString((credentials["username"] + ":" + credentials["password"]).toByteArray())))))

    fun createIndex(indexName: String, noOfShards: Int, noOfReplicas: Int,
                            properties: Map<String, Map<String, Any>>, addPercolateSetting: Boolean): Boolean {
        val request = CreateIndexRequest(indexName)

        var settings = Settings.builder()
            .put("index.number_of_shards", noOfShards)
            .put("index.number_of_replicas", noOfReplicas)
            .put("index.hidden", true)
        if (addPercolateSetting) {
            settings = settings.put("index.percolator.map_unmapped_fields_as_text", true)
        }

        request.settings(settings)

        val mappings = mapOf<String, Any>("properties" to properties)
        request.mapping(mappings)

        val response = client.indices().create(request, RequestOptions.DEFAULT)
        return response.index().equals(indexName)
    }

    fun updateIndex(indexName: String, properties: Map<String, Map<String, Any>>): Boolean {
        val request = PutMappingRequest(indexName)

        val mappings = mapOf<String, Any>("properties" to properties)
        request.source(mappings)

        val response = client.indices().putMapping(request, RequestOptions.DEFAULT)
        return response.isAcknowledged
    }

    fun getIndex(indexName: String): GetIndexResponse {
        val request = GetIndexRequest(indexName)
        return client.indices().get(request, RequestOptions.DEFAULT)
    }

    fun insertData(indexName: String, data: List<Map<String, Map<String, Any>>>): Boolean {
        val request = BulkRequest()

        data.forEach {
            it.forEach{ record ->
                val indexRequest = IndexRequest(indexName).id(record.key)
                indexRequest.source(record.value)
/*                record.value.forEach { (t, u) ->
                    indexRequest.source(XContentType.JSON, t, u)
                }*/
                request.add(indexRequest)
            }
        }
        val response = client.bulk(request, RequestOptions.DEFAULT)
        return response.status().status == 200
    }

    fun insertData(indexName: String, id: String, data: Map<String, Any>): Boolean {
        val request = IndexRequest(indexName).id(id).source(data)

        val response = client.index(request, RequestOptions.DEFAULT)
        return response.status().status == 200
    }

    fun multiSearchIndex(indexName: String, ids: List<MutableMap<String, Any>>, percolateIndex: String): Map<String, List<Any?>> {
        /*val multiSearchRequest = MultiSearchRequest()
        ids.forEach { id ->
            val percolateQueryBuilder = PercolateQueryBuilder("query",
                percolateIndex, null, id, null, null, null)

            val searchRequest = SearchRequest(indexName)
            val searchSourceBuilder = SearchSourceBuilder()
            searchSourceBuilder.query(percolateQueryBuilder)
            searchRequest.source(searchSourceBuilder)

            multiSearchRequest.add(searchRequest)
        }*/
        val idsRef = ids.map { id ->
            var xContentBuilder = XContentFactory.jsonBuilder().startObject()
            id.forEach { (k, v) ->
                xContentBuilder = xContentBuilder.field(k, v)
            }
            xContentBuilder = xContentBuilder.endObject()
            BytesReference.bytes(xContentBuilder)
        }
        val percolateQueryBuilder = PercolateQueryBuilder("query", idsRef, XContentType.JSON);

        val searchRequest = SearchRequest(indexName)
        val searchSourceBuilder = SearchSourceBuilder()
        searchSourceBuilder.query(percolateQueryBuilder)
        searchRequest.source(searchSourceBuilder)

        val response = client.search(searchRequest, RequestOptions.DEFAULT)

        var resultsMap = HashMap<String, List<Any?>>()
        resultsMap["hit"] = response.hits.map { hit ->
            ((hit.sourceAsMap["query"] as HashMap<*, *>)["query_string"] as HashMap<*, *>)["query"]
        }
        return resultsMap
//        return response.responses
    }

    fun queryIndex(indexName: String, field: String, filters: Pair<Int, Int>): Array<SearchHit> {
        val searchRequest = SearchRequest(indexName)

        val boolQueryBuilder = BoolQueryBuilder()
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("_seq_no").gt(filters.first).lte(filters.second))

        val searchSourceBuilder = SearchSourceBuilder()
        searchSourceBuilder.query(boolQueryBuilder)
        searchRequest.source(searchSourceBuilder)

        val response = client.search(searchRequest, RequestOptions.DEFAULT)
        return response.hits.hits
    }

    fun close() {
        client.close()
    }
}



fun main(args: Array<String>) {
    val percolateClientTest = PercolateClientTest("127.0.0.1", 9200, mapOf("username" to "admin",
        "password" to "admin"))
    val brand = mapOf(Pair("type", "keyword"))
    val model = mapOf(Pair("type", "keyword"))
    val price = mapOf(Pair("type", "long"))

    val properties = mapOf<String, Map<String, Any>>("brand" to brand,
        "model" to model, "price" to price)

    val data = listOf(mapOf("1" to mapOf<String, Any>("brand" to "Tesla", "model" to "3", "price" to 60000)),
    mapOf("2" to mapOf<String, Any>("brand" to "BMW", "model" to "330e", "price" to 40000)),
    mapOf("4" to mapOf<String, Any>("brand" to "Mercedes", "model" to "E500", "price" to 50000)))

    percolateClientTest.createIndex("cars1", 2, 0, properties, false)

    val response = percolateClientTest.getIndex("cars1")

    var props1 = (response.mappings["cars1"]?.sourceAsMap?.get("properties")) as Map<String, Map<String, Any>>
    val query = mapOf(Pair("type", "percolator"))
    val props = mapOf(Pair("query", query))
    percolateClientTest.createIndex("percolator-queries1", 1, 0, props, false)
    percolateClientTest.updateIndex("percolator-queries1", props1)

    percolateClientTest.insertData("cars1", data)

    val percolateData = mapOf("query" to mapOf("query_string" to mapOf("query" to "brand:Tesla AND model:3 AND price:<=50000")))
    percolateClientTest.insertData("percolator-queries1", "tesla_model_3_alert", percolateData)

    val newData = listOf(mapOf("5" to mapOf<String, Any>("brand" to "Tesla", "model" to "3", "price" to 50000)))
    percolateClientTest.insertData("cars1", newData)

    val hits = percolateClientTest.queryIndex("cars1", "_id", Pair(-1, 20))
    val docs = hits.map { it.sourceAsMap }

    val results = percolateClientTest.multiSearchIndex("percolator-queries1", docs, "cars1")
    results.forEach { result ->
        println(result.key + " - " + result.value)
    }
    percolateClientTest.close()
    println("hit")
}