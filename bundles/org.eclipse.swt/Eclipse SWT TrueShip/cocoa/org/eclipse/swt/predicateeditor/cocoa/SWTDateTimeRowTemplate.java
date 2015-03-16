package org.eclipse.swt.predicateeditor.cocoa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.widgets.PredicateEditor.AttributeType;
import org.eclipse.swt.widgets.PredicateEditor.ComparisonPredicateModifier;

public class SWTDateTimeRowTemplate extends NSPredicateEditorRowTemplate {
    private static final long NSDATE_REFERENCE_DATE = 978307200000L;

    static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};
    
    static Callback proc2Callback;
    static long /*int*/ proc2CallbackAddress;
    
    static Callback proc3Callback;
    static long /*int*/ proc3CallbackAddress;
    
    static CallbackFpret matchForPredicateCallback;
    static long /*int*/ matchForPredicateCallbackAddress;
    
    long /*int*/ jniRef;
    
    HashMap<String, String> keyPathToTitleMap;

    private boolean hasDateAndTimeRightExpression;
    private boolean wasUpdated;
    
    static HashMap<String, String> numericToDateOperatorsTitleMap = new HashMap<String, String>();
    
    static {
        numericToDateOperatorsTitleMap.put("is less than", "is before");
        numericToDateOperatorsTitleMap.put("is less than or equal to", "is before or on");
        numericToDateOperatorsTitleMap.put("is greater than", "is after");
        numericToDateOperatorsTitleMap.put("is greater than or equal to", "is after or on");
    }
    
    static {
        Class<SWTDateTimeRowTemplate> clazz = SWTDateTimeRowTemplate.class;
        
        proc2Callback = new Callback(clazz, "proc2", 2);
        proc2CallbackAddress = proc2Callback.getAddress();
        if (proc2CallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        proc3Callback = new Callback(clazz, "proc3", 3);
        proc3CallbackAddress = proc3Callback.getAddress();
        if (proc3CallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        matchForPredicateCallback = new CallbackFpret(clazz, "procMatchForPredicate", 3);
        matchForPredicateCallbackAddress = matchForPredicateCallback.getAddress();
        if (matchForPredicateCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
               
        long /*int*/ cls = OS.objc_allocateClassPair(OS.class_NSPredicateEditorRowTemplate, SWTDateTimeRowTemplate.class.getSimpleName(), 0);
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
    
    public SWTDateTimeRowTemplate (HashMap<String, String> keyPathToTitleMap) {
        this(keyPathToTitleMap, false);
    }
    
    public SWTDateTimeRowTemplate(HashMap<String, String> keyPathToTitleMap, boolean hasDateAndTimeRightExpression) {
        super(0);
        alloc();
        
        this.keyPathToTitleMap = keyPathToTitleMap;
        this.hasDateAndTimeRightExpression = hasDateAndTimeRightExpression;
        
        jniRef = OS.NewGlobalRef(this);
        if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
        OS.object_setInstanceVariable(this.id, SWT_OBJECT, jniRef);
    }

    public id initWithLeftExpressions(NSArray leftExpressions, long /*int*/ attributeType, long /*int*/ modifier, NSArray operators, long /*int*/ options) {
        return super.initWithLeftExpressions(leftExpressions, AttributeType.NSDateAttributeType.value(), modifier, operators, options);
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
        
        if (!wasUpdated)
            updateRowForDateControl(views);
        
        return views.id;
    }
    
    static SWTDateTimeRowTemplate getThis(long /*int*/ id) {
        if (id == 0) return null;
        
        long /*int*/ [] jniRef = new long /*int*/ [1];
        OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
        if (jniRef[0] == 0) return null;
        
        return (SWTDateTimeRowTemplate)OS.JNIGetObject(jniRef[0]);
    }
    
    static long /*int*/ proc2(long /*int*/ id, long /*int*/ sel) {
    	SWTDateTimeRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        if (sel == OS.sel_templateViews) {
            return template.templateViewsProc();
        } else if (sel == OS.sel_dealloc) {
            return template.deallocProc();
        }
        
        return 0;
    }
    
    static long /*int*/ proc3(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
        SWTDateTimeRowTemplate template = getThis(id);
        if (template == null) return 0;
        
        if (sel == OS.sel_setPredicate_) {
            return template.setPredicate(arg0);
        } else if (sel == OS.sel_predicateWithSubpredicates_) {
            return template.predicateWithSubpredicates(arg0);
        } else if (sel == OS.sel_copyWithZone_) {
            return template.copyWithZoneProc(arg0);
        }
        
        return 0;
    }
    
    long /*int*/ copyWithZoneProc(long /*int*/ arg0) {
        SWTDateTimeRowTemplate newTemplate = new SWTDateTimeRowTemplate(this.keyPathToTitleMap);
        
        newTemplate.initWithLeftExpressions(this.leftExpressions(), 
                                            this.rightExpressionAttributeType(), 
                                            this.modifier(), 
                                            this.operators(), 
                                            this.options());
        
        newTemplate.hasDateAndTimeRightExpression = this.hasDateAndTimeRightExpression;
        
        return newTemplate.id;
    }
    
    long /*int*/ superSetPredicate(long /*int*/ predicate) {
        objc_super super_struct = new objc_super();
        super_struct.receiver = id;
        super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
        
        return OS.objc_msgSendSuper(super_struct, OS.sel_setPredicate_, predicate);
    }
    
    long /*int*/ setPredicate(long /*int*/ predicateId) {
        NSPredicate predicate = new NSPredicate(predicateId);
        
        if (!predicate.isKindOfClass(OS.class_NSComparisonPredicate)) 
            return this.superSetPredicate(predicate.id);
        
        NSComparisonPredicate comparisonPredicate = new NSComparisonPredicate(predicate.id);
        
        Date javaDate = getDate(comparisonPredicate.rightExpression().description().getString());
        if (javaDate == null)
            javaDate = new Date();
             
        NSComparisonPredicate newPredicate = (NSComparisonPredicate) new NSComparisonPredicate().alloc();
        
        newPredicate.initWithLeftExpression(comparisonPredicate.leftExpression(),
                                            createNSPredicateDateExpression(javaDate),
                                            ComparisonPredicateModifier.NSDirectPredicateModifier.value(), 
                                            comparisonPredicate.predicateOperatorType(), 
                                            0);
        
        NSArray views = new NSArray(this.superTemplateViews());
        for (int i = 0; i < views.count(); i++) {
            NSView subview = new NSView(views.objectAtIndex(i));
            if (subview.isKindOfClass(OS.class_NSDatePicker)) {
                NSDatePicker datePicker = new NSDatePicker(subview);
                datePicker.setDateValue(new NSDate(NSDate.dateWithTimeIntervalSinceReferenceDate(dateToNSDate(javaDate))));
                break;
            }
        }
            
        return this.superSetPredicate(newPredicate.id);
    }
    
    long /*int*/ superPredicateWithSubpredicates(long /*int*/ predicate) {
        objc_super super_struct = new objc_super();
        super_struct.receiver = id;
        super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
        
        return OS.objc_msgSendSuper(super_struct, OS.sel_predicateWithSubpredicates_, predicate);
    }
    
    long /*int*/ predicateWithSubpredicates(long /*int*/ arg0) {
        NSPredicate predicate = new NSPredicate(superPredicateWithSubpredicates(arg0));
        
        if (!predicate.isKindOfClass(OS.class_NSComparisonPredicate)) return predicate.id;
        
        NSComparisonPredicate comparisonPredicate = new NSComparisonPredicate(predicate);
          
        NSExpression newRightExpression = NSExpression.expressionForConstantValue(NSString.stringWith(createDateRightExpressionValue(comparisonPredicate)));
        
        NSComparisonPredicate newPredicate = (NSComparisonPredicate) new NSComparisonPredicate().alloc();
        
        newPredicate.initWithLeftExpression(comparisonPredicate.leftExpression(), 
                                            newRightExpression, 
                                            ComparisonPredicateModifier.NSDirectPredicateModifier.value(), 
                                            comparisonPredicate.predicateOperatorType(), 
                                            0);
             
        return newPredicate.id;
    }
    
    static double /*float*/ procMatchForPredicate(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
        SWTDateTimeRowTemplate template = getThis(id);
        if (template == null) return 0;
                
        NSPredicate predicate = new NSPredicate(arg0);
        
        if (!predicate.isKindOfClass(OS.class_NSComparisonPredicate))
            return 0;

        NSComparisonPredicate comparisonPredicate = new NSComparisonPredicate(predicate);

        if (!template.keyPathToTitleMap.containsKey(comparisonPredicate.leftExpression().description().getString()))
            return 0;
        
        String rightExpr = comparisonPredicate.rightExpression().description().getString().toUpperCase().trim();
                
        return rightExpr.startsWith("\"DATE(")? 1 : 0;
    }
    
    long /*int*/ deallocProc() {
        internal_dispose();
        
        objc_super super_struct = new objc_super();
        super_struct.receiver = id;
        super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
        
        return OS.objc_msgSendSuper(super_struct, OS.sel_dealloc);
    }
    
    private NSExpression createNSPredicateDateExpression(Date date) {
        NSNumber dateAsNumber = NSNumber.numberWithDouble(dateToNSDate(date));
        NSExpression dateAsNumberExpression = NSExpression.expressionForConstantValue(dateAsNumber);
        
        NSMutableArray castArray = NSMutableArray.arrayWithCapacity(2);
        castArray.addObject(dateAsNumberExpression);
        castArray.addObject(NSExpression.expressionForConstantValue(NSString.stringWith("NSDate")));
        
        NSExpression castDateExpression = NSExpression.expressionForFunction(NSString.stringWith("castObject:toType:"), castArray);
        
        return castDateExpression;
    }
    
    private String createDateRightExpressionValue(NSComparisonPredicate comparisonPredicate) {
        NSExpression rightExpression = comparisonPredicate.rightExpression();
        NSDate nsDate = new NSDate(rightExpression.expressionValueWithObject(null, null));
        
        Date javaDate = new Date((long) (nsDate.timeIntervalSince1970() * 1000));
        SimpleDateFormat sdf;
        if (hasDateAndTimeRightExpression) {
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        else
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        return "DATE('" + sdf.format(javaDate) + "')";
    }

    private double dateToNSDate(Date date) {
        return (date.getTime() - NSDATE_REFERENCE_DATE)/1000;
    }
    
    private Date getDate(String dateRightExpressionValue) {
        int startPos = dateRightExpressionValue.indexOf('(');
        int endPos = dateRightExpressionValue.indexOf(')');
      
        String dateString = dateRightExpressionValue.substring(startPos + 1, endPos).replace("'", "").trim();
        
        SimpleDateFormat sdf;
        if (hasDateAndTimeRightExpression) {
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }
        
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            return null;    
        }
    }
    
    private void updateRowForDateControl(NSArray views) {
        makeDateTimePredicateOperators(new NSView(views.objectAtIndex(1)));
        
        NSDatePicker datePicker = new NSDatePicker(views.objectAtIndex(2));
        adjustDateTimeControl(datePicker);
        
        wasUpdated = true;
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
