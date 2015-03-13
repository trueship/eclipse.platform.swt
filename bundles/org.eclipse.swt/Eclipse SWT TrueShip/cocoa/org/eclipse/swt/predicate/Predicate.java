package org.eclipse.swt.predicate;

import java.util.*;

import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.keyvaluecoding.*;

public class Predicate {
    NSPredicate nsPredicate;
    
    public static Predicate predicateWithFormat(String format, List<String> args) {
        NSMutableArray predicateArgs = NSMutableArray.arrayWithCapacity(args.size());
        for (String arg : args)
            predicateArgs.addObject(NSString.stringWith(arg));
        
        NSPredicate nsPredicate = NSPredicate.predicateWithFormat(NSString.stringWith(format), predicateArgs);
        
        return new Predicate(nsPredicate);
    }
    
    public long /*int*/ id() {
        return nsPredicate.id;
    }
    
    private Predicate(NSPredicate predicate) {
        this.nsPredicate = predicate;
    }

    public Predicate(long /*int*/ id) {
        this.nsPredicate = new NSPredicate(id);
    }

    public String predicateFormat() {
        return nsPredicate.predicateFormat().getString();
    }

    public boolean evaluateWithObject(KeyValueCoding object) {
        boolean result = false;
        
        KeyValueCodingDecorator kvObject = new KeyValueCodingDecorator(object);
        try {
            result = nsPredicate.evaluateWithObject(new id(kvObject.id()));
        } finally {
            kvObject.release();
        }
        
        return result ;
    }
    
    public boolean isComparisonPredicate() {
        return nsPredicate.isKindOfClass(OS.class_NSComparisonPredicate);
    }
    
    public ComparisonPredicate getComparisonPredicate() {
        if (!isComparisonPredicate()) {
            throw new IllegalStateException("Not a comparison predicate!");
        }
        return new ComparisonPredicate(id());
    }
    
    public boolean isCompoundPredicate() {
        return nsPredicate.isKindOfClass(OS.class_NSCompoundPredicate);
    }
    
    public CompoundPredicate getCompoundPredicate() {
        if (!isCompoundPredicate()) {
    	    throw new IllegalStateException("Not a compound predicate!");
        }
        return new CompoundPredicate(id());
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        
        if (!(o instanceof Predicate))
            return false;
        
        Predicate predicate = (Predicate)o;
        
        if (this.nsPredicate.id != predicate.nsPredicate.id)
            return false;
        
        return true;
    }
    
    @Override
    public int hashCode() {
        return nsPredicate.hashCode();
    }
}
