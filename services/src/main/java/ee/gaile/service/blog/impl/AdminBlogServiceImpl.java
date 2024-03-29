package ee.gaile.service.blog.impl;

import ee.gaile.entity.blog.BlogEntity;
import ee.gaile.repository.blog.BlogRepository;
import ee.gaile.service.blog.AdminBlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Blogs saving service
 *
 * @author Aleksei Gaile
 */
@Service
@RequiredArgsConstructor
public class AdminBlogServiceImpl implements AdminBlogService {
    private final BlogRepository blogRepository;

    @Override
    public void saveBlog(String headline, String article, MultipartFile image) throws IOException {
        byte[] bytes = image.getBytes();
        BlogEntity blog = new BlogEntity(headline, article, bytes);
        blogRepository.save(blog);
    }

}
