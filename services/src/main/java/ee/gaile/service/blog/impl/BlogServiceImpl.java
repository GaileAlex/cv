package ee.gaile.service.blog.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.gaile.entity.blog.BlogEntity;
import ee.gaile.entity.blog.CommentEntity;
import ee.gaile.models.blog.BlogWrapper;
import ee.gaile.models.blog.CommentWrapper;
import ee.gaile.repository.blog.BlogRepository;
import ee.gaile.repository.blog.CommentsRepository;
import ee.gaile.service.blog.BlogService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final CommentsRepository commentsRepository;
    private final ObjectMapper modelMapper;

    @Override
    public List<BlogWrapper> findAllBlogs() {
        List<BlogEntity> blogs = blogRepository.findAllOrderByDate();
        List<BlogWrapper> blogWrappers = new ArrayList<>();
        for (BlogEntity blog : blogs) {
            blogWrappers.add(toDto(blog));
        }

        return blogWrappers;
    }

    @Override
    public BlogWrapper findBlogById(Long blogId) {
        BlogEntity blog = blogRepository.findBlogById(blogId);

        if (Objects.isNull(blog)) {
            return new BlogWrapper();
        }

        if (Objects.nonNull(blog.getComments())) {
            blog.getComments().sort(Comparator.comparing(CommentEntity::getDate).reversed());
        }

        return toDto(blog);
    }

    @Override
    public void saveComment(CommentWrapper comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        CommentEntity newComment = new CommentEntity(comment.getComment(), currentUserName,
                blogRepository.findById(comment.getBlogId()).orElseThrow(NoSuchElementException::new));

        commentsRepository.save(newComment);
    }

    /**
     * Adds a picture to a blog, translates a blog to a BlogWrapper
     *
     * @param blog - blog
     * @return - BlogWrapper
     */
    private BlogWrapper toDto(BlogEntity blog) {
        BlogWrapper blogWrapper = modelMapper.convertValue(blog, BlogWrapper.class);
        blogWrapper.setImage("data:image/png;base64," + Base64.getEncoder().encodeToString(blog.getImage()));

        return blogWrapper;
    }

}
