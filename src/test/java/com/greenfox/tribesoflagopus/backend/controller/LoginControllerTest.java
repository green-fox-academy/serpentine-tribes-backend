package com.greenfox.tribesoflagopus.backend.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.greenfox.tribesoflagopus.backend.BackendApplication;
import com.greenfox.tribesoflagopus.backend.service.TokenService;
import com.greenfox.tribesoflagopus.backend.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class)
@WebAppConfiguration
public class LoginControllerTest {

  public static final String
      MOCK_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJOb2VtaSJ9.sSmeKXCzvwc7jDmd5rkbNJHQyn4HGaFG2accPpDkcpc";

  private static final String TEST_USERNAME_EXISTING = "Test_Username";
  private static final String TEST_USERNAME_NOT_EXISTING = "Nonexistent_User";
  private static final String TEST_PASSWORD_CORRECT = "Test_Password1";
  private static final String TEST_PASSWORD_WRONG = "Wrong_Test_Password";

  @MockBean
  private TokenService tokenService;
  @MockBean
  private UserService userService;

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void loginWithAllCorrectParam() throws Exception {
    Mockito.when(userService.existsUserByUsername(TEST_USERNAME_EXISTING)).thenReturn(true);
    Mockito.when(userService.isPasswordCorrect(TEST_USERNAME_EXISTING, TEST_PASSWORD_CORRECT)).thenReturn(true);
    Mockito.when(tokenService.saveNewTokenToUser(TEST_USERNAME_EXISTING)).thenReturn(MOCK_TOKEN);

    mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{"
                    + "\"username\" : \"" + TEST_USERNAME_EXISTING + "\","
                    + "\"password\" : \"" + TEST_PASSWORD_CORRECT + "\""
                    + "}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("ok")))
            .andExpect(jsonPath("$.token").exists())
            .andDo(print());
  }

  @Test
  public void loginWithMissingUsername() throws Exception {

    mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{"
                    + "\"password\" : \"" + TEST_PASSWORD_CORRECT + "\""
                    + "}"))
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("Missing parameter(s): username!")))
            .andDo(print());
  }

  @Test
  public void loginWithMissingPassword() throws Exception {

    mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{"
                    + "\"username\" : \"" + TEST_USERNAME_EXISTING + "\""
                    + "}"))
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("Missing parameter(s): password!")))
            .andDo(print());
  }

  @Test
  public void loginWithAllParamMissing() throws Exception {

    mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("Missing input")))
            .andDo(print());
  }

  @Test
  public void loginWithWrongPassword() throws Exception {
    Mockito.when(userService.existsUserByUsername(TEST_USERNAME_EXISTING)).thenReturn(true);
    Mockito.when(userService.isPasswordCorrect(TEST_USERNAME_EXISTING, TEST_PASSWORD_WRONG)).thenReturn(false);

    mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{"
                    + "\"username\" : \"" + TEST_USERNAME_EXISTING + "\","
                    + "\"password\" : \"" + TEST_PASSWORD_WRONG + "\""
                    + "}"))
            .andExpect(status().is(401))
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("Wrong password")))
            .andDo(print());
  }

  @Test
  public void loginWithNonExistentUser() throws Exception {

    mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{"
                    + "\"username\" : \"" + TEST_USERNAME_NOT_EXISTING + "\","
                    + "\"password\" : \"" + TEST_PASSWORD_CORRECT + "\""
                    + "}"))
            .andExpect(status().is(401))
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("No such user: " + TEST_USERNAME_NOT_EXISTING)))
            .andDo(print());
  }
}
