package com.github.arpan.kafka.client

import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.protocol.types.Field
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.XContentType
import org.slf4j.LoggerFactory
import java.io.IOException

data class ElasticSearchAuth(
        val hostName: String,
        val isAuthenticated: Boolean = false,
        val userName: String = "",
        val password: String = ""
)

class TwitterElasticSearchClient(private val elasticSearchAuth: ElasticSearchAuth) {

    private val logger = LoggerFactory.getLogger(TwitterElasticSearchClient::class.java)

    private val esClient: RestHighLevelClient = create()

    private fun create(): RestHighLevelClient {
        val credentialsProvider: BasicCredentialsProvider? =
                if (elasticSearchAuth.isAuthenticated) {
                    BasicCredentialsProvider().apply {
                        setCredentials(
                                AuthScope.ANY,
                                UsernamePasswordCredentials(elasticSearchAuth.userName, elasticSearchAuth.password)
                        )
                    }
                } else null

        val httpHost = if (elasticSearchAuth.isAuthenticated) HttpHost(elasticSearchAuth.hostName, 443, "https")
        else HttpHost(elasticSearchAuth.hostName, 9200, "http")

        val restClientBuilder = if (elasticSearchAuth.isAuthenticated) {
            RestClient.builder(httpHost)
                    .setHttpClientConfigCallback { httpClientBuilder ->
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                    }
        } else RestClient.builder(httpHost)

        return RestHighLevelClient(restClientBuilder)
    }

    fun dumpKafkaRecordsToEs(index: String, records: ConsumerRecords<String, String>) {
        val bulkRequest = createBulkRequest(index, records)
        val bulkResponse = esClient.bulk(bulkRequest, RequestOptions.DEFAULT)
        bulkResponse.forEach { bulkItemResponse ->
            logger.info("Indexed record with ID: ${bulkItemResponse.id} at index ${bulkItemResponse.index}")
        }
    }

    private fun createRequest(index: String, id: String, message: String): IndexRequest =
            IndexRequest(index).id(id).source(message, XContentType.JSON)

    private fun createBulkRequest(index: String, records: ConsumerRecords<String, String>): BulkRequest {
        val bulkRequest = BulkRequest()

        records.forEach { record ->
            val id = record.key()
            val message = record.value()
            try {
                logger.info("Received record from Topic: ${record.topic()} from Partition: ${record.partition()} and " +
                        "Offset: ${record.offset()} at Timestamp: ${record.timestamp()}")
                bulkRequest.add(createRequest(index, id, message))
            } catch (exception: IOException) {
                logger.error("Error indexing record with ID: $id :", exception)
            }
        }
        return bulkRequest
    }

    fun close() = esClient.close()
}