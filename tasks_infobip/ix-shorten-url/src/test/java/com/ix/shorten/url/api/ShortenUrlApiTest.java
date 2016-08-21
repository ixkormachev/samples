package com.ix.shorten.url.api;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
//import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.ix.shorten.url.dao.ShortenUrlDao;
import com.ix.shorten.url.dto.RegisterRequest;
import com.ix.shorten.url.exception.ShortenUrlError;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BEAppConfiguration.class, BEWebConfiguration.class })
@ActiveProfiles("dev")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@Transactional

@WebAppConfiguration
public class ShortenUrlApiTest {
  protected MockMvc backEndMockMvc;

  @Autowired
  private WebApplicationContext backEndwebApplicationContext;

  @Autowired
  private FilterChainProxy springSecurityFilterChain;

  @Autowired
  private ShortenUrlDao dao;
  private final Logger logger = LoggerFactory.getLogger(ShortenUrlApiTest.class);

  @Before
  public final void initMockMvc() throws Exception, ShortenUrlError {
    backEndMockMvc = webAppContextSetup(backEndwebApplicationContext)
        .addFilters(springSecurityFilterChain).build();
  }

  @After
  public final void tearDown() throws Exception, ShortenUrlError {
  }

  @Test
  public void test_url_account_post_Then_created() throws ShortenUrlError, Exception {
    try {
      MvcResult result = backEndMockMvc.perform(post("/account")
          .contentType(MediaType.APPLICATION_JSON)
          .content("{ \"AccountId\" : \"myAccountId\"}"))
          .andExpect(status().isCreated()).andReturn();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void test_url_account_2posts_Then_found() throws ShortenUrlError, Exception {
    try {
      backEndMockMvc.perform(post("/account")
          .contentType(MediaType.APPLICATION_JSON)
          .content("{ \"AccountId\" : \"myAccountId\"}"))
          .andExpect(status().isCreated())
          .andExpect(content()
              .contentType("application/json"))
          .andExpect(jsonPath("description")
              .value("Your account is opened"));

      backEndMockMvc.perform(post("/account")
          .contentType(MediaType.APPLICATION_JSON)
          .content("{ \"AccountId\" : \"myAccountId\"}"))
          .andExpect(status().isFound())
          .andExpect(content()
              .contentType("application/json"))
          .andExpect(jsonPath("description")
              .value("Account with that ID already exists"));
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void registerUrl_when_wrong_token_Then_403() throws ShortenUrlError, Exception {
    RegisterRequest registerRequest = new RegisterRequest();
    registerRequest.setUrl(
        "url: \"http://stackoverflow.com/questions/1567929/website-safe-data-access-architecture-question?rq=1");
    String badAuthHeader = "Basic YWNjb3VudGlkMToxMjM0NTYBBB==";
//    String goodAuthHeader = "Basic YWNjb3VudGlkMToxMjM0NTY3OA==";
    String requestBody =
        "{\"url\":\"url: 'http://stackoverflow.com/questions/1567929/website-safe-data-access-architecture-question?rq\u003d1'\",\"redirectType\":301}";
    backEndMockMvc.perform(post("/register")
        .header("Authorization", badAuthHeader)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void registerUrl_register_post_Then_found() throws ShortenUrlError, Exception {
    RegisterRequest registerRequest = new RegisterRequest();
    registerRequest
        .setUrl("url: \"http://stackoverflow.com/questions/1567929/website-safe-data-access");
    registerRequest.setRedirectType(301);

    String username = "accountid1"; // Basic YWNjb3VudGlkMToxMjM0NTY3OA==
    String password = "12345678";
    String auth = username + ":" + password;
    byte[] encodedAuth = Base64.encodeBase64(
        auth.getBytes(Charset.forName("US-ASCII")));
    String authHeader = "Basic " + new String(encodedAuth);
    // set( "Authorization", authHeader );

    // authHeader = "Basic YWNjb3VudGlkMToxMjM0NTYBBB==";
    try {
      backEndMockMvc.perform(post("/register")
          .header("Authorization", authHeader)
          .contentType(MediaType.APPLICATION_JSON)
          .content(new Gson().toJson(registerRequest)))
          .andExpect(status().isOk());
      //// .andExpect(content()
      // .contentType("application/json"))
      // .andExpect(jsonPath("description")
      // .value("Your account is opened"));
      int i = 0;
    } catch (final Exception e) {
      e.printStackTrace();
    }
    int i = 0;
  }

  @Test
  @Ignore
  public void test_url_account_get() throws ShortenUrlError, Exception {
    try {
      final MvcResult mockServiceResult =
          backEndMockMvc.perform(get("/account")).andExpect(status().isOk())
              /* .andExpect(content().contentType("application/hal+json")) */.andReturn();
      // return
      // rsPagedResourcesConverter.convertMockServiceResult2Items(mockServiceResult
      // .getResponse().getContentAsString());
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @Ignore
  public void test_url_items_get() throws ShortenUrlError, Exception {
    try {
      final MvcResult mockServiceResult =
          backEndMockMvc.perform(post("/accounts")).andExpect(status().isOk())
              .andExpect(content().contentType("application/hal+json")).andReturn();
      // return
      // rsPagedResourcesConverter.convertMockServiceResult2Items(mockServiceResult
      // .getResponse().getContentAsString());
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
  // return null;
  // }
  // assertEquals("Cleanup things to remember:", items.get(0).getItemText());

  // @Test
  // public void test_url_items_get_1() throws Exception, BEError {
  // final Item item = itemService.findById(1L);
  // assertEquals("Cleanup things to remember:", item.getItemText());
  // }
  //
  // @Test
  // public void test_url_items_add() throws Exception {
  // final Item item = itemService.findById(1L);
  // itemService.add(item);
  //
  // }
  //
  // @Test
  // public void test_url_items_put() throws Exception {
  // final Item item = itemService.findById(1L);
  // item.setItemText("text has been updated");
  // itemService.update(item);
  // }
  //
  // @Test
  // public void test_url_items_delete() throws Exception {
  // itemService.delete(1L);
  // }
  //
  // @Test
  // public void test_url_items_findAllByPage() throws Exception, BEError {
  // Sort sort = null;
  // final String orderBy = "parentId,id";
  // sort = new Sort(Sort.Direction.DESC, orderBy);
  //
  // final Page<Item> itemPage = itemService.findAllByPage(new PageRequest(0, 2,
  // sort));
  // System.out.println("### itemPage lists: " + itemPage.getContent());
  // log("### items: ", itemPage.getContent());
  // }
  //
  // @Test
  // public void test_url_items_findByParentId() throws Exception, BEError {
  // final List<Item> items = itemService.findByParentId(1L);
  // log("### items: ", items);
  // }
  //
  // @Test
  // public void test_url_items_findByParentIdOrderedById() throws Exception,
  // BEError {
  // final List<Item> items = itemService.findByParentIdOrderedById(1L);
  // log("### items: ", items);
  // }
  //
  // @Test
  // public void test_url_items_findByParentIdOrderByIdAsc() throws Exception,
  // BEError {
  // final List<Item> items = itemService.findByParentId(1L);
  // log("### items: ", items);
  // }
  //
  // private void log(String header, Object obj) {
  // logger.info(header + obj);
  // }
}