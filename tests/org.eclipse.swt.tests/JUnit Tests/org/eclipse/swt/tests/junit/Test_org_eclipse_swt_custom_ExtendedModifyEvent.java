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
 * Automated Test Suite for class org.eclipse.swt.custom.ExtendedModifyEvent
 *
 * @see org.eclipse.swt.custom.ExtendedModifyEvent
 */
public class Test_org_eclipse_swt_custom_ExtendedModifyEvent extends Test_org_eclipse_swt_events_TypedEvent {

public Test_org_eclipse_swt_custom_ExtendedModifyEvent(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
}

protected void tearDown() {
	super.tearDown();
}

public void test_ConstructorLorg_eclipse_swt_custom_StyledTextEvent() {
	// this method tested by other test classes
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_ExtendedModifyEvent((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_custom_StyledTextEvent");
	methodNames.addAll(Test_org_eclipse_swt_events_TypedEvent.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_custom_StyledTextEvent")) test_ConstructorLorg_eclipse_swt_custom_StyledTextEvent();
	else super.runTest();
}
}
