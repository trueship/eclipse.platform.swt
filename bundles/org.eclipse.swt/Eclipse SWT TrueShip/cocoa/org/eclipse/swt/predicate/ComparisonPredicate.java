package org.eclipse.swt.predicate;

import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.widgets.PredicateEditor.PredicateOperatorType;

public class ComparisonPredicate extends Predicate {

    public ComparisonPredicate(long /*int*/ id) {
        super(id);
    }

    public String leftExpressionString() {
        NSComparisonPredicate cp  = new NSComparisonPredicate(id());
        return cp.leftExpression().description().getString();
    }

    public String rightExpressionString() {
        NSComparisonPredicate cp  = new NSComparisonPredicate(id());
        return cp.rightExpression().description().getString().replaceAll("\"", "");
    }

    public PredicateOperatorType predicateOperatorType() {
        NSComparisonPredicate cp  = new NSComparisonPredicate(id());
        return PredicateOperatorType.values()[(int) cp.predicateOperatorType()];
    }
}
