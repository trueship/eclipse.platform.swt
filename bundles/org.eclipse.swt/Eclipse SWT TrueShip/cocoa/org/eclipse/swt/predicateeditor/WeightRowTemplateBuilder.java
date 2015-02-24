package org.eclipse.swt.predicateeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.swt.predicateeditor.cocoa.SWTWeightRowTemplate;
import org.eclipse.swt.widgets.PredicateEditor.AttributeType;
import org.eclipse.swt.widgets.PredicateEditor.ComparisonPredicateModifier;

public class WeightRowTemplateBuilder extends ComparisonPredicateEditorRowTemplateBuilder {
    private HashMap<String, String> keyPathToTitleMap;
    private List<String> units;
    
    public ComparisonPredicateEditorRowTemplateBuilder withLeftExpressionsForKeyPathWithTitle(HashMap<String, String> keyPathToTitleMap) {      
        this.keyPathToTitleMap = new HashMap<String, String>();
        for (Entry<String, String> entry : keyPathToTitleMap.entrySet())
            this.keyPathToTitleMap.put(entry.getKey() + ".WEIGHT", entry.getValue());
       
        return this.withLeftExpressions(new ArrayList<String>(this.keyPathToTitleMap.keySet()))
                   .withRightExpressionAttributeType(AttributeType.NSDecimalAttributeType)
                   .withModifier(ComparisonPredicateModifier.NSDirectPredicateModifier);
    }
    
    @Override
    public PredicateEditorRowTemplate build() {
        return new PredicateEditorRowTemplate(initTemplate(new SWTWeightRowTemplate(keyPathToTitleMap, units)).id);
    }

    public WeightRowTemplateBuilder withUnits(List<String> units) {
        this.units = units;
        
        return this;
    }

}
