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

public class NSDecimalNumber extends NSNumber {

public NSDecimalNumber() {
	super();
}

public NSDecimalNumber(long /*int*/ id) {
	super(id);
}

public NSDecimalNumber(id id) {
	super(id);
}

public static NSDecimalNumber decimalNumberWithString(NSString numberValue) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_decimalNumberWithString_, numberValue != null ? numberValue.id : 0);
	return result != 0 ? new NSDecimalNumber(result) : null;
}

public id initWithString(NSString numberValue) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithString_, numberValue != null ? numberValue.id : 0);
	return result != 0 ? new id(result) : null;
}

public static NSNumber numberWithBool(boolean value) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_numberWithBool_, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithDouble(double value) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_numberWithDouble_, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithFloat(float value) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_numberWithFloat_, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithInt(int value) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_numberWithInt_, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithInteger(long /*int*/ value) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_numberWithInteger_, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSValue valueWithPoint(NSPoint point) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_valueWithPoint_, point);
	return result != 0 ? new NSValue(result) : null;
}

public static NSValue valueWithRange(NSRange range) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_valueWithRange_, range);
	return result != 0 ? new NSValue(result) : null;
}

public static NSValue valueWithRect(NSRect rect) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_valueWithRect_, rect);
	return result != 0 ? new NSValue(result) : null;
}

public static NSValue valueWithSize(NSSize size) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSDecimalNumber, OS.sel_valueWithSize_, size);
	return result != 0 ? new NSValue(result) : null;
}

}
