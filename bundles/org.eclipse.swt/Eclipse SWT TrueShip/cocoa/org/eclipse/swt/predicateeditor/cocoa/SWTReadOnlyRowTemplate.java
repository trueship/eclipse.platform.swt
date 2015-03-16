package org.eclipse.swt.predicateeditor.cocoa;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;

public class SWTReadOnlyRowTemplate extends NSPredicateEditorRowTemplate {
    static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};
    
    static Callback proc2Callback;
    static long /*int*/ proc2CallbackAddress;
    
    static Callback copyWithZoneCallback;
    static long /*int*/ copyWithZoneCallbackAddress;
    
    long /*int*/ jniRef;
    
    HashMap<String, String> keyPathToTitleMap;
    
    static {
        Class<SWTReadOnlyRowTemplate> clazz = SWTReadOnlyRowTemplate.class;
        
        proc2Callback = new Callback(clazz, "proc2", 2);
        proc2CallbackAddress = proc2Callback.getAddress();
        if (proc2CallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        copyWithZoneCallback = new Callback(clazz, "copyWithZoneProc", 3);
        copyWithZoneCallbackAddress = copyWithZoneCallback.getAddress();
        if (copyWithZoneCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
               
        long /*int*/ cls = OS.objc_allocateClassPair(OS.class_NSPredicateEditorRowTemplate, SWTReadOnlyRowTemplate.class.getSimpleName(), 0);
        byte[] types = {'*','\0'};
        int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;
        
        OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
        OS.class_addMethod(cls, OS.sel_templateViews, proc2CallbackAddress, "@:");
        OS.class_addMethod(cls, OS.sel_copyWithZone_, copyWithZoneCallbackAddress, "@:@");
        OS.class_addMethod(cls, OS.sel_dealloc, proc2CallbackAddress, "@:");
        
        OS.objc_registerClassPair(cls);
    }
    
    public SWTReadOnlyRowTemplate(HashMap<String, String> keyPathToTitleMap) {
        super(0);
        alloc();
        
        this.keyPathToTitleMap = keyPathToTitleMap;
        
        jniRef = OS.NewGlobalRef(this);
        if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
        OS.object_setInstanceVariable(this.id, SWT_OBJECT, jniRef);
    }

    public id initWithLeftExpressions(NSArray leftExpressions, NSArray rightExpressions, long /*int*/ modifier, NSArray operators, long /*int*/ options) {
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
        
        return views.id;
    }
    
    static SWTReadOnlyRowTemplate getThis(long /*int*/ id) {
        if (id == 0) return null;
        
        long /*int*/ [] jniRef = new long /*int*/ [1];
        OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
        if (jniRef[0] == 0) return null;
        
        return (SWTReadOnlyRowTemplate)OS.JNIGetObject(jniRef[0]);
    }
    
    static long /*int*/ proc2(long /*int*/ id, long /*int*/ sel) {
        SWTReadOnlyRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        if (sel == OS.sel_templateViews) {
            return template.templateViewsProc();
        } else if (sel == OS.sel_dealloc) {
            return template.deallocProc();
        }
        
        return 0;
    }
    
    static long /*int*/ copyWithZoneProc(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
        SWTReadOnlyRowTemplate thisTemplate = getThis(id);
        if (thisTemplate == null) return 0;
           
        SWTReadOnlyRowTemplate newTemplate = new SWTReadOnlyRowTemplate(thisTemplate.keyPathToTitleMap);
        
        newTemplate.initWithLeftExpressions(
                thisTemplate.leftExpressions(), 
                thisTemplate.rightExpressions(), 
                thisTemplate.modifier(), 
                thisTemplate.operators(), 
                thisTemplate.options());
        
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
}
