package com.noname.learningSpring.thymeleaf.dialect;

import org.springframework.expression.Expression;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DisplayStylesDialect extends AbstractProcessorDialect {
    private static final String DIALECT_NAME = "DisplayStylesDialect";
    public static final String PREFIX = "displayStyles";
    private final Map<String, Expression> expressionMap;
    private final SecurityExpressionHandler expressionHandler;

    protected DisplayStylesDialect(Map<String, Expression> expressionMap, SecurityExpressionHandler expressionHandler) {
        super(DIALECT_NAME, PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);
        this.expressionMap = expressionMap;
        this.expressionHandler = expressionHandler;
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new CustomIdAttributeTagProcessor(dialectPrefix, this.expressionMap, this.expressionHandler));
        return processors;
    }
}
