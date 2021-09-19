package ee.gaile.service.blog;

import ee.gaile.models.blog.BlogWrapper;
import ee.gaile.models.blog.CommentWrapper;

import java.util.List;

/**
 * @author Aleksei Gaile 18-Sep-21
 */
public interface BlogService {

    /**
     * Finds all blogs
     *
     * @return all blogs
     */
    List<BlogWrapper> findAllBlogs();

    /**
     * Finds blog bi ID
     *
     * @param blogId - blog ID
     * @return - blog
     */
    BlogWrapper findBlogById(Long blogId);

    /**
     * Saves blog comment
     *
     * @param comment - comment
     */
    void saveComment(CommentWrapper comment);

}
