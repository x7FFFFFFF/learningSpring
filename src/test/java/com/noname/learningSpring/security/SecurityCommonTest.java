package com.noname.learningSpring.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootTest
public class SecurityCommonTest {
    @Autowired
    private MessageService messageService;



    @Test
    @WithMockCustomUser(username = "anon", password = "1",  role = "ROLE_ANON", priveleges = {"GET /*"})
    public void getMessageUnauthenticated() {
        messageService.getMessage();
    }

}