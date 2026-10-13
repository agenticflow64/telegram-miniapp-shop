package ru.webshop.backend.repository

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch.core.SearchRequest
import org.springframework.stereotype.Repository
import ru.webshop.backend.document.ProductDocument
import co.elastic.clients.elasticsearch._types.query_dsl.*
import co.elastic.clients.elasticsearch.core.SearchResponse
import co.elastic.clients.elasticsearch.core.search.Hit
import java.io.IOException
import java.util.stream.Collectors
@Repository
class ProductDocumentRepository(
    private val elasticsearchClient: ElasticsearchClient
){

    fun weightedSearch(query: String,
                       from: Int = 0,
                       size: Int = 10): List<ProductDocument> {

        val request = SearchRequest.of { s ->
            s.index("products")
                .from(from)
                .size(size)
                .query { q ->
                    q.bool { b ->
                        b.must { m ->
                            m.multiMatch {
                                it.query(query)
                                    .fields("name^5", "name.ngr^2", "values.value^2", "description")
                                    .fuzziness("AUTO")
                                    .prefixLength(1)
                                    .maxExpansions(50)
                            }
                        }
                    }
                }
        }

        val response = elasticsearchClient.search(request, ProductDocument::class.java)
        return response.hits().hits().mapNotNull { it.source() }
    }

}