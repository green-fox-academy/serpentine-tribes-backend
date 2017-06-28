package com.greenfox.tribesoflagopus.backend.controller;

import static org.junit.Assert.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.greenfox.tribesoflagopus.backend.BackendApplication;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.User;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class)
@WebAppConfiguration
public class LoginControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  UserRepository testUserRepository;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void loginWithAllCorrectParam() throws Exception {

    mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{"
                    + "\"username\" : \"Noemi\","
                    + "\"password\" : \"passnoemi\""
                    + "}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.username").exists())
            .andExpect(jsonPath("$.token").exists())
            .andDo(print());
  }

  @Test
  public void loginWithMissingUsername() throws Exception {

    mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{"
                    + "\"password\" : \"TestPassword\""
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
                    + "\"username\" : \"TestUser\""
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
  public void loginWithInCorrectPassword() throws Exception {

    mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{"
                    + "\"username\" : \"Noemi\","
                    + "\"password\" : \"passno\""
                    + "}"))
            .andExpect(status().is(401))
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("Wrong password")))
            .andDo(print());
  }

  @Test
  public void loginWithIncorrectUser() throws Exception {

    mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{"
                    + "\"username\" : \"No\","
                    + "\"password\" : \"passnoemi\""
                    + "}"))
            .andExpect(status().is(401))
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("No such user: No")))
            .andDo(print());
  }
}
