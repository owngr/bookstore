package ch.wngr.bookstore.models

import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    val id: Int,
    val main: Boolean,
    val name: String,
)
