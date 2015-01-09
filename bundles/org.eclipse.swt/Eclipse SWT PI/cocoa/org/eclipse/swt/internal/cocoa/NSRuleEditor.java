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

public class NSRuleEditor extends NSControl {

public NSRuleEditor() {
	super();
}

public NSRuleEditor(long /*int*/ id) {
	super(id);
}

public NSRuleEditor(id id) {
	super(id);
}

public void addRow(id sender) {
	OS.objc_msgSend(this.id, OS.sel_addRow_, sender != null ? sender.id : 0);
}

public boolean canRemoveAllRows() {
	return OS.objc_msgSend_bool(this.id, OS.sel_canRemoveAllRows);
}

public NSArray criteriaForRow(long /*int*/ row) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_criteriaForRow_, row);
	return result != 0 ? new NSArray(result) : null;
}

public NSString criteriaKeyPath() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_criteriaKeyPath);
	return result != 0 ? new NSString(result) : null;
}

public id delegate() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public NSArray displayValuesForRow(long /*int*/ row) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_displayValuesForRow_, row);
	return result != 0 ? new NSArray(result) : null;
}

public NSString displayValuesKeyPath() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_displayValuesKeyPath);
	return result != 0 ? new NSString(result) : null;
}

public NSDictionary formattingDictionary() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_formattingDictionary);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSString formattingStringsFilename() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_formattingStringsFilename);
	return result != 0 ? new NSString(result) : null;
}

public void insertRowAtIndex(long /*int*/ rowIndex, long /*int*/ rowType, long /*int*/ parentRow, boolean shouldAnimate) {
	OS.objc_msgSend(this.id, OS.sel_insertRowAtIndex_withType_asSubrowOfRow_animate_, rowIndex, rowType, parentRow, shouldAnimate);
}

public boolean isEditable() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isEditable);
}

public long /*int*/ nestingMode() {
	return OS.objc_msgSend(this.id, OS.sel_nestingMode);
}

public long /*int*/ numberOfRows() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfRows);
}

public long /*int*/ parentRowForRow(long /*int*/ rowIndex) {
	return OS.objc_msgSend(this.id, OS.sel_parentRowForRow_, rowIndex);
}

public NSPredicate predicate() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_predicate);
	return result != 0 ? new NSPredicate(result) : null;
}

public NSPredicate predicateForRow(long /*int*/ row) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_predicateForRow_, row);
	return result != 0 ? new NSPredicate(result) : null;
}

public void reloadCriteria() {
	OS.objc_msgSend(this.id, OS.sel_reloadCriteria);
}

public void reloadPredicate() {
	OS.objc_msgSend(this.id, OS.sel_reloadPredicate);
}

public void removeRowAtIndex(long /*int*/ rowIndex) {
	OS.objc_msgSend(this.id, OS.sel_removeRowAtIndex_, rowIndex);
}

public void removeRowsAtIndexes(NSIndexSet rowIndexes, boolean includeSubrows) {
	OS.objc_msgSend(this.id, OS.sel_removeRowsAtIndexes_includeSubrows_, rowIndexes != null ? rowIndexes.id : 0, includeSubrows);
}

public long /*int*/ rowClass() {
	return OS.objc_msgSend(this.id, OS.sel_rowClass);
}

public long /*int*/ rowForDisplayValue(id displayValue) {
	return OS.objc_msgSend(this.id, OS.sel_rowForDisplayValue_, displayValue != null ? displayValue.id : 0);
}

public double /*float*/ rowHeight() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_rowHeight);
}

public long /*int*/ rowTypeForRow(long /*int*/ rowIndex) {
	return OS.objc_msgSend(this.id, OS.sel_rowTypeForRow_, rowIndex);
}

public NSString rowTypeKeyPath() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_rowTypeKeyPath);
	return result != 0 ? new NSString(result) : null;
}

public void selectRowIndexes(NSIndexSet indexes, boolean extend) {
	OS.objc_msgSend(this.id, OS.sel_selectRowIndexes_byExtendingSelection_, indexes != null ? indexes.id : 0, extend);
}

public NSIndexSet selectedRowIndexes() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_selectedRowIndexes);
	return result != 0 ? new NSIndexSet(result) : null;
}

public void setCanRemoveAllRows(boolean val) {
	OS.objc_msgSend(this.id, OS.sel_setCanRemoveAllRows_, val);
}

public void setCriteria(NSArray criteria, NSArray values, long /*int*/ rowIndex) {
	OS.objc_msgSend(this.id, OS.sel_setCriteria_andDisplayValues_forRowAtIndex_, criteria != null ? criteria.id : 0, values != null ? values.id : 0, rowIndex);
}

public void setCriteriaKeyPath(NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_setCriteriaKeyPath_, keyPath != null ? keyPath.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, delegate != null ? delegate.id : 0);
}

public void setDisplayValuesKeyPath(NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_setDisplayValuesKeyPath_, keyPath != null ? keyPath.id : 0);
}

public void setEditable(boolean editable) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_, editable);
}

public void setFormattingDictionary(NSDictionary dictionary) {
	OS.objc_msgSend(this.id, OS.sel_setFormattingDictionary_, dictionary != null ? dictionary.id : 0);
}

public void setFormattingStringsFilename(NSString stringsFilename) {
	OS.objc_msgSend(this.id, OS.sel_setFormattingStringsFilename_, stringsFilename != null ? stringsFilename.id : 0);
}

public void setNestingMode(long /*int*/ mode) {
	OS.objc_msgSend(this.id, OS.sel_setNestingMode_, mode);
}

public void setRowClass(long /*int*/ rowClass) {
	OS.objc_msgSend(this.id, OS.sel_setRowClass_, rowClass);
}

public void setRowHeight(double /*float*/ height) {
	OS.objc_msgSend(this.id, OS.sel_setRowHeight_, height);
}

public void setRowTypeKeyPath(NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_setRowTypeKeyPath_, keyPath != null ? keyPath.id : 0);
}

public void setSubrowsKeyPath(NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_setSubrowsKeyPath_, keyPath != null ? keyPath.id : 0);
}

public NSIndexSet subrowIndexesForRow(long /*int*/ rowIndex) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_subrowIndexesForRow_, rowIndex);
	return result != 0 ? new NSIndexSet(result) : null;
}

public NSString subrowsKeyPath() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_subrowsKeyPath);
	return result != 0 ? new NSString(result) : null;
}

public static long /*int*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSRuleEditor, OS.sel_cellClass);
}

public static void setCellClass(long /*int*/ factoryId) {
	OS.objc_msgSend(OS.class_NSRuleEditor, OS.sel_setCellClass_, factoryId);
}

}
