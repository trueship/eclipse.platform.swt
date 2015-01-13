package org.eclipse.swt.predicateeditor;

import java.util.*;

import org.eclipse.swt.predicateeditor.cocoa.*;
import org.eclipse.swt.predicateeditor.RightValuesCallback;
import org.eclipse.swt.widgets.PredicateEditor;

public class DynamicRightValuesRowTemplateBuilder {
    private String leftKeyPath;
    private String leftKeyPathTitle;
    private RightValuesCallback tokensCallback;
    private PredicateEditor predicateEditor;
    
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
    
    public DynamicRightValuesRowTemplateBuilder withPredicateEditor(PredicateEditor predicateEditor) {
        this.predicateEditor = predicateEditor;
        
        return this;
    }
    
    public PredicateEditorRowTemplate build() {
        if (tokensCallback != null) {
            SWTDynamicRightValuesRowTemplate template = new SWTDynamicRightValuesRowTemplate(this.leftKeyPath, this.leftKeyPathTitle, this.tokensCallback, this.predicateEditor);
            
            return new PredicateEditorRowTemplate(template.id);
        }
        
        return null;
    }
}
