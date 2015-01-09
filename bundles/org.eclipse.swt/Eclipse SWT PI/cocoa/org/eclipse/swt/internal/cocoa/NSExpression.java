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

public class NSExpression extends NSObject {

public NSExpression() {
	super();
}

public NSExpression(long /*int*/ id) {
	super(id);
}

public NSExpression(id id) {
	super(id);
}

public id collection() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_collection);
	return result != 0 ? new id(result) : null;
}

public id constantValue() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_constantValue);
	return result != 0 ? new id(result) : null;
}

public static NSExpression expressionForConstantValue(id obj) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSExpression, OS.sel_expressionForConstantValue_, obj != null ? obj.id : 0);
	return result != 0 ? new NSExpression(result) : null;
}

public static NSExpression expressionForKeyPath(NSString keyPath) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSExpression, OS.sel_expressionForKeyPath_, keyPath != null ? keyPath.id : 0);
	return result != 0 ? new NSExpression(result) : null;
}

public NSString keyPath() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_keyPath);
	return result != 0 ? new NSString(result) : null;
}

public NSPredicate predicate() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_predicate);
	return result != 0 ? new NSPredicate(result) : null;
}

}
