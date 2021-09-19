/*
package ee.gaile.service.blog;

import ee.gaile.ApplicationIT;
import ee.gaile.models.blog.BlogWrapper;
import ee.gaile.models.blog.CommentWrapper;
import ee.gaile.entity.blog.BlogEntity;
import ee.gaile.repository.blog.BlogRepository;
import ee.gaile.repository.blog.CommentsRepository;
import org.apache.poi.xssf.model.Comments;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BlogServiceTest extends ApplicationIT {

    private List<BlogEntity> blogs;

    @Autowired
    private BlogServiceImpl blogService;

    @Autowired
    private AdminBlogService adminBlogService;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @BeforeEach
    void setUp() throws IOException {
        String headline = "testHeadline";
        String article = "textArticle";
        MockMultipartFile image = new MockMultipartFile("image", "image", null, "image".getBytes());

        adminBlogService.saveBlog(headline, article, image);

        blogs = blogRepository.findAllOrderByDate();
    }

    @AfterEach
    void tearDown() {
        blogRepository.delete(blogs.get(0));
    }

    @Test
    void checkSaveBlog_and_findAllBlogs() {
        List<BlogWrapper> blogWrappers = blogService.findAllBlogs();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(blogs.get(0).getHeadline()).isEqualTo("testHeadline");
            softly.assertThat(blogs.get(0).getArticle()).isEqualTo("textArticle");
            softly.assertThat(blogs.get(0).getImage()).isEqualTo("image".getBytes());
            softly.assertThat(blogWrappers.get(0).getHeadline()).isEqualTo("testHeadline");
            softly.assertThat(blogWrappers.get(0).getArticle()).isEqualTo("textArticle");
            softly.assertThat(blogWrappers.get(0).getImage()).isEqualTo("data:image/png;base64,aW1hZ2U=");
        });

    }

    @Test
    void findBlogById() {
        BlogWrapper blogWrapper = blogService.findBlogById(blogs.get(0).getId());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(blogs.get(0).getHeadline()).isEqualTo("testHeadline");
            softly.assertThat(blogs.get(0).getArticle()).isEqualTo("textArticle");
            softly.assertThat(blogs.get(0).getImage()).isEqualTo("image".getBytes());
            softly.assertThat(blogWrapper.getHeadline()).isEqualTo("testHeadline");
            softly.assertThat(blogWrapper.getArticle()).isEqualTo("textArticle");
            softly.assertThat(blogWrapper.getImage()).isEqualTo("data:image/png;base64,aW1hZ2U=");
        });

    }

    @Test
    void saveComment() {
        CommentWrapper commentWrapper = new CommentWrapper("comment", blogs.get(0).getId());
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("Test");

        blogService.saveComment(commentWrapper);

        List<Comments> comments = commentsRepository.findAll();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(blogs.get(0).getHeadline()).isEqualTo("testHeadline");
            softly.assertThat(blogs.get(0).getArticle()).isEqualTo("textArticle");
            softly.assertThat(blogs.get(0).getImage()).isEqualTo("image".getBytes());
            softly.assertThat(comments.get(0).getComment()).isEqualTo("comment");
            softly.assertThat(comments.get(0).getUserName()).isEqualTo("Test");
        });

    }
}
*/
