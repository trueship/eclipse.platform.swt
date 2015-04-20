package org.eclipse.swt.predicateeditor;

import java.util.*;

import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.predicateeditor.cocoa.*;
import org.eclipse.swt.widgets.PredicateEditor;
import org.eclipse.swt.widgets.PredicateEditor.AttributeType;
import org.eclipse.swt.widgets.PredicateEditor.ComparisonPredicateModifier;
import org.eclipse.swt.widgets.PredicateEditor.PredicateOperatorType;

public class NumericRowTemplateBuilder extends ComparisonPredicateEditorRowTemplateBuilder {
    private HashMap<String, String> keyPathToTitleMap;
    
    public NumericRowTemplateBuilder withLeftExpressionsForKeyPathWithTitle(HashMap<String, String> keyPathToTitleMap) {
        this.keyPathToTitleMap = keyPathToTitleMap;
       
        return (NumericRowTemplateBuilder) this.withLeftExpressions(new ArrayList<String>(keyPathToTitleMap.keySet()));
    }
    
    public NumericRowTemplateBuilder withNumericRightExpressionType(PredicateEditor.AttributeType type) {
        if (!isNumericType(type))
            throw new IllegalArgumentException("Non numeric attribute type: " + type.name());
        
        return (NumericRowTemplateBuilder) this.withRightExpressionAttributeType(type)
                .withModifier(ComparisonPredicateModifier.NSDirectPredicateModifier)
                .withOperators(EnumSet.of(
                        PredicateOperatorType.NSLessThanPredicateOperatorType,
                        PredicateOperatorType.NSLessThanOrEqualToPredicateOperatorType,
                        PredicateOperatorType.NSEqualToPredicateOperatorType,
                        PredicateOperatorType.NSGreaterThanPredicateOperatorType,
                        PredicateOperatorType.NSGreaterThanOrEqualToPredicateOperatorType));
    }
    
    public PredicateEditorRowTemplate build() {
        NSPredicateEditorRowTemplate template = new SWTNumericRowTemplate(keyPathToTitleMap);
        
        return new PredicateEditorRowTemplate(initTemplate(template).id);
    }
    
    private boolean isNumericType(PredicateEditor.AttributeType type) {
        return  type == AttributeType.NSDecimalAttributeType ||
                type == AttributeType.NSDoubleAttributeType ||
                type == AttributeType.NSFloatAttributeType ||
                type == AttributeType.NSInteger16AttributeType ||
                type == AttributeType.NSInteger32AttributeType ||
                type == AttributeType.NSInteger64AttributeType;
    }
}
