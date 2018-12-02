package com.noname.learningSpring;

import com.noname.learningSpring.entities.*;
import com.noname.learningSpring.repositories.*;
import com.noname.learningSpring.security.AccountBuilder;
import com.noname.learningSpring.security.LocalSecurityContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.math.BigDecimal;
import java.util.Random;

@SpringBootApplication
public class LearningSpringApplication {

    @Bean
    public ViewResolver adminViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/");
        resolver.setSuffix(".html");
        resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return resolver;
    }


    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(LearningSpringApplication.class, args);
        initTestData(context);
    }

    @Transactional
    private static void initTestData(ConfigurableApplicationContext context) {


      new AccountBuilder( context.getBean(LocalSecurityContext.class)).role("ROLE_MANAGER").userName("manager1").password("123")
              .privileges("GET /*", "GET /admin/*", "#*").build();




        addProducts(context, 10);
    }



    private static void addProducts(ConfigurableApplicationContext context, int amount) {
        final ProductRepository productRepository = context.getBean(ProductRepository.class);
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < amount; i++) {
            Product product = new Product();
            product.setName(product.toString());
            product.setPrice(BigDecimal.valueOf(random.nextInt(10000) + 1));
            product.setCode("code" + product.getPrice().intValue());
            productRepository.save(product);
        }
    }

}

