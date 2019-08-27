package ee.gaile.CV.Blog.web;

import ee.gaile.CV.Blog.model.Blog;
import ee.gaile.CV.Blog.model.Comments;
import ee.gaile.CV.Blog.postgresql.BlogRepository;
import ee.gaile.CV.Blog.postgresql.CommentsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/admin-blog")
public class AdminBlogController {

    private final BlogRepository blogRepository;
    private final CommentsRepository commentsRepository;

    private List<Comments> listComments;

    @Autowired
    public AdminBlogController(BlogRepository blogRepository, CommentsRepository commentsRepository) {
        this.blogRepository = blogRepository;
        this.commentsRepository = commentsRepository;
    }

    @GetMapping
    public String addFormBlog(Model model) {

        listComments = (List<Comments>) commentsRepository.findAll();

        List<Blog> listBlog = (List<Blog>) blogRepository.findAll();
        Collections.reverse(listBlog);

        Map<Long, String> imageMap=new HashMap<>();
        byte[] encode;

        for (Blog blog : listBlog) {

            encode = Base64.getEncoder().encode(blog.getImage());
            String getImage=new String(encode, StandardCharsets.UTF_8);
            imageMap.put(blog.getId(), getImage);
        }

        model.addAttribute("listBlog", listBlog);
        model.addAttribute("image", imageMap);
        model.addAttribute("blog", new Blog());
        model.addAttribute("listComments", listComments);
        return "blog/admin-blog";
    }

    @PostMapping
    public String addBlog(@RequestParam("image") MultipartFile image,@Valid Blog blog, Errors errors) throws IOException {

        byte[] bytes = image.getBytes();
        blog.setImage(bytes);

        blog.setDate(new Date());

        if (errors.hasErrors()) {
            if(blog.getHeadline().isEmpty()||blog.getArticle().isEmpty()){

                return "blog/admin-blog";
            }else

                blogRepository.save(blog);
        }

        return "redirect:/admin-blog";

    }
    @PostMapping("/delete-blog")
    public String deleteBlog(@RequestParam Long deleteBlog){
        blogRepository.deleteById(deleteBlog);
        return "redirect:/admin-blog";
    }

    @PostMapping("/delete-comment")
    public String deleteComment(@RequestParam Long deleteComment){
        commentsRepository.deleteById(deleteComment);
        return "redirect:/admin-blog";
    }



}
