package org.eclipse.swt.keyvaluecoding.cocoa;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.keyvaluecoding.*;

public class SWTKeyValueCodingDecorator extends NSObject {
    static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};

    static Callback valueForKeyCallback;
    static long /*int*/ valueForKeyCallbackAddress;
    
    static Callback setValueForKeyCallback;
    static long /*int*/ setValueForKeyCallbackAddress;
    
    long /*int*/ jniRef;
    
    KeyValueCoding decoratedObject;

    static {
        Class<SWTKeyValueCodingDecorator> clazz = SWTKeyValueCodingDecorator.class;

        valueForKeyCallback = new Callback(clazz, "valueForKeyProc", 3);
        valueForKeyCallbackAddress = valueForKeyCallback.getAddress();
        if (valueForKeyCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        setValueForKeyCallback = new Callback(clazz, "setValueForKeyProc", 4);
        setValueForKeyCallbackAddress = setValueForKeyCallback.getAddress();
        if (setValueForKeyCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
        
        long /*int*/ cls = OS.objc_allocateClassPair(OS.class_NSObject, SWTKeyValueCodingDecorator.class.getSimpleName(), 0);
        byte[] types = {'*','\0'};
        int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;

        OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);

        OS.class_addProtocol(cls, OS.protocol_NSKeyValueCoding);
        OS.class_addMethod(cls, OS.sel_valueForKey_, valueForKeyCallbackAddress, "@:@");
        OS.class_addMethod(cls,  OS.sel_setValue_forKey_, setValueForKeyCallbackAddress, "@:@@");

        OS.objc_registerClassPair(cls);
    }
    
    public SWTKeyValueCodingDecorator(KeyValueCoding object) {
        super(0);
        alloc().init();
        
        jniRef = OS.NewGlobalRef(this);
        if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
        
        OS.object_setInstanceVariable(this.id, SWT_OBJECT, jniRef);
        this.decoratedObject = object;
    }
    
    @Override
    public void release() {
        internal_dispose();
        
        super.release();
    }

    public void internal_dispose() {
        if (jniRef != 0) OS.DeleteGlobalRef(jniRef);
        jniRef = 0;
        OS.object_setInstanceVariable(this.id, SWT_OBJECT, 0);
    }
    
    static SWTKeyValueCodingDecorator getThis(long /*int*/ id) {
        if (id == 0) return null;
        
        long /*int*/ [] jniRef = new long /*int*/ [1];
        OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
        if (jniRef[0] == 0) return null;
        
        return (SWTKeyValueCodingDecorator)OS.JNIGetObject(jniRef[0]);
    }

    static long /*int*/ valueForKeyProc(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
        SWTKeyValueCodingDecorator thisKV = getThis(id);
        if (thisKV == null) return 0;
        
        if (sel != OS.sel_valueForKey_) return 0;
        
        String key = new NSString(arg0).getString();
        Object value = thisKV.decoratedObject.valueForKey(key);
        
        if (value instanceof String)
            return NSString.stringWith((String)value).id;
        else if (value instanceof Integer)
            return NSNumber.numberWithInteger(((Integer)value)).id;
        else if (value instanceof Long)
            return NSNumber.numberWithInteger(((Long)value).intValue()).id;
        else if (value instanceof Float)
            return NSNumber.numberWithFloat((Float)value).id;
        else if (value instanceof Double)
            return NSNumber.numberWithDouble((Double)value).id;
        else if (value instanceof BigDecimal)
            return NSDecimalNumber.numberWithDouble(((BigDecimal)value).doubleValue()).id;
        else if (value instanceof Boolean)
            return NSNumber.numberWithBool((Boolean)value).id;
        else if (value instanceof Date)
            return NSDate.dateWithTimeIntervalSince1970(((Date)value).getTime()/1000).id;
        else if (value instanceof KeyValueCodingDecorator)
            return ((KeyValueCodingDecorator)value).id();
        else if (value instanceof KeyValueCoding) {
            return new KeyValueCodingDecorator((KeyValueCoding) value).id();
        }
        else if (value instanceof id)
            return ((id)value).id; 
        
        return 0;
    }
    
    static long /*int*/ setValueForKeyProc(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1) {
        SWTKeyValueCodingDecorator thisKV = getThis(id);
        if (thisKV == null) return 0;
        
        if (sel != OS.sel_setValue_forKey_) return 0;
        
        NSObject value = new NSObject(new id(arg0));
        String key = new NSString(arg1).getString();
        
        Object javaObject = value;
        
        if (value.isKindOfClass(OS.class_NSNull))
            javaObject = null;
        else if (value.isKindOfClass(OS.class_NSString))
            javaObject = new NSString(value).getString();
        else if (value.isKindOfClass(OS.class_NSNumber)) {
            NSNumber number = new NSNumber(value);
            
            //FTL: check if this is correct. Also see it in action.
            int type = (int) OS.CFNumberGetType(number.id);
            switch (type) {
            case OS.kCFNumberSInt8Type:
            case OS.kCFNumberSInt16Type:
            case OS.kCFNumberSInt32Type:
            case OS.kCFNumberShortType:
            case OS.kCFNumberIntType:
            case OS.kCFNumberLongType:
            case OS.kCFNumberNSIntegerType:
                javaObject = number.longValue();
                break;
            case OS.kCFNumberSInt64Type:
            case OS.kCFNumberLongLongType:
                javaObject = number.longLongValue();
                break;
            case OS.kCFNumberFloat32Type:
            case OS.kCFNumberFloatType:
                javaObject = number.floatValue();
                break;
            case OS.kCFNumberFloat64Type:
            case OS.kCFNumberDoubleType:
            case OS.kCFNumberCGFloatType:
                javaObject = number.doubleValue();
                break;
            }
        } else if (value.isKindOfClass(OS.class_NSDate)) {
            NSDate nsDate = new NSDate(value);
            javaObject = new Date((long) (nsDate.timeIntervalSince1970() * 1000));
        }
        
        thisKV.decoratedObject.setValueForKey(javaObject, key);

        return 0;
    }
}
