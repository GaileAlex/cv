package ee.gaile.service.blog;

import ee.gaile.entity.blog.Blog;
import ee.gaile.entity.blog.BlogWrapper;
import ee.gaile.entity.blog.Comments;
import ee.gaile.entity.users.Users;
import ee.gaile.service.repository.blog.BlogRepository;
import ee.gaile.service.repository.blog.CommentsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class BlogService {
    private final BlogRepository blogRepository;
    private final CommentsRepository commentsRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public BlogService(BlogRepository blogRepository, CommentsRepository commentsRepository) {
        this.blogRepository = blogRepository;
        this.commentsRepository = commentsRepository;
    }

    public List<BlogWrapper> findAllBlogs() {
        List<Blog> blogs = blogRepository.findAllOrderByDate();
        List<BlogWrapper> blogWrappers = new ArrayList<>();
        for (Blog blog : blogs) {
            blogWrappers.add(toDto(blog));
        }
        return blogWrappers;
    }

    public BlogWrapper findBlogById(Long blogId) {
        Optional<Blog> blog = blogRepository.findById(blogId);
        return toDto(blog.get());
    }

    public void saveComment(String comment, Long blogId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) authentication.getPrincipal();
        Comments newComment = new Comments(comment, user.getUsername(), blogRepository.findById(blogId).get());
        commentsRepository.save(newComment);
    }

    private BlogWrapper toDto(Blog blog) {
        BlogWrapper blogWrapper = modelMapper.map(blog, BlogWrapper.class);

        StringBuilder base64 = new StringBuilder("data:image/png;base64,");
        base64.append(Base64.getEncoder().encodeToString(blog.getImage()));
        blogWrapper.setImage(base64.toString());
        return blogWrapper;
    }

}
