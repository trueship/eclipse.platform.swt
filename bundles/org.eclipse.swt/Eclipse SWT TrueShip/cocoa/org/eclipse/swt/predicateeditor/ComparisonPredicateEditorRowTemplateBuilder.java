package org.eclipse.swt.predicateeditor;

import java.util.*;

import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.widgets.PredicateEditor.AttributeType;
import org.eclipse.swt.widgets.PredicateEditor.ComparisonPredicateModifier;
import org.eclipse.swt.widgets.PredicateEditor.ComparisonPredicateOption;
import org.eclipse.swt.widgets.PredicateEditor.PredicateOperatorType;


public class ComparisonPredicateEditorRowTemplateBuilder {
    NSMutableArray keyPaths;
    AttributeType attributeType;
    ComparisonPredicateModifier predicateModifier;
    NSMutableArray operators;
    int options = 0;
    NSMutableArray rightExpressions;
    private boolean hasDateWithTimeRightExpression;
    
    public ComparisonPredicateEditorRowTemplateBuilder withLeftExpressions(List<String> expressions) {
        keyPaths = NSMutableArray.arrayWithCapacity(expressions.size());
        for (String expression : expressions)
            keyPaths.addObject(NSExpression.expressionForKeyPath(NSString.stringWith(expression)));
        
        return this;
    }

    public ComparisonPredicateEditorRowTemplateBuilder withRightExpressionAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
        
        return this;
    }

    public ComparisonPredicateEditorRowTemplateBuilder withModifier(ComparisonPredicateModifier predicateModifier) {
        this.predicateModifier = predicateModifier;
        
        return this;
    }

    public ComparisonPredicateEditorRowTemplateBuilder withOperators(EnumSet<PredicateOperatorType> predicateOperators) {
        operators = NSMutableArray.arrayWithCapacity(predicateOperators.size());
        for (PredicateOperatorType predicateOperator : predicateOperators)
            operators.addObject(NSNumber.numberWithInt(predicateOperator.value()));
        
        return this;
    }

    public ComparisonPredicateEditorRowTemplateBuilder withOptions(EnumSet<ComparisonPredicateOption> predicateOptions) {
        for (ComparisonPredicateOption predicateOption : predicateOptions)
            options |= predicateOption.value();
        
        return this;
    }

    public ComparisonPredicateEditorRowTemplateBuilder withRightExpressions(List<String> expressions) {
        rightExpressions = NSMutableArray.arrayWithCapacity(expressions.size());
        for (String expression : expressions)
            rightExpressions.addObject(NSExpression.expressionForConstantValue(NSString.stringWith(expression)));
        
        return this;
    }
    
    public ComparisonPredicateEditorRowTemplateBuilder withDateAndTimeRightExpressionType() {
        this.attributeType = AttributeType.NSDateAttributeType;
        this.hasDateWithTimeRightExpression = true;
        
        return this;
    }
    
    public boolean hasDateAndTimeRightExpression() {
        return hasDateWithTimeRightExpression;
    }
    
    public ComparisonPredicateEditorRowTemplateBuilder withStringRightExpressionType() {
        return this.withRightExpressionAttributeType(AttributeType.NSStringAttributeType)
                   .withModifier(ComparisonPredicateModifier.NSDirectPredicateModifier)
                   .withOperators(EnumSet.of(
                       PredicateOperatorType.NSEqualToPredicateOperatorType,
                       PredicateOperatorType.NSEndsWithPredicateOperatorType,
                       PredicateOperatorType.NSNotEqualToPredicateOperatorType,
                       PredicateOperatorType.NSBeginsWithPredicateOperatorType,
                       PredicateOperatorType.NSLikePredicateOperatorType,
                       PredicateOperatorType.NSGreaterThanOrEqualToPredicateOperatorType)) // Faking the CONTAINS operator
                   .withOptions(EnumSet.of(
                       ComparisonPredicateOption.NSCaseInsensitivePredicateOption,
                       ComparisonPredicateOption.NSDiacriticInsensitivePredicateOption));
    }
    
    public ComparisonPredicateEditorRowTemplateBuilder withIntegerRightExpressionType() {
        return this.withRightExpressionAttributeType(AttributeType.NSInteger64AttributeType)
                   .withModifier(ComparisonPredicateModifier.NSDirectPredicateModifier)
                   .withOperators(EnumSet.of(
                       PredicateOperatorType.NSLessThanPredicateOperatorType,
                       PredicateOperatorType.NSLessThanOrEqualToPredicateOperatorType,
                       PredicateOperatorType.NSEqualToPredicateOperatorType,
                       PredicateOperatorType.NSGreaterThanPredicateOperatorType,
                       PredicateOperatorType.NSGreaterThanOrEqualToPredicateOperatorType));
    }
    
    public ComparisonPredicateEditorRowTemplateBuilder withBooleanRightExpressionType() {
        return this.withRightExpressions(Arrays.asList("true", "false"))
                   .withModifier(ComparisonPredicateModifier.NSDirectPredicateModifier)
                   .withOperators(EnumSet.of(
                       PredicateOperatorType.NSEqualToPredicateOperatorType,
                       PredicateOperatorType.NSNotEqualToPredicateOperatorType));
    }
    
    public PredicateEditorRowTemplate build() {
        NSPredicateEditorRowTemplate template = (NSPredicateEditorRowTemplate) new NSPredicateEditorRowTemplate().alloc();

        return new PredicateEditorRowTemplate(initTemplate(template).id);
    }

    NSPredicateEditorRowTemplate initTemplate(NSPredicateEditorRowTemplate template) {
        if (rightExpressions != null)
            template.initWithLeftExpressions(keyPaths, rightExpressions, predicateModifier.value(), operators, options);
        else
            template.initWithLeftExpressions(keyPaths, attributeType.value(), predicateModifier.value(), operators, options);
        
        return template;
    }
}
