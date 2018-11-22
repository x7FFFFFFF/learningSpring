package com.noname.learningSpring.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class SecurityCommonTest {
    @Autowired
    private MockMvc mvc;

    /**
     *  Ant-style path patterns support added
     * @see  org.springframework.util.AntPathMatcher
     */

    @Test
    @WithMockCustomUser(username = "anon", password = "1",  role = "ROLE_ANON", priveleges = {"GET /*"})
    public void testAnon() throws Exception {
        mvc.perform(get("/index"))
                .andExpect(status().isNotFound());

        mvc.perform(get("/admin/accountInfo"))
                .andExpect(status().isForbidden());

        mvc.perform(get("/admin/login"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(username = "manager", password = "1",  role = "ROLE_MANAGER", priveleges = {"GET /**"})
    public void testManager() throws Exception {
        mvc.perform(get("/index"))
                .andExpect(status().isNotFound());

        mvc.perform(get("/admin/accountInfo"))
                .andExpect(status().isOk());

        mvc.perform(get("/admin/login"))
                .andExpect(status().isOk());
    }

}