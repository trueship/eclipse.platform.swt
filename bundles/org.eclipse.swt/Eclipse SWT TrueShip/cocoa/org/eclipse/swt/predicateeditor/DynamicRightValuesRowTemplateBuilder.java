package org.eclipse.swt.predicateeditor;

import java.util.*;

import org.eclipse.swt.predicate.*;
import org.eclipse.swt.predicateeditor.cocoa.*;
import org.eclipse.swt.predicateeditor.RightValuesCallback;

public class DynamicRightValuesRowTemplateBuilder {
    private String leftKeyPath;
    private String leftKeyPathTitle;
    private RightValuesCallback tokensCallback;
    private HashMap<Predicate, DynamicRightValuesRowTemplate> predicateToRowMap;
    
    public DynamicRightValuesRowTemplateBuilder withLeftKeyPath(String keyPath) {
        this.leftKeyPath = keyPath;
        
        return this;
    }
    
    public DynamicRightValuesRowTemplateBuilder withLeftKeyPathTitle(String keyPathTitle) {
        this.leftKeyPathTitle = keyPathTitle;
        
        return this;
    }

    public DynamicRightValuesRowTemplateBuilder withRightDynamicValuesCallback(RightValuesCallback tokensCallback) {
        this.tokensCallback = tokensCallback;
        
        return this;
    }

    public DynamicRightValuesRowTemplateBuilder withPredicateToRowMap(HashMap<Predicate, DynamicRightValuesRowTemplate> predicateToRowMap) {
        this.predicateToRowMap = predicateToRowMap;
        
        return this;
    }
    
    public PredicateEditorRowTemplate build() {
        if (tokensCallback != null) {
            SWTDynamicRightValuesRowTemplate template = new SWTDynamicRightValuesRowTemplate(this.leftKeyPath, this.leftKeyPathTitle, this.tokensCallback, this.predicateToRowMap);
            
            return new PredicateEditorRowTemplate(template.id);
        }
        
        return null;
    }
}
