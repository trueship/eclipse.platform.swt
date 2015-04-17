package org.eclipse.swt.predicateeditor.cocoa;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.util.GuiUtil;
import org.eclipse.swt.util.NumericTextFieldValidator;

public class SWTNumericRowTemplate extends NSPredicateEditorRowTemplate {
    static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};
    
    private static final int NUMERIC_TEXTFIELD_WIDTH = 100;
    
    static Callback proc2Callback;
    static long /*int*/ proc2CallbackAddress;
    
    static Callback proc3Callback;
    static long /*int*/ proc3CallbackAddress;
    
    long /*int*/ jniRef;
    
    HashMap<String, String> keyPathToTitleMap;

    private NSTextField textField;

    private boolean isTextFieldInitialized;
    
    private NumericTextFieldValidator numericValidator;
    
    static {
        Class<SWTNumericRowTemplate> clazz = SWTNumericRowTemplate.class;
        
        proc2Callback = new Callback(clazz, "proc2", 2);
        proc2CallbackAddress = proc2Callback.getAddress();
        if (proc2CallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        proc3Callback = new Callback(clazz, "proc3", 3);
        proc3CallbackAddress = proc3Callback.getAddress();
        if (proc3CallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
               
        long /*int*/ cls = OS.objc_allocateClassPair(OS.class_NSPredicateEditorRowTemplate, SWTNumericRowTemplate.class.getSimpleName(), 0);
        byte[] types = {'*','\0'};
        int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;
        
        OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
        OS.class_addMethod(cls, OS.sel_templateViews, proc2CallbackAddress, "@:");
        OS.class_addMethod(cls, OS.sel_copyWithZone_, proc3CallbackAddress, "@:@");
        OS.class_addMethod(cls, OS.sel_controlTextDidChange, proc3CallbackAddress, "v@:@");
        OS.class_addMethod(cls, OS.sel_dealloc, proc2CallbackAddress, "@:");
        
        OS.objc_registerClassPair(cls);
    }
    
    public SWTNumericRowTemplate(HashMap<String, String> keyPathToTitleMap) {
        super(0);
        alloc();
        
        this.keyPathToTitleMap = keyPathToTitleMap;
        
        jniRef = OS.NewGlobalRef(this);
        if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
        OS.object_setInstanceVariable(this.id, SWT_OBJECT, jniRef);
    }

    public id initWithLeftExpressions(NSArray leftExpressions, long /*int*/ attributeType, long /*int*/ modifier, NSArray operators, long /*int*/ options) {
        return super.initWithLeftExpressions(leftExpressions, attributeType, modifier, operators, options);
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
        
        GuiUtil.replaceKeyPathWithTitle(new NSPopUpButton(views.objectAtIndex(0)), keyPathToTitleMap);
        
        if (!isTextFieldInitialized)
            initializeTextField(views.objectAtIndex(2).id);
        
        return views.id;
    }
    
    static SWTNumericRowTemplate getThis(long /*int*/ id) {
        if (id == 0) return null;
        
        long /*int*/ [] jniRef = new long /*int*/ [1];
        OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
        if (jniRef[0] == 0) return null;
        
        return (SWTNumericRowTemplate)OS.JNIGetObject(jniRef[0]);
    }
    
    static long /*int*/ proc2(long /*int*/ id, long /*int*/ sel) {
        SWTNumericRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        if (sel == OS.sel_templateViews) {
            return template.templateViewsProc();
        } else if (sel == OS.sel_dealloc) {
            return template.deallocProc();
        }
        
        return 0;
    }
    
    static long /*int*/ proc3(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
        SWTNumericRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        if (sel == OS.sel_copyWithZone_) {
            return template.copyWithZoneProc(arg0);
        } else if (sel == OS.sel_controlTextDidChange) {
            return template.controlTextDidChange(arg0);
        }
        
        return 0;
    }
    
    long /*int*/ copyWithZoneProc(long /*int*/ arg0) {
        SWTNumericRowTemplate newTemplate = new SWTNumericRowTemplate(keyPathToTitleMap);
        
        newTemplate.initWithLeftExpressions(
                this.leftExpressions(), 
                this.rightExpressionAttributeType(), 
                this.modifier(), 
                this.operators(), 
                this.options());
        
        return newTemplate.id;
    }
    
    // Used for allowing only numeric input into the text field.
    // (alternative to using a custom NSNumberFormatter, as for some reason the isPartialStringValid* overriden methods are not called in SWT).
    private long /*int*/ controlTextDidChange(long /*int*/ arg0) {
        NSNotification notification = new NSNotification(arg0);
        
        if (notification.object().id == this.textField.id)
            numericValidator().validate();
        
        return 0;
    }
    
    private NumericTextFieldValidator numericValidator() {
        if (numericValidator == null)
            numericValidator = new NumericTextFieldValidator(textField, this.rightExpressionAttributeType());
        
        return numericValidator;
    }

    long /*int*/ deallocProc() {
        internal_dispose();
        
        objc_super super_struct = new objc_super();
        super_struct.receiver = id;
        super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
        
        return OS.objc_msgSendSuper(super_struct, OS.sel_dealloc);
    }
    
    private void initializeTextField(long /*int*/ id) {
        textField = new NSTextField(id);
        textField.setAlignment(OS.NSRightTextAlignment);
        GuiUtil.changeTextFieldSize(textField, NUMERIC_TEXTFIELD_WIDTH, textField.frame().height);
        textField.setDelegate(this);
    }
}
