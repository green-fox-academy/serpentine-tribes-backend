package com.greenfox.tribesoflagopus.backend.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.greenfox.tribesoflagopus.backend.BackendApplication;
import com.greenfox.tribesoflagopus.backend.mockbuilder.MockBuildingListBuilder;
import com.greenfox.tribesoflagopus.backend.mockbuilder.MockKingdomBuilder;
import com.greenfox.tribesoflagopus.backend.repository.BuildingRepository;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import com.greenfox.tribesoflagopus.backend.service.TokenService;
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

  public static final String TOKEN_INPUT_REQUEST_HEADER = "X-tribes-token";
  public static final String mockToken =
      "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJOb2VtaSJ9.sSmeKXCzvwc7jDmd5rkbNJHQyn4HGaFG2accPpDkcpc";


  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  TokenService mockTokenService;

  @MockBean
  UserRepository mockUserRepository;

  @MockBean
  BuildingRepository mockBuildingRepository;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void getBuildingListWithInValidUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(mockToken)).thenReturn(1L);
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(false);
    mockMvc.perform(get("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, mockToken))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("UserId not found")))
        .andDo(print());
  }

  @Test
  public void addNewBuildingMissingBuildingType() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(mockToken)).thenReturn(1L);
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(true);
    mockMvc.perform(post("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, mockToken)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"motvalidtype\" : \"notvalid\"" + "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing parameter(s): type!")))
        .andDo(print());
  }

  @Test
  public void addNewBuildingWithNoParameter() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(mockToken)).thenReturn(1L);
    mockMvc.perform(post("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, mockToken))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing input")))
        .andDo(print());
  }

  @Test
  public void addNewBuildingWithInValidUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(mockToken)).thenReturn(1L);
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(false);
    mockMvc.perform(post("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, mockToken)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"type\" : \"farm\"" + "}"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("UserId not found")))
        .andDo(print());
  }

  @Test
  public void addNewBuildingWithInValidBuildingType() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(mockToken)).thenReturn(1L);
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(true);
    mockMvc.perform(post("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, mockToken)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"type\" : \"house\"" + "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Invalid building type!")))
        .andDo(print());
  }

  @Test
  public void updateBuildingWithNoParameter() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(mockToken)).thenReturn(1L);
    mockMvc.perform(put("/kingdom/buildings/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, mockToken))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing input")))
        .andDo(print());
  }

  @Test
  public void updateBuildingWithInValidBuildingId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(mockToken)).thenReturn(1L);
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(true);
    Mockito.when(mockBuildingRepository.exists(1L)).thenReturn(false);
    mockMvc.perform(put("/kingdom/buildings/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, mockToken)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"level\" : " + 2 + "}"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Id: 1 not found!")))
        .andDo(print());
  }

  @Test
  public void updateBuildingWithInValidUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(mockToken)).thenReturn(1L);
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(false);
    Mockito.when(mockBuildingRepository.exists(1L)).thenReturn(true);
    mockMvc.perform(put("/kingdom/buildings/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, mockToken)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"level\" : " + 2 + "}"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Id: 1 not found!")))
        .andDo(print());
  }

  @Test
  public void updateBuildingWithMissingBuildingLevel() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(mockToken)).thenReturn(1L);
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(true);
    Mockito.when(mockBuildingRepository.exists(1L)).thenReturn(true);
    mockMvc.perform(put("/kingdom/buildings/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, mockToken)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"notvalidtype\" : " + 2 + "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing parameter(s): level!")))
        .andDo(print());
  }

  @Test
  public void updateBuildingWithInvalidBuildingLevelAsInt() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(mockToken)).thenReturn(1L);
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(true);
    Mockito.when(mockBuildingRepository.exists(1L)).thenReturn(true);
    mockMvc.perform(put("/kingdom/buildings/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, mockToken)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"level\" : " + 0 + "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Invalid building level!")))
        .andDo(print());
  }
}