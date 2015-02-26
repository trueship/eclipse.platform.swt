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

public class NSViewController extends NSResponder {

public NSViewController() {
	super();
}

public NSViewController(long /*int*/ id) {
	super(id);
}

public NSViewController(id id) {
	super(id);
}

public boolean commitEditing() {
	return OS.objc_msgSend_bool(this.id, OS.sel_commitEditing);
}

public void commitEditingWithDelegate(id delegate, long /*int*/ didCommitSelector, long /*int*/ contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_commitEditingWithDelegate_didCommitSelector_contextInfo_, delegate != null ? delegate.id : 0, didCommitSelector, contextInfo);
}

public void discardEditing() {
	OS.objc_msgSend(this.id, OS.sel_discardEditing);
}

public id initWithNibName(NSString nibNameOrNil, NSBundle nibBundleOrNil) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithNibName_bundle_, nibNameOrNil != null ? nibNameOrNil.id : 0, nibBundleOrNil != null ? nibBundleOrNil.id : 0);
	return result != 0 ? new id(result) : null;
}

public void loadView() {
	OS.objc_msgSend(this.id, OS.sel_loadView);
}

public NSBundle nibBundle() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_nibBundle);
	return result != 0 ? new NSBundle(result) : null;
}

public NSString nibName() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_nibName);
	return result != 0 ? new NSString(result) : null;
}

public id representedObject() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_representedObject);
	return result != 0 ? new id(result) : null;
}

public void setRepresentedObject(id representedObject) {
	OS.objc_msgSend(this.id, OS.sel_setRepresentedObject_, representedObject != null ? representedObject.id : 0);
}

public void setTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_, title != null ? title.id : 0);
}

public void setView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setView_, view != null ? view.id : 0);
}

public NSString title() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

public NSView view() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_view);
	return result != 0 ? new NSView(result) : null;
}

}
