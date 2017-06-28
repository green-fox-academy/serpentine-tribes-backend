package com.greenfox.tribesoflagopus.backend.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.greenfox.tribesoflagopus.backend.BackendApplication;
import com.greenfox.tribesoflagopus.backend.model.entity.User;
import com.greenfox.tribesoflagopus.backend.repository.BuildingRepository;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import com.greenfox.tribesoflagopus.backend.repository.LocationRepository;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
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
public class RegistrationControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  UserRepository testUserRepository;

  @Autowired
  KingdomRepository testKingdomRepository;

  @Autowired
  BuildingRepository testBuildingRepository;

  @Autowired
  LocationRepository testLocationRepository;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void registerUserWithAllParameter() throws Exception {
    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{"
                    + "\"username\" : \"TestUser\","
                    + "\"password\" : \"testUserPassword\","
                    + "\"kingdom\" : \"TestUserKingdom\""
                    + "}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.username").exists())
            .andExpect(jsonPath("$.kingdom_id").exists())
            .andDo(print());

    long kingdomId = testUserRepository.findByUsername("TestUser").getKingdom().getId();
    assertTrue(testUserRepository.existsByUsername("TestUser"));
    assertTrue(testKingdomRepository.existsById(kingdomId));
    assertTrue(testBuildingRepository.existsByTypeAndKingdomId("townhall", kingdomId));
    assertTrue(testLocationRepository.existsByKingdomId(kingdomId));

    testUserRepository.deleteByUsername("TestUser");
  }

  @Test
  public void registerUserWithoutKingdom() throws Exception {
    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{"
                    + "\"username\" : \"TestUserWithoutKingdom\","
                    + "\"password\" : \"TestUserWithoutKingdomPassword\""
                    + "}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.username").exists())
            .andExpect(jsonPath("$.kingdom_id").exists())
            .andDo(print());

    long kingdomId = testUserRepository.findByUsername("TestUserWithoutKingdom").getKingdom().getId();
    assertTrue(testUserRepository.existsByUsername("TestUserWithoutKingdom"));
    assertTrue(testKingdomRepository.existsById(kingdomId));
    assertTrue(testBuildingRepository.existsByTypeAndKingdomId("townhall", kingdomId));
    assertTrue(testLocationRepository.existsByKingdomId(kingdomId));

    testUserRepository.deleteByUsername("TestUserWithoutKingdom");
  }

  @Test
  public void registerErrorUserWithoutUserName() throws Exception {
    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{"
                    + "\"password\" : \"password123\""
                    + "}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("Missing parameter(s): username!")))
            .andDo(print());
  }

  @Test
  public void registerErrorUserWithoutPassword() throws Exception {
    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{"
                    + "\"username\" : \"Bond\""
                    + "}"))
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
            .andExpect(jsonPath("$.message", is("Missing input")))
            .andDo(print());
  }

  @Test
  public void registerErrorAlreadyExistingUser() throws Exception {
    User testUser = User.builder()
        .username("OccupiedTestUser")
        .password("testPasswordOfTestUser")
        .points(0)
        .build();

    testUserRepository.save(testUser);

    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{"
                    + "\"username\" : \"OccupiedTestUser\","
                    + "\"password\" : \"testPasswordOfTestUser\""
                    + "}"))
            .andExpect(status().is(409))
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message",
                    is("Username already taken, please choose an other one.")))
            .andDo(print());

    testUserRepository.delete(testUser);
  }
}


