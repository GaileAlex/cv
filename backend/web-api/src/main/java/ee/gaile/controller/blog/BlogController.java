package ee.gaile.controller.blog;

import ee.gaile.repository.blog.BlogRepository;
import ee.gaile.repository.entity.blog.Blog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/blog")
public class BlogController {
    private final BlogRepository blogRepository;

    @Autowired
    public BlogController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @GetMapping
    public String goToBlog(Model model) {
        List<Blog> listBlog;
        Map<UUID, String> imageMap;
        listBlog = (List<Blog>) blogRepository.findAll();
        Collections.reverse(listBlog);

        imageMap = new HashMap<>();
        byte[] encode;

        for (Blog blog : listBlog) {
            encode = Base64.getEncoder().encode(blog.getImage());
            String getImage = new String(encode, StandardCharsets.UTF_8);
            imageMap.put(blog.getId(), getImage);
        }
        model.addAttribute("images", imageMap);
        model.addAttribute("listBlog", listBlog);

        return "blog/blog";
    }
}
