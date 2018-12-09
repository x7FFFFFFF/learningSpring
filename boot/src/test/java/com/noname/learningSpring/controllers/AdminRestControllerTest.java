package com.noname.learningSpring.controllers;

import com.noname.learningSpring.security.WithMockCustomUser;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class AdminRestControllerTest {
    @LocalServerPort
    int randomServerPort;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpHeaders headers = new HttpHeaders();

    @Test
    @WithMockCustomUser(username = "anon", password = "1",  role = "ROLE_ANON", priveleges = {"GET /*"})
    public void testLogin() throws JSONException {
        Map<String,String> map = new HashMap<>();
        map.put("userName","");
        map.put("password","");

        HttpEntity<Map> entity = new HttpEntity<Map>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/login"),
                HttpMethod.POST, entity, String.class);

        String expected = "{id:Course1,name:Spring,description:10 Steps}";

        JSONAssert.assertEquals(expected, response.getBody(), false);

    }


    private String createURLWithPort(String uri) {
        return "http://localhost:" + randomServerPort + uri;
    }
}