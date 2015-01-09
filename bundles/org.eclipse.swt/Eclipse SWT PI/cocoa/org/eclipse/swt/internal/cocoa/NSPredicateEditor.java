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

public class NSPredicateEditor extends NSRuleEditor {

public NSPredicateEditor() {
	super();
}

public NSPredicateEditor(long /*int*/ id) {
	super(id);
}

public NSPredicateEditor(id id) {
	super(id);
}

public NSArray rowTemplates() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_rowTemplates);
	return result != 0 ? new NSArray(result) : null;
}

public void setRowTemplates(NSArray rowTemplates) {
	OS.objc_msgSend(this.id, OS.sel_setRowTemplates_, rowTemplates != null ? rowTemplates.id : 0);
}

public static long /*int*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSPredicateEditor, OS.sel_cellClass);
}

public static void setCellClass(long /*int*/ factoryId) {
	OS.objc_msgSend(OS.class_NSPredicateEditor, OS.sel_setCellClass_, factoryId);
}

}
