package ee.gaile.controller.librarian;

import ee.gaile.ApplicationIT;
import ee.gaile.configuration.BooksToRepoConfig;
import ee.gaile.models.librarian.FilterWrapper;
import ee.gaile.service.librarian.LibrarianService;
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
class LibrarianControllerTest extends ApplicationIT {
    @MockBean
    private LibrarianService searchService;

    @MockBean
    BooksToRepoConfig booksToRepoConfig;

    @Autowired
    private MockMvc mvc;

    @Test
    void checkFindAll() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(API_V1_PREFIX + "/librarian/find-all")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void checkGetBooksByFilter() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(API_V1_PREFIX + "/librarian/{condition}", 1)
                .content(asJsonString(new FilterWrapper()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
}
