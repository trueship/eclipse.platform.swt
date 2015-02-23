package org.eclipse.swt.keyvaluecoding;

import org.eclipse.swt.keyvaluecoding.cocoa.*;

public class KeyValueCodingDecorator {
    SWTKeyValueCodingDecorator swtKeyValueCodingDecorator;

    public KeyValueCodingDecorator(KeyValueCoding object) {
        swtKeyValueCodingDecorator = new SWTKeyValueCodingDecorator(object);
    }
    
    public long /*int*/ id() {
        return swtKeyValueCodingDecorator.id; 
    }

    public void release() {
        if (swtKeyValueCodingDecorator != null) {
            swtKeyValueCodingDecorator.release();
            swtKeyValueCodingDecorator = null;
        }
    }
}
