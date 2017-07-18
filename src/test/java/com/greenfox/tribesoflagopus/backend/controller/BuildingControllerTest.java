package com.greenfox.tribesoflagopus.backend.controller;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.greenfox.tribesoflagopus.backend.BackendApplication;
import com.greenfox.tribesoflagopus.backend.mockbuilder.MockBuildingDtoBuilder;
import com.greenfox.tribesoflagopus.backend.mockbuilder.MockBuildingListDtoBuilder;
import com.greenfox.tribesoflagopus.backend.mockbuilder.MockUpdatedBuildingDtoBuilder;
import com.greenfox.tribesoflagopus.backend.service.BuildingService;
import com.greenfox.tribesoflagopus.backend.service.TokenService;
import com.greenfox.tribesoflagopus.backend.service.UserService;
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
  UserService mockUserService;

  @MockBean
  BuildingService mockBuildingService;

  @Autowired
  MockBuildingListDtoBuilder mockBuildingListDtoBuilder;

  @Autowired
  MockBuildingDtoBuilder mockBuildingDtoBuilder;

  @Autowired
  MockUpdatedBuildingDtoBuilder mockUpdatedBuildingDtoBuilder;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void getBuildingListWithValidUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockUserService.existsUserById(1L)).thenReturn(true);
    Mockito.when(mockBuildingService.getBuildingList(1L))
        .thenReturn(mockBuildingListDtoBuilder.build());
    mockMvc.perform(get("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.buildings[0].id", is(1)))
        .andExpect(jsonPath("$.buildings[0].type", is("townhall")))
        .andExpect(jsonPath("$.buildings[0].level", is(1)))
        .andExpect(jsonPath("$.buildings[0].hp", is(0)))
        .andExpect(jsonPath("$.buildings[0].started_at").exists())
        .andExpect(jsonPath("$.buildings[0].finished_at").exists())
        .andExpect(jsonPath("$.buildings[1].id", is(2)))
        .andExpect(jsonPath("$.buildings[1].type", is("farm")))
        .andExpect(jsonPath("$.buildings[1].level", is(1)))
        .andExpect(jsonPath("$.buildings[1].hp", is(0)))
        .andExpect(jsonPath("$.buildings[0].started_at").exists())
        .andExpect(jsonPath("$.buildings[0].finished_at").exists())
        .andDo(print());
  }

  @Test
  public void getBuildingListWithInvalidUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockUserService.existsUserById(1L)).thenReturn(false);
    mockMvc.perform(get("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("UserId not found")))
        .andDo(print());
  }

  @Test
  public void addNewBuildingWithValidInputs() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockUserService.existsUserById(1L)).thenReturn(true);
    Mockito.when(mockBuildingService.validBuildingType("farm")).thenReturn(true);
    Mockito.when(mockBuildingService.userHasEnoughGold(1L)).thenReturn(true);
    Mockito.when(mockBuildingService.addNewBuilding("farm", 1L))
        .thenReturn(mockBuildingDtoBuilder.build());
    mockMvc.perform(post("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"type\" : \"farm\"" + "}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.type", is("farm")))
        .andExpect(jsonPath("$.level", is(1)))
        .andExpect(jsonPath("$.hp", is(0)))
        .andExpect(jsonPath("$.started_at").exists())
        .andExpect(jsonPath("$.finished_at").exists())
        .andDo(print());
  }

  @Test
  public void addNewBuildingMissingBuildingType() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockUserService.existsUserById(1L)).thenReturn(true);
    mockMvc.perform(post("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"notvalidtype\" : \"notvalid\"" + "}"))
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
  public void addNewBuildingWithInvalidUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockUserService.existsUserById(1L)).thenReturn(false);
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
  public void addNewBuildingWithInvalidBuildingType() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockUserService.existsUserById(1L)).thenReturn(true);
    Mockito.when(mockBuildingService.validBuildingType("farm")).thenReturn(false);
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
  public void addNewBuildingWithNotEnoughGold() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockUserService.existsUserById(1L)).thenReturn(true);
    Mockito.when(mockBuildingService.validBuildingType("farm")).thenReturn(true);
    Mockito.when(mockBuildingService.userHasEnoughGold(1L)).thenReturn(false);
    mockMvc.perform(post("/kingdom/buildings")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"type\" : \"farm\"" + "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Not enough gold!")))
        .andDo(print());
  }

  @Test
  public void updateBuildingWithValidInputs() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockBuildingService.existsByBuildingIdAndUserId(1L, 1L)).thenReturn(true);
    Mockito.when(mockBuildingService.updateBuilding(1L, 2))
        .thenReturn(mockUpdatedBuildingDtoBuilder.build());
    mockMvc.perform(put("/kingdom/buildings/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"level\" : " + 2 + "}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.type", is("farm")))
        .andExpect(jsonPath("$.level", is(2)))
        .andExpect(jsonPath("$.hp", is(0)))
        .andExpect(jsonPath("$.started_at").exists())
        .andExpect(jsonPath("$.finished_at").exists())
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
  public void updateBuildingWithInvalidBuildingIdAndUserId() throws Exception {
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

  @Test
  public void getIndividualBuildingWithValidBuildingIdAndUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockBuildingService.existsByBuildingIdAndUserId(2L, 1L)).thenReturn(true);
    Mockito.when(mockBuildingService.getBuildingData(2L))
        .thenReturn(mockBuildingDtoBuilder.build());
    mockMvc.perform(get("/kingdom/buildings/2")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.type", is("farm")))
        .andExpect(jsonPath("$.level", is(1)))
        .andExpect(jsonPath("$.hp", is(0)))
        .andExpect(jsonPath("$.started_at").exists())
        .andExpect(jsonPath("$.finished_at").exists())
        .andDo(print());
  }

  @Test
  public void getIndividualBuildingWithInvalidBuildingIdAndUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(mockBuildingService.existsByBuildingIdAndUserId(1L, 1L)).thenReturn(false);
    mockMvc.perform(get("/kingdom/buildings/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Id: 1 not found!")))
        .andDo(print());
  }
}
