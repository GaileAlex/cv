package ee.gaile.controller.blog;

import ee.gaile.models.blog.BlogWrapper;
import ee.gaile.models.blog.CommentWrapper;
import ee.gaile.service.blog.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST service controller for fetching blog data and saving article comments
 *
 * @author Aleksei Gaile
 */
@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
@Tag(name = "BlogController", description = "Blogging controller")
public class BlogController {
    private final BlogService blogService;

    @GetMapping
    @Operation(summary = "Service for displaying blogs")
    public ResponseEntity<List<BlogWrapper>> findAll() {
        return new ResponseEntity<>(blogService.findAllBlogs(), HttpStatus.OK);
    }

    @GetMapping("/find-blog/{blogId}")
    @Operation(summary = "Service for finding a blog by id")
    public ResponseEntity<BlogWrapper> findBlogById(@PathVariable("blogId") Long blogId) {
        return new ResponseEntity<>(blogService.findBlogById(blogId), HttpStatus.OK);
    }

    @PostMapping("/comments")
    @Operation(summary = "Service for saving a comment in a blog")
    public void saveComment(@RequestBody CommentWrapper comment) {
        blogService.saveComment(comment);
    }

}
