package com.noname.learningSpring.thymeleaf.dialect;

import org.springframework.expression.Expression;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Map;

public class CustomIdAttributeTagProcessor extends AbstractAttributeTagProcessor {
    private static final String ATTR_NAME = "id";
    private static final int PRECEDENCE = 10000;
    private final Map<String, Expression> expressionMap;

    public CustomIdAttributeTagProcessor(final String dialectPrefix, Map<String, Expression> expressionMap) {
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
    }        // Remove the matched attribute afterwards


    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {
        final IEngineConfiguration configuration = context.getConfiguration();

        /*
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
        final String id = (String) expression.execute(context);
        if (expressionMap.containsKey(id)) {
            final Expression expr = expressionMap.get(id);
            final Boolean value = (Boolean) expr.getValue();
            if (!value) {
                structureHandler.setBody("", false); // false == 'non-processable'
            }
        }

        structureHandler.setAttribute(ATTR_NAME, id);


    }
}
