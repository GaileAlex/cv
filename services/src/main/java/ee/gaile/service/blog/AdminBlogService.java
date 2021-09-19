package ee.gaile.service.blog;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Aleksei Gaile 18-Sep-21
 */
public interface AdminBlogService {

    /**
     * Saves the blog to the database
     *
     * @param headline -Blog Title
     * @param article  - the content of the article
     * @param image    - image
     * @throws IOException - getBytes
     */
    void saveBlog(String headline, String article, MultipartFile image) throws IOException;

}
