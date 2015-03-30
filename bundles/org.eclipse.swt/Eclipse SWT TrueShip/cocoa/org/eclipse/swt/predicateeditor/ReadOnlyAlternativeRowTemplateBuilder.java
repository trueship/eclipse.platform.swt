package org.eclipse.swt.predicateeditor;

import java.util.*;

import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.predicateeditor.cocoa.*;

public class ReadOnlyAlternativeRowTemplateBuilder extends ComparisonPredicateEditorRowTemplateBuilder {
    private HashMap<String, String> keyPathToTitleMap;
    private List<String> alternativeToRightExpressions;
    
    public ComparisonPredicateEditorRowTemplateBuilder withLeftExpressionsForKeyPathWithTitle(HashMap<String, String> keyPathToTitleMap) {
        this.keyPathToTitleMap = keyPathToTitleMap;
       
        return this.withLeftExpressions(new ArrayList<String>(keyPathToTitleMap.keySet())).withRightExpressions(Arrays.asList("Other..."));
    }
    
    public PredicateEditorRowTemplate build() {
        NSPredicateEditorRowTemplate template = new SWTReadOnlyAlternativeRowTemplate(keyPathToTitleMap, alternativeToRightExpressions);
        
        return new PredicateEditorRowTemplate(initTemplate(template).id);
    }

    public ReadOnlyAlternativeRowTemplateBuilder withAlternativeTo(List<String> alternativeToRightExpressions) {
        this.alternativeToRightExpressions = alternativeToRightExpressions;
        
        return this;
    }
}
