package ee.gaile.controller.blog;

import ee.gaile.repository.blog.BlogRepository;
import ee.gaile.repository.blog.CommentsRepository;
import ee.gaile.repository.entity.blog.Blog;
import ee.gaile.repository.entity.blog.Comments;
import ee.gaile.repository.entity.blog.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Controller
@RequestMapping
public class ArticleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);
    private final BlogRepository blogRepository;
    private final CommentsRepository commentsRepository;
    private List<Comments> listComments;
    private Map<UUID, String> imageMap;
    private Blog blog;

    @Autowired
    public ArticleController(BlogRepository blogRepository, CommentsRepository commentsRepository) {
        this.blogRepository = blogRepository;
        this.commentsRepository = commentsRepository;
    }

    @GetMapping("/article-blog/{articleId}")
    public String goToArticle(@PathVariable UUID articleId, Model model) {

        listComments = (List<Comments>) commentsRepository.findAll();

        Optional<Blog> optBlog = blogRepository.findById(articleId);
        blog = optBlog.get();

        imageMap = new HashMap<>();
        byte[] encode;

        encode = Base64.getEncoder().encode(blog.getImage());
        String getImage = new String(encode, StandardCharsets.UTF_8);
        imageMap.put(blog.getId(), getImage);

        model.addAttribute("listComments", listComments);
        model.addAttribute("images", imageMap);
        model.addAttribute("comments", new Comments());
        model.addAttribute("blog", blog);

        return "blog/article";
    }

    @PostMapping("/article")
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
        User user = (User) authentication.getPrincipal();

        comments.setComment(comments.getComment());
        comments.setUsername(user.getUsername());
        comments.setBlog(blog);
        commentsRepository.save(comments);

        String articleId = blog.getId().toString();

        return "redirect:/article-blog/" + articleId;
    }
}
