package ee.gaile.controller.blog;

import ee.gaile.dto.blog.BlogWrapper;
import ee.gaile.dto.blog.CommentWrapper;
import ee.gaile.service.blog.BlogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<BlogWrapper> findAll() {
        return blogService.findAllBlogs();
    }

    @GetMapping("/find-blog/{blogId}")
    public BlogWrapper findBlogById(@PathVariable("blogId") Long blogId) {
        return blogService.findBlogById(blogId);
    }

    @PostMapping("/comments")
    public void saveComment(@RequestBody CommentWrapper comment) {
        blogService.saveComment(comment);
    }

}
