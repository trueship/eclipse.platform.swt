package org.eclipse.swt.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.internal.cocoa.NSString;
import org.eclipse.swt.internal.cocoa.NSTextField;
import org.eclipse.swt.internal.cocoa.OS;
import org.eclipse.swt.widgets.PredicateEditor.AttributeType;

public class NumericTextFieldValidator {
    private static Pattern decimalNumberPattern = Pattern.compile("^(\\d+(\\.\\d*)?)?$");
    private static Pattern integerNumberPattern = Pattern.compile("^(\\d+)?$");
    
    private NSTextField textField;
    private Pattern pattern;
    private String lastValue = "";

    public NumericTextFieldValidator(NSTextField textField, long attribute) {
        this(textField, numberPatternForAttributeType(attribute));
    }
    
    public NumericTextFieldValidator(NSTextField textField, Pattern numericPattern) {
        this.textField = textField;
        this.pattern = numericPattern;
    }

    public boolean validate() {
        if (textField == null || pattern == null)
            return true;
        
        String currentValue = textField.stringValue().getString();
          
        Matcher matcher = pattern.matcher(currentValue);
            
        if (matcher.matches()) {
            lastValue = currentValue;
            return true;
        }
        else {
            OS.NSBeep();
            textField.setStringValue(NSString.stringWith(lastValue));
            return false;
        }   
    }
    
    public static Pattern decimalNumberPattern() {
      return decimalNumberPattern;
    }
    
    public static Pattern integerNumberPattern() {
        return integerNumberPattern;
    }

    private static Pattern numberPatternForAttributeType(long attribute) {
        if (isIntegerType(attribute))
            return integerNumberPattern();
        else if (isDecimalType(attribute))
            return decimalNumberPattern();
        
        return null;
    }
    
    private static boolean isIntegerType(long attribute) {
        return attribute == AttributeType.NSInteger16AttributeType.value() ||
               attribute == AttributeType.NSInteger32AttributeType.value() ||
               attribute == AttributeType.NSInteger64AttributeType.value();
    }
    
    private static boolean isDecimalType(long attribute) {
        return attribute == AttributeType.NSDecimalAttributeType.value() ||
               attribute == AttributeType.NSDoubleAttributeType.value() ||
               attribute == AttributeType.NSFloatAttributeType.value();
    }
}
