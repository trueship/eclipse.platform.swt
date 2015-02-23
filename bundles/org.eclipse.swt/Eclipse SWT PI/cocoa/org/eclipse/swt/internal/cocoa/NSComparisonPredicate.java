/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSComparisonPredicate extends NSPredicate {

public NSComparisonPredicate() {
	super();
}

public NSComparisonPredicate(long /*int*/ id) {
	super(id);
}

public NSComparisonPredicate(id id) {
	super(id);
}

public long /*int*/ comparisonPredicateModifier() {
	return OS.objc_msgSend(this.id, OS.sel_comparisonPredicateModifier);
}

public id initWithLeftExpression(NSExpression lhs, NSExpression rhs, long /*int*/ modifier, long /*int*/ type, long /*int*/ options) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithLeftExpression_rightExpression_modifier_type_options_, lhs != null ? lhs.id : 0, rhs != null ? rhs.id : 0, modifier, type, options);
	return result != 0 ? new id(result) : null;
}

public NSExpression leftExpression() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_leftExpression);
	return result != 0 ? new NSExpression(result) : null;
}

public long /*int*/ options() {
	return OS.objc_msgSend(this.id, OS.sel_options);
}

public long /*int*/ predicateOperatorType() {
	return OS.objc_msgSend(this.id, OS.sel_predicateOperatorType);
}

public NSExpression rightExpression() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_rightExpression);
	return result != 0 ? new NSExpression(result) : null;
}

public static NSPredicate predicateWithFormat(NSString predicateFormat) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSComparisonPredicate, OS.sel_predicateWithFormat_, predicateFormat != null ? predicateFormat.id : 0);
	return result != 0 ? new NSPredicate(result) : null;
}

public static NSPredicate predicateWithFormat(NSString predicateFormat, NSArray arguments) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSComparisonPredicate, OS.sel_predicateWithFormat_argumentArray_, predicateFormat != null ? predicateFormat.id : 0, arguments != null ? arguments.id : 0);
	return result != 0 ? new NSPredicate(result) : null;
}

public static NSPredicate predicateWithFormat(NSString predicateFormat, long /*int*/ argList) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSComparisonPredicate, OS.sel_predicateWithFormat_arguments_, predicateFormat != null ? predicateFormat.id : 0, argList);
	return result != 0 ? new NSPredicate(result) : null;
}

}
