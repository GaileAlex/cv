package ee.gaile.service.blog;

import ee.gaile.entity.blog.Blog;
import ee.gaile.repository.blog.BlogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@AllArgsConstructor
public class AdminBlogService {
    private final BlogRepository blogRepository;

    public void saveBlog(String headline, String article, MultipartFile image) throws IOException {
        byte[] bytes = image.getBytes();
        Blog blog = new Blog(headline, article, bytes);
        blogRepository.save(blog);

    }
}
