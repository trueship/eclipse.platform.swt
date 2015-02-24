package org.eclipse.swt.predicateeditor.cocoa;

import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.widgets.PredicateEditor.ComparisonPredicateModifier;
import org.eclipse.swt.widgets.PredicateEditor.CompoundPredicateType;
import org.eclipse.swt.widgets.PredicateEditor.PredicateOperatorType;

public class SWTLengthRowTemplate extends NSPredicateEditorRowTemplate {
    private static final int TEXTFIELD_WIDTH = 100;

    static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};
    
    static Callback proc2Callback;
    static long /*int*/ proc2CallbackAddress;
    
    static Callback proc3Callback;
    static long /*int*/ proc3CallbackAddress;
    
    static CallbackFpret matchForPredicateCallback;
    static long /*int*/ matchForPredicateCallbackAddress;
    
    long /*int*/ jniRef;
    
    protected HashMap<String, String> keyPathToTitleMap;
    protected NSPopUpButton unitPopUpButton;
    protected String keyPath;
    protected List<String> units;
    
    static {
        Class<SWTLengthRowTemplate> clazz = SWTLengthRowTemplate.class;
        
        proc2Callback = new Callback(clazz, "proc2", 2);
        proc2CallbackAddress = proc2Callback.getAddress();
        if (proc2CallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        proc3Callback = new Callback(clazz, "proc3", 3);
        proc3CallbackAddress = proc3Callback.getAddress();
        if (proc3CallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        matchForPredicateCallback = new CallbackFpret(clazz, "procMatchForPredicate", 3);
        matchForPredicateCallbackAddress = matchForPredicateCallback.getAddress();
        if (matchForPredicateCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
               
        long /*int*/ cls = OS.objc_allocateClassPair(OS.class_NSPredicateEditorRowTemplate, SWTLengthRowTemplate.class.getSimpleName(), 0);
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
    
    public SWTLengthRowTemplate(HashMap<String, String> keyPathToTitleMap, List<String> units) {
        super(0);
        alloc();
        
        this.keyPathToTitleMap = keyPathToTitleMap;
        this.units = units;
        
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
    
    NSPopUpButton lengthPopUpButton() {
        if (unitPopUpButton == null) {
        	unitPopUpButton = (NSPopUpButton) new NSPopUpButton().alloc();
            
            NSRect rect = new NSRect();
            rect.width = 0; rect.height = 0;
            unitPopUpButton.initWithFrame(rect, false);

            NSMenu unitMenu = unitPopUpButton.menu();

            for (String unit : units)
                unitMenu.addItemWithTitle(NSString.stringWith(unit), OS.sel_null, NSString.stringWith(""));
        }
        
        return unitPopUpButton;
    }
     
    long /*int*/ templateViewsProc() {
        NSMutableArray views = new NSMutableArray(new NSArray(this.superTemplateViews()).mutableCopy());
        
        replaceKeyPathWithTitle(new NSPopUpButton(views.objectAtIndex(0)));
        
        views.addObject(lengthPopUpButton());
        
        adjustTextField(views);
        
        views.autorelease();
        
        return views.id;
    }

    private void adjustTextField(NSMutableArray views) {
        NSTextField textField = new NSTextField(views.objectAtIndex(2));
        
        NSSize size = new NSSize();
        size.width = TEXTFIELD_WIDTH;
        size.height = textField.frame().height;
        textField.setFrameSize(size);
        
        textField.setAlignment(OS.NSRightTextAlignment);
    }
    
    static SWTLengthRowTemplate getThis(long /*int*/ id) {
        if (id == 0) return null;
        
        long /*int*/ [] jniRef = new long /*int*/ [1];
        OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
        if (jniRef[0] == 0) return null;
        
        return (SWTLengthRowTemplate)OS.JNIGetObject(jniRef[0]);
    }
    
    static long /*int*/ proc2(long /*int*/ id, long /*int*/ sel) {
    	SWTLengthRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        if (sel == OS.sel_templateViews) {
            return template.templateViewsProc();
        } else if (sel == OS.sel_dealloc) {
            return template.deallocProc();
        }
        
        return 0;
    }
    
    static long /*int*/ proc3(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
    	SWTLengthRowTemplate template = getThis(id);
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
    	SWTLengthRowTemplate newTemplate = new SWTLengthRowTemplate(keyPathToTitleMap, units);
        
        newTemplate.initWithLeftExpressions(this.leftExpressions(), 
                                            this.rightExpressionAttributeType(), 
                                            this.modifier(), 
                                            this.operators(), 
                                            this.options());
        
        newTemplate.keyPath = keyPath;
        
        return newTemplate.id;
    }
    
    long /*int*/ superSetPredicateProc(long /*int*/ predicate) {
        objc_super super_struct = new objc_super();
        super_struct.receiver = id;
        super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
        
        return OS.objc_msgSendSuper(super_struct, OS.sel_setPredicate_, predicate);
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
        
        NSComparisonPredicate unitPredicate = (NSComparisonPredicate) new NSComparisonPredicate().alloc();
        
        NSExpression left = NSExpression.expressionForKeyPath(NSString.stringWith(keyPath + ".UNIT"));
        NSExpression right = NSExpression.expressionForConstantValue(NSString.stringWith(units.get((int) unitPopUpButton.indexOfSelectedItem())));
        
        unitPredicate.initWithLeftExpression(left, 
                                                 right, 
                                                 ComparisonPredicateModifier.NSDirectPredicateModifier.value(), 
                                                 PredicateOperatorType.NSEqualToPredicateOperatorType.value(), 
                                                 0);
        
        NSMutableArray lengthSubpredicates = NSMutableArray.arrayWithCapacity(2);
        lengthSubpredicates.addObject(predicate);
        lengthSubpredicates.addObject(unitPredicate);
        
        NSCompoundPredicate unitAndPredicate = (NSCompoundPredicate) new NSCompoundPredicate().alloc();
        unitAndPredicate.initWithType(CompoundPredicateType.NSAndPredicateType.value(), lengthSubpredicates);
        
        // Add a special false predicate to force the parser (in case of saving and reloading the predicate)
        // to send the compound predicate as a whole to matchForPredicate,
        // otherwise it'll split the length and unit parts into two different predicates.
        NSPredicate falsePredicate = NSPredicate.predicateWithFormat(NSString.stringWith("0 > 1"));
        
        NSMutableArray subpredicates = NSMutableArray.arrayWithCapacity(2);
        subpredicates.addObject(unitAndPredicate);
        subpredicates.addObject(falsePredicate);
        
        NSCompoundPredicate newPredicate = (NSCompoundPredicate) new NSCompoundPredicate().alloc();
        newPredicate.initWithType(CompoundPredicateType.NSOrPredicateType.value(), subpredicates);
        
        return newPredicate.id;
    }
    
    long /*int*/ setPredicateProc(long /*int*/ predicateId) {
        NSPredicate predicate = new NSPredicate(predicateId);
        
        if (predicate.isKindOfClass(OS.class_NSCompoundPredicate)) {
            NSCompoundPredicate compoundPredicate = new NSCompoundPredicate(predicate.id);
            NSCompoundPredicate lengthPredicate = new NSCompoundPredicate(compoundPredicate.subpredicates().objectAtIndex(0));
            
            NSComparisonPredicate newComparisonPredicate = new NSComparisonPredicate(lengthPredicate.subpredicates().objectAtIndex(0));
            
            predicate = new NSPredicate(newComparisonPredicate.id);
        }
        
        return this.superSetPredicateProc(predicate.id);
    }
    
    static double /*float*/ procMatchForPredicate(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
        SWTLengthRowTemplate template = getThis(id);
        if (template == null) return 0;
                
        NSPredicate predicate = new NSPredicate(arg0);
        
        if (!predicate.isKindOfClass(OS.class_NSCompoundPredicate))
            return 0;
        
        NSCompoundPredicate compoundPredicate = new NSCompoundPredicate(predicate);

        String predicateFormat = compoundPredicate.predicateFormat().getString().trim();
        for (String keyPath : template.keyPathToTitleMap.keySet()) {
            if (predicateFormat.startsWith("(" + keyPath)) {
                template.keyPath = keyPath.split("\\.")[0];
                return 1;
            }
        }
        
        return 0;
    }
    
    long /*int*/ deallocProc() {
        internal_dispose();
        
        unitPopUpButton.release();
        
        return superDeallocProc();
    }

    private long /*int*/ superDeallocProc() {
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
