package org.eclipse.swt.predicateeditor;

public interface PredicateVisitable {
    public void acceptPredicateVisitor(PredicateVisitor visitor, Object context);
}
