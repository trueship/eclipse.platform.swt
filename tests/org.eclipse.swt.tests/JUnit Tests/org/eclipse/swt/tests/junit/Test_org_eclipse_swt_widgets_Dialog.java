package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2002. All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Dialog
 *
 * @see org.eclipse.swt.widgets.Dialog
 */
public class Test_org_eclipse_swt_widgets_Dialog extends SwtTestCase {

public Shell shell;
private Dialog dialog;
private int junitStyle;

public Test_org_eclipse_swt_widgets_Dialog(String name) {
	super(name);
}


protected void setUp() {
	shell = new Shell();
}

protected void tearDown() {
	shell.dispose();
}

protected void setDialog(Dialog newDialog) {
	dialog = newDialog;
}
protected void setStyle(int style) {
	style = junitStyle;
}

public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_Shell not written");
}

public void test_ConstructorLorg_eclipse_swt_widgets_ShellI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_ShellI not written");
}

public void test_checkSubclass() {
	warnUnimpl("Test test_checkSubclass not written");
}

public void test_getParent() {
	assertTrue(":a:", dialog.getParent() == shell);
}

public void test_getStyle () {
	// we use this call in a Constructor test so that we can
	// check if the style is the one that was created
	dialog.getStyle();
}

public void test_getText() {
	warnUnimpl("Test test_getText not written");
}

public void test_setTextLjava_lang_String() {
	assertTrue(":1:", dialog.getText() == "");
	String testStr = "test string";
	dialog.setText(testStr);
	assertTrue(":2:", dialog.getText().equals(testStr));
	dialog.setText("");
	assertTrue(":3:", dialog.getText().equals(""));
	if (fCheckSwtNullExceptions) {
		try {
			dialog.setText(null);
			fail("No exception thrown for string = null");
		}
		catch (IllegalArgumentException e) {
		}
	}
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Shell");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ShellI");
	methodNames.addElement("test_checkSubclass");
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_getStyle");
	methodNames.addElement("test_getText");
	methodNames.addElement("test_setTextLjava_lang_String");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Shell")) test_ConstructorLorg_eclipse_swt_widgets_Shell();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ShellI")) test_ConstructorLorg_eclipse_swt_widgets_ShellI();
	else if (getName().equals("test_checkSubclass")) test_checkSubclass();
	else if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_getStyle")) test_getStyle();
	else if (getName().equals("test_getText")) test_getText();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
}
}
