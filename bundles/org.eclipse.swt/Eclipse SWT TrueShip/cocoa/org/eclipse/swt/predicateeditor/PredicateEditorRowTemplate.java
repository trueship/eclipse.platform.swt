package org.eclipse.swt.predicateeditor;

import org.eclipse.swt.internal.cocoa.*;

public class PredicateEditorRowTemplate {
    NSPredicateEditorRowTemplate nsTemplate;
    
    public PredicateEditorRowTemplate(long /*int*/ id) {
        nsTemplate = new NSPredicateEditorRowTemplate(id);
    }

    public long /*int*/ id() {
        return nsTemplate.id;
    }
}
