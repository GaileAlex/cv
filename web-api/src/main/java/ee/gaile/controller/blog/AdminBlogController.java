package ee.gaile.controller.blog;

import ee.gaile.entity.blog.Blog;
import ee.gaile.entity.blog.Comments;
import ee.gaile.service.repository.blog.BlogRepository;
import ee.gaile.service.repository.blog.CommentsRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

@Slf4j
@RestController
@RequestMapping(API_V1_PREFIX + "/admin-blog")
public class AdminBlogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminBlogController.class);
    private final BlogRepository blogRepository;
    private final CommentsRepository commentsRepository;
    private List<Comments> listComments;

    public AdminBlogController(BlogRepository blogRepository, CommentsRepository commentsRepository) {
        this.blogRepository = blogRepository;
        this.commentsRepository = commentsRepository;
    }

    @PostMapping
    public Blog addBlog(@RequestBody Blog blog) {
        return blogRepository.save(blog);
    }


    @PostMapping("/delete-blog")
    public String deleteBlog(@RequestParam Long deleteBlog) {
        blogRepository.deleteById(deleteBlog);
        return "redirect:/admin-blog";
    }

    @PostMapping("/delete-comment")
    public String deleteComment(@RequestParam Long deleteComment) {
        commentsRepository.deleteById(deleteComment);
        return "redirect:/admin-blog";
    }
}

