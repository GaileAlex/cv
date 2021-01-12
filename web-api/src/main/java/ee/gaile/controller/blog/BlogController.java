package ee.gaile.controller.blog;

import ee.gaile.models.blog.BlogWrapper;
import ee.gaile.models.blog.CommentWrapper;
import ee.gaile.service.blog.BlogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(API_V1_PREFIX + "/blog")
public class BlogController {
    BlogService blogService;

    @GetMapping
    public ResponseEntity<List<BlogWrapper>> findAll() {
        return new ResponseEntity<>(blogService.findAllBlogs(), HttpStatus.OK);
    }

    @GetMapping("/find-blog/{blogId}")
    public ResponseEntity<BlogWrapper> findBlogById(@PathVariable("blogId") Long blogId) {
        return new ResponseEntity<>(blogService.findBlogById(blogId), HttpStatus.OK);
    }

    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.OK)
    public void saveComment(@RequestBody CommentWrapper comment) {
        blogService.saveComment(comment);
    }

}
