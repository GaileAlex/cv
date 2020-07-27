package ee.gaile.controller.blog;

import ee.gaile.entity.blog.Comments;
import ee.gaile.service.blog.AdminBlogService;
import ee.gaile.service.repository.blog.BlogRepository;
import ee.gaile.service.repository.blog.CommentsRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

@Slf4j
@RestController
@RequestMapping(API_V1_PREFIX + "/admin-blog")
public class AdminBlogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminBlogController.class);
    private final BlogRepository blogRepository;
    private final CommentsRepository commentsRepository;
    private final AdminBlogService adminBlogService;
    private List<Comments> listComments;

    public AdminBlogController(BlogRepository blogRepository, CommentsRepository commentsRepository,
                               AdminBlogService adminBlogService) {
        this.blogRepository = blogRepository;
        this.commentsRepository = commentsRepository;
        this.adminBlogService = adminBlogService;
    }

    @PostMapping
    public void addBlog(@RequestParam("headline") String headline,
                        @RequestParam("article") String article,
                        @RequestParam("image") MultipartFile image) throws IOException {
        adminBlogService.saveBlog(headline, article, image);
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

