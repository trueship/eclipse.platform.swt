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
    private static final int DEFAULT_TOKENFIELD_WIDTH = 400;
    private static final int DEFAULT_TOKENFIELD_HEIGHT = 22;
    private static final String IS_NOT = "is not";
    private static final String NOT_IN = "not in";
    
    private static final long /*int*/ sel_unselectTokenFieldText = OS.sel_registerName("unselectTokenFieldText");

    static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};
    
    static Callback proc2Callback;
    static long /*int*/ proc2CallbackAddress;
    
    static Callback proc3Callback;
    static long /*int*/ proc3CallbackAddress;
    
    static Callback tokenFieldCompletionCallback;
    static long /*int*/ tokenFieldCompletionCallbackAddress;
    
    static Callback tokenFieldAddObjectsCallback;
    static long /*int*/ tokenFieldAddObjectsCallbackAddress;
    
    static CallbackFpret matchForPredicateCallback;
    static long /*int*/ matchForPredicateCallbackAddress;
    
    protected long /*int*/ jniRef;
    
    protected NSMutableArray runLoopModes;
    
    protected String keyPath;
    protected String title;
    NSTokenField tokenField;
    protected ArrayList<String> displayedTokens = new ArrayList<String>();
    protected List<String> validTokens;
    protected String criterion;
    protected RightValuesCallback rightValuesCallback;
    protected PredicateEditor predicateEditor;
    private boolean released = false;
    
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
        
        matchForPredicateCallback = new CallbackFpret(clazz, "procMatchForPredicate", 3);
        matchForPredicateCallbackAddress = matchForPredicateCallback.getAddress();
        if (matchForPredicateCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
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
        OS.class_addMethod(cls, OS.sel_matchForPredicate_, matchForPredicateCallbackAddress, "d@:@");
        OS.class_addMethod(cls, sel_unselectTokenFieldText, proc2CallbackAddress, "@:");
        
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
        
        NSMutableArray operators = NSMutableArray.arrayWithCapacity(2);
        operators.addObject(NSNumber.numberWithInteger(PredicateOperatorType.NSInPredicateOperatorType.value()));
        operators.addObject(NSNumber.numberWithInteger(PredicateOperatorType.NSNotEqualToPredicateOperatorType.value())); // For 'not in' operator
        
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
        
        updateValidTokenList();
        updateTokenFieldValue();
    }
    
    public void setTokensCallback(RightValuesCallback callback) {
        rightValuesCallback = callback;
        
        updateValidTokenList();
        updateTokenFieldValue();
    }
    
    public void refreshLayout() {
        if (isReleased()) return;
        
        NSSize size = new NSSize();
        
        // Make the tokenfield stretch to the left of remove template ("-") button.
        size.width = (computeRemoveTemplateButtonRect().x - tokenField.frame().x) - 5;
        size.height = tokenField.frame().height;
            
        tokenField.setFrameSize(size);
    }
    
    public void refreshUI() {
        this.performSelector(sel_unselectTokenFieldText, null, 0.0, runLoopModes());
    }
    
    public boolean isReleased() {
        return released;
    }
    
    void internal_dispose() {
        if (jniRef != 0) OS.DeleteGlobalRef(jniRef);
        jniRef = 0;
        OS.object_setInstanceVariable(this.id, SWT_OBJECT, 0);
    }
    
    void updateValidTokenList() {
        validTokens = null;
        
        if (rightValuesCallback != null)
            validTokens = rightValuesCallback.getRightValues(criterion);
        
        if (validTokens == null)
            validTokens = new ArrayList<String>();
    }
    
    void updateTokenFieldValue() {
        if (criterion == null || rightValuesCallback == null || tokenField == null) return;
        
        NSArray currentTokens = new NSArray(tokenField.objectValue().id); 
        if (currentTokens.count() == 0) return;
        
        // Keep the tokens if in 'free tokens' mode (i.e. not criterion was set).
        if (criterion != null) {
            displayedTokens.clear();
            
            for (int i = 0; i < currentTokens.count(); i++) {
                String token = new NSObject(currentTokens.objectAtIndex(i).id).description().getString();
                if (isTokenValid(token))
                    displayedTokens.add(token);
            }
        }
        
        tokenField.setObjectValue(NSString.stringWith(makeTokenFieldValue()));
        
        doUnselectTokenFieldText();
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
            rect.width = DEFAULT_TOKENFIELD_WIDTH;
            rect.height = DEFAULT_TOKENFIELD_HEIGHT;
            tokenField.initWithFrame(rect);
            tokenField.setDelegate(this);
            tokenField.cell().setWraps(false);
        }
        
        return tokenField;
    }
    
    String makeTokenFieldValue() {
        if (displayedTokens.size() == 0) return "";
        
        StringBuilder sb = new StringBuilder();
        for (String token : displayedTokens)
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
        } else if (sel == sel_unselectTokenFieldText) {
            return template.unselectTokenFieldTextProc();
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
    
    // Match 'keyPath in ...' for 'in' operator and '(not keyPath in ...)' for 'not in' operator.
    static double /*float*/ procMatchForPredicate(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
        SWTDynamicRightValuesRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        NSPredicate predicate = new NSPredicate(arg0);
        
        if (predicate.isKindOfClass(OS.class_NSCompoundPredicate)) {
            NSCompoundPredicate compoundPredicate = new NSCompoundPredicate(predicate);
            
            if (compoundPredicate.compoundPredicateType() != CompoundPredicateType.NSNotPredicateType.value()) return 0;
            
            NSArray subpredicates = compoundPredicate.subpredicates();
            if (subpredicates == null || subpredicates.count() != 1) return 0;
            
            NSPredicate subpredicate = new NSPredicate(subpredicates.objectAtIndex(0));
            if (!subpredicate.isKindOfClass(OS.class_NSComparisonPredicate)) return 0;
            
            predicate = subpredicate;
        }
        
        NSComparisonPredicate cp = new NSComparisonPredicate(predicate);
        
        if (cp.predicateOperatorType() != PredicateOperatorType.NSInPredicateOperatorType.value()) return 0;
        
        NSExpression expression = cp.leftExpression();
        if (expression == null) return 0;
        
        NSString expressionKeyPath = expression.keyPath();
        if (expressionKeyPath == null) return 0;
        
        return  expressionKeyPath.getString().equalsIgnoreCase(template.keyPath) ? 1 : 0;
    }
    
    long /*int*/ templateViewsProc() {
        NSMutableArray views = new NSMutableArray(new NSArray(this.superTemplateViews()).mutableCopy());
        
        NSView view = new NSView(views.objectAtIndex(2).id);
        if (!view.isKindOfClass(OS.class_NSTokenField))
            views.replaceObjectAtIndex(2, this.shipTypeTokenField());
          
        if (displayedTokens.size() > 0)
            tokenField.setObjectValue(NSString.stringWith(makeTokenFieldValue()));
        
        NSPopUpButton left = new NSPopUpButton(views.objectAtIndex(0));
        
        NSArray items = left.itemArray();
        for (int i = 0; i < items.count(); i++) {
            NSMenuItem item = new NSMenuItem(items.objectAtIndex(i).id);
            NSExpression keyPathExpression = new NSExpression(item.representedObject());
            if (keyPathExpression.keyPath().getString().equalsIgnoreCase(this.keyPath))
                item.setTitle(NSString.stringWith(this.title));
        }
        
        makeNotInPredicateOperator(views);
        
        return views.id;
    }
       
    long /*int*/ copyWithZoneProc(long /*int*/ arg0) {
        SWTDynamicRightValuesRowTemplate newTemplate = new SWTDynamicRightValuesRowTemplate(this.keyPath, this.title, this.rightValuesCallback, this.predicateEditor);
        newTemplate.setCriterion(this.criterion);
        newTemplate.setTokensCallback(this.rightValuesCallback);
           
        return newTemplate.id;
    }
    
    long /*int*/ setPredicateProc(long /*int*/ predicateId) {
        NSPredicate predicate = new NSPredicate(predicateId);
        
        if (predicate.isKindOfClass(OS.class_NSCompoundPredicate)) {
            // Change a '(NOT %K in ...)' compound predicate into a '%K != ...' comparison predicate.
            NSCompoundPredicate compoundPredicate = new NSCompoundPredicate(predicate.id);
            NSComparisonPredicate comparisonPredicate = new NSComparisonPredicate(compoundPredicate.subpredicates().objectAtIndex(0));
            
            NSComparisonPredicate newComparisonPredicate = (NSComparisonPredicate) new NSComparisonPredicate().alloc();
            newComparisonPredicate.initWithLeftExpression(comparisonPredicate.leftExpression(), 
                                                          comparisonPredicate.rightExpression(), 
                                                          comparisonPredicate.comparisonPredicateModifier(), 
                                                          PredicateOperatorType.NSNotEqualToPredicateOperatorType.value(), 
                                                          comparisonPredicate.options());
            
            selectNotInPredicateOperator();
            predicate = new NSPredicate(newComparisonPredicate.id);
        }
        
        updateTokenFieldFromPredicate(new NSComparisonPredicate(predicate.id));
        
        return this.superSetPredicateProc(predicate.id);
    }

    private void updateTokenFieldFromPredicate(NSComparisonPredicate predicate) {
        String tokenFieldValue = predicate.rightExpression().description().getString().replaceAll("\"", "");
        this.displayedTokens = new ArrayList<String>(Arrays.asList(tokenFieldValue.split(",")));
    }
    
    // Handles token deletion.
    long /*int*/ controlTextDidChangeProc(long /*int*/ notification) {
        String tokenFieldText = tokenField.stringValue().getString();
        
        List<String> currentTokens = Arrays.asList(tokenFieldText.trim().split("\\s*,\\s*"));
        ArrayList<String> newTokens = new ArrayList<String>(displayedTokens);
        for (int i = 0; i < displayedTokens.size(); i++) {
            if (!currentTokens.contains(displayedTokens.get(i)))
                newTokens.remove(i);
        }
        
        this.displayedTokens = newTokens;
        
        return 0;
    }
    
    // Clears the tokenfield of any residual editing value when moving the focus in the middle of an edit session.
    long /*int*/ controlTextDidEndEditing(long /*int*/ notification) {
        tokenField.setStringValue(NSString.stringWith(makeTokenFieldValue()));
        
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
            if (!template.displayedTokens.contains(token)) {
                if (template.isTokenValid(token)) {
                    template.displayedTokens.add(token);
                    newTokens.addObject(objectsToAdd.objectAtIndex(i));
                }
            }
        }
        
        return newTokens.id;
    }
    
    boolean isTokenValid(String token) {
       if (criterion == null || validTokens == null) return true;
       
       return validTokens.contains(token);
    }
    
    static long /*int*/ tokenFieldCompletionProc(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3) {
        SWTDynamicRightValuesRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        if (template.criterion == null || template.rightValuesCallback == null) return 0;
        
        if (template.validTokens == null || template.validTokens.isEmpty())
            template.updateValidTokenList();
            
        NSMutableArray tokensArray = NSMutableArray.arrayWithCapacity(template.validTokens.size());
        for (String token : template.validTokens) {
            if (!template.displayedTokens.contains(token))
                tokensArray.addObject(NSString.stringWith(token));
        }
        
        return tokensArray.id;
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
        String prefix = "";
        if (comparisonPredicate.predicateOperatorType() == PredicateOperatorType.NSNotEqualToPredicateOperatorType.value())
            prefix = "NOT ";
    
        NSArray tokens = new NSArray(this.tokenField.objectValue().id);
        
        String format = prefix + "%K IN \"\"";
        
        long numberOfTokens = tokens.count();
        
        if (numberOfTokens > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < numberOfTokens; i++) {
                sb.append(new NSObject(tokens.objectAtIndex(i).id).description().getString());
                sb.append(",");
            }
            
            format = prefix + "%K IN \"" + sb.substring(0, sb.length() - 1) + "\"";
        }
        
        Predicate newPredicate = Predicate.predicateWithFormat(format, Arrays.asList(this.keyPath));
        
        DynamicRightValuesRowTemplate newTemplate = new DynamicRightValuesRowTemplate(this);
        newTemplate.setPredicate(newPredicate);
        
        this.predicateEditor.addDynamicRowTemplateInstance(newTemplate);
        
        return newPredicate.id();
    }
    
    long /*int*/ unselectTokenFieldTextProc() {
        doUnselectTokenFieldText();
        
        return 0;
    }

    NSArray runLoopModes() {
        if (runLoopModes == null) {
                runLoopModes = NSMutableArray.arrayWithCapacity(3);
                runLoopModes.addObject(OS.NSEventTrackingRunLoopMode);
                runLoopModes.addObject(OS.NSDefaultRunLoopMode);
                runLoopModes.retain();
        }

        runLoopModes.retain();
        runLoopModes.autorelease();
        
        return runLoopModes;
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
        
        released = true;
        
        objc_super super_struct = new objc_super();
        super_struct.receiver = id;
        super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
        
        return OS.objc_msgSendSuper(super_struct, OS.sel_dealloc);
    }
    
    private NSRect computeRemoveTemplateButtonRect() {
        NSArray subviews = tokenField.superview().subviews();
         
        NSRect maxXRect = new NSRect();
        NSRect prevMaxXRect = new NSRect(); // the remove template ("-") button is the one before last.
         
        for (int i = 0; i < subviews.count(); i++) {
            NSView view = new NSView(subviews.objectAtIndex(i));
            NSRect viewRect = view.frame();
            if (viewRect.x > maxXRect.x) {
                prevMaxXRect = maxXRect;
                maxXRect = viewRect;
            } else if (viewRect.x > prevMaxXRect.x) {
                prevMaxXRect = viewRect;
            }
        }
         
        return prevMaxXRect;
     }
    
    private void makeNotInPredicateOperator(NSMutableArray views) {
        NSPopUpButton operatorButton = new NSPopUpButton(views.objectAtIndex(1));
        
        NSArray items = operatorButton.itemArray();
        for (int i = 0; i < items.count(); i++) {
            NSMenuItem item = new NSMenuItem(items.objectAtIndex(i).id);
            if (item.title().isEqualToString(NSString.stringWith(IS_NOT))) {
                item.setTitle(NSString.stringWith(NOT_IN));
                return ;
            }
        }
    }
    
    private void selectNotInPredicateOperator() {
        NSMutableArray views = new NSMutableArray(new NSArray(this.superTemplateViews()).mutableCopy());
        NSPopUpButton operatorButton = new NSPopUpButton(views.objectAtIndex(1));
        
        NSArray items = operatorButton.itemArray();
        for (int i = 0; i < items.count(); i++) {
            NSMenuItem item = new NSMenuItem(items.objectAtIndex(i).id);
            if (item.title().isEqualToString(NSString.stringWith(IS_NOT))) {
                operatorButton.selectItemAtIndex(i);
                return ;
            }
        }
    }
    
    private void doUnselectTokenFieldText() {
        NSCell cell = tokenField.cell();
        if (cell == null) return;
        
        NSTextView textView = cell.fieldEditorForView(tokenField);
        if (textView == null) return;
        
        NSRange range = new NSRange();
        range.length = 0;
        range.location = 0;
        
        textView.setSelectedRange(range);
    }
}
