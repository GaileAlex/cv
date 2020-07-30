package ee.gaile.service.blog;

import ee.gaile.entity.blog.Blog;
import ee.gaile.entity.blog.BlogWrapper;
import ee.gaile.entity.blog.CommentWrapper;
import ee.gaile.entity.blog.Comments;
import ee.gaile.service.repository.blog.BlogRepository;
import ee.gaile.service.repository.blog.CommentsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
        Blog blog = blogRepository.findBlogById(blogId);
        return toDto(blog);
    }

    public void saveComment(CommentWrapper comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Comments newComment = new Comments(comment.getComment(), currentUserName,
                blogRepository.findById(comment.getBlogId()).get());
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
