package org.eclipse.swt.predicateeditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.predicate.Predicate;
import org.eclipse.swt.predicateeditor.cocoa.*;

public class DynamicRightValuesRowTemplate {
    SWTDynamicRightValuesRowTemplate swtTemplate;
    private Predicate predicate;

    public DynamicRightValuesRowTemplate(SWTDynamicRightValuesRowTemplate template) {
        swtTemplate = template;
        predicate = template.getPredicate();
    }

    public void setCriterion(String criterion) {
        swtTemplate.setCriterion(criterion);
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        
        if (!(other instanceof DynamicRightValuesRowTemplate))
            return false;
        
        DynamicRightValuesRowTemplate template = (DynamicRightValuesRowTemplate)other;
        
        if (this.swtTemplate.id != template.swtTemplate.id)
            return false;
        
        return true;
    }
    
    @Override
    public int hashCode() {
        return swtTemplate.hashCode();
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public void refreshLayout() {
        swtTemplate.refreshLayout();
    }

    public void refreshUI() {
        if (!swtTemplate.isReleased())
            swtTemplate.refreshUI();
    }

    public String getKeyPath() {
        return swtTemplate.getKeyPath();
    }

    public int preferredWidth(GC gc, int currentWidth) {
        if (swtTemplate.isReleased())
            return 0;
        
        List<String> tokens = swtTemplate.getDisplayedTokens();
        
        if (tokens.size() == 0)
            return currentWidth;
        
        StringBuilder sb = new StringBuilder();
        for (String token : tokens) {
            sb.append(token);
            sb.append("          "); // account for aprox. 10 spaces around a token.
        }
        
        int neededTokenFieldWidth = gc.textExtent(sb.toString()).x;
        int currentTokenFieldWidth = swtTemplate.getCurrentTokenFieldWidth();
        
        int diffWidth = neededTokenFieldWidth - currentTokenFieldWidth;
        
        if (diffWidth < 0)
            return currentWidth;
        
        return currentWidth + diffWidth;
    }
}
