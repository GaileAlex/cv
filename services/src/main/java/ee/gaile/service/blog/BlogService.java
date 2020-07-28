package ee.gaile.service.blog;

import ee.gaile.entity.blog.Blog;
import ee.gaile.entity.blog.BlogWrapper;
import ee.gaile.service.repository.blog.BlogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import javax.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;

    public List<BlogWrapper> findAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        List<BlogWrapper> blogWrappers = new ArrayList<>();
        for (Blog blog : blogs) {
            blogWrappers.add(toDto(blog));
        }

        return blogWrappers;
    }

    private BlogWrapper toDto(Blog blog) {
        BlogWrapper blogWrapper = modelMapper.map(blog, BlogWrapper.class);

        StringBuilder base64 = new StringBuilder("data:image/png;base64,");
        base64.append(Base64.getEncoder().encodeToString(blog.getImage()));
        blogWrapper.setImage(base64.toString());
        return blogWrapper;
    }

}
