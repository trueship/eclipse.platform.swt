package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2002. All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

/**
 * Automated Test Suite for class org.eclipse.swt.events.FocusAdapter
 *
 * @see org.eclipse.swt.events.FocusAdapter
 */
public class Test_org_eclipse_swt_events_FocusAdapter extends SwtTestCase {

public Test_org_eclipse_swt_events_FocusAdapter(String name) {
	super(name);
}


protected void setUp() {
}

protected void tearDown() {
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_focusGainedLorg_eclipse_swt_events_FocusEvent() {
	warnUnimpl("Test test_focusGainedLorg_eclipse_swt_events_FocusEvent not written");
}

public void test_focusLostLorg_eclipse_swt_events_FocusEvent() {
	warnUnimpl("Test test_focusLostLorg_eclipse_swt_events_FocusEvent not written");
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_focusGainedLorg_eclipse_swt_events_FocusEvent");
	methodNames.addElement("test_focusLostLorg_eclipse_swt_events_FocusEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_focusGainedLorg_eclipse_swt_events_FocusEvent")) test_focusGainedLorg_eclipse_swt_events_FocusEvent();
	else if (getName().equals("test_focusLostLorg_eclipse_swt_events_FocusEvent")) test_focusLostLorg_eclipse_swt_events_FocusEvent();
}
}
