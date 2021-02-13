package ee.gaile.service.blog;

import ee.gaile.entity.blog.Blog;
import ee.gaile.entity.blog.Comments;
import ee.gaile.models.blog.BlogWrapper;
import ee.gaile.models.blog.CommentWrapper;
import ee.gaile.repository.blog.BlogRepository;
import ee.gaile.repository.blog.CommentsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Blog service
 *
 * @author Aleksei Gaile
 */
@Service
public class BlogService {
    private final BlogRepository blogRepository;
    private final CommentsRepository commentsRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public BlogService(BlogRepository blogRepository, CommentsRepository commentsRepository) {
        this.blogRepository = blogRepository;
        this.commentsRepository = commentsRepository;
    }

    /**
     * Finds all blogs
     *
     * @return all blogs
     */
    public List<BlogWrapper> findAllBlogs() {
        List<Blog> blogs = blogRepository.findAllOrderByDate();
        List<BlogWrapper> blogWrappers = new ArrayList<>();
        for (Blog blog : blogs) {
            blogWrappers.add(toDto(blog));
        }
        return blogWrappers;
    }

    /**
     * Finds blog bi ID
     *
     * @param blogId - blog ID
     * @return - blog
     */
    public BlogWrapper findBlogById(Long blogId) {
        Blog blog = blogRepository.findBlogById(blogId);
        if (blog.getComments() != null) {
            blog.getComments().sort(Comparator.comparing(Comments::getDate).reversed());
        }

        return toDto(blog);
    }

    /**
     * Saves blog comment
     *
     * @param comment - comment
     */
    public void saveComment(CommentWrapper comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Comments newComment = new Comments(comment.getComment(), currentUserName,
                blogRepository.findById(comment.getBlogId()).orElseThrow(NoSuchElementException::new));

        commentsRepository.save(newComment);
    }

    /**
     * Adds a picture to a blog, translates a blog to a BlogWrapper
     *
     * @param blog - blog
     * @return - BlogWrapper
     */
    private BlogWrapper toDto(Blog blog) {
        BlogWrapper blogWrapper = modelMapper.map(blog, BlogWrapper.class);
        blogWrapper.setImage("data:image/png;base64," + Base64.getEncoder().encodeToString(blog.getImage()));

        return blogWrapper;
    }

}
