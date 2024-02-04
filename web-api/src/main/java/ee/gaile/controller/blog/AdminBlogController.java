package ee.gaile.controller.blog;

import ee.gaile.service.blog.AdminBlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * REST service controller for saving new blog posts
 *
 * @author Aleksei Gaile
 */
@RestController
@RequestMapping("/admin-blog")
@RequiredArgsConstructor
@Tag(name = "AdminBlogController", description = "Controller for saving blogs")
public class AdminBlogController {
    private final AdminBlogService adminBlogService;

    @PostMapping
    @Operation(summary = "Blog saving service")
    public void addBlog(@RequestParam("headline") String headline,
                        @RequestParam("article") String article,
                        @RequestParam("image") MultipartFile image) throws IOException {
        adminBlogService.saveBlog(headline, article, image);
    }

}

