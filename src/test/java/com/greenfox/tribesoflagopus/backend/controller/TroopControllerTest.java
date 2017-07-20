package com.greenfox.tribesoflagopus.backend.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.greenfox.tribesoflagopus.backend.BackendApplication;
import com.greenfox.tribesoflagopus.backend.mockbuilder.MockUpdatedTroopDtoBuilder;
import com.greenfox.tribesoflagopus.backend.model.dto.TroopDto;
import com.greenfox.tribesoflagopus.backend.model.dto.TroopListDto;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import com.greenfox.tribesoflagopus.backend.service.BuildingService;
import com.greenfox.tribesoflagopus.backend.service.TokenService;
import com.greenfox.tribesoflagopus.backend.service.TroopService;
import com.greenfox.tribesoflagopus.backend.service.UserService;
import java.sql.Timestamp;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class)
@WebAppConfiguration
public class TroopControllerTest {

  public static final String TOKEN_INPUT_REQUEST_HEADER = "X-tribes-token";
  public static final String
      MOCK_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJOb2VtaSJ9.sSmeKXCzvwc7jDmd5rkbNJHQyn4HGaFG2accPpDkcpc";
  public static final TroopDto TEST_TROOP_DTO_1 =
      TroopDto.builder().id(1L).level(1).hp(1).attack(1).defence(1).startedAt(new Timestamp(1500036357654L))
          .finishedAt(new Timestamp(0)).build();
  public static final TroopDto TEST_TROOP_DTO_2 =
      TroopDto.builder().id(1L).level(1).hp(1).attack(1).defence(1).startedAt(new Timestamp(1500036357654L))
          .finishedAt(new Timestamp(0)).build();
  public static final TroopListDto TEST_TROOP_DTO_LIST =
      TroopListDto.builder().troop(TEST_TROOP_DTO_1).troop(TEST_TROOP_DTO_2).build();

  @MockBean
  private TokenService mockTokenService;
  @MockBean
  private TroopService troopService;
  @MockBean
  private UserService userService;
  @MockBean
  private BuildingService mockBuildingService;

  private MockMvc mockMvc;
  private HttpMessageConverter mappingJackson2HttpMessageConverter;

  @Autowired
  MockUpdatedTroopDtoBuilder mockUpdatedTroopDtoBuilder;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  void setConverters(HttpMessageConverter<?>[] converters) {

    this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
        .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
        .findAny()
        .orElse(null);

    assertNotNull("the JSON message converter must not be null",
        this.mappingJackson2HttpMessageConverter);
  }

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void listTroops_withExistingUserId() throws Exception {
    Long testUserId = 1L;
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(testUserId);
    Mockito.when(userService.existsUserById(testUserId)).thenReturn(true);
    Mockito.when(troopService.listTroopsOfUser(testUserId)).thenReturn(TEST_TROOP_DTO_LIST);

    mockMvc.perform(get("/kingdom/troops")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.troops").exists())
        .andDo(print());
  }

  @Test
  public void listTroops_withNonExistentUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(666L);

    mockMvc.perform(get("/kingdom/troops")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("UserId not found")))
        .andDo(print());
  }

  @Test
  public void showOneTroop_withExistingIds() throws Exception {
    Long testUserId = 1L;
    Long testTroopId = 1L;
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(troopService.existsByIdAndUserId(testTroopId, testUserId)).thenReturn(true);
    Mockito.when(troopService.fetchTroop(testUserId, testTroopId)).thenReturn(TEST_TROOP_DTO_1);

    mockMvc.perform(get("/kingdom/troops/1").header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.level").exists())
        .andExpect(jsonPath("$.hp").exists())
        .andExpect(jsonPath("$.attack").exists())
        .andExpect(jsonPath("$.defence").exists())
        .andExpect(jsonPath("$.started_at").exists())
        .andExpect(jsonPath("$.finished_at").exists())
        .andDo(print());
  }

  @Test
  public void showOneTroop_withNonExistentTroopId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);

    mockMvc.perform(get("/kingdom/troops/666").header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Troop ID not found")))
        .andDo(print());
  }

  @Test
  public void createNewTroop() throws Exception {
    Long testUserId = 1L;
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(testUserId);
    Mockito.when(userService.existsUserById(testUserId)).thenReturn(true);
    Mockito.when(mockBuildingService.hasUserBuildingType(BuildingType.BARRACK, testUserId)).thenReturn(true);
    Mockito.when(troopService.hasEnoughGoldForNewTroop(testUserId)).thenReturn(true);
    Mockito.when(troopService.addNewTroop(testUserId)).thenReturn(TEST_TROOP_DTO_1);

    mockMvc.perform(post("/kingdom/troops")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.level").exists())
        .andExpect(jsonPath("$.hp").exists())
        .andExpect(jsonPath("$.attack").exists())
        .andExpect(jsonPath("$.defence").exists())
        .andExpect(jsonPath("$.started_at").exists())
        .andExpect(jsonPath("$.finished_at").exists())
        .andDo(print());
  }

  @Test
  public void createNewTroop_userHasNoBarrack() throws Exception {
    Long testUserId = 1L;
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(testUserId);
    Mockito.when(userService.existsUserById(testUserId)).thenReturn(true);
    Mockito.when(mockBuildingService.hasUserBuildingType(BuildingType.BARRACK, testUserId)).thenReturn(false);
    Mockito.when(troopService.hasEnoughGoldForNewTroop(testUserId)).thenReturn(true);

    mockMvc.perform(post("/kingdom/troops")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Without a barrack you can't have a troop!")))
        .andDo(print());
  }

  @Test
  public void createNewTroop_notEnoughGold() throws Exception {
    Long testUserId = 1L;
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(testUserId);
    Mockito.when(userService.existsUserById(testUserId)).thenReturn(true);
    Mockito.when(mockBuildingService.hasUserBuildingType(BuildingType.BARRACK, testUserId)).thenReturn(true);
    Mockito.when(troopService.hasEnoughGoldForNewTroop(testUserId)).thenReturn(false);

    mockMvc.perform(post("/kingdom/troops")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Not enough gold!")))
        .andDo(print());
  }

  @Test
  public void updateTroopWithValidInputs() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(troopService.existsByTroopIdAndUserId(1L, 1L)).thenReturn(true);
    Mockito.when(troopService.hasEnoughGoldForTroopUpgrade(1L)).thenReturn(true);
    Mockito.when(troopService.updateTroop(1L, 2))
        .thenReturn(mockUpdatedTroopDtoBuilder.build());
    mockMvc.perform(put("/kingdom/troops/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"level\" : " + 2 + "}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.level", is(2)))
        .andExpect(jsonPath("$.attack", is(2)))
        .andExpect(jsonPath("$.defence", is(2)))
        .andDo(print());
  }

  @Test
  public void updateTroopWithNoParameter() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    mockMvc.perform(put("/kingdom/troops/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing input")))
        .andDo(print());
  }

  @Test
  public void updateTroopWithInvalidTroopIdAndUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(troopService.existsByTroopIdAndUserId(1L, 1L)).thenReturn(false);
    mockMvc.perform(put("/kingdom/troops/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"level\" : " + 2 + "}"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Id: 1 not found!")))
        .andDo(print());
  }

  @Test
  public void updateTroopWithMissingTroopLevel() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(troopService.existsByTroopIdAndUserId(1L, 1L)).thenReturn(true);
    mockMvc.perform(put("/kingdom/troops/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"notvalidtype\" : " + 2 + "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing parameter(s): level!")))
        .andDo(print());
  }

  @Test
  public void updateTroopWithInvalidTroopLevelAsInt() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(troopService.existsByTroopIdAndUserId(1L, 1L)).thenReturn(true);
    mockMvc.perform(put("/kingdom/troops/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"level\" : " + 0 + "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Invalid troop level!")))
        .andDo(print());
  }

  @Test
  public void updateTroopWithNotEnoughGold() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);
    Mockito.when(troopService.existsByTroopIdAndUserId(1L, 1L)).thenReturn(true);
    Mockito.when(troopService.hasEnoughGoldForTroopUpgrade(1L)).thenReturn(false);
    mockMvc.perform(put("/kingdom/troops/1")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{" + "\"level\" : " + 2 + "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Not enough gold!")))
        .andDo(print());
  }

}