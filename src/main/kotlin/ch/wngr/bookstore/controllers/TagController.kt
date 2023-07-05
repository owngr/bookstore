package ch.wngr.bookstore.controllers

import ch.wngr.bookstore.models.TagDto
import ch.wngr.bookstore.services.TagService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tag")
class TagController @Autowired constructor(
    private val tagService: TagService,
) {
    @GetMapping("")
    fun getTags(): ResponseEntity<List<TagDto>> {
        return tagService.getTags()
    }

    @PutMapping("")
    fun updateTag(@RequestBody tag: TagDto): TagDto {
        return tagService.updateTag(tag)
    }

    @PostMapping("")
    fun addTag(@RequestBody tag: TagDto): TagDto {
        return tagService.createTag(tag)
    }
}