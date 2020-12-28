package ee.gaile.controller.user;

import ee.gaile.ApplicationIT;
import ee.gaile.service.security.request.SignupRequest;
import ee.gaile.service.security.settings.AuthRefreshDTO;
import ee.gaile.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.servlet.http.HttpServletRequest;

import static ee.gaile.UtilsTests.asJsonString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UserControllerTest extends ApplicationIT {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    void checkGetUserLoginToken() throws Exception {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        SignupRequest signupRequest = SignupRequest.builder()
                .username("Test")
                .role("USER")
                .password("testUser")
                .email("test@test.com")
                .build();

        mvc.perform(MockMvcRequestBuilders
                .post("/api/auth/login")
                .content(asJsonString(signupRequest))
                .content(asJsonString(req))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void checkGetAccessTokenByRefreshToken() throws Exception {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        AuthRefreshDTO authRefreshDTO = new AuthRefreshDTO();

        mvc.perform(MockMvcRequestBuilders
                .post("/api/auth/refresh")
                .content(asJsonString(authRefreshDTO))
                .content(asJsonString(req))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void checkRegisterUser() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder()
                .username("Test")
                .role("USER")
                .password("testUser")
                .email("test@test.com")
                .build();

        mvc.perform(MockMvcRequestBuilders
                .post("/api/auth/register")
                .content(asJsonString(signupRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
}
