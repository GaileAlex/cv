package ee.gaile.controller.blog;

import ee.gaile.ApplicationIT;
import ee.gaile.models.blog.CommentWrapper;
import ee.gaile.service.blog.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static ee.gaile.UtilsTests.asJsonString;
import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class BlogControllerTest extends ApplicationIT {
    @MockBean
    private BlogService blogService;

    @Autowired
    private MockMvc mvc;

    @Test
    void checkFindAll() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(API_V1_PREFIX + "/blog")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void checkFindBlogById() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(API_V1_PREFIX + "/blog/find-blog/{blogId}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void saveComment() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(API_V1_PREFIX + "/blog/comments")
                .content(asJsonString(new CommentWrapper("firstName4", 3L)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }


}
