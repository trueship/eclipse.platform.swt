package org.eclipse.swt.predicateeditor;

import org.eclipse.swt.predicateeditor.cocoa.SWTLengthRowTemplate;

public class LengthRowTemplate {
    SWTLengthRowTemplate swtTemplate;

    public LengthRowTemplate(SWTLengthRowTemplate template) {
        swtTemplate = template;
    }

    public void refreshLayout() {
        swtTemplate.refreshLayout();
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        
        if (!(other instanceof LengthRowTemplate))
            return false;
        
        LengthRowTemplate template = (LengthRowTemplate)other;
        
        if (this.swtTemplate.id != template.swtTemplate.id)
            return false;
        
        return true;
    }
    
    @Override
    public int hashCode() {
        return swtTemplate.hashCode();
    }
    
    public long /*int*/ id() {
        return swtTemplate.id; 
    }

}
