package com.greenfox.tribesoflagopus.backend.controller;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.greenfox.tribesoflagopus.backend.BackendApplication;
import com.greenfox.tribesoflagopus.backend.mockbuilder.MockBuildingListDtoBuilder;
import com.greenfox.tribesoflagopus.backend.repository.BuildingRepository;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import com.greenfox.tribesoflagopus.backend.service.BuildingService;
import com.greenfox.tribesoflagopus.backend.service.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class)
@WebAppConfiguration
public class BuildingControllerTest {

  public static final String TOKEN_INPUT_REQUEST_HEADER = "X-tribes-token";
  public static final String MOCK_TOKEN =
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

  @MockBean
  BuildingService mockBuildingService;

  @Autowired
  MockBuildingListDtoBuilder mockBuildingListDtoBuilder;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void getBuildingListWithValidUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(true);
    Mockito.when(mockBuildingService.getBuildingList(1L))
        .thenReturn(mockBuildingListDtoBuilder.build());
    mockMvc.perform(get("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.buildings[0].id", is(1)))
        .andExpect(jsonPath("$.buildings[0].type", is("townhall")))
        .andExpect(jsonPath("$.buildings[0].level", is(1)))
        .andExpect(jsonPath("$.buildings[0].hp", is(0)))
        .andExpect(jsonPath("$.buildings[1].id", is(2)))
        .andExpect(jsonPath("$.buildings[1].type", is("farm")))
        .andExpect(jsonPath("$.buildings[1].level", is(1)))
        .andExpect(jsonPath("$.buildings[1].hp", is(0)))
        .andDo(print());
  }

  @Test
  public void getBuildingListWithInValidUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(false);
    mockMvc.perform(get("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("UserId not found")))
        .andDo(print());
  }

  @Test
  public void addNewBuildingMissingBuildingType() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(true);
    mockMvc.perform(post("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"motvalidtype\" : \"notvalid\"" + "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing parameter(s): type!")))
        .andDo(print());
  }

  @Test
  public void addNewBuildingWithNoParameter() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    mockMvc.perform(post("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing input")))
        .andDo(print());
  }

  @Test
  public void addNewBuildingWithInValidUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(false);
    mockMvc.perform(post("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"type\" : \"farm\"" + "}"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("UserId not found")))
        .andDo(print());
  }

  @Test
  public void addNewBuildingWithInValidBuildingType() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockUserRepository.exists(1L)).thenReturn(true);
    mockMvc.perform(post("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"type\" : \"house\"" + "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Invalid building type!")))
        .andDo(print());
  }

  @Test
  public void updateBuildingWithNoParameter() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    mockMvc.perform(put("/kingdom/buildings/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing input")))
        .andDo(print());
  }


  @Test
  public void updateBuildingWithInValidBuildingIdAndUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockBuildingService.existsByBuildingIdAndUserId(1L, 1L)).thenReturn(false);
    mockMvc.perform(put("/kingdom/buildings/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"level\" : " + 2 + "}"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Id: 1 not found!")))
        .andDo(print());
  }

  @Test
  public void updateBuildingWithMissingBuildingLevel() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockBuildingService.existsByBuildingIdAndUserId(1L, 1L)).thenReturn(true);
    mockMvc.perform(put("/kingdom/buildings/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"notvalidtype\" : " + 2 + "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing parameter(s): level!")))
        .andDo(print());
  }

  @Test
  public void updateBuildingWithInvalidBuildingLevelAsInt() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockBuildingService.existsByBuildingIdAndUserId(1L, 1L)).thenReturn(true);
    mockMvc.perform(put("/kingdom/buildings/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"level\" : " + 0 + "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Invalid building level!")))
        .andDo(print());
  }
}