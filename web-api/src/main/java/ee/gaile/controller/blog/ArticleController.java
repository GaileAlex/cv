package ee.gaile.controller.blog;

import ee.gaile.entity.blog.Blog;
import ee.gaile.entity.blog.Comments;
import ee.gaile.entity.users.Users;
import ee.gaile.service.repository.blog.BlogRepository;
import ee.gaile.service.repository.blog.CommentsRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

@Slf4j
@RestController
@RequestMapping(API_V1_PREFIX + "/article")
public class ArticleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);
    private final BlogRepository blogRepository;
    private final CommentsRepository commentsRepository;
    private List<Comments> listComments;
    private Map<Long, String> imageMap;
    private Blog blog;

    @Autowired
    public ArticleController(BlogRepository blogRepository, CommentsRepository commentsRepository) {
        this.blogRepository = blogRepository;
        this.commentsRepository = commentsRepository;
    }

    @GetMapping(API_V1_PREFIX + "/article-blog/{articleId}")
    public String goToArticle(@PathVariable Long articleId, Model model) {

        listComments = commentsRepository.findAll();

        Optional<Blog> optBlog = blogRepository.findById(articleId);
        blog = optBlog.get();

        imageMap = new HashMap<>();
        byte[] encode;


        model.addAttribute("listComments", listComments);
        model.addAttribute("images", imageMap);
        model.addAttribute("comments", new Comments());
        model.addAttribute("blog", blog);

        return "blog/article";
    }

    @PostMapping(API_V1_PREFIX + "/article")
    public String addComment(@Valid Comments comments, Errors errors,
                             Authentication authentication, Model model) {

        if (errors.hasErrors()) {
            if (comments.getComment().isEmpty()) {
                LOGGER.warn("no comments");
                model.addAttribute("listComments", listComments);
                model.addAttribute("images", imageMap);
                model.addAttribute("blog", blog);
                return "blog/article";
            }
        }
        Users user = (Users) authentication.getPrincipal();


        commentsRepository.save(comments);

        String articleId = blog.getId().toString();

        return "redirect:/article-blog/" + articleId;
    }
}

