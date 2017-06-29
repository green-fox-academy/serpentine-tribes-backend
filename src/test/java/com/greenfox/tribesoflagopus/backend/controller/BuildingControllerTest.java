package com.greenfox.tribesoflagopus.backend.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.greenfox.tribesoflagopus.backend.BackendApplication;
import com.greenfox.tribesoflagopus.backend.mockbuilder.MockBuildingListBuilder;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
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

  @MockBean
  KingdomRepository mockKingdomRepository;

  @Autowired
  MockBuildingListBuilder mockBuildingListBuilder;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  /*
  @Test
  public void getBuildingListWithValidUserId() throws Exception {
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(true);
    Mockito.when(mockKingdomRepository.findOneByUserId(1L).getBuildings())
        .thenReturn(mockBuildingListBuilder.build());
    mockMvc.perform(get("/1/kingdom/buildings")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").exists())
        .andExpect(jsonPath("$[0].type", is("townhall")))
        .andExpect(jsonPath("$[0].level", is(1)))
        .andExpect(jsonPath("$[0].hp", is(0)))
        .andExpect(jsonPath("$[0].id").exists())
        .andExpect(jsonPath("$[0].type", is("farm")))
        .andExpect(jsonPath("$[0].level", is(1)))
        .andExpect(jsonPath("$[0].hp", is(0)));
  }*/

  @Test
  public void getBuildingListWithInValidUserId() throws Exception {
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(false);
    mockMvc.perform(get("/1/kingdom/buildings"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("UserId not found")))
        .andDo(print());
  }

  /*
  @Test
  public void addNewBuildingMissingBuildingType() throws Exception {
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(true);
    mockMvc.perform(post("/1/kingdom/buildings")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{"
            + "\"notValidFieldName\" : \"notValidInput\","
            + "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing parameter(s): type!")))
        .andDo(print());
  }*/

  @Test
  public void addNewBuildingWithNoParameter() throws Exception {
    mockMvc.perform(post("/1/kingdom/buildings"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing input")))
        .andDo(print());
  }

  @Test
  public void addNewBuildingWithInValidUserId() throws Exception {
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(false);
    mockMvc.perform(post("/1/kingdom/buildings")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"type\" : \"farm\"" + "}"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("UserId not found")))
        .andDo(print());
  }

}