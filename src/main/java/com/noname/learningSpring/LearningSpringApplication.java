package com.noname.learningSpring;

import com.noname.learningSpring.entities.Account;
import com.noname.learningSpring.entities.Product;
import com.noname.learningSpring.repositories.AccountRepository;
import com.noname.learningSpring.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;
import java.util.Random;

@SpringBootApplication
public class LearningSpringApplication {


	public static void main(String[] args) {
		final ConfigurableApplicationContext context = SpringApplication.run(LearningSpringApplication.class, args);
		initTestData(context);
	}

	private static void initTestData(ConfigurableApplicationContext context) {
		final AccountRepository accountRepository = context.getBean(AccountRepository.class);
		accountRepository.save(new Account("manager1", "$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu",
				true, "ROLE_MANAGER"));  //password: 123

		addProducts(context, 10);
	}

	private static void addProducts(ConfigurableApplicationContext context, int amount) {
		final ProductRepository productRepository = context.getBean(ProductRepository.class);
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < amount; i++) {
			Product product = new Product();
			product.setName(product.toString());
			product.setPrice(BigDecimal.valueOf(random.nextInt(10000) + 1));
			productRepository.save(product);
		}
	}
}
