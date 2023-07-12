package ch.wngr.bookstore.services

import ch.wngr.bookstore.models.TagDto
import org.springframework.http.ResponseEntity

interface TagService {
    fun getTags(): ResponseEntity<List<TagDto>>
    fun updateTag(tagDto: TagDto): TagDto
    fun createTag(tagDto: TagDto): TagDto
    fun deleteTag(tagID: Int): ResponseEntity<TagDto>
}