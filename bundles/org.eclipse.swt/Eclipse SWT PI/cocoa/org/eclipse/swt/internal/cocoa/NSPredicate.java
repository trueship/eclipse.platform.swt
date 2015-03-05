/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSPredicate extends NSObject {

public NSPredicate() {
	super();
}

public NSPredicate(long /*int*/ id) {
	super(id);
}

public NSPredicate(id id) {
	super(id);
}

public boolean evaluateWithObject(id object) {
	return OS.objc_msgSend_bool(this.id, OS.sel_evaluateWithObject_, object != null ? object.id : 0);
}

public NSString predicateFormat() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_predicateFormat);
	return result != 0 ? new NSString(result) : null;
}

public static NSPredicate predicateWithFormat(NSString predicateFormat) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSPredicate, OS.sel_predicateWithFormat_, predicateFormat != null ? predicateFormat.id : 0);
	return result != 0 ? new NSPredicate(result) : null;
}

public static NSPredicate predicateWithFormat(NSString predicateFormat, NSArray arguments) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSPredicate, OS.sel_predicateWithFormat_argumentArray_, predicateFormat != null ? predicateFormat.id : 0, arguments != null ? arguments.id : 0);
	return result != 0 ? new NSPredicate(result) : null;
}

public static NSPredicate predicateWithFormat(NSString predicateFormat, long /*int*/ argList) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSPredicate, OS.sel_predicateWithFormat_arguments_, predicateFormat != null ? predicateFormat.id : 0, argList);
	return result != 0 ? new NSPredicate(result) : null;
}

public static NSPredicate predicateWithValue(boolean value) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSPredicate, OS.sel_predicateWithValue_, value);
	return result != 0 ? new NSPredicate(result) : null;
}

}
