package ru.webshop.backend.document

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Id
import org.springframework.data.elasticsearch.annotations.*

@Document(indexName = "products")
@JsonIgnoreProperties(ignoreUnknown = true)
data class ProductDocument(
    @Id
    @field:JsonProperty("id")
    @Field(type = FieldType.Long)
    val id: Long? = null,

    @Field(type = FieldType.Text, analyzer = "ru_text", searchAnalyzer = "ru_text")
    val name: String? = null,

    @field:JsonProperty("articleNumber")
    @Field(type = FieldType.Long)
    val articleNumber: Long? = null,

    @field:JsonProperty("balanceInStock")
    @Field(type = FieldType.Long)
    val balanceInStock: Long? = null,

    @field:JsonProperty("category")
    @Field(type = FieldType.Text)
    val category: String? = null,

    @field:JsonProperty("category_id")
    @Field(type = FieldType.Long)
    val categoryId: Long? = null,

    @field:JsonProperty("description")
    @Field(type = FieldType.Text)
    val description: String? = null,

    @field:JsonProperty("numberOfOrders")
    @Field(type = FieldType.Long)
    val numberOfOrders: Long? = null,

    @field:JsonProperty("price")
    @Field(type = FieldType.Long)
    val price: Long? = null,

    @field:JsonProperty("rating")
    @Field(type = FieldType.Long)
    val rating: Long? = null,

    @field:JsonProperty("totalReviews")
    @Field(type = FieldType.Long)
    val totalReviews: Long? = null,

    @field:JsonProperty("values")
    @Field(type = FieldType.Nested)
    val values: List<ProductValue> = emptyList(),

    @field:JsonProperty("reviews")
    @Field(type = FieldType.Object)
    val reviews: List<Any>? = null,

    @field:JsonProperty("photos")
    @Field(type = FieldType.Object)
    val photos: List<Any>? = null
) {
    data class ProductValue(
        @field:JsonProperty("id")
        @Field(type = FieldType.Long)
        val id: Long? = null,

        @field:JsonProperty("value")
        @Field(type = FieldType.Text)
        val value: String? = null
    )
}