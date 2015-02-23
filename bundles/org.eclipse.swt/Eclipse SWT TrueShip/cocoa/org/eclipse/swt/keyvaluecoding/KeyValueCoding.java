package org.eclipse.swt.keyvaluecoding;

public interface KeyValueCoding {
    Object valueForKey(String key);
    void setValueForKey(Object value, String key);
}
