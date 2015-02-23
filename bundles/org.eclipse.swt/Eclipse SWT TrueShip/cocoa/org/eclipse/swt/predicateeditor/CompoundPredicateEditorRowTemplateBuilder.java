package org.eclipse.swt.predicateeditor;

import java.util.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.widgets.PredicateEditor.CompoundPredicateType;

public class CompoundPredicateEditorRowTemplateBuilder {

    private NSMutableArray compoundTypes;
    
    public CompoundPredicateEditorRowTemplateBuilder withCompoundTypes(EnumSet<CompoundPredicateType> predicateTypes) {
        compoundTypes = NSMutableArray.arrayWithCapacity(predicateTypes.size());
        for (CompoundPredicateType predicateType: predicateTypes) 
            compoundTypes.addObject(NSNumber.numberWithInteger(predicateType.value()));
        
        return this;
    }

    public PredicateEditorRowTemplate build() {
        NSPredicateEditorRowTemplate compound = (NSPredicateEditorRowTemplate) new NSPredicateEditorRowTemplate().alloc();
        compound.initWithCompoundTypes(compoundTypes);
        
        return new PredicateEditorRowTemplate(compound.id);
    }
}
