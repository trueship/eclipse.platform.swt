package org.eclipse.swt.predicateeditor.cocoa;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.widgets.PredicateEditor.ComparisonPredicateModifier;

public class SWTReadOnlyAlternativeRowTemplate extends NSPredicateEditorRowTemplate {
    static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};
    
    private static final int TEXTFIELD_WIDTH = 150;
    private static final int TEXTFIELD_HEIGHT = 22;
    
    static Callback proc2Callback;
    static long /*int*/ proc2CallbackAddress;
    
    static Callback proc3Callback;
    static long /*int*/ proc3CallbackAddress;
    
    static CallbackFpret matchForPredicateCallback;
    static long /*int*/ matchForPredicateCallbackAddress;
    
    long /*int*/ jniRef;
    
    HashMap<String, String> keyPathToTitleMap;
    List<String> alternativeToRightExpressions;
    
    NSTextField otherTextField;
    
    static {
        Class<SWTReadOnlyAlternativeRowTemplate> clazz = SWTReadOnlyAlternativeRowTemplate.class;
        
        proc2Callback = new Callback(clazz, "proc2", 2);
        proc2CallbackAddress = proc2Callback.getAddress();
        if (proc2CallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        proc3Callback = new Callback(clazz, "proc3", 3);
        proc3CallbackAddress = proc3Callback.getAddress();
        if (proc3CallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        matchForPredicateCallback = new CallbackFpret(clazz, "procMatchForPredicate", 3);
        matchForPredicateCallbackAddress = matchForPredicateCallback.getAddress();
        if (matchForPredicateCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
               
        long /*int*/ cls = OS.objc_allocateClassPair(OS.class_NSPredicateEditorRowTemplate, SWTReadOnlyAlternativeRowTemplate.class.getSimpleName(), 0);
        byte[] types = {'*','\0'};
        int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;
        
        OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
        OS.class_addMethod(cls, OS.sel_templateViews, proc2CallbackAddress, "@:");
        OS.class_addMethod(cls, OS.sel_copyWithZone_, proc3CallbackAddress, "@:@");
        OS.class_addMethod(cls, OS.sel_setPredicate_, proc3CallbackAddress, "v@:@");
        OS.class_addMethod(cls, OS.sel_predicateWithSubpredicates_, proc3CallbackAddress, "@:@");
        OS.class_addMethod(cls, OS.sel_matchForPredicate_, matchForPredicateCallbackAddress, "d@:@");
        OS.class_addMethod(cls, OS.sel_dealloc, proc2CallbackAddress, "@:");
        
        OS.objc_registerClassPair(cls);
    }
    
    public SWTReadOnlyAlternativeRowTemplate(HashMap<String, String> keyPathToTitleMap, List<String> alternativeToRightExpressions) {
        super(0);
        alloc();
        
        this.keyPathToTitleMap = keyPathToTitleMap;
        this.alternativeToRightExpressions = alternativeToRightExpressions;
        
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
        NSMutableArray views = new NSMutableArray(new NSArray(this.superTemplateViews()).mutableCopy());
        
        views.addObject(otherTextField());
        
        replaceKeyPathWithTitle(new NSPopUpButton(views.objectAtIndex(0)));
        
        views.autorelease();
        
        return views.id;
    }
    
    NSTextField otherTextField() {
        if (otherTextField == null) {
            otherTextField = (NSTextField) new NSTextField().alloc();
            
            NSRect rect = new NSRect();
            rect.width = TEXTFIELD_WIDTH;
            rect.height = TEXTFIELD_HEIGHT;
            otherTextField.initWithFrame(rect);
        }
        
        return otherTextField;
    }
    
    static SWTReadOnlyAlternativeRowTemplate getThis(long /*int*/ id) {
        if (id == 0) return null;
        
        long /*int*/ [] jniRef = new long /*int*/ [1];
        OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
        if (jniRef[0] == 0) return null;
        
        return (SWTReadOnlyAlternativeRowTemplate)OS.JNIGetObject(jniRef[0]);
    }
    
    static long /*int*/ proc2(long /*int*/ id, long /*int*/ sel) {
        SWTReadOnlyAlternativeRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        if (sel == OS.sel_templateViews) {
            return template.templateViewsProc();
        } else if (sel == OS.sel_dealloc) {
            return template.deallocProc();
        }
        
        return 0;
    }
    
    static long /*int*/ proc3(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
        SWTReadOnlyAlternativeRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        if (sel == OS.sel_setPredicate_) {
            return template.setPredicateProc(arg0);
        } else if (sel == OS.sel_predicateWithSubpredicates_) {
            return template.predicateWithSubpredicatesProc(arg0);
        } else if (sel == OS.sel_copyWithZone_) {
            return template.copyWithZoneProc(arg0);
        }
        
        return 0;
    }
    
    long /*int*/ copyWithZoneProc(long /*int*/ arg0) {
        SWTReadOnlyAlternativeRowTemplate newTemplate = new SWTReadOnlyAlternativeRowTemplate(keyPathToTitleMap, alternativeToRightExpressions);
        
        newTemplate.initWithLeftExpressions(this.leftExpressions(), 
                                            this.rightExpressions(), 
                                            this.modifier(), 
                                            this.operators(), 
                                            this.options());
        
        return newTemplate.id;
    }
    
    long /*int*/ superPredicateWithSubpredicates(long /*int*/ predicate) {
        objc_super super_struct = new objc_super();
        super_struct.receiver = id;
        super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
        
        return OS.objc_msgSendSuper(super_struct, OS.sel_predicateWithSubpredicates_, predicate);
    }
    
    long /*int*/ predicateWithSubpredicatesProc(long /*int*/ arg0) {
        NSPredicate predicate = new NSPredicate(superPredicateWithSubpredicates(arg0));
        
        if (!predicate.isKindOfClass(OS.class_NSComparisonPredicate)) return predicate.id;
        
        NSComparisonPredicate comparisonPredicate = new NSComparisonPredicate(predicate);
        
        NSComparisonPredicate newPredicate = (NSComparisonPredicate) new NSComparisonPredicate().alloc();
          
        NSExpression newRightExpression = NSExpression.expressionForConstantValue(otherTextField.stringValue());
        
        newPredicate.initWithLeftExpression(comparisonPredicate.leftExpression(), 
                                            newRightExpression, 
                                            ComparisonPredicateModifier.NSDirectPredicateModifier.value(), 
                                            comparisonPredicate.predicateOperatorType(), 
                                            comparisonPredicate.options());
             
        return newPredicate.id;
    }
    
    long /*int*/ superSetPredicateProc(long /*int*/ predicate) {
        objc_super super_struct = new objc_super();
        super_struct.receiver = id;
        super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
        
        return OS.objc_msgSendSuper(super_struct, OS.sel_setPredicate_, predicate);
    }
    
    long /*int*/ setPredicateProc(long /*int*/ predicateId) {
        NSPredicate predicate = new NSPredicate(predicateId);
        
        if (!predicate.isKindOfClass(OS.class_NSComparisonPredicate)) 
            return this.superSetPredicateProc(predicate.id);
        
        NSComparisonPredicate comparisonPredicate = new NSComparisonPredicate(predicate.id);
             
        String rightValue = comparisonPredicate.rightExpression().description().getString();
        otherTextField().setStringValue(NSString.stringWith(rightValue.substring(1, rightValue.length() - 1)));

        return this.superSetPredicateProc(predicate.id);
    }
    
    static double /*float*/ procMatchForPredicate(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
        SWTReadOnlyAlternativeRowTemplate template = getThis(id);
        if (template == null) return 0;
                
        NSPredicate predicate = new NSPredicate(arg0);
        
        if (!predicate.isKindOfClass(OS.class_NSComparisonPredicate))
            return 0;
        
        NSComparisonPredicate comparisonPredicate = new NSComparisonPredicate(predicate);

        if (!template.keyPathToTitleMap.containsKey(comparisonPredicate.leftExpression().description().getString()))
            return 0;
        
        NSExpression rightExpression = comparisonPredicate.rightExpression();
        if (rightExpression.expressionType() == OS.NSConstantValueExpressionType) {  
            NSObject rightExpressionValue = new NSObject(rightExpression.constantValue());
                if (!rightExpressionValue.isKindOfClass(OS.class_NSString))
                    return 0;
        }
        
        String rightExpr = comparisonPredicate.rightExpression().description().getString().toUpperCase().trim();
        if (rightExpr.length() < 2)
            return 0;
        
        rightExpr = rightExpr.substring(1, rightExpr.length() - 1);
        
        for (String alt : template.alternativeToRightExpressions) {
            if (alt.equalsIgnoreCase(rightExpr))
                return 0;
        }
        
        return (float)OS.CGFLOAT_MAX;
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
