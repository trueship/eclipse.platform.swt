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

public class NSPredicateEditorRowTemplate extends NSObject {

public NSPredicateEditorRowTemplate() {
	super();
}

public NSPredicateEditorRowTemplate(long /*int*/ id) {
	super(id);
}

public NSPredicateEditorRowTemplate(id id) {
	super(id);
}

public id initWithCompoundTypes(NSArray compoundTypes) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithCompoundTypes_, compoundTypes != null ? compoundTypes.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithLeftExpressions(NSArray leftExpressions, long /*int*/ attributeType, long /*int*/ modifier, NSArray operators, long /*int*/ options) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithLeftExpressions_rightExpressionAttributeType_modifier_operators_options_, leftExpressions != null ? leftExpressions.id : 0, attributeType, modifier, operators != null ? operators.id : 0, options);
	return result != 0 ? new id(result) : null;
}

public id initWithLeftExpressions(NSArray leftExpressions, NSArray rightExpressions, long /*int*/ modifier, NSArray operators, long /*int*/ options) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithLeftExpressions_rightExpressions_modifier_operators_options_, leftExpressions != null ? leftExpressions.id : 0, rightExpressions != null ? rightExpressions.id : 0, modifier, operators != null ? operators.id : 0, options);
	return result != 0 ? new id(result) : null;
}

public NSArray leftExpressions() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_leftExpressions);
	return result != 0 ? new NSArray(result) : null;
}

public double matchForPredicate(NSPredicate predicate) {
	return OS.objc_msgSend_fpret(this.id, OS.sel_matchForPredicate_, predicate != null ? predicate.id : 0);
}

public long /*int*/ modifier() {
	return OS.objc_msgSend(this.id, OS.sel_modifier);
}

public NSArray operators() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_operators);
	return result != 0 ? new NSArray(result) : null;
}

public long /*int*/ options() {
	return OS.objc_msgSend(this.id, OS.sel_options);
}

public NSPredicate predicateWithSubpredicates(NSArray subpredicates) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_predicateWithSubpredicates_, subpredicates != null ? subpredicates.id : 0);
	return result != 0 ? new NSPredicate(result) : null;
}

public long /*int*/ rightExpressionAttributeType() {
	return OS.objc_msgSend(this.id, OS.sel_rightExpressionAttributeType);
}

public NSArray rightExpressions() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_rightExpressions);
	return result != 0 ? new NSArray(result) : null;
}

public void setPredicate(NSPredicate predicate) {
	OS.objc_msgSend(this.id, OS.sel_setPredicate_, predicate != null ? predicate.id : 0);
}

public NSArray templateViews() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_templateViews);
	return result != 0 ? new NSArray(result) : null;
}

}
