package org.eclipse.swt.widgets;

import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.keyvaluecoding.*;
import org.eclipse.swt.keyvaluecoding.cocoa.*;
import org.eclipse.swt.predicate.*;
import org.eclipse.swt.predicateeditor.*;
import org.eclipse.swt.widgets.Composite;

public class PredicateEditor extends Control implements PredicateVisitable {
    
    public enum PredicateOperatorType {
        NSLessThanPredicateOperatorType(0),
        NSLessThanOrEqualToPredicateOperatorType(1),
        NSGreaterThanPredicateOperatorType(2),
        NSGreaterThanOrEqualToPredicateOperatorType(3),
        NSEqualToPredicateOperatorType(4),
        NSNotEqualToPredicateOperatorType(5),
        NSMatchesPredicateOperatorType(6),
        NSLikePredicateOperatorType(7),
        NSBeginsWithPredicateOperatorType(8),
        NSEndsWithPredicateOperatorType(9),
        NSInPredicateOperatorType(10),
        NSCustomSelectorPredicateOperatorType(11), 
        NSContainsPredicateOperatorType(99),  // OSX 10.5 and later
        NSBetweenPredicateOperatorType(100);   // OSX 10.5 and later

        private int value;
        
        private PredicateOperatorType(int value) {
            this.value = value;
        }
        
        public int value() {
            return value;
        }
    }
    
    public enum CompoundPredicateType {
        NSNotPredicateType,
        NSAndPredicateType,
        NSOrPredicateType;
        
        public int value() {
            return this.ordinal();
        }
     }
        
    public enum AttributeType {
        NSUndefinedAttributeType(0),
        NSInteger16AttributeType(100),
        NSInteger32AttributeType(200),
        NSInteger64AttributeType(300),
        NSDecimalAttributeType(400),
        NSDoubleAttributeType(500),
        NSFloatAttributeType(600),
        NSStringAttributeType(700),
        NSBooleanAttributeType(800),
        NSDateAttributeType(900),
        NSBinaryDataAttributeType(1000),
        NSTransformableAttributeType(1800),
        NSObjectIDAttributeType(2000);
        
        private int value;
        
        private AttributeType(int value) {
            this.value = value;
        }
        
        public int value() {
            return value;
        }
    }
    
    public enum ComparisonPredicateModifier {
        NSDirectPredicateModifier,
        NSAllPredicateModifier,
        NSAnyPredicateModifier;
        
        public int value() {
            return this.ordinal();
        }
    }
    
    public enum ComparisonPredicateOption {
        NSCaseInsensitivePredicateOption(0x01),
        NSDiacriticInsensitivePredicateOption(0x02),
        NSNormalizedPredicateOption(0x04),
        NSLocaleSensitivePredicateOption(0x08);
        
        private int value;
        
        private ComparisonPredicateOption(int value) {
            this.value = value;
        }
        
        public int value() {
            return value;
        }
    }
    
    public enum AutoresizingMaskOptions {
        NSViewNotSizable(0),
        NSViewMinXMargin(1),
        NSViewWidthSizable(2),
        NSViewMaxXMargin(4),
        NSViewMinYMargin(8),
        NSViewHeightSizable(16),
        NSViewMaxYMargin(32);
        
        private int value;
        
        private AutoresizingMaskOptions(int value) {
            this.value = value;
        }
        
        public int value() {
            return value;
        }
    }
    
    class PredicateEditorNotification implements KeyValueCoding {
        
        public void setValueForKey(Object value, String key) {
            currentPredicate = new NSPredicate((id)value);
            
            firstTimePredicateUpdate = false;
                              
            if (enabledNotifications)               
                sendSelectionEvent (SWT.Selection);
            
            handlePredicateChanged();
        }

        public Object valueForKey(String key) {
            return currentPredicate;
        }
    }
    
    private NSPredicateEditor nsPredicateEditor;
    
    boolean firstTimePredicateUpdate = true;
    NSPredicate currentPredicate;
    protected List<NSPredicateEditorRowTemplate> rowTemplates = new ArrayList<NSPredicateEditorRowTemplate>();
    protected HashSet<DynamicRightValuesRowTemplate> dynamicRowTemplateInstances = new HashSet<DynamicRightValuesRowTemplate>();
    protected HashSet<String> dynamicRightValuesRowsKeyPaths = new HashSet<String>();
    
    protected HashSet<MoneyRowTemplate> moneyRowTemplateInstances = new HashSet<MoneyRowTemplate>();
    protected HashSet<LengthRowTemplate> lengthRowTemplateInstances = new HashSet<LengthRowTemplate>();
    protected HashSet<WeightRowTemplate> weightRowTemplateInstances = new HashSet<WeightRowTemplate>();
    
    private boolean enabledNotifications = false;
    protected PredicateEditorNotification notification = new PredicateEditorNotification();
    SWTKeyValueCodingDecorator kvNotification;
    private long lastNumberOfRows;

    private boolean dirty = false;

    private int totalSelectionListeners = 0;
    
    private boolean enabledDragAndDrop = true;
    
    public PredicateEditor(Composite parent, int style) {
        super(parent, style);
        
        kvNotification = new SWTKeyValueCodingDecorator(notification);
        bind("value", kvNotification, "value");
    }
    
    public void addSelectionListener(SelectionListener listener) {
        checkWidget ();
        
        if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
        
        TypedListener typedListener = new TypedListener(listener);
        addListener (SWT.Selection,typedListener);
        addListener (SWT.DefaultSelection,typedListener);
        
        totalSelectionListeners++;
        
        enableNotifications();
    }
    
    public void enableNotifications() {
        enableNotifications(true);
    }
    
    public void disableNotifications() {
        enableNotifications(false);
    }
    
    public void enableNotifications(boolean enable) {
        this.enabledNotifications = enable;
    }
    
    public void enableDragAndDrop() {
        enableDragAndDrop(true);
    }
    
    public void disableDragAndDrop() {
        enableDragAndDrop(false);
    }
    
    public void enableDragAndDrop(boolean enable) {
        this.enabledDragAndDrop = enable;
    }
    
    public boolean isDragAndDropEnabled() {
        return this.enabledDragAndDrop;
    }
    
    public void removeSelectionListener(SelectionListener listener) {
        checkWidget ();
        
        if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
        
        if (eventTable == null) return;
        
        eventTable.unhook (SWT.Selection, listener);
        eventTable.unhook (SWT.DefaultSelection, listener);
        
        if (totalSelectionListeners > 0)
            totalSelectionListeners--;
        
        if (totalSelectionListeners == 0)
            disableNotifications();
    }
    
    public void setRowTemplates(List<NSPredicateEditorRowTemplate> rowTemplates) {                
        NSMutableArray templates = NSMutableArray.arrayWithCapacity(rowTemplates.size());
        for (NSPredicateEditorRowTemplate template : rowTemplates)
            templates.addObject(template);
        
        nsPredicateEditor.setRowTemplates(templates);
        
        this.rowTemplates = rowTemplates;
    }
    
    public void setRowTemplates() {
        setRowTemplates(rowTemplates);
    }
    
    public void addRowTemplate(PredicateEditorRowTemplate template) {
        rowTemplates.add(new NSPredicateEditorRowTemplate(template.id()));
    }
    
    public void setPredicateWithFormat(String format) {
        setPredicate(NSPredicate.predicateWithFormat(NSString.stringWith(format)));
    }
    
    public void setPredicateWithFormat(String format, String[] arguments) {
        NSMutableArray args = NSMutableArray.arrayWithCapacity(arguments.length);
        for (int i = 0; i < arguments.length; i++)
            args.addObject(NSString.stringWith(arguments[i]));
        
        setPredicate(NSPredicate.predicateWithFormat(NSString.stringWith(format), args));
    }
    
    void setPredicate(NSPredicate predicate) {
        this.currentPredicate = predicate;
        nsPredicateEditor.setObjectValue(predicate);
    }
    
    public void acceptPredicateVisitor(PredicateVisitor visitor, Object context) {
        if (firstTimePredicateUpdate) {
            this.currentPredicate = nsPredicateEditor.predicate();
            firstTimePredicateUpdate = false;
        }
        visitor.visit(new Predicate(this.currentPredicate.id), context);
    }
    
    public void acceptPredicateVisitor(PredicateVisitor visitor) {
        acceptPredicateVisitor(visitor, null);
    }
    
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        
        NSSize size = new NSSize();
        size.width = width;
        size.height = height;
        nsPredicateEditor.setFrameSize(size);
    }
    
    @Override
    public void setSize (Point size) {
        setSize(Math.max (0, size.x), Math.max (0, size.y));
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }
    
    @Override
    protected void checkSubclass () {
    }
    
    @Override
    public void dispose() {
        if (this.isDisposed()) return;
        
        disableNotifications();
        
        unbind("value");
        
        if (kvNotification != null) {
            kvNotification.release();
            kvNotification = null;
        }
        
        for (NSPredicateEditorRowTemplate rowTemplate : rowTemplates) {
            if (rowTemplate != null)
                rowTemplate.release();
        }
        
        rowTemplates.clear();
        
        super.dispose();
    }
    
    @Override
    void createHandle () {
        super.createHandle ();
        
        nsPredicateEditor = (NSPredicateEditor) new SWTPredicateEditor().alloc();
        nsPredicateEditor = (NSPredicateEditor) nsPredicateEditor.init();
        
        nsPredicateEditor.setAutoresizingMask(AutoresizingMaskOptions.NSViewWidthSizable.value() |
                                              AutoresizingMaskOptions.NSViewHeightSizable.value());
        
        nsPredicateEditor.setCanRemoveAllRows(false);
        
        view = nsPredicateEditor;
    }
    
    @Override
    public Point computeSize (int wHint, int hHint, boolean changed) {
        checkWidget();
        
        int width = (int) nsPredicateEditor.frame().width;
        int height = (int) nsPredicateEditor.frame().height;
        
        int maxDynRowWidth = 0;
        GC gc = new GC(this);
        for (DynamicRightValuesRowTemplate template : dynamicRowTemplateInstances) {
             maxDynRowWidth = Math.max(maxDynRowWidth, template.preferredWidth(gc, width));
        }
        
        width = Math.max(width, maxDynRowWidth);
        
        if (wHint != SWT.DEFAULT) 
            width = wHint;
        
        if (hHint != SWT.DEFAULT) {
            height = hHint;
        }

        return new Point(width, height);
    }
    
    @Override
    long /*int*/ hitTest (long /*int*/ id, long /*int*/ sel, NSPoint point) {
        long /*int*/ viewId = super.hitTest(id, sel, point);
        
        if (viewId == 0 || nsPredicateEditor == null || isDragAndDropEnabled())
            return viewId;
        
        // Disable NSPredicateEditor DnD by hiding mouse-down events, except those destined to its subviews.
        return nsPredicateEditor.frame().width == new NSView(viewId).frame().width ? 0 : viewId;
    }

    // Get the current predicate, 
    // but modified so that 'NOT (predicate)' becomes 'NOT (predicate OR FALSEPREDICATE)' , 
    // which allows NSPredicateEditor to restore simple negated predicates.
    public Predicate getPredicate() {
        if (dirty) {
            reloadPredicate();
            dirty = false;
        }
        return new Predicate(normalizePredicate(nsPredicateEditor.predicate()).id);
    }
    
    public void setDirty() {
        this.dirty = true;
    }
    
    public void reloadPredicate() {
        boolean oldEnabledNotifications = enabledNotifications;
        disableNotifications();
        nsPredicateEditor.reloadPredicate();
        enableNotifications(oldEnabledNotifications);
    }
    
    /*
     * Replace one leg compound predicates to equivalent ones with 2 subpredicates.
     * Ex. 'AND KEY_PATH > 5' will change to 'KEY_PATH > 5 AND TRUEPREDICATE',
     *     'OR  KEY_PATH > 5' will change to 'KEY_PATH > 5 OR FALSEPREDICATE',
     *     'NOT (KEY_PATH > 5)' will change to 'NOT (KEY_PATH > 5 OR FALSEPREDICATE)'.
     */
    private id normalizePredicate(NSPredicate predicate) {
        if (predicate.isKindOfClass(OS.class_NSComparisonPredicate))
            return predicate;
         
        NSCompoundPredicate nsCompoundPredicate = new NSCompoundPredicate(predicate.id);
        
        NSMutableArray normalizedSubpredicates = normalizeSubpredicates(nsCompoundPredicate);
        
        long compoundPredicateType = nsCompoundPredicate.compoundPredicateType();
        
        if (compoundPredicateType == CompoundPredicateType.NSNotPredicateType.value() || normalizedSubpredicates.count() > 1)
            return createCompoundPredicate(new NSCompoundPredicate(predicate.id).compoundPredicateType(), normalizedSubpredicates);
        else if (compoundPredicateType == CompoundPredicateType.NSAndPredicateType.value())
            return createCompoundAndPredicateFromSinglePredicate(normalizedSubpredicates.objectAtIndex(0));
        else if (compoundPredicateType == CompoundPredicateType.NSOrPredicateType.value())
            return createCompoundOrPredicateFromSinglePredicate(normalizedSubpredicates.objectAtIndex(0));
        else
            return predicate;
    }
    
    private NSMutableArray normalizeSubpredicates(NSCompoundPredicate nsCompoundPredicate) {
        NSArray subpredicates = nsCompoundPredicate.subpredicates();
        
        NSMutableArray normalizedSubpredicates = NSMutableArray.arrayWithCapacity(subpredicates.count());
        for (int i = 0; i < subpredicates.count(); i++)
            normalizedSubpredicates.addObject(normalizePredicate(new NSPredicate(subpredicates.objectAtIndex(i))));
        
        return normalizedSubpredicates;
    }

    private id createCompoundOrPredicateFromSinglePredicate(id predicate) {
        NSMutableArray subpredicates = NSMutableArray.arrayWithCapacity(2);
        subpredicates.addObject(predicate);
        subpredicates.addObject(NSPredicate.predicateWithValue(false));
        
        return createCompoundPredicate(CompoundPredicateType.NSOrPredicateType.value(), subpredicates);
    }

    private id createCompoundAndPredicateFromSinglePredicate(id predicate) {
        NSMutableArray subpredicates = NSMutableArray.arrayWithCapacity(2);
        subpredicates.addObject(predicate);
        subpredicates.addObject(NSPredicate.predicateWithValue(true));
        
        return createCompoundPredicate(CompoundPredicateType.NSAndPredicateType.value(), subpredicates);
    }
    
    private id createCompoundPredicate(long /*int*/ compoundPredicateType, NSMutableArray subpredicates) {
      NSCompoundPredicate predicate = (NSCompoundPredicate) new NSCompoundPredicate().alloc();
      predicate.initWithType(compoundPredicateType, subpredicates);
      
      return predicate;
    }

    public void updatePredicate(String leftExpr, String rightExpr) {        
        NSPredicate newPredicate = new NSPredicate(updatePredicate(nsPredicateEditor.predicate(), leftExpr, rightExpr));
        
        nsPredicateEditor.setObjectValue(newPredicate); 
    }

    public void updatePredicateWithPredicate(Predicate predicate, String leftExpr, String rightExpr) {
        NSPredicate newPredicate = new NSPredicate(updatePredicate(new NSPredicate(predicate.id()), leftExpr, rightExpr));
        nsPredicateEditor.setObjectValue(newPredicate); 
    }
    
    public HashMap<String, String> getAllComparisonSubpredicatesLeftRightExpressions() {
        HashMap<String, String>lrExprMap = new HashMap<String, String>();
        NSCompoundPredicate predicate = new NSCompoundPredicate(nsPredicateEditor.predicate().id);
        extractLRExpressionsToMap(predicate, lrExprMap);
        
        return lrExprMap;
    }
    
    public HashSet<DynamicRightValuesRowTemplate> allDynamicRowTemplateInstances() {
        return dynamicRowTemplateInstances;
    }
    
    private void bind(String binding, SWTKeyValueCodingDecorator toObject, String keyPath) {
        nsPredicateEditor.bind(NSString.stringWith(binding), toObject, NSString.stringWith(keyPath), null);
    }
    
    private void unbind(String binding) {
        nsPredicateEditor.unbind(NSString.stringWith(binding));
    }
    
    private id updatePredicate(NSPredicate predicate, String leftExpr, String rightExpr) {
        if (predicate.isKindOfClass(OS.class_NSComparisonPredicate)) {
            NSComparisonPredicate cp = new NSComparisonPredicate(predicate.id);
            
            if (leftExpr.equalsIgnoreCase(cp.leftExpression().description().getString())) {
                NSExpression newRightExpr = NSExpression.expressionForConstantValue(NSString.stringWith(rightExpr));
                
                NSComparisonPredicate newPredicate = (NSComparisonPredicate) new NSComparisonPredicate().alloc();
                return newPredicate.initWithLeftExpression(
                        cp.leftExpression(), 
                        newRightExpr, 
                        cp.comparisonPredicateModifier(), 
                        cp.predicateOperatorType(), 
                        cp.options());
            }
            else 
                return predicate;
        }
        else {
            NSArray subpredicates = new NSCompoundPredicate(predicate.id).subpredicates();
            
            NSMutableArray updatedSubpredicates = NSMutableArray.arrayWithCapacity(subpredicates.count());
            for (int i = 0; i < subpredicates.count(); i++)
                updatedSubpredicates.addObject(updatePredicate(new NSPredicate(subpredicates.objectAtIndex(i)), leftExpr, rightExpr));
            
            NSCompoundPredicate newPredicate = (NSCompoundPredicate) new NSCompoundPredicate().alloc();
            return newPredicate.initWithType(new NSCompoundPredicate(predicate.id).compoundPredicateType(), updatedSubpredicates);
        }
    }

    private void extractLRExpressionsToMap(NSCompoundPredicate compoundPredicate, HashMap<String, String> lrExprMap) {
        NSArray subpredicates = compoundPredicate.subpredicates();
        for (int i = 0; i < subpredicates.count(); i++) {
            NSPredicate predicate = new NSPredicate(subpredicates.objectAtIndex(i));
            if (predicate.isKindOfClass(OS.class_NSComparisonPredicate)) {
                NSComparisonPredicate p = new NSComparisonPredicate(predicate.id);
                lrExprMap.put(p.leftExpression().description().getString(), p.rightExpression().description().getString());
            } else {
                extractLRExpressionsToMap(new NSCompoundPredicate(predicate.id), lrExprMap);
            }
        }
    }

    public void addDynamicRowTemplateInstance(DynamicRightValuesRowTemplate newTemplate) {
        boolean foundTemplate = false;
        
        for (DynamicRightValuesRowTemplate t : dynamicRowTemplateInstances) {
            if (t.equals(newTemplate)) {
                t.setPredicate(newTemplate.getPredicate());
                foundTemplate = true;
                break ;
            }
        }
               
        if (!foundTemplate) {
            dynamicRowTemplateInstances.add(newTemplate);
            dynamicRightValuesRowsKeyPaths.add(newTemplate.getKeyPath());
        }
    }

    public void removeDynamicRowTemplateInstance(DynamicRightValuesRowTemplate template) {
        for (DynamicRightValuesRowTemplate t : dynamicRowTemplateInstances) {
            if (t.equals(template)) {
                dynamicRowTemplateInstances.remove(t);
                return;
            }
        }
    }

    public void addMoneyRowTemplateInstance(MoneyRowTemplate template) {
        moneyRowTemplateInstances.add(template);    
    }
    
    public void removeMoneyRowTemplateInstance(MoneyRowTemplate template) {
        moneyRowTemplateInstances.remove(template);
    }
    
    public void addLengthRowTemplateInstance(LengthRowTemplate template) {
        lengthRowTemplateInstances.add(template);    
    }
    
    public void removeLengthRowTemplateInstance(LengthRowTemplate template) {
    	lengthRowTemplateInstances.remove(template);
    }

    public void addWeightRowTemplateInstance(WeightRowTemplate template) {
        weightRowTemplateInstances.add(template);    
    }
    
    public void removeWeightRowTemplateInstance(WeightRowTemplate template) {
    	weightRowTemplateInstances.remove(template);
    }
    
    public void refreshLayout() {
        for (DynamicRightValuesRowTemplate template : dynamicRowTemplateInstances)
            template.refreshLayout();
        
        for (MoneyRowTemplate template : moneyRowTemplateInstances)
            template.refreshLayout();
        
        for (LengthRowTemplate template : lengthRowTemplateInstances)
            template.refreshLayout();
        
        for (WeightRowTemplate template : weightRowTemplateInstances)
            template.refreshLayout();
    }
    
    public void refreshUI() {
        for (DynamicRightValuesRowTemplate template : dynamicRowTemplateInstances)
            template.refreshUI();
    }
    
    private void handlePredicateChanged() {
        if (lastNumberOfRows > 0 && !checkIfRowWasRemoved())
            refreshUI();
    
        lastNumberOfRows = nsPredicateEditor.numberOfRows();
    }
    
    private boolean checkIfRowWasRemoved() {
        long currentNumberOfRows = nsPredicateEditor.numberOfRows();
        
        if (currentNumberOfRows == lastNumberOfRows)
            return countDynamicRowsPredicates(currentPredicate.predicateFormat().getString()) < dynamicRowTemplateInstances.size();
        else
            return currentNumberOfRows < lastNumberOfRows;
    }
    
    private int countDynamicRowsPredicates(String predicateFormat) {
        int count = 0;
        
        for (String keyPath : dynamicRightValuesRowsKeyPaths)
            count += countPredicates(predicateFormat, keyPath + " ");
        
        return count;
    }
    
    private int countPredicates(String predicateFormat, String keyPath) {
        if (predicateFormat == null) return 0;
        
        Pattern p = Pattern.compile(keyPath);
        Matcher m = p.matcher(predicateFormat);
        int count = 0;
        while (m.find())
            count++;
        
        return count;
    }
}
