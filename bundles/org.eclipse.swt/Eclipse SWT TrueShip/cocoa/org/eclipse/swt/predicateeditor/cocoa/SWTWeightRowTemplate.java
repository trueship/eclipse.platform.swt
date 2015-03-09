package org.eclipse.swt.predicateeditor.cocoa;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.C;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.CallbackFpret;
import org.eclipse.swt.internal.cocoa.NSArray;
import org.eclipse.swt.internal.cocoa.NSComparisonPredicate;
import org.eclipse.swt.internal.cocoa.NSCompoundPredicate;
import org.eclipse.swt.internal.cocoa.NSExpression;
import org.eclipse.swt.internal.cocoa.NSMenu;
import org.eclipse.swt.internal.cocoa.NSMenuItem;
import org.eclipse.swt.internal.cocoa.NSMutableArray;
import org.eclipse.swt.internal.cocoa.NSNotification;
import org.eclipse.swt.internal.cocoa.NSPopUpButton;
import org.eclipse.swt.internal.cocoa.NSPredicate;
import org.eclipse.swt.internal.cocoa.NSPredicateEditorRowTemplate;
import org.eclipse.swt.internal.cocoa.NSRect;
import org.eclipse.swt.internal.cocoa.NSSize;
import org.eclipse.swt.internal.cocoa.NSString;
import org.eclipse.swt.internal.cocoa.NSView;
import org.eclipse.swt.internal.cocoa.NSTextField;
import org.eclipse.swt.internal.cocoa.OS;
import org.eclipse.swt.internal.cocoa.id;
import org.eclipse.swt.internal.cocoa.objc_super;
import org.eclipse.swt.predicateeditor.LengthRowTemplate;
import org.eclipse.swt.predicateeditor.WeightRowTemplate;
import org.eclipse.swt.widgets.PredicateEditor;
import org.eclipse.swt.widgets.PredicateEditor.ComparisonPredicateModifier;
import org.eclipse.swt.widgets.PredicateEditor.CompoundPredicateType;
import org.eclipse.swt.widgets.PredicateEditor.PredicateOperatorType;

public class SWTWeightRowTemplate extends NSPredicateEditorRowTemplate {
    private static final int TEXTFIELD_WIDTH = 100;
    private static final int UNITBUTTON_WIDTH = 50;
    private static final int MIN_OPERATOR_WIDTH = 50;
    private static final int SEPARATOR_WIDTH = 5;

    static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};
    
    static Callback proc2Callback;
    static long /*int*/ proc2CallbackAddress;
    
    static Callback proc3Callback;
    static long /*int*/ proc3CallbackAddress;
    
    static CallbackFpret matchForPredicateCallback;
    static long /*int*/ matchForPredicateCallbackAddress;
    
    long /*int*/ jniRef;
    
    protected HashMap<String, String> keyPathToTitleMap;
    protected NSPopUpButton unitsPopUpButton;
    protected List<String> units;

    private NSTextField textField;
    private String lastAmountValue = "";
    private Pattern decimalNumberPattern;

    private boolean isTextFieldInitialized;

    private PredicateEditor predicateEditor;
 
    static {
        Class<SWTWeightRowTemplate> clazz = SWTWeightRowTemplate.class;
        
        proc2Callback = new Callback(clazz, "proc2", 2);
        proc2CallbackAddress = proc2Callback.getAddress();
        if (proc2CallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        proc3Callback = new Callback(clazz, "proc3", 3);
        proc3CallbackAddress = proc3Callback.getAddress();
        if (proc3CallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        matchForPredicateCallback = new CallbackFpret(clazz, "procMatchForPredicate", 3);
        matchForPredicateCallbackAddress = matchForPredicateCallback.getAddress();
        if (matchForPredicateCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
               
        long /*int*/ cls = OS.objc_allocateClassPair(OS.class_NSPredicateEditorRowTemplate, SWTWeightRowTemplate.class.getSimpleName(), 0);
        byte[] types = {'*','\0'};
        int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;
        
        OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
        OS.class_addMethod(cls, OS.sel_templateViews, proc2CallbackAddress, "@:");
        OS.class_addMethod(cls, OS.sel_copyWithZone_, proc3CallbackAddress, "@:@");
        OS.class_addMethod(cls, OS.sel_setPredicate_, proc3CallbackAddress, "v@:@");
        OS.class_addMethod(cls, OS.sel_controlTextDidChange, proc3CallbackAddress, "v@:@");
        OS.class_addMethod(cls, OS.sel_predicateWithSubpredicates_, proc3CallbackAddress, "@:@");
        OS.class_addMethod(cls, OS.sel_matchForPredicate_, matchForPredicateCallbackAddress, "d@:@");
        OS.class_addMethod(cls, OS.sel_dealloc, proc2CallbackAddress, "@:");
        
        OS.objc_registerClassPair(cls);
    }
    
    public SWTWeightRowTemplate(HashMap<String, String> keyPathToTitleMap, List<String> units, PredicateEditor predicateEditor) {
        super(0);
        alloc();
        
        this.keyPathToTitleMap = keyPathToTitleMap;
        this.units = units;
        this.predicateEditor = predicateEditor;
        
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
    
    NSPopUpButton unitsPopUpButton() {
        if (unitsPopUpButton == null) {
            unitsPopUpButton = (NSPopUpButton) new NSPopUpButton().alloc();
            
            NSRect rect = new NSRect();
            rect.width = 0; rect.height = 0;
            unitsPopUpButton.initWithFrame(rect, false);

            NSMenu unitsMenu = unitsPopUpButton.menu();

            for (String unit : units)
            	unitsMenu.addItemWithTitle(NSString.stringWith(unit), OS.sel_null, NSString.stringWith(""));
        }
        
        return unitsPopUpButton;
    }
    
    Pattern decimalNumberPattern() {
        if (decimalNumberPattern == null) {
            decimalNumberPattern = Pattern.compile("^(\\d+(\\.\\d*)?)?$");
        }
        
        return decimalNumberPattern;
    }
    
    void selectUnit(String unit) {
        int index = 0;
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).equalsIgnoreCase(unit))
                index = i;
        }
        unitsPopUpButton().selectItemAtIndex(index);
    }
     
    long /*int*/ templateViewsProc() {
        NSMutableArray views = new NSMutableArray(new NSArray(this.superTemplateViews()).mutableCopy());
        
        replaceKeyPathWithTitle(new NSPopUpButton(views.objectAtIndex(0)));
        
        views.addObject(unitsPopUpButton());
        
        if (!isTextFieldInitialized)
            initializeTextField(views.objectAtIndex(2).id);
        
        views.autorelease();
        
        return views.id;
    }

    private void initializeTextField(long /*int*/ id) {
        textField = new NSTextField(id);
            
        textField.setAlignment(OS.NSRightTextAlignment);
        
        setTextFieldSize();
        
        textField.setDelegate(this);

        isTextFieldInitialized = true;
    }
    
    private void setTextFieldSize() {
        NSSize size = new NSSize();
        size.width = TEXTFIELD_WIDTH;
        size.height = textField.frame().height;
        textField.setFrameSize(size);
    }
    
    static SWTWeightRowTemplate getThis(long /*int*/ id) {
        if (id == 0) return null;
        
        long /*int*/ [] jniRef = new long /*int*/ [1];
        OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
        if (jniRef[0] == 0) return null;
        
        return (SWTWeightRowTemplate)OS.JNIGetObject(jniRef[0]);
    }
    
    static long /*int*/ proc2(long /*int*/ id, long /*int*/ sel) {
    	SWTWeightRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        if (sel == OS.sel_templateViews) {
            return template.templateViewsProc();
        } else if (sel == OS.sel_dealloc) {
            return template.deallocProc();
        }
        
        return 0;
    }
    
    static long /*int*/ proc3(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
    	SWTWeightRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        if (sel == OS.sel_setPredicate_) {
            return template.setPredicateProc(arg0);
        } else if (sel == OS.sel_predicateWithSubpredicates_) {
            return template.predicateWithSubpredicatesProc(arg0);
        } else if (sel == OS.sel_copyWithZone_) {
            return template.copyWithZoneProc(arg0);
        } else if (sel == OS.sel_controlTextDidChange) {
            return template.controlTextDidChange(arg0);
        }
        
        return 0;
    }
    
    // Used for allowing only decimal numbers input into the money text field.
    // (alternative to using a custom NSNumberFormatter, as for some reason the isPartialStringValid* overriden methods are not called in SWT).
    private long /*int*/ controlTextDidChange(long /*int*/ arg0) {
    	NSNotification notification = new NSNotification(arg0);

    	if (notification.object().id == this.textField.id) {
    		String currentValue = textField.stringValue().getString();

    		Matcher matcher = decimalNumberPattern().matcher(currentValue);
    		if (matcher.matches()) {
    			lastAmountValue = currentValue;
    		}
    		else {
    			OS.NSBeep();
    			textField.setStringValue(NSString.stringWith(lastAmountValue));
    		}   
    	}

    	return 0;
    }

    long /*int*/ copyWithZoneProc(long /*int*/ arg0) {
    	SWTWeightRowTemplate newTemplate = new SWTWeightRowTemplate(keyPathToTitleMap, units, this.predicateEditor);
        
        newTemplate.initWithLeftExpressions(this.leftExpressions(), 
                                            this.rightExpressionAttributeType(), 
                                            this.modifier(), 
                                            this.operators(), 
                                            this.options());

        predicateEditor.addWeightRowTemplateInstance(new WeightRowTemplate(newTemplate));
        
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
        
        NSComparisonPredicate comparisonPredicate = new NSComparisonPredicate(predicate);
        
        NSComparisonPredicate newPredicate = (NSComparisonPredicate) new NSComparisonPredicate().alloc();
          
        NSExpression newRightExpression = NSExpression.expressionForConstantValue(NSString.stringWith(createWeightRightExpressionValue(comparisonPredicate)));
        
        newPredicate.initWithLeftExpression(comparisonPredicate.leftExpression(), 
                                            newRightExpression, 
                                            ComparisonPredicateModifier.NSDirectPredicateModifier.value(), 
                                            comparisonPredicate.predicateOperatorType(), 
                                            0);
             
        return newPredicate.id;
    }
    
    private String createWeightRightExpressionValue(NSComparisonPredicate comparisonPredicate) {
        String amount = comparisonPredicate.rightExpression().description().getString();
        if ("NaN".equalsIgnoreCase(amount))
            amount = "0";
        
        return "WEIGHT('" + amount + " " + units.get((int) unitsPopUpButton.indexOfSelectedItem()) + "')";

    }

    long /*int*/ setPredicateProc(long /*int*/ predicateId) {
        NSPredicate predicate = new NSPredicate(predicateId);
        
        if (!predicate.isKindOfClass(OS.class_NSComparisonPredicate)) 
            return this.superSetPredicateProc(predicate.id);
        
        NSComparisonPredicate comparisonPredicate = new NSComparisonPredicate(predicate.id);
             
        String[] unitParts = getWeightParts(comparisonPredicate.rightExpression().description().getString());

        selectUnit(unitParts[1]);
        
        NSComparisonPredicate newPredicate = (NSComparisonPredicate) new NSComparisonPredicate().alloc();
        newPredicate.initWithLeftExpression(comparisonPredicate.leftExpression(),
                                            NSExpression.expressionForConstantValue(NSString.stringWith(unitParts[0])), 
                                            comparisonPredicate.comparisonPredicateModifier(), 
                                            comparisonPredicate.predicateOperatorType(), 
                                            comparisonPredicate.options());
            
        return this.superSetPredicateProc(newPredicate.id);
    }
    
    private String[] getWeightParts(String rightExpressionValue) {
        int startPos = rightExpressionValue.indexOf('(');
        int endPos = rightExpressionValue.indexOf(')');
      
        return rightExpressionValue.substring(startPos + 1, endPos).replace("'", "").split(" ");
    }

    static double /*float*/ procMatchForPredicate(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
    	SWTWeightRowTemplate template = getThis(id);
        if (template == null) return 0;
                
        NSPredicate predicate = new NSPredicate(arg0);
        
        if (!predicate.isKindOfClass(OS.class_NSComparisonPredicate))
            return 0;
        
        NSComparisonPredicate comparisonPredicate = new NSComparisonPredicate(predicate);

        if (!template.keyPathToTitleMap.containsKey(comparisonPredicate.leftExpression().description().getString()))
            return 0;
        
        String rightExpr = comparisonPredicate.rightExpression().description().getString().toUpperCase().trim();
                
        return rightExpr.startsWith("\"WEIGHT(") ? 1 : 0;
    }
    
    long /*int*/ deallocProc() {
    	predicateEditor.removeWeightRowTemplateInstance(new WeightRowTemplate(this));
    	
        internal_dispose();
        
        unitsPopUpButton.release();
        
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
    
    public void refreshLayout() {
        resizeControls();
    }
    
    private TreeSet<NSView> getXSortedViews() {
        if (textField == null) return null;
        
        NSArray subviews = textField.superview().subviews();
        
        TreeSet<NSView> sortedViews = new TreeSet<NSView>(
                new Comparator<NSView>() {
                    public int compare(NSView v1, NSView v2) {
                        if (v1.frame().x < v2.frame().x)
                            return -1;
                        else 
                            return v1.frame().x > v2.frame().x ? 1 : 0;
                    }
                });
         
        for (int i = 0; i < subviews.count(); i++) {
            NSView view = new NSView(subviews.objectAtIndex(i));
            sortedViews.add(view);
         }
        
        return sortedViews;
    }
    
    private void resizeControls() {
        if (textField == null) return;
        
        TreeSet<NSView> sortedViews = getXSortedViews();
        if (sortedViews == null) return;
        
        sortedViews.pollLast();
        NSView removeButton = sortedViews.pollLast();
        NSView unitButton = sortedViews.pollLast();
        NSView textView = sortedViews.pollLast();
        NSView operatorView = sortedViews.pollLast();
        
        float availableWidth = (float) (removeButton.frame().x - textView.frame().x);
        float neededWidth = TEXTFIELD_WIDTH + SEPARATOR_WIDTH + UNITBUTTON_WIDTH + SEPARATOR_WIDTH;
              
        boolean didAdjustOperatorWidth = false;
        float adjustWidth = 0;

        // If necessary, shrink the operator button and make the amount text field larger.
        // (alternative to making the unit button slide to the right, because of (re)layout issues). 
        if (availableWidth < neededWidth) {
            float addTextWidth = (float) (TEXTFIELD_WIDTH - textView.frame().width);

            if (addTextWidth > 0) {            
                NSRect operatorFrame = operatorView.frame();
                float availableOperatorWidth = (float) (operatorFrame.width - MIN_OPERATOR_WIDTH);

                if (availableOperatorWidth > 0) {
                    adjustWidth = Math.min(addTextWidth, availableOperatorWidth);
                    operatorFrame.width -= adjustWidth ;
                    operatorView.setFrame(operatorFrame);
                    operatorView.setNeedsDisplay(true); 
                    didAdjustOperatorWidth = true;
                }
            }
        }

        NSRect textFrame = textView.frame();
        textFrame.width = unitButton.frame().x - textFrame.x - SEPARATOR_WIDTH ;
        if (didAdjustOperatorWidth) {
            textFrame.width += adjustWidth;
            textFrame.x -= adjustWidth;
        }
        textView.setFrame(textFrame);
        textView.setNeedsDisplay(true);
    }

}
