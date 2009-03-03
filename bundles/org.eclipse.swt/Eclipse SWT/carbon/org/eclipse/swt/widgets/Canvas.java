/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.carbon.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class provide a surface for drawing
 * arbitrary graphics.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * This class may be subclassed by custom control implementors
 * who are building controls that are <em>not</em> constructed
 * from aggregates of other controls. That is, they are either
 * painted using SWT graphics calls or are handled by native
 * methods.
 * </p>
 *
 * @see Composite
 * @see <a href="http://www.eclipse.org/swt/snippets/#canvas">Canvas snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class Canvas extends Composite {
	Caret caret;
	IME ime;

Canvas () {
	/* Do nothing */
}

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Canvas (Composite parent, int style) {
	super (parent, style);
}

/** 
 * Fills the interior of the rectangle specified by the arguments,
 * with the receiver's background. 
 *
 * @param gc the gc where the rectangle is to be filled
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the gc has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void drawBackground (GC gc, int x, int y, int width, int height) {
	checkWidget ();
	if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	Control control = findBackgroundControl ();
	if (control != null) {
		control.fillBackground (handle, gc.handle, new Rectangle (x, y, width, height));
	} else {
		gc.fillRectangle (x, y, width, height);
	}
}

void drawWidget (int control, int context, int damageRgn, int visibleRgn, int theEvent) {
	super.drawWidget (control, context, damageRgn, visibleRgn, theEvent);
	if (OS.VERSION >= 0x1040) {
		if (control != handle) return;
		if (caret == null) return;
		if (caret.isShowing) {
			OS.CGContextSaveGState (context);
			CGRect rect = new CGRect ();
			rect.x = caret.x;
			rect.y = caret.y;
			Image image = caret.image;
			OS.CGContextSetBlendMode (context, OS.kCGBlendModeDifference);
			if (image != null) {
				rect.width = OS.CGImageGetWidth (image.handle);
				rect.height = OS.CGImageGetHeight (image.handle);
			 	OS.CGContextScaleCTM (context, 1, -1);
			 	OS.CGContextTranslateCTM (context, 0, -(rect.height + 2 * rect.y));
				OS.CGContextDrawImage (context, rect, image.handle);
			} else {
				rect.width = caret.width != 0 ? caret.width : Caret.DEFAULT_WIDTH;
				rect.height = caret.height;
				OS.CGContextSetShouldAntialias (context, false);
				int colorspace = OS.CGColorSpaceCreateDeviceRGB ();
				OS.CGContextSetFillColorSpace (context, colorspace);
				OS.CGContextSetFillColor (context, new float[]{1, 1, 1, 1});
				OS.CGColorSpaceRelease (colorspace);
				OS.CGContextFillRect (context, rect);
			}
			OS.CGContextRestoreGState (context);
		}
	}
}

/**
 * Returns the caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 *
 * @return the caret for the receiver, may be null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Caret getCaret () {
	checkWidget();
    return caret;
}

/**
 * Returns the IME.
 *
 * @return the IME
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public IME getIME () {
	checkWidget();
    return ime;
}

int kEventControlDraw (int nextHandler, int theEvent, int userData) {
	int [] theControl = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamDirectObject, OS.typeControlRef, null, 4, null, theControl);
	boolean isFocus = OS.VERSION < 0x1040 && theControl [0] == handle && caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	int result = super.kEventControlDraw (nextHandler, theEvent, userData);
	if (isFocus) caret.setFocus ();
	return result;
}

int kEventControlSetFocusPart (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlSetFocusPart (nextHandler, theEvent, userData);
	if (result == OS.noErr) {
		if (!isDisposed ()) {
			Shell shell = getShell ();
			short [] part = new short [1];
			OS.GetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, null, 2, null, part);
			if (part [0] != OS.kControlFocusNoPart) {
				if (caret != null) caret.setFocus ();
				OS.ActivateTSMDocument (shell.imHandle);
			} else {
				if (caret != null) caret.killFocus ();
				OS.DeactivateTSMDocument (shell.imHandle);
			}
		}
	}
	return result;
}

int kEventTextInputOffsetToPos (int nextHandler, int theEvent, int userData) {
	if (ime != null) {
		int result = ime.kEventTextInputOffsetToPos (nextHandler, theEvent, userData);
		if (result != OS.eventNotHandledErr) return result;
	}
	return super.kEventTextInputOffsetToPos (nextHandler, theEvent, userData);
}

int kEventTextInputPosToOffset (int nextHandler, int theEvent, int userData) {
	if (ime != null) {
		int result = ime.kEventTextInputPosToOffset (nextHandler, theEvent, userData);
		if (result != OS.eventNotHandledErr) return result;
	}
	return super.kEventTextInputPosToOffset (nextHandler, theEvent, userData);
}

int kEventTextInputUnicodeForKeyEvent (int nextHandler, int theEvent, int userData) {
	int result = super.kEventTextInputUnicodeForKeyEvent(nextHandler, theEvent, userData);
	if (result != OS.noErr) {
		if (caret != null) {
			if (OS.CGCursorIsVisible ()) OS.CGDisplayHideCursor (OS.CGMainDisplayID ());
		}
	}
	return result;
}

int kEventTextInputUpdateActiveInputArea (int nextHandler, int theEvent, int userData) {
	if (ime != null) {
		int result = ime.kEventTextInputUpdateActiveInputArea (nextHandler, theEvent, userData);
		if (result != OS.eventNotHandledErr) return result;
	}
	return super.kEventTextInputUpdateActiveInputArea (nextHandler, theEvent, userData);
}

int kEventTextInputGetSelectedText (int nextHandler, int theEvent, int userData) {
	if (ime != null) {
		int result = ime.kEventTextInputGetSelectedText (nextHandler, theEvent, userData);
		if (result != OS.eventNotHandledErr) return result;
	}
	return super.kEventTextInputGetSelectedText (nextHandler, theEvent, userData);
}

void redrawWidget (int control, boolean children) {
	boolean isFocus = OS.VERSION < 0x1040 && caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	super.redrawWidget (control, children);
	if (isFocus) caret.setFocus ();
}

void redrawWidget (int control, int x, int y, int width, int height, boolean all) {
	boolean isFocus = OS.VERSION < 0x1040 && caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	super.redrawWidget (control, x, y, width, height, all);
	if (isFocus) caret.setFocus ();
}

void releaseChildren (boolean destroy) {
	if (caret != null) {
		caret.release (false);
		caret = null;
	}
	if (ime != null) {
		ime.release (false);
		ime = null;
	}
	super.releaseChildren (destroy);
}

/**
 * Scrolls a rectangular area of the receiver by first copying 
 * the source area to the destination and then causing the area
 * of the source which is not covered by the destination to
 * be repainted. Children that intersect the rectangle are
 * optionally moved during the operation. In addition, outstanding
 * paint events are flushed before the source area is copied to
 * ensure that the contents of the canvas are drawn correctly.
 *
 * @param destX the x coordinate of the destination
 * @param destY the y coordinate of the destination
 * @param x the x coordinate of the source
 * @param y the y coordinate of the source
 * @param width the width of the area
 * @param height the height of the area
 * @param all <code>true</code>if children should be scrolled, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void scroll (int destX, int destY, int x, int y, int width, int height, boolean all) {
	checkWidget();
	if (width <= 0 || height <= 0) return;
	int deltaX = destX - x, deltaY = destY - y;
	if (deltaX == 0 && deltaY == 0) return;
	if (!isDrawing ()) return;
	if (!OS.IsControlVisible (handle)) return;
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	Rectangle clientRect = getClientArea ();
	Rectangle sourceRect = new Rectangle (x, y, width, height);
	if (sourceRect.intersects (clientRect)) {
		update (all);
	}
	Control control = findBackgroundControl ();
	if (control != null && control.backgroundImage != null) {
		redrawWidget (handle, x, y, width, height, false);
		redrawWidget (handle, destX, destY, width, height, false);
	} else {
	    GC gc = new GC (this);
	    gc.copyArea (x, y, width, height, destX, destY);
	    gc.dispose ();
	}
    if (all) {
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			Control child = children [i];
			Rectangle rect = child.getBounds ();
			if (Math.min(x + width, rect.x + rect.width) >= Math.max (x, rect.x) && 
				Math.min(y + height, rect.y + rect.height) >= Math.max (y, rect.y)) {
					child.setLocation (rect.x + deltaX, rect.y + deltaY);
			}
		}
	}
	if (isFocus) caret.setFocus ();
}

/**
 * Sets the receiver's caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 * @param caret the new caret for the receiver, may be null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the caret has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCaret (Caret caret) {
	checkWidget();
	Caret newCaret = caret;
	Caret oldCaret = this.caret;
	this.caret = newCaret;
	if (hasFocus ()) {
		if (oldCaret != null) oldCaret.killFocus ();
		if (newCaret != null) {
			if (newCaret.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
			newCaret.setFocus ();
		}
	}
}

public void setFont (Font font) {
	checkWidget ();
	if (caret != null) caret.setFont (font);
	super.setFont (font);
}

/**
 * Sets the receiver's IME.
 * 
 * @param ime the new IME for the receiver, may be null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the IME has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public void setIME (IME ime) {
	checkWidget ();
	if (ime != null && ime.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	this.ime = ime;
}

}
