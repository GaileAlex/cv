package ee.gaile.service.blog;

import ee.gaile.entity.blog.Blog;
import ee.gaile.entity.blog.BlogWrapper;
import ee.gaile.service.repository.blog.BlogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class BlogService {
    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
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

    private BlogWrapper toDto(Blog blog) {
        BlogWrapper blogWrapper = modelMapper.map(blog, BlogWrapper.class);

        StringBuilder base64 = new StringBuilder("data:image/png;base64,");
        base64.append(Base64.getEncoder().encodeToString(blog.getImage()));
        blogWrapper.setImage(base64.toString());
        return blogWrapper;
    }

}
