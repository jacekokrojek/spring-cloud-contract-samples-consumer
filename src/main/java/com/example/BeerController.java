package com.example;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;

enum ResponseStatus {
  OK,
  NOT_OK
}

/** @author Marcin Grzejszczak */
@RestController
class BeerController {

  private final RestTemplate restTemplate;

  int port = 8090;

  BeerController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @RequestMapping(
      method = RequestMethod.POST,
      value = "/beer",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public String gimmeABeer(@RequestBody Person person) {
    ResponseEntity<String> response =
        this.restTemplate.exchange(
            RequestEntity.post(URI.create("http://localhost:" + this.port + "/check"))
                .contentType(APPLICATION_JSON)
                .body(person),
            String.class);


    return response.getBody() != null ? response.getBody() : EMPTY;
  }

}

class Person {
  public String name;
  public int age;

  public Person(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public Person() {}
}

class Response {
  public ResponseStatus status;
}
