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
 * Automated Test Suite for class org.eclipse.swt.dnd.TextTransfer
 *
 * @see org.eclipse.swt.dnd.TextTransfer
 */
public class Test_org_eclipse_swt_dnd_TextTransfer extends Test_org_eclipse_swt_dnd_ByteArrayTransfer {

public Test_org_eclipse_swt_dnd_TextTransfer(String name) {
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

public void test_getInstance() {
	warnUnimpl("Test test_getInstance not written");
}

public void test_javaToNativeLjava_lang_ObjectLorg_eclipse_swt_dnd_TransferData() {
	warnUnimpl("Test test_javaToNativeLjava_lang_ObjectLorg_eclipse_swt_dnd_TransferData not written");
}

public void test_nativeToJavaLorg_eclipse_swt_dnd_TransferData() {
	warnUnimpl("Test test_nativeToJavaLorg_eclipse_swt_dnd_TransferData not written");
}

public void test_getTypeIds() {
	warnUnimpl("Test test_getTypeIds not written");
}

public void test_getTypeNames() {
	warnUnimpl("Test test_getTypeNames not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_dnd_TextTransfer((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_getInstance");
	methodNames.addElement("test_javaToNativeLjava_lang_ObjectLorg_eclipse_swt_dnd_TransferData");
	methodNames.addElement("test_nativeToJavaLorg_eclipse_swt_dnd_TransferData");
	methodNames.addElement("test_getTypeIds");
	methodNames.addElement("test_getTypeNames");
	methodNames.addAll(Test_org_eclipse_swt_dnd_ByteArrayTransfer.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_getInstance")) test_getInstance();
	else if (getName().equals("test_javaToNativeLjava_lang_ObjectLorg_eclipse_swt_dnd_TransferData")) test_javaToNativeLjava_lang_ObjectLorg_eclipse_swt_dnd_TransferData();
	else if (getName().equals("test_nativeToJavaLorg_eclipse_swt_dnd_TransferData")) test_nativeToJavaLorg_eclipse_swt_dnd_TransferData();
	else if (getName().equals("test_getTypeIds")) test_getTypeIds();
	else if (getName().equals("test_getTypeNames")) test_getTypeNames();
	else super.runTest();
}
}
