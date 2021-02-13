package ee.gaile.service.blog;

import ee.gaile.entity.blog.Blog;
import ee.gaile.repository.blog.BlogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Blogs saving service
 *
 * @author Aleksei Gaile
 */
@Service
@AllArgsConstructor
public class AdminBlogService {
    private final BlogRepository blogRepository;

    /**
     * Saves the blog to the database
     *
     * @param headline -Blog Title
     * @param article  - the content of the article
     * @param image    - image
     * @throws IOException - getBytes
     */
    public void saveBlog(String headline, String article, MultipartFile image) throws IOException {
        byte[] bytes = image.getBytes();
        Blog blog = new Blog(headline, article, bytes);
        blogRepository.save(blog);

    }
}
