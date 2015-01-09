package org.eclipse.swt.predicateeditor;

import java.util.*;

import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.predicateeditor.cocoa.*;

public class KeyPathWithTitleRowTemplateBuilder extends ComparisonPredicateEditorRowTemplateBuilder {
    private HashMap<String, String> keyPathToTitleMap;
	
    public ComparisonPredicateEditorRowTemplateBuilder withLeftExpressionsForKeyPathWithTitle(HashMap<String, String> keyPathToTitleMap) {
        this.keyPathToTitleMap = keyPathToTitleMap;
       
        return this.withLeftExpressions(new ArrayList<String>(keyPathToTitleMap.keySet()));
    }
    
    public PredicateEditorRowTemplate build() {
        NSPredicateEditorRowTemplate template = new SWTKeyPathWithTitleRowTemplate(keyPathToTitleMap);
        
        return new PredicateEditorRowTemplate(initTemplate(template).id);
    }
}
