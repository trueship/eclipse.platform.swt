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

public class NSPopover extends NSObject {

public NSPopover() {
	super();
}

public NSPopover(long /*int*/ id) {
	super(id);
}

public NSPopover(id id) {
	super(id);
}

public boolean animates() {
	return OS.objc_msgSend_bool(this.id, OS.sel_animates);
}

public long /*int*/ appearance() {
	return OS.objc_msgSend(this.id, OS.sel_appearance);
}

public long /*int*/ behavior() {
	return OS.objc_msgSend(this.id, OS.sel_behavior);
}

public void close() {
	OS.objc_msgSend(this.id, OS.sel_close);
}

public NSSize contentSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_contentSize);
	return result;
}

public NSViewController contentViewController() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_contentViewController);
	return result != 0 ? new NSViewController(result) : null;
}

public id delegate() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public boolean isShown() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isShown);
}

public void performClose(id sender) {
	OS.objc_msgSend(this.id, OS.sel_performClose_, sender != null ? sender.id : 0);
}

public NSRect positioningRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_positioningRect);
	return result;
}

public void setAnimates(boolean animates) {
	OS.objc_msgSend(this.id, OS.sel_setAnimates_, animates);
}

public void setAppearance(long /*int*/ appearance) {
	OS.objc_msgSend(this.id, OS.sel_setAppearance_, appearance);
}

public void setBehavior(long /*int*/ behavior) {
	OS.objc_msgSend(this.id, OS.sel_setBehavior_, behavior);
}

public void setContentSize(NSSize contentSize) {
	OS.objc_msgSend(this.id, OS.sel_setContentSize_, contentSize);
}

public void setContentViewController(NSViewController contentViewController) {
	OS.objc_msgSend(this.id, OS.sel_setContentViewController_, contentViewController != null ? contentViewController.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, delegate != null ? delegate.id : 0);
}

public void setPositioningRect(NSRect positioningRect) {
	OS.objc_msgSend(this.id, OS.sel_setPositioningRect_, positioningRect);
}

public void showRelativeToRect(NSRect positioningRect, NSView positioningView, long /*int*/ preferredEdge) {
	OS.objc_msgSend(this.id, OS.sel_showRelativeToRect_ofView_preferredEdge_, positioningRect, positioningView != null ? positioningView.id : 0, preferredEdge);
}

}
