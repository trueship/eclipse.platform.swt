package org.eclipse.swt.predicateeditor;

import org.eclipse.swt.predicate.*;

public abstract class AbstractPredicateVisitor implements PredicateVisitor {
    public void visit(Predicate predicate) {
        visit(predicate, null);
    }
}
