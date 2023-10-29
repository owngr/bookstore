package ch.wngr.bookstore.services

import ch.wngr.bookstore.converters.toTagDTO
import ch.wngr.bookstore.entities.Tag
import ch.wngr.bookstore.models.TagDto
import ch.wngr.bookstore.repositories.TagRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TagServiceImpl @Autowired constructor(
    val tagRepository: TagRepository,
) : TagService {

    override fun getTags(): ResponseEntity<List<TagDto>> {
        val tagList = tagRepository.findByOrderByMainDescNameAsc()
        val tagDtoList = tagList.map { tag: Tag -> tag.toTagDTO() }
        return ResponseEntity(tagDtoList, HttpStatus.OK)
    }

    override fun updateTag(tagDto: TagDto): TagDto {
        val tag = tagRepository.findById(tagDto.id).orElse(null)
        if (tag != null) {
            tag.name = tagDto.name
            tag.main = tagDto.main
            tagRepository.save(tag)
            return tag.toTagDTO()
        } else {
            println("The tag could not be found")
            throw Exception("The tag could not be found")
        }
    }

    override fun createTag(tagDto: TagDto): TagDto {
        val tag = Tag(name = tagDto.name, main = tagDto.main)
        return tagRepository.save(tag).toTagDTO()
    }

    override fun deleteTag(tagID: Int): ResponseEntity<TagDto> {
        val tag = tagRepository.findById(tagID).orElse(null)
        if (tag != null) {
            tagRepository.delete(tag)
            return ResponseEntity(tag.toTagDTO(), HttpStatus.OK)
        } else {
            println("Thetag could not be found")
            throw Exception("The tag could not be found")
        }
    }

}