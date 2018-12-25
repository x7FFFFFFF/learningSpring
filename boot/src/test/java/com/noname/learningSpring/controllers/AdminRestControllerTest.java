package com.noname.learningSpring.controllers;

import com.noname.learningSpring.Constants;
import com.noname.learningSpring.security.WithMockCustomUser;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class AdminRestControllerTest {

    /*   static {
           System.setProperty(SecurityContextHolder.SYSTEM_PROPERTY, SecurityContextHolder.MODE_GLOBAL);
       }
   */
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
///response.getHeaders().get("Set-Cookie")
        JSONAssert.assertEquals("{\"result\":{\"name\":\"manager1\",\"roles\":[\"ROLE_MANAGER\"]},\"errors\":[]}", response.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    @WithMockCustomUser(username = "anon", password = "1", role = "ROLE_ANON", priveleges = {"GET /*"})
    public void testGetRolesAnon() {
        Map<String, String> req = new HashMap<>();
        req.put("page", "1");
        req.put("limit", "100");
        HttpEntity<Map> entity = new HttpEntity<>(req, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("roles"),
                HttpMethod.GET, entity, String.class);
        System.out.println("response = " + response);
        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }

    @Test
    @WithMockCustomUser(username = "manager1", password = "123", role = "ROLE_MANAGER",
            priveleges = {"GET /*", "GET /admin/*", "GET /api/v1/**", "#*"})
    public void testGetRolesManger() throws JSONException {
        final String cookie = login("manager1", "123");
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("roles"))
                .queryParam("page", "0")
                .queryParam("size", "100");

        HttpEntity<Map> entity = new HttpEntity<>(getHeaders(cookie));
        ResponseEntity<String> response = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET, entity, String.class);
        System.out.println("response = " + response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals("{\"result\":{\"content\":[{\"name\":\"ROLE_ANONYMOUS\",\"parent\":null,\"privileges\":[\"GET /*/\"]},{\"name\":\"ROLE_MANAGER\",\"parent\":null,\"privileges\":[\"GET /*\",\"GET /admin/*\",\"GET /api/v1/**\",\"#*\"]}],\"pageable\":{\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"pageSize\":100,\"pageNumber\":0,\"offset\":0,\"paged\":true,\"unpaged\":false},\"totalPages\":1,\"totalElements\":2,\"last\":true,\"first\":true,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"numberOfElements\":2,\"size\":100,\"number\":0,\"empty\":false},\"errors\":[]}"
                , response.getBody(), JSONCompareMode.LENIENT);

    }

    private Map<String, String> createRequest(String... args) {
        int counter = 1;
        Map<String, String> map = new HashMap<>();
        String prev = null;
        for (String arg : args) {
            if (counter % 2 == 0) {
                map.put(prev, arg);
            } else {
                prev = arg;
            }
            counter++;
        }
        return map;
    }

    private HttpHeaders getHeaders(String cookie) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.COOKIE, cookie);
        return httpHeaders;
    }

    private HttpEntity<Map> loginRequest(String user, String pass) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", user);
        map.put("password", pass);
        return new HttpEntity<>(map, new HttpHeaders());
    }

    private String login(String user, String pass) {
        final HttpEntity<Map> request = loginRequest(user, pass);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("login"),
                HttpMethod.POST, request, String.class);
        return Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).get(0);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + randomServerPort + constants.apiEntryPoint + uri;
    }
}