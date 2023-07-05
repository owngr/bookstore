package ch.wngr.bookstore.converters

import ch.wngr.bookstore.entities.Tag
import ch.wngr.bookstore.models.TagDto

fun Tag.toTagDTO() = TagDto(
    id = id,
    name = name,
    main = main,
)