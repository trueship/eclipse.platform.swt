package org.eclipse.swt.predicateeditor;

import org.eclipse.swt.predicateeditor.cocoa.*;

public class MoneyRowTemplate {
    SWTMoneyRowTemplate swtTemplate;

    public MoneyRowTemplate(SWTMoneyRowTemplate template) {
        swtTemplate = template;
    }

    public void refreshLayout() {
        if (!swtTemplate.isReleased())
            swtTemplate.refreshLayout();
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        
        if (!(other instanceof MoneyRowTemplate))
            return false;
        
        MoneyRowTemplate template = (MoneyRowTemplate)other;
        
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
