package org.eclipse.swt.predicateeditor;

import org.eclipse.swt.predicateeditor.cocoa.*;

public class DynamicRightValuesRowTemplate {
    SWTDynamicRightValuesRowTemplate swtTemplate;

    public DynamicRightValuesRowTemplate(SWTDynamicRightValuesRowTemplate template) {
        swtTemplate = template;
    }

    public void setCriterion(String shipVia) {
        swtTemplate.setCriterion(shipVia);
    }
}
