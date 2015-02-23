package org.eclipse.swt.predicate;

import java.util.*;

import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.widgets.PredicateEditor.*;

public class CompoundPredicate extends Predicate {
    public CompoundPredicate(long /*int*/ id) {
        super(id);
    }

    public CompoundPredicateType compoundPredicateType() {
        NSCompoundPredicate cp = new NSCompoundPredicate(id());   
        return CompoundPredicateType.values()[(int) cp.compoundPredicateType()];
    }

    public List<Predicate> subpredicates() {
        NSCompoundPredicate cp = new NSCompoundPredicate(id());
        NSArray subpredicates = cp.subpredicates();
        
        ArrayList<Predicate> subpredicatesList = new ArrayList<Predicate>();
       
        for (int i = 0; i < subpredicates.count(); i++)
            subpredicatesList.add(new Predicate(subpredicates.objectAtIndex(i).id));
        
        return subpredicatesList;
    }
}
