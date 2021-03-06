package ee.gaile.controller.blog;

import ee.gaile.ApplicationIT;
import ee.gaile.service.blog.AdminBlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class AdminBlogControllerTest extends ApplicationIT {
    @MockBean
    private AdminBlogService adminBlogService;

    @Autowired
    private MockMvc mvc;

    @Test
    void checkAddBlog() throws Exception {
        MockMultipartFile image = new MockMultipartFile("data", "filename.txt", "text/plain", "image".getBytes());
        mvc.perform(MockMvcRequestBuilders
                .multipart(API_V1_PREFIX + "/admin-blog")
                .file("image", image.getBytes())
                .param("headline", "headline")
                .param("article", "article")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

}
