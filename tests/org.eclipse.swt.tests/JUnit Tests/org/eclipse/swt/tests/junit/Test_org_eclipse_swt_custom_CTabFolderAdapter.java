package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2002. All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.CTabFolderAdapter
 *
 * @see org.eclipse.swt.custom.CTabFolderAdapter
 */
public class Test_org_eclipse_swt_custom_CTabFolderAdapter extends SwtTestCase {

public Test_org_eclipse_swt_custom_CTabFolderAdapter(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_itemClosedLorg_eclipse_swt_custom_CTabFolderEvent() {
	warnUnimpl("Test test_itemClosedLorg_eclipse_swt_custom_CTabFolderEvent not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_CTabFolderAdapter((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_itemClosedLorg_eclipse_swt_custom_CTabFolderEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_itemClosedLorg_eclipse_swt_custom_CTabFolderEvent")) test_itemClosedLorg_eclipse_swt_custom_CTabFolderEvent();
}
}
