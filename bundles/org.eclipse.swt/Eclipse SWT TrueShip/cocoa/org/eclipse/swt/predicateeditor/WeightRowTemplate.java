package org.eclipse.swt.predicateeditor;

import org.eclipse.swt.predicateeditor.cocoa.SWTWeightRowTemplate;

public class WeightRowTemplate {
    SWTWeightRowTemplate swtTemplate;

    public WeightRowTemplate(SWTWeightRowTemplate template) {
        swtTemplate = template;
    }
    
    public void refreshLayout() {
        swtTemplate.refreshLayout();
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        
        if (!(other instanceof WeightRowTemplate))
            return false;
        
        WeightRowTemplate template = (WeightRowTemplate)other;
        
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
