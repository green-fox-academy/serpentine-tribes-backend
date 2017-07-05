package com.greenfox.tribesoflagopus.backend.controller;

import static org.junit.Assert.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.greenfox.tribesoflagopus.backend.BackendApplication;
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

  @MockBean
  TokenService mockTokenService;

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
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);

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
    Mockito.when(mockTokenService.getIdFromToken(MOCK_TOKEN)).thenReturn(1L);

    mockMvc.perform(put("/kingdom")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{"
            + "\"name\" : \"MI5\","
            + "\"location\" : "
            + "{"
            + "\"x\" : 1,"
            + "\"y\" : 1"
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

    mockMvc.perform(put("/kingdom")
        .header(TOKEN_INPUT_REQUEST_HEADER, MOCK_TOKEN)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content("{"
            + "\"name\" : \"MI5\","
            + "\"location\" : "
            + "{"
            + "\"x\" : 1,"
            + "\"y\" : 1"
            + "}"
            + "}"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("user_id not found")))
        .andDo(print());
  }
}