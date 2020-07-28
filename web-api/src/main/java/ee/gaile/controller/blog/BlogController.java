package ee.gaile.controller.blog;

import ee.gaile.entity.blog.Blog;
import ee.gaile.entity.blog.BlogWrapper;
import ee.gaile.service.blog.BlogService;
import ee.gaile.service.repository.blog.BlogRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
