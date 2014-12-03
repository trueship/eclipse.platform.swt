package org.eclipse.swt.widgets;

import org.eclipse.swt.widgets.Text;

public class Popover extends Composite {

    protected PopoverBehavior behavior;
    protected PopoverAppearance appearance;
    protected PopoverPrefferedEdge prefferedEdge;
    protected boolean animates;
    protected Composite target;
    protected int width;
    protected int height;

    public enum PopoverBehavior {
        PopoverBehaviorApplicationDefined,
        PopoverBehaviorTransient,
        PopoverBehaviorSemitransient
    }

    public enum PopoverAppearance {
        PopoverAppearanceMinimal,
        PopoverAppearanceHUD
    }

    public enum PopoverPrefferedEdge {
        MinXEdge, // Specifies the left edge of the input rectangle.
        MinYEdge, // Specifies the bottom edge of the input rectangle.
        MaxXEdge, // Specifies the right edge of the input rectangle.
        MaxYEdge  // Specifies the top edge of the input rectangle.
    }

    public Popover (Shell parent, Composite target, int style) {
        super(parent, style);
    }

    public void show () {

    }

    public void close () {

    }

    public Composite getParent () {
        return this.parent;
    }

    public void setSize (int w, int h) {

    }

    public void setFieldsTabList (Text[] list) {

    }

    public PopoverBehavior getBehavior () {
        return behavior;
    }

    public void setBehavior (PopoverBehavior behavior) {
        this.behavior = behavior;
    }

    public PopoverAppearance getAppearance () {
        return appearance;
    }

    public void setAppearance (PopoverAppearance appearance) {
        this.appearance = appearance;
    }

    public PopoverPrefferedEdge getPrefferedEdge () {
        return prefferedEdge;
    }

    public void setPrefferedEdge (PopoverPrefferedEdge prefferedEdge) {
        this.prefferedEdge = prefferedEdge;
    }

    public boolean isAnimates () {
        return animates;
    }

    public void setAnimates (boolean animates) {
        this.animates = animates;
    }
    
    public Text getFirstResponder () {
        return null;
    }

    public void setFirstResponder (Text field) {
    }
}
