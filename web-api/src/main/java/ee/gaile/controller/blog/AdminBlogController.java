package ee.gaile.controller.blog;

import ee.gaile.service.blog.AdminBlogService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

@Slf4j
@RestController
@RequestMapping(API_V1_PREFIX + "/admin-blog")
public class AdminBlogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminBlogController.class);
    private final AdminBlogService adminBlogService;

    public AdminBlogController(AdminBlogService adminBlogService) {
        this.adminBlogService = adminBlogService;
    }

    @PostMapping
    public void addBlog(@RequestParam("headline") String headline,
                        @RequestParam("article") String article,
                        @RequestParam("image") MultipartFile image) throws IOException {
        adminBlogService.saveBlog(headline, article, image);
    }

}

