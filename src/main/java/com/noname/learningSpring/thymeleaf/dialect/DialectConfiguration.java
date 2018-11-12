package com.noname.learningSpring.thymeleaf.dialect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
public class DialectConfiguration {


    @Autowired
    public DialectConfiguration(SpringTemplateEngine templateEngine,
                                @Value("${dialect.thymeleaf.styles}") String globalStyles, SecurityExpressionHandler expressionHandler) throws IOException {
        if (globalStyles == null) {
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        Rule[] rules;
        try (final InputStream inputStream = Objects.requireNonNull(DialectConfiguration.class.getClassLoader().getResource(globalStyles)).openStream()) {
           rules = mapper.readValue(inputStream, Rule[].class);
        }
        Map<String, Expression> expressionMap = new HashMap<>();
        final ExpressionParser expressionParser = expressionHandler.getExpressionParser();
        for (Rule rule : rules) {
            final Expression expression = expressionParser.parseExpression(rule.getDisplay());
            for (String id : rule.getIds()) {
                expressionMap.put(id, expression);
            }
        }

        System.out.println("rules = " + rules);
        templateEngine.addDialect(new DisplayStylesDialect(expressionMap, expressionHandler));
    }
}
