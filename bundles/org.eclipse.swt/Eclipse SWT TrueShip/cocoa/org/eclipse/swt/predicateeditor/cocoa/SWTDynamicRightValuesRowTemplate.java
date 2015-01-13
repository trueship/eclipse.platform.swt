package org.eclipse.swt.predicateeditor.cocoa;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.predicate.*;
import org.eclipse.swt.predicateeditor.*;
import org.eclipse.swt.widgets.PredicateEditor;
import org.eclipse.swt.widgets.PredicateEditor.*;

public class SWTDynamicRightValuesRowTemplate extends NSPredicateEditorRowTemplate {
    static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};
    
    static Callback templateViewsCallback;
    static long /*int*/ templateViewsCallbackAddress;
    
    static Callback copyWithZoneCallback;
    static long /*int*/ copyWithZoneCallbackAddress;
    
    static Callback tokenFieldCompletionCallback;
    static long /*int*/ tokenFieldCompletionCallbackAddress;
    
    static Callback tokenFieldAddObjectsCallback;
    static long /*int*/ tokenFieldAddObjectsCallbackAddress;
    
    static Callback predicateWithSubpredicatesCallback;
    static long /*int*/ predicateWithSubpredicatesCallbackAddress;
    
    static Callback setPredicateCallback;
    static long /*int*/ setPredicateCallbackAddress;
    
    static Callback deallocCallback;
    static long /*int*/ deallocCallbackAddress;
    
    protected long /*int*/ jniRef;
    
    protected String keyPath;
    protected String title;
    NSTokenField tokenField;
    protected ArrayList<String> tokens = new ArrayList<String>();
    protected String criterion;
    protected RightValuesCallback rightValuesCallback;
    protected PredicateEditor predicateEditor;
    
    static {
        Class<SWTDynamicRightValuesRowTemplate> clazz = SWTDynamicRightValuesRowTemplate.class;
        
        templateViewsCallback = new Callback(clazz, "templateViewsProc", 2);
        templateViewsCallbackAddress = templateViewsCallback.getAddress();
        if (templateViewsCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        copyWithZoneCallback = new Callback(clazz, "copyWithZoneProc", 3);
        copyWithZoneCallbackAddress = copyWithZoneCallback.getAddress();
        if (copyWithZoneCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        tokenFieldCompletionCallback = new Callback(clazz, "tokenFieldCompletionProc", 6);
        tokenFieldCompletionCallbackAddress = tokenFieldCompletionCallback.getAddress();
        if (tokenFieldCompletionCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        tokenFieldAddObjectsCallback = new Callback(clazz, "tokenFieldAddObjectsProc", 5);
        tokenFieldAddObjectsCallbackAddress = tokenFieldAddObjectsCallback.getAddress();
        if (tokenFieldAddObjectsCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        predicateWithSubpredicatesCallback = new Callback(clazz, "predicateWithSubpredicatesProc", 3);
        predicateWithSubpredicatesCallbackAddress = predicateWithSubpredicatesCallback.getAddress();
        if (predicateWithSubpredicatesCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        setPredicateCallback = new Callback(clazz, "setPredicateProc", 3);
        setPredicateCallbackAddress = setPredicateCallback.getAddress();
        if (setPredicateCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        deallocCallback = new Callback(clazz, "deallocProc", 2);
        deallocCallbackAddress = deallocCallback.getAddress();
        if (deallocCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        long /*int*/ cls = OS.objc_allocateClassPair(OS.class_NSPredicateEditorRowTemplate, SWTDynamicRightValuesRowTemplate.class.getSimpleName(), 0);
        byte[] types = {'*','\0'};
        int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;
        
        OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
        
        OS.class_addMethod(cls, OS.sel_templateViews, templateViewsCallbackAddress, "@:");
        OS.class_addMethod(cls, OS.sel_copyWithZone_, copyWithZoneCallbackAddress, "@:@");
        OS.class_addMethod(cls, OS.sel_predicateWithSubpredicates_, predicateWithSubpredicatesCallbackAddress, "@:@");
        OS.class_addMethod(cls, OS.sel_tokenField_completionsForSubstring_indexOfToken_indexOfSelectedItem_ , tokenFieldCompletionCallbackAddress, "@:@@@@");
        OS.class_addMethod(cls, OS.sel_tokenField_shouldAddObjects_atIndex_, tokenFieldAddObjectsCallbackAddress, "@:@@@");
        OS.class_addMethod(cls, OS.sel_setPredicate_, setPredicateCallbackAddress, "v@:@");
        OS.class_addMethod(cls, OS.sel_dealloc, deallocCallbackAddress, "@:");
        
        OS.objc_registerClassPair(cls);
    }
    
    public SWTDynamicRightValuesRowTemplate (String keyPath, String title, RightValuesCallback tokensCallback, PredicateEditor predicateEditor) {
        super(0);
        alloc();
        
        this.keyPath = keyPath;
        this.title = title;
        this.rightValuesCallback = tokensCallback;
        this.predicateEditor = predicateEditor;
        
        NSArray keyPaths = NSArray.arrayWithObject(NSExpression.expressionForKeyPath(NSString.stringWith(keyPath)));
        NSArray operators = NSArray.arrayWithObject(NSNumber.numberWithInteger(PredicateOperatorType.NSInPredicateOperatorType.value()));

        initWithLeftExpressions(keyPaths, 
                               AttributeType.NSStringAttributeType.value(),
                               ComparisonPredicateModifier.NSDirectPredicateModifier.value(),
                               operators,
                               0);
        
        jniRef = OS.NewGlobalRef(this);
        if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
        OS.object_setInstanceVariable(this.id, SWT_OBJECT, jniRef);
    }
    
    public void setCriterion(String criterion) {
        this.criterion = criterion;
        
        updateRightValues();
    }
    
    private void updateRightValues() {
        if (criterion == null || rightValuesCallback == null || tokenField == null) return;
        
        NSArray currentTokens = new NSArray(tokenField.objectValue().id); 
        if (currentTokens.count() == 0) return;
        
        List<String> validTokens = rightValuesCallback.getRightValues(criterion);
        
        tokens.clear();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < currentTokens.count(); i++) {
            String token = new NSObject(currentTokens.objectAtIndex(i).id).description().getString();
            if (validTokens.contains(token)) {
                tokens.add(token);
                sb.append(token);
                sb.append(",");
            }
        }
        
        String tokenFieldValue = sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
        tokenField.setObjectValue(NSString.stringWith(tokenFieldValue));
    }
    
    public void setTokensCallback(RightValuesCallback callback) {
        rightValuesCallback = callback;
        
        updateRightValues();
    }
    
    public void internal_dispose() {
        if (jniRef != 0) OS.DeleteGlobalRef(jniRef);
        jniRef = 0;
        OS.object_setInstanceVariable(this.id, SWT_OBJECT, 0);
    }
    
    long /*int*/ superSetPredicateProc(long /*int*/ predicate) {
        objc_super super_struct = new objc_super();
        super_struct.receiver = id;
        super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
        
        return OS.objc_msgSendSuper(super_struct, OS.sel_templateViews, predicate);
    }
    
    long /*int*/ superTemplateViews() {
        objc_super super_struct = new objc_super();
        super_struct.receiver = id;
        super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
        
        return OS.objc_msgSendSuper(super_struct, OS.sel_templateViews);
    }
    
    id shipTypeTokenField() {
        if (tokenField == null) {
            tokenField = (NSTokenField) new NSTokenField().alloc();
            NSRect rect = new NSRect();
            rect.x = 0;
            rect.y = 0;
            rect.width = 400;
            rect.height = 22;
            tokenField.initWithFrame(rect);
            tokenField.setDelegate(this);
        }
        
        return tokenField;
    }
    
    static long /*int*/ templateViewsProc(long /*int*/ id, long /*int*/ sel) {
        SWTDynamicRightValuesRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        NSMutableArray views = new NSMutableArray(new NSArray(template.superTemplateViews()).mutableCopy());
        
        NSView view = new NSView(views.objectAtIndex(2).id);
        if (!view.isKindOfClass(OS.class_NSTokenField))
            views.replaceObjectAtIndex(2, template.shipTypeTokenField());
          
        if (template.tokens.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (String token : template.tokens) {
                sb.append(token);
                sb.append(",");
            }
            template.tokenField.setObjectValue(NSString.stringWith(sb.substring(0, sb.length() - 1)));
        }
        
        NSPopUpButton left = new NSPopUpButton(views.objectAtIndex(0));
        NSArray items = left.itemArray();
        for (int i = 0; i < items.count(); i++) {
            NSMenuItem item = new NSMenuItem(items.objectAtIndex(i).id);
            NSExpression keyPathExpression = new NSExpression(item.representedObject());
            if (keyPathExpression.keyPath().getString().equalsIgnoreCase(template.keyPath))
                item.setTitle(NSString.stringWith(template.title));
        }
        
        return views.id;
    }
    
    static long /*int*/ copyWithZoneProc(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
        SWTDynamicRightValuesRowTemplate template = getThis(id);
        if (template == null) return 0;
           
        SWTDynamicRightValuesRowTemplate newTemplate = new SWTDynamicRightValuesRowTemplate(template.keyPath, template.title, template.rightValuesCallback, template.predicateEditor);
        newTemplate.setCriterion(template.criterion);
        newTemplate.setTokensCallback(template.rightValuesCallback);
           
        return newTemplate.id;
    }
    
    static long /*int*/ setPredicateProc(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
        SWTDynamicRightValuesRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        String tokenFieldValue = new NSComparisonPredicate(arg0).rightExpression().description().getString().replaceAll("\"", "");
        
        template.tokens = new ArrayList<String>(Arrays.asList(tokenFieldValue.split(",")));
        
        return template.superSetPredicateProc(arg0);
    }
    
    static long /*int*/ tokenFieldAddObjectsProc(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2) {
        SWTDynamicRightValuesRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        NSArray objectsToAdd = new NSArray(arg1);
        
        NSMutableArray newTokens = NSMutableArray.arrayWithCapacity(objectsToAdd.count());
        for (int i = 0; i < objectsToAdd.count(); i++) {
            String token = new NSObject(objectsToAdd.objectAtIndex(i).id).description().getString();
            if (!template.tokens.contains(token)) {
                template.tokens.add(token);
                newTokens.addObject(objectsToAdd.objectAtIndex(i));
            }
        }
        
        return newTokens.id;
    }
    
    static long /*int*/ tokenFieldCompletionProc(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3) {
        SWTDynamicRightValuesRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        if (template.criterion == null || template.rightValuesCallback == null) return 0;
        
        List<String> tokens = template.rightValuesCallback.getRightValues(template.criterion);
        if (tokens == null) return 0;
        
        NSMutableArray tokensArray = NSMutableArray.arrayWithCapacity(tokens.size());
        for (String token : tokens)
            tokensArray.addObject(NSString.stringWith(token));
        
        return tokensArray.id;
    }
    
    static long /*int*/ predicateWithSubpredicatesProc(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
        SWTDynamicRightValuesRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        NSArray tokens = new NSArray(template.tokenField.objectValue().id);
        
        String format = "%K IN \"\"";
        
        long numberOfTokens = tokens.count();
        
        if (numberOfTokens > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < numberOfTokens; i++) {
                sb.append(new NSObject(tokens.objectAtIndex(i).id).description().getString());
                sb.append(",");
            }
            
            format = "%K IN \"" + sb.substring(0, sb.length() - 1) + "\"";
        }
        
        Predicate predicate = Predicate.predicateWithFormat(format, Arrays.asList(template.keyPath));
        
        DynamicRightValuesRowTemplate newTemplate = new DynamicRightValuesRowTemplate(template);
        newTemplate.setPredicate(predicate);
        
        template.predicateEditor.addDynamicRowTemplateInstance(newTemplate);
        
        return predicate.id();
    }
    
    static SWTDynamicRightValuesRowTemplate getThis(long /*int*/ id) {
        if (id == 0) return null;
        
        long /*int*/ [] jniRef = new long /*int*/ [1];
        OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
        if (jniRef[0] == 0) return null;
        
        return (SWTDynamicRightValuesRowTemplate)OS.JNIGetObject(jniRef[0]);
    }
    
    static long /*int*/ deallocProc(long /*int*/ id, long /*int*/ sel) {
        SWTDynamicRightValuesRowTemplate thisTemplate = getThis(id);
        if (thisTemplate == null) return 0;
        
        thisTemplate.internal_dispose();
        
        objc_super super_struct = new objc_super();
        super_struct.receiver = id;
        super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
        
        return OS.objc_msgSendSuper(super_struct, OS.sel_dealloc);
    }
}
