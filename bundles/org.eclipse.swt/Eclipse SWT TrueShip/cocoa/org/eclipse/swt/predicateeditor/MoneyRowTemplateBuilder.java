package org.eclipse.swt.predicateeditor;

import java.util.*;
import java.util.Map.Entry;

import org.eclipse.swt.predicateeditor.cocoa.*;
import org.eclipse.swt.widgets.PredicateEditor;
import org.eclipse.swt.widgets.PredicateEditor.AttributeType;
import org.eclipse.swt.widgets.PredicateEditor.ComparisonPredicateModifier;

public class MoneyRowTemplateBuilder extends ComparisonPredicateEditorRowTemplateBuilder {
    private HashMap<String, String> keyPathToTitleMap;
    private List<String> currencies;
    private PredicateEditor predicateEditor;
    
    public ComparisonPredicateEditorRowTemplateBuilder withLeftExpressionsForKeyPathWithTitle(HashMap<String, String> keyPathToTitleMap) {      
        this.keyPathToTitleMap = keyPathToTitleMap;
       
        return this.withLeftExpressions(new ArrayList<String>(this.keyPathToTitleMap.keySet()))
                   .withRightExpressionAttributeType(AttributeType.NSDecimalAttributeType)
                   .withModifier(ComparisonPredicateModifier.NSDirectPredicateModifier);
    }
    
    @Override
    public PredicateEditorRowTemplate build() {
        return new PredicateEditorRowTemplate(initTemplate(new SWTMoneyRowTemplate(keyPathToTitleMap, currencies, this.predicateEditor)).id);
    }

    public MoneyRowTemplateBuilder withCurrencies(List<String> currencies) {
        this.currencies = currencies;
        
        return this;
    }
    
    public MoneyRowTemplateBuilder withPredicateEditor(PredicateEditor predicateEditor) {
        this.predicateEditor = predicateEditor;
        
        return this;
    }
}
