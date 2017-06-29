package com.greenfox.tribesoflagopus.backend.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.greenfox.tribesoflagopus.backend.BackendApplication;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import com.greenfox.tribesoflagopus.backend.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class)
@WebAppConfiguration
public class BuildingControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  UserRepository mockUserRepository;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void getBuildingListWithInValidId() throws Exception {
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(false);
    mockMvc.perform(get("/1/kingdom/buildings"))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("UserId not found")))
        .andDo(print());
  }
}