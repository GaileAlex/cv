package ee.gaile.controller.blog;

import ee.gaile.service.repository.blog.BlogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

@Slf4j
@RestController
@RequestMapping(API_V1_PREFIX + "/blog")
public class BlogController {
    private final BlogRepository blogRepository;

    @Autowired
    public BlogController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @GetMapping
    public String findAll() {
        return "blog response";
    }

    /*@GetMapping
    public String goToBlog(Model model) {
        List<Blog> listBlog;
        Map<Long, String> imageMap;
        listBlog = blogRepository.findAll();
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
    }*/
}
