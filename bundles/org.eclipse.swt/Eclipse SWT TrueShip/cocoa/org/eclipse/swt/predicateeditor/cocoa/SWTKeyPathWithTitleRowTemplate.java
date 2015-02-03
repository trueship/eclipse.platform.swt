package org.eclipse.swt.predicateeditor.cocoa;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.widgets.PredicateEditor.AttributeType;

public class SWTKeyPathWithTitleRowTemplate extends NSPredicateEditorRowTemplate {
    private static final int NUMERIC_TEXTFIELD_WIDTH = 100;

    static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};
    
    static Callback proc2Callback;
    static long /*int*/ proc2CallbackAddress;
    
    static Callback copyWithZoneCallback;
    static long /*int*/ copyWithZoneCallbackAddress;
    
    long /*int*/ jniRef;
    
    HashMap<String, String> keyPathToTitleMap;
    
    boolean initWithAttributeType;
    long /*int*/ attributeType;

    private boolean hasDateAndTimeRightExpression;
    private boolean updatedRow;
    
    static HashMap<String, String> numericToDateOperatorsTitleMap = new HashMap<String, String>();
    
    static {
        numericToDateOperatorsTitleMap.put("is less than", "is before");
        numericToDateOperatorsTitleMap.put("is less than or equal to", "is before or on");
        numericToDateOperatorsTitleMap.put("is greater than", "is after");
        numericToDateOperatorsTitleMap.put("is greater than or equal to", "is after or on");
    }
    
    static {
        Class<SWTKeyPathWithTitleRowTemplate> clazz = SWTKeyPathWithTitleRowTemplate.class;
        
        proc2Callback = new Callback(clazz, "proc2", 2);
        proc2CallbackAddress = proc2Callback.getAddress();
        if (proc2CallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        copyWithZoneCallback = new Callback(clazz, "copyWithZoneProc", 3);
        copyWithZoneCallbackAddress = copyWithZoneCallback.getAddress();
        if (copyWithZoneCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
               
        long /*int*/ cls = OS.objc_allocateClassPair(OS.class_NSPredicateEditorRowTemplate, SWTKeyPathWithTitleRowTemplate.class.getSimpleName(), 0);
        byte[] types = {'*','\0'};
        int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;
        
        OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
        OS.class_addMethod(cls, OS.sel_templateViews, proc2CallbackAddress, "@:");
        OS.class_addMethod(cls, OS.sel_copyWithZone_, copyWithZoneCallbackAddress, "@:@");
        OS.class_addMethod(cls, OS.sel_dealloc, proc2CallbackAddress, "@:");
        
        OS.objc_registerClassPair(cls);
    }
    
    public SWTKeyPathWithTitleRowTemplate (HashMap<String, String> keyPathToTitleMap) {
        this(keyPathToTitleMap, false);
    }
    
    public SWTKeyPathWithTitleRowTemplate(HashMap<String, String> keyPathToTitleMap, boolean hasDateAndTimeRightExpression) {
        super(0);
        alloc();
        
        this.keyPathToTitleMap = keyPathToTitleMap;
        this.hasDateAndTimeRightExpression = hasDateAndTimeRightExpression;
        
        jniRef = OS.NewGlobalRef(this);
        if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
        OS.object_setInstanceVariable(this.id, SWT_OBJECT, jniRef);
    }

    public id initWithLeftExpressions(NSArray leftExpressions, long /*int*/ attributeType, long /*int*/ modifier, NSArray operators, long /*int*/ options) {
        initWithAttributeType = true;
        this.attributeType = attributeType;
        
        return super.initWithLeftExpressions(leftExpressions, attributeType, modifier, operators, options);
    }

    public id initWithLeftExpressions(NSArray leftExpressions, NSArray rightExpressions, long /*int*/ modifier, NSArray operators, long /*int*/ options) {
        initWithAttributeType = false;
        
        return super.initWithLeftExpressions(leftExpressions, rightExpressions, modifier, operators, options);
    }
    
    public void internal_dispose() {
        if (jniRef != 0) OS.DeleteGlobalRef(jniRef);
        jniRef = 0;
        OS.object_setInstanceVariable(this.id, SWT_OBJECT, 0);
    }
    
    long /*int*/ superTemplateViews() {
        objc_super super_struct = new objc_super();
        super_struct.receiver = id;
        super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
        
        return OS.objc_msgSendSuper(super_struct, OS.sel_templateViews);
    }
    
    long /*int*/ templateViewsProc() {
        NSArray views = new NSArray(this.superTemplateViews());
        
        replaceKeyPathWithTitle(new NSPopUpButton(views.objectAtIndex(0)));
        
        if (!updatedRow)
            updateRowForRightSideType(views);
        
        return views.id;
    }

    private void updateRowForRightSideType(NSArray views) {
        if (isNumericTypeRow())
            adjustTextFieldForNumericType(new NSTextField(views.objectAtIndex(2)));
        else if (isDateTypeRow()) {
            makeDateTimePredicateOperators(new NSView(views.objectAtIndex(1)));
            adjustDateTimeControl(new NSView(views.objectAtIndex(2)));
        }
        
        updatedRow = true;
    }
    
    private void adjustTextFieldForNumericType(NSTextField textField) {
        NSSize size = new NSSize();
        size.width = NUMERIC_TEXTFIELD_WIDTH;
        size.height = textField.frame().height;
        textField.setFrameSize(size);
        
        textField.setAlignment(OS.NSRightTextAlignment);
    }
    
    static SWTKeyPathWithTitleRowTemplate getThis(long /*int*/ id) {
        if (id == 0) return null;
        
        long /*int*/ [] jniRef = new long /*int*/ [1];
        OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
        if (jniRef[0] == 0) return null;
        
        return (SWTKeyPathWithTitleRowTemplate)OS.JNIGetObject(jniRef[0]);
    }
    
    static long /*int*/ proc2(long /*int*/ id, long /*int*/ sel) {
        SWTKeyPathWithTitleRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        if (sel == OS.sel_templateViews) {
            return template.templateViewsProc();
        } else if (sel == OS.sel_dealloc) {
            return template.deallocProc();
        }
        
        return 0;
    }
    
    static long /*int*/ copyWithZoneProc(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
        SWTKeyPathWithTitleRowTemplate thisTemplate = getThis(id);
        if (thisTemplate == null) return 0;
           
        SWTKeyPathWithTitleRowTemplate newTemplate = new SWTKeyPathWithTitleRowTemplate(thisTemplate.keyPathToTitleMap);
        
        if (thisTemplate.initWithAttributeType)
            newTemplate.initWithLeftExpressions(
                    thisTemplate.leftExpressions(), 
                    thisTemplate.rightExpressionAttributeType(), 
                    thisTemplate.modifier(), 
                    thisTemplate.operators(), 
                    thisTemplate.options());
        else
            newTemplate.initWithLeftExpressions(
                    thisTemplate.leftExpressions(), 
                    thisTemplate.rightExpressions(), 
                    thisTemplate.modifier(), 
                    thisTemplate.operators(), 
                    thisTemplate.options());
        
        newTemplate.hasDateAndTimeRightExpression = thisTemplate.hasDateAndTimeRightExpression;
        
        return newTemplate.id;
    }
    
    long /*int*/ deallocProc() {
        internal_dispose();
        
        objc_super super_struct = new objc_super();
        super_struct.receiver = id;
        super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
        
        return OS.objc_msgSendSuper(super_struct, OS.sel_dealloc);
    }
    
    private void replaceKeyPathWithTitle(NSPopUpButton button) {
        NSArray items = button.itemArray();
        for (int i = 0; i < items.count(); i++) {
            NSMenuItem item = new NSMenuItem(items.objectAtIndex(i).id);
            NSExpression keyPathExpression = new NSExpression(item.representedObject());
            String title = this.keyPathToTitleMap.get(keyPathExpression.keyPath().getString());
            if (title != null)
                item.setTitle(NSString.stringWith(title));
        }
    }
    
    private void adjustDateTimeControl(NSView view) {
        if (!view.isKindOfClass(OS.class_NSDatePicker)) return;
        
        NSDatePicker datePicker = new NSDatePicker(view.id);
        
        if (hasDateAndTimeRightExpression) {
            datePicker.setDatePickerElements(OS.NSYearMonthDayDatePickerElementFlag | OS.NSHourMinuteSecondDatePickerElementFlag);
            datePicker.sizeToFit();
        } else if (isDateOnlyDatePicker(datePicker)) {
            setDatePickerTimeToZero(datePicker);
        }
    }
    
    private boolean isNumericTypeRow() {
        return (initWithAttributeType && 
                (attributeType == AttributeType.NSDecimalAttributeType.value() ||
                 attributeType == AttributeType.NSDoubleAttributeType.value() ||
                 attributeType == AttributeType.NSFloatAttributeType.value() ||
                 attributeType == AttributeType.NSInteger16AttributeType.value() ||
                 attributeType == AttributeType.NSInteger32AttributeType.value() ||
                 attributeType == AttributeType.NSInteger64AttributeType.value()
                 ));
    }
    
    private boolean isDateTypeRow() {
        return (initWithAttributeType && attributeType == AttributeType.NSDateAttributeType.value());
    }

    private void makeDateTimePredicateOperators(NSView view) {
        NSPopUpButton operatorButton = new NSPopUpButton(view);
        
        NSArray items = operatorButton.itemArray();
        for (int i = 0; i < items.count(); i++) {
            NSMenuItem item = new NSMenuItem(items.objectAtIndex(i).id);
            String newTitle = numericToDateOperatorsTitleMap.get(item.title().getString());
            if (newTitle != null)
                item.setTitle(NSString.stringWith(newTitle));
        }
    }
    
    private boolean isDateOnlyDatePicker(NSDatePicker datePicker) {
        long /*int*/ style = datePicker.datePickerStyle();
        return (style == OS.NSTextFieldAndStepperDatePickerStyle || style == OS.NSTextFieldDatePickerStyle);
    }
    
    private void setDatePickerTimeToZero(NSDatePicker datePicker) {
        NSDate selectedDate = datePicker.dateValue();
        NSCalendar calendar = new NSCalendar(NSCalendar.currentCalendar());
        datePicker.setDateValue(calendar.dateFromComponents(calendar.components((OS.NSDayCalendarUnit | OS.NSMonthCalendarUnit | OS.NSYearCalendarUnit), selectedDate)));
    }
}
