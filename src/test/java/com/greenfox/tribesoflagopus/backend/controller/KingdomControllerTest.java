package com.greenfox.tribesoflagopus.backend.controller;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.greenfox.tribesoflagopus.backend.BackendApplication;
import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.dto.KingdomDto;
import com.greenfox.tribesoflagopus.backend.model.dto.KingdomInputModifyDto;
import com.greenfox.tribesoflagopus.backend.model.dto.LocationDto;
import com.greenfox.tribesoflagopus.backend.model.dto.TroopDto;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import com.greenfox.tribesoflagopus.backend.service.KingdomService;
import com.greenfox.tribesoflagopus.backend.service.UserService;
import java.util.Arrays;

import com.greenfox.tribesoflagopus.backend.service.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class)
@WebAppConfiguration
public class KingdomControllerTest {

  public static final String TOKEN_INPUT_REQUEST_HEADER = "X-tribes-token";
  public static final String
      MOCK_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJOb2VtaSJ9.sSmeKXCzvwc7jDmd5rkbNJHQyn4HGaFG2accPpDkcpc";

  public static final long TEST_USER_ID = 1L;

  public static final String TEST_KINGDOM_NAME = "London";
  public static final String TEST_KINGDOM_MODIFIED_NAME = "MI5";

  public static final LocationDto TEST_LOCATION_DTO = LocationDto.builder().x(1).y(1).build();
  public static final LocationDto TEST_MODIFIED_LOCATION_DTO = LocationDto.builder().x(5).y(5)
      .build();

  public static final KingdomInputModifyDto TEST_INPUT_MODIFY_KINGDOM_DTO =
      KingdomInputModifyDto.builder()
          .name(TEST_KINGDOM_MODIFIED_NAME)
          .location(TEST_MODIFIED_LOCATION_DTO)
          .build();

  public static final KingdomDto TEST_KINGDOM_DTO =
      KingdomDto.builder()
          .id(1L)
          .name(TEST_KINGDOM_NAME)
          .userId(TEST_USER_ID)
          .building(BuildingDto.builder().id(1L).type(BuildingType.TOWNHALL).level(1).hp(1).build())
          .building(BuildingDto.builder().id(2L).type(BuildingType.FARM).level(2).hp(2).build())
          .troop(TroopDto.builder().id(1L).level(1).hp(1).attack(1).defence(1).build())
          .troop(TroopDto.builder().id(2L).level(2).hp(1).attack(1).defence(1).build())
          .location(TEST_LOCATION_DTO)
          .build();

  public static final KingdomDto TEST_MODIFIED_KINGDOM_DTO =
      TEST_KINGDOM_DTO.toBuilder()
          .name(TEST_KINGDOM_MODIFIED_NAME)
          .location(TEST_MODIFIED_LOCATION_DTO)
          .build();

  @MockBean
  private TokenService mockTokenService;
  @MockBean
  private KingdomService kingdomService;
  @MockBean
  private UserService userService;

  private MockMvc mockMvc;
  private HttpMessageConverter mappingJackson2HttpMessageConverter;


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
  public void showKingdom_getKnownExistingKingdom() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(TEST_USER_ID);
    Mockito.when(userService.existsUserById(TEST_USER_ID)).thenReturn(true);
    Mockito.when(kingdomService.createKingdomDto(TEST_USER_ID)).thenReturn(TEST_KINGDOM_DTO);

    mockMvc.perform(get("/kingdom")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").exists())
        .andExpect(jsonPath("$.user_id").exists())
        .andExpect(jsonPath("$.buildings").exists())
        .andExpect(jsonPath("$.resources").exists())
        .andExpect(jsonPath("$.troops").exists())
        .andExpect(jsonPath("$").value(hasKey("location")))
        .andDo(print());
  }

  @Test
  public void showKingdom_withNonExistentUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(666L);

    mockMvc.perform(get("/kingdom")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("user_id not found")))
        .andDo(print());
  }

  @Test
  public void modifyKingdom_withValidRequestFields() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(TEST_USER_ID);
    Mockito.when(userService.existsUserById(TEST_USER_ID)).thenReturn(true);
    Mockito.when(kingdomService.createModifiedKingdomDto(eq(TEST_USER_ID),
        eq(TEST_INPUT_MODIFY_KINGDOM_DTO))).thenReturn(TEST_MODIFIED_KINGDOM_DTO);

    mockMvc.perform(put("/kingdom")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{"
            + "\"name\" : \"" + TEST_INPUT_MODIFY_KINGDOM_DTO.getName() + "\","
            + "\"location\" : "
            + "{"
            + "\"x\" : " + String.valueOf(TEST_MODIFIED_LOCATION_DTO.getX()) + ","
            + "\"y\" : " + String.valueOf(TEST_MODIFIED_LOCATION_DTO.getY())
            + "}"
            + "}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").exists())
        .andExpect(jsonPath("$.user_id").exists())
        .andExpect(jsonPath("$.buildings").exists())
        .andExpect(jsonPath("$.resources").exists())
        .andExpect(jsonPath("$.troops").exists())
        .andExpect(jsonPath("$").value(hasKey("location")))
        .andDo(print());
  }

  @Test
  public void modifyKingdom_withNonExistentUserId() throws Exception {
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(666L);
    Mockito.when(userService.existsUserById(TEST_USER_ID)).thenReturn(true);
    Mockito
        .when(kingdomService.createModifiedKingdomDto(TEST_USER_ID, TEST_INPUT_MODIFY_KINGDOM_DTO))
        .thenReturn(TEST_MODIFIED_KINGDOM_DTO);

    mockMvc.perform(put("/kingdom")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{"
            + "\"name\" : \"" + TEST_KINGDOM_MODIFIED_NAME + "\","
            + "\"location\" : "
            + "{"
            + "\"x\" : " + TEST_MODIFIED_LOCATION_DTO.getX() + ","
            + "\"y\" : " + TEST_MODIFIED_LOCATION_DTO.getY()
            + "}"
            + "}"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("user_id not found")))
        .andDo(print());
  }
}