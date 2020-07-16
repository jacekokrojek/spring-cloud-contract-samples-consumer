package com.example;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@DirtiesContext
// @AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
// ids = "com.example:spring-cloud-contract-sample-producer:+:stubs:8090")
@AutoConfigureStubRunner(
    stubsMode = StubRunnerProperties.StubsMode.REMOTE,
    repositoryRoot = "git://https://github.com/jacekokrojek/spring-cloud-contract-samples-git.git",
    ids = "com.example:spring-cloud-contract-sample-producer:+:stubs:8090")
public class BeerControllerTest extends AbstractTest {

  @Autowired MockMvc mockMvc;

  @Test
  public void should_give_me_a_beer_when_im_old_enough_RestTemplate() throws Exception {

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> request =
        new HttpEntity<String>(this.json.write(new Person("marcin", 22)).getJson(), headers);

    String personResultAsJsonStr =
        restTemplate.postForObject("http://localhost:8090/check", request, String.class);
    BDDAssertions.then(personResultAsJsonStr).isEqualTo("{\"status\":\"OK\"}");
  }

  @Test
  public void should_give_me_a_beer_when_im_old_enough_MockMvc() throws Exception {

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post("/beer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.json.write(new Person("marcin", 22)).getJson()))
        .andExpect(status().isOk())
        .andExpect(content().string("{\"status\":\"OK\"}"));
  }
}
