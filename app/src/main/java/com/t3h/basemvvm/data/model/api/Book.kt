package com.t3h.basemvvm.data.model.api

import java.io.Serializable

data class Book(
    val author: String,
    val id: String,
    val image: String,
    val intro: String,
    val mp3: String,
    val title: String,
val coverImage: String
    ): Serializable