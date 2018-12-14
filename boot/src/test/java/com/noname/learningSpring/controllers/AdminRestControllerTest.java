package com.noname.learningSpring.controllers;

import com.noname.learningSpring.Constants;
import com.noname.learningSpring.security.WithMockCustomUser;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
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

    @Autowired
    Constants constants;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpHeaders headers = new HttpHeaders();

    @Test
    @WithMockCustomUser(username = "anon", password = "1", role = "ROLE_ANON", priveleges = {"GET /*"})
    public void testLoginForbidden() throws JSONException {
        Map<String, String> map = new HashMap<>();
        map.put("userName", "");
        map.put("password", "");

        HttpEntity<Map> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("login"),
                HttpMethod.POST, entity, String.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        String expected = "{\"result\":null,\"errors\":[{\"code\":\"401\",\"message\":\"Unauthorized\"}]}";
        JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.LENIENT);

    }


    @Test
    @WithMockCustomUser(username = "manager1", password = "123", role = "ROLE_MANAGER", priveleges = {"GET /*", "GET /admin/*", "#*"})
    public void testLoginSuccessful() throws JSONException {
        Map<String, String> map = new HashMap<>();
        map.put("userName", "manager1");
        map.put("password", "123");
        HttpEntity<Map> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("login"),
                HttpMethod.POST, entity, String.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        JSONAssert.assertEquals("{\"result\":{\"name\":\"manager1\",\"roles\":[\"ROLE_MANAGER\"]},\"errors\":[]}", response.getBody(), JSONCompareMode.LENIENT);
    }


    private String createURLWithPort(String uri) {
        return "http://localhost:" + randomServerPort + constants.apiEntryPoint + uri;
    }
}