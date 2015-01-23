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

public class NSDateComponents extends NSObject {

public NSDateComponents() {
	super();
}

public NSDateComponents(long /*int*/ id) {
	super(id);
}

public NSDateComponents(id id) {
	super(id);
}

public NSCalendar calendar() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_calendar);
	return result != 0 ? new NSCalendar(result) : null;
}

}
