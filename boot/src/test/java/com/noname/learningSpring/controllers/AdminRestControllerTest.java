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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class AdminRestControllerTest {

    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String PARENT = "parent";
    public static final String PRIVILEGES = "privileges";
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
        UriComponentsBuilder uriComponentsBuilder = getRolesPath()
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

    @Test
    @WithMockCustomUser(username = "manager1", password = "123", role = "ROLE_MANAGER",
            priveleges = {"GET /*", "GET /admin/*", "GET /api/v1/**", "#*"})
    public void testRoleCrud() throws JSONException {
        final String cookie = login("manager1", "123");
        Integer id = getFistRoleId(cookie);


        //final Integer id = (Integer)getResult(response, "result", "content", 0, "id");
        UriComponentsBuilder urlGet = getRolesPath()
                .pathSegment(id.toString());
        ResponseEntity<Map> responseGet = get(cookie, urlGet);
        Map<String, Object> role = getRole(responseGet);
        role.put(NAME, "ROLE_TEST");
        ResponseEntity<Map> responsePut = put(cookie, urlGet, role);

        ResponseEntity<Map> responseGet2 = get(cookie, urlGet);
        Map<String, Object> roleChanged = getRole(responseGet2);
        Assert.assertEquals("ROLE_TEST", roleChanged.get(NAME));







    }

    private Map<String, Object> getRole(ResponseEntity<Map> responseGet) {
        final Result result = new Result(responseGet).parse("result");
        Map<String, Object> map = new HashMap<>();
        map.put(ID, result.parse(ID).getValue());
        map.put(NAME, result.parse(NAME).getValue());
        map.put(PARENT, result.parse(PARENT).getValue());
        map.put(PRIVILEGES, result.parse(PRIVILEGES).getValue());
        return map;
    }

    private Integer getFistRoleId(String cookie) {
        UriComponentsBuilder urlList = getRolesPath()
                .queryParam("page", "0")
                .queryParam("size", "100");

        ResponseEntity<Map> response = get(cookie, urlList);
        return new Result(response).parse( "result", "content", 0, ID).getValue();
    }

    private UriComponentsBuilder getRolesPath() {
        return UriComponentsBuilder.fromHttpUrl(createURLWithPort("roles"));
    }

    private ResponseEntity<Map> get(String cookie, UriComponentsBuilder urlList) {
        HttpEntity<Map> entity = new HttpEntity<>(getHeaders(cookie));
        return restTemplate.exchange(
                urlList.toUriString(),
                HttpMethod.GET, entity, Map.class);
    }

    private ResponseEntity<Map> put(String cookie, UriComponentsBuilder urlList, Map body) {
        HttpEntity<Map> entity = new HttpEntity<>(body, getHeaders(cookie));
        return restTemplate.exchange(
                urlList.toUriString(),
                HttpMethod.PUT, entity, Map.class);
    }


    static class Result {
        Object result;

        public Result(Object result) {
            this.result = result;
        }

        Result(ResponseEntity response) {
            this.result = response.getBody();
        }
        Result parse(Object... args) {
            Object res = result;
            for (Object arg : args) {
                if (res instanceof Map) {
                    res = ((Map) res).get(arg);
                } else if (res instanceof List) {
                    res = ((List) res).get((int) arg);
                } else {
                    throw new IllegalStateException();
                }
            }
            return new Result(res);
        }


        @SuppressWarnings("unchecked")
        <T> T getValue(){
            return (T) result;
        }

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