package com.greenfox.tribesoflagopus.backend.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.greenfox.tribesoflagopus.backend.BackendApplication;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class TroopControllerTest {

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
  public void listTroops_withExistingUserId() throws Exception {
    mockMvc.perform(get("/1/kingdom/troops"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.troops").exists())
        .andDo(print());
  }

  @Test
  public void listTroops_withNonExistentUserId() throws Exception {
    mockMvc.perform(get("/666/kingdom/troops"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("UserId not found")))
        .andDo(print());
  }

  @Test
  public void showOneTroop_withExistingIds() throws Exception {
    mockMvc.perform(get("/1/kingdom/troops/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.level").exists())
        .andExpect(jsonPath("$.hp").exists())
        .andExpect(jsonPath("$.attack").exists())
        .andExpect(jsonPath("$.defence").exists())
        .andDo(print());
  }

  @Test
  public void showOneTroop_withNonExistentUserId() throws Exception {
    mockMvc.perform(get("/666/kingdom/troops/1"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("<id> not found")))
        .andDo(print());
  }

  @Test
  public void showOneTroop_withNonExistentTroopId() throws Exception {
    mockMvc.perform(get("/1/kingdom/troops/666"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("<id> not found")))
        .andDo(print());
  }
}