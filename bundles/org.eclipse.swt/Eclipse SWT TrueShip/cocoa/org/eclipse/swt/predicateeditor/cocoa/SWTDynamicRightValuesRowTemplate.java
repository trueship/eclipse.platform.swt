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
    
    static Callback proc2Callback;
    static long /*int*/ proc2CallbackAddress;
    
    static Callback proc3Callback;
    static long /*int*/ proc3CallbackAddress;
    
    static Callback tokenFieldCompletionCallback;
    static long /*int*/ tokenFieldCompletionCallbackAddress;
    
    static Callback tokenFieldAddObjectsCallback;
    static long /*int*/ tokenFieldAddObjectsCallbackAddress;
    
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
        
        proc2Callback = new Callback(clazz, "proc2", 2);
        proc2CallbackAddress = proc2Callback.getAddress();
        if (proc2CallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        proc3Callback = new Callback(clazz, "proc3", 3);
        proc3CallbackAddress = proc3Callback.getAddress();
        if (proc3CallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        tokenFieldAddObjectsCallback = new Callback(clazz, "tokenFieldAddObjectsProc", 5);
        tokenFieldAddObjectsCallbackAddress = tokenFieldAddObjectsCallback.getAddress();
        if (tokenFieldAddObjectsCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        tokenFieldCompletionCallback = new Callback(clazz, "tokenFieldCompletionProc", 6);
        tokenFieldCompletionCallbackAddress = tokenFieldCompletionCallback.getAddress();
        if (tokenFieldCompletionCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        long /*int*/ cls = OS.objc_allocateClassPair(OS.class_NSPredicateEditorRowTemplate, SWTDynamicRightValuesRowTemplate.class.getSimpleName(), 0);
        byte[] types = {'*','\0'};
        int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;
        
        OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
        
        OS.class_addMethod(cls, OS.sel_templateViews, proc2CallbackAddress, "@:");
        OS.class_addMethod(cls, OS.sel_copyWithZone_, proc3CallbackAddress, "@:@");
        OS.class_addMethod(cls, OS.sel_setPredicate_, proc3CallbackAddress, "v@:@");
        OS.class_addMethod(cls, OS.sel_predicateWithSubpredicates_, proc3CallbackAddress, "@:@");
        OS.class_addMethod(cls, OS.sel_tokenField_completionsForSubstring_indexOfToken_indexOfSelectedItem_ , tokenFieldCompletionCallbackAddress, "@:@@@@");
        OS.class_addMethod(cls, OS.sel_tokenField_shouldAddObjects_atIndex_, tokenFieldAddObjectsCallbackAddress, "@:@@@");
        OS.class_addMethod(cls, OS.sel_controlTextDidChange, proc3CallbackAddress, "v@:@");
        OS.class_addMethod(cls, OS.sel_controlTextDidEndEditing, proc3CallbackAddress, "v@:@");
        OS.class_addMethod(cls, OS.sel_dealloc, proc2CallbackAddress, "@:");
        
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
    
    String makeTokenValue() {
        if (tokens.size() == 0) return "";
        
        StringBuilder sb = new StringBuilder();
        for (String token : tokens)
            sb.append(token).append(",");
        
        return sb.substring(0, sb.length() - 1);
    }
    
    static long /*int*/ proc2(long /*int*/ id, long /*int*/ sel) {
        SWTDynamicRightValuesRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        if (sel == OS.sel_templateViews) {
            return template.templateViewsProc();
        } else if (sel == OS.sel_dealloc) {
            return template.deallocProc();
        }
        
        return 0;
    }
    
    static long /*int*/ proc3(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
        SWTDynamicRightValuesRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        if (sel == OS.sel_controlTextDidChange) {
            return template.controlTextDidChangeProc(arg0);
        } else if (sel == OS.sel_controlTextDidEndEditing) {
            return template.controlTextDidEndEditing(arg0);
        } else if (sel == OS.sel_setPredicate_) {
            return template.setPredicateProc(arg0);
        } else if (sel == OS.sel_predicateWithSubpredicates_) {
            return template.predicateWithSubpredicatesProc(arg0);
        } else if (sel == OS.sel_copyWithZone_) {
            return template.copyWithZoneProc(arg0);
        }
        
        return 0;
    }
    
    long /*int*/ templateViewsProc() {
        NSMutableArray views = new NSMutableArray(new NSArray(this.superTemplateViews()).mutableCopy());
        
        NSView view = new NSView(views.objectAtIndex(2).id);
        if (!view.isKindOfClass(OS.class_NSTokenField))
            views.replaceObjectAtIndex(2, this.shipTypeTokenField());
          
        if (tokens.size() > 0)
            tokenField.setObjectValue(NSString.stringWith(makeTokenValue()));
        
        NSPopUpButton left = new NSPopUpButton(views.objectAtIndex(0));
        NSArray items = left.itemArray();
        for (int i = 0; i < items.count(); i++) {
            NSMenuItem item = new NSMenuItem(items.objectAtIndex(i).id);
            NSExpression keyPathExpression = new NSExpression(item.representedObject());
            if (keyPathExpression.keyPath().getString().equalsIgnoreCase(this.keyPath))
                item.setTitle(NSString.stringWith(this.title));
        }
        
        return views.id;
    }
       
    long /*int*/ copyWithZoneProc(long /*int*/ arg0) {
        SWTDynamicRightValuesRowTemplate newTemplate = new SWTDynamicRightValuesRowTemplate(this.keyPath, this.title, this.rightValuesCallback, this.predicateEditor);
        newTemplate.setCriterion(this.criterion);
        newTemplate.setTokensCallback(this.rightValuesCallback);
           
        return newTemplate.id;
    }
    
    long /*int*/ setPredicateProc(long /*int*/ predicate) {
        String tokenFieldValue = new NSComparisonPredicate(predicate).rightExpression().description().getString().replaceAll("\"", "");
        
        this.tokens = new ArrayList<String>(Arrays.asList(tokenFieldValue.split(",")));
        
        return this.superSetPredicateProc(predicate);
    }
    
    // Handles token deletion.
    long /*int*/ controlTextDidChangeProc(long /*int*/ notification) {
        String tokenFieldText = tokenField.stringValue().getString();
        
        List<String> currentTokens = Arrays.asList(tokenFieldText.trim().split("\\s*,\\s*"));
        ArrayList<String> newTokens = new ArrayList<String>(tokens);
        for (int i = 0; i < tokens.size(); i++) {
            if (!currentTokens.contains(tokens.get(i)))
                newTokens.remove(i);
        }
        
        this.tokens = newTokens;
        
        return 0;
    }
    
    // Clears the tokenfield of any residual editing value when moving the focus in the middle of an edit session.
    long /*int*/ controlTextDidEndEditing(long /*int*/ notification) {
        tokenField.setStringValue(NSString.stringWith(makeTokenValue()));
        
        return 0;
    }
    
    // Handles token addition.
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
    
    long /*int*/ predicateWithSubpredicatesProc(long /*int*/ arg0) {
        
        NSArray tokens = new NSArray(this.tokenField.objectValue().id);
        
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
        
        Predicate predicate = Predicate.predicateWithFormat(format, Arrays.asList(this.keyPath));
        
        DynamicRightValuesRowTemplate newTemplate = new DynamicRightValuesRowTemplate(this);
        newTemplate.setPredicate(predicate);
        
        this.predicateEditor.addDynamicRowTemplateInstance(newTemplate);
        
        return predicate.id();
    }
    
    static SWTDynamicRightValuesRowTemplate getThis(long /*int*/ id) {
        if (id == 0) return null;
        
        long /*int*/ [] jniRef = new long /*int*/ [1];
        OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
        if (jniRef[0] == 0) return null;
        
        return (SWTDynamicRightValuesRowTemplate)OS.JNIGetObject(jniRef[0]);
    }
    
    long /*int*/ deallocProc() {
        internal_dispose();
        
        objc_super super_struct = new objc_super();
        super_struct.receiver = id;
        super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
        
        return OS.objc_msgSendSuper(super_struct, OS.sel_dealloc);
    }
}
