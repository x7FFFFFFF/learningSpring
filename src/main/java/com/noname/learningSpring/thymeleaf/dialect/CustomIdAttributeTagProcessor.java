package com.noname.learningSpring.thymeleaf.dialect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.extras.springsecurity5.auth.AuthUtils;
import org.thymeleaf.extras.springsecurity5.util.SpringVersionSpecificUtils;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class CustomIdAttributeTagProcessor extends AbstractAttributeTagProcessor {
    private static final String ATTR_NAME = "id";
    private static final int PRECEDENCE = 10000;
    private final Map<String, Expression> expressionMap;
    private SecurityExpressionHandler expressionHandler;

    public CustomIdAttributeTagProcessor(final String dialectPrefix, Map<String, Expression> expressionMap, SecurityExpressionHandler expressionHandler) {
        super(
                TemplateMode.HTML, // This processor will apply only to HTML mode
                null,     // Prefix to be applied to name for matching
                null,              // No tag name: match any tag name
                false,             // No prefix to be applied to tag name
                ATTR_NAME,         // Name of the attribute that will be matched
                true,              // Apply dialect prefix to attribute name
                PRECEDENCE,        // Precedence (inside dialect's own precedence)
                true);             // Remove the matched attribute afterwards
        this.expressionMap = expressionMap;
        this.expressionHandler = expressionHandler;
    }        // Remove the matched attribute afterwards


    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {
        final String id = getId(context, attributeValue);
        if (expressionMap.containsKey(id)) {
            final Expression expr = expressionMap.get(id);
            final EvaluationContext ctx = getEvaluationContext(context);
            if (! ExpressionUtils.evaluateAsBoolean(expr, ctx)) {
                structureHandler.setBody("", false); // false == 'non-processable'
            }
        }
        structureHandler.setAttribute(ATTR_NAME, id);


    }

    private EvaluationContext getEvaluationContext(ITemplateContext context) {
        final Authentication authentication = AuthUtils.getAuthenticationObject(context);
        if (SpringVersionSpecificUtils.isWebFluxContext(context)){
            throw new UnsupportedOperationException("WebFlux");
        }
        final HttpServletRequest request = SpringVersionSpecificUtils.getHttpServletRequest(context);
        final HttpServletResponse response = SpringVersionSpecificUtils.getHttpServletResponse(context);

        final FilterInvocation filterInvocation = new FilterInvocation(request, response, (request1, response1) -> {
            throw new UnsupportedOperationException();
        });

        return expressionHandler.createEvaluationContext(authentication, filterInvocation);
    }

    private String getId(ITemplateContext context, String attributeValue) {
        final IEngineConfiguration configuration = context.getConfiguration();        /*
         * Obtain the Thymeleaf Standard Expression parser
         */
        final IStandardExpressionParser parser =
                StandardExpressions.getExpressionParser(configuration);
        /*
         * Parse the attribute value as a Thymeleaf Standard Expression
         */
        final IStandardExpression expression = parser.parseExpression(context, attributeValue);
        /*
         * Execute the expression just parsed
         */
        return (String) expression.execute(context);
    }
}
