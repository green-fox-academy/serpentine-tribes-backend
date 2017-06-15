package com.greenfox.tribesoflagopus.backend.controller;

import static org.junit.Assert.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.greenfox.tribesoflagopus.backend.BackendApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.http.MediaType;

/**
 * Created by K on 2017.06.14..
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class)
@WebAppConfiguration
public class RegistrationControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void registerUserWithAllParameter() throws Exception {
    mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .param("username", "Bond")
        .param("password", "password123")
        .param("kingdom", "MI6"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.username").exists())
        .andExpect(jsonPath("$.kingdomId").exists())
        .andDo(print());
  }

  @Test
  public void registerUserWithoutKingdom() throws Exception {
    mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .param("username", "Bond")
        .param("password", "password123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.username").exists())
        .andExpect(jsonPath("$.kingdomId").exists())
        .andDo(print());
  }

  @Test
  public void registerErrorUserWithoutUserName() throws Exception {
    mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .param("password", "password123"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing parameter(s): username!")))
        .andDo(print());
  }

  @Test
  public void registerErrorUserWithoutPassword() throws Exception {
    mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .param("username", "Bond"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing parameter(s): password!")))
        .andDo(print());
  }

  @Test
  public void registerErrorUserAllParametersMissing() throws Exception {
    mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing parameter(s): username, password!")))
        .andDo(print());
  }

  @Test
  public void registerErrorAlreadyExistingUser() throws Exception {
    mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .param("username", "occupiedUserName")
        .param("password", "password123"))
        .andExpect(status().is(409))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Username already taken, please choose an other one.")))
        .andDo(print());
  }
}


