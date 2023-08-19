package com.book.model

import com.google.gson.annotations.SerializedName

data class Book(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("author")
    val author: String = "",
    @SerializedName("price")
    val price: Float = 0.0f,
)