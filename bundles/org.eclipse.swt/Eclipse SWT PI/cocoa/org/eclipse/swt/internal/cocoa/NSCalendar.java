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

public class NSCalendar extends NSObject {

public NSCalendar() {
	super();
}

public NSCalendar(long /*int*/ id) {
	super(id);
}

public NSCalendar(id id) {
	super(id);
}

public NSDateComponents components(long /*int*/ unitFlags, NSDate date) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_components_fromDate_, unitFlags, date != null ? date.id : 0);
	return result != 0 ? new NSDateComponents(result) : null;
}

public static id currentCalendar() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCalendar, OS.sel_currentCalendar);
	return result != 0 ? new id(result) : null;
}

public NSDate dateFromComponents(NSDateComponents comps) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_dateFromComponents_, comps != null ? comps.id : 0);
	return result != 0 ? new NSDate(result) : null;
}

public id initWithCalendarIdentifier(NSString ident) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithCalendarIdentifier_, ident != null ? ident.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setTimeZone(NSTimeZone tz) {
	OS.objc_msgSend(this.id, OS.sel_setTimeZone_, tz != null ? tz.id : 0);
}

}
