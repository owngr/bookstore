package ch.wngr.bookstore.converters

import ch.wngr.bookstore.entities.Publisher
import ch.wngr.bookstore.models.Editor

fun Publisher.toEditor() = Editor(
    name = name,
    defaultDistributor = defaultDistributor.toString()
)