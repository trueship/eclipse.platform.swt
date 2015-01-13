package org.eclipse.swt.predicateeditor;

import org.eclipse.swt.predicate.*;

public interface PredicateVisitor {
    void visit(Predicate predicate, Object context);
}
