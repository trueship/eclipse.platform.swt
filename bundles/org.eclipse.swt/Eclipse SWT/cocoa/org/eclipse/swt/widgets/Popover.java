package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.internal.cocoa.NSButton;
import org.eclipse.swt.internal.cocoa.NSButtonCell;
import org.eclipse.swt.internal.cocoa.NSGraphicsContext;
import org.eclipse.swt.internal.cocoa.NSPopover;
import org.eclipse.swt.internal.cocoa.NSRect;
import org.eclipse.swt.internal.cocoa.NSSize;
import org.eclipse.swt.internal.cocoa.NSView;
import org.eclipse.swt.internal.cocoa.NSViewController;
import org.eclipse.swt.internal.cocoa.NSWindow;
import org.eclipse.swt.internal.cocoa.id;

/**
 * Popover cocoa widget.
 *
 * @author Igor Karpenko, TrueShip LLC
 */
public class Popover extends Composite {

    protected NSPopover popover;
    protected PopoverBehavior behavior;
    protected PopoverAppearance appearance;
    protected PopoverPrefferedEdge prefferedEdge;
    protected boolean animates;
    protected Composite target;
    protected NSSize size;
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

    public Popover(Shell parent, Composite target, int style) {
        super(parent, style);
        //this.display = target.getDisplay();
      //  createWidget();
        appearance = PopoverAppearance.PopoverAppearanceMinimal;
        behavior = PopoverBehavior.PopoverBehaviorApplicationDefined;
        prefferedEdge = PopoverPrefferedEdge.MinYEdge;
        animates = true;
        this.target = target;
    }

    void createHandle () {
        super.createHandle ();

        popover = (NSPopover) new NSPopover().alloc();
        popover = (NSPopover) popover.init();
        popover.setAppearance(0);
        popover.setBehavior(0);
        popover.setAnimates(true);

        size = new NSSize();

      /*  NSViewController controller = (NSViewController) new NSViewController().alloc();
        controller = (NSViewController)controller.init();
        controller.setView(view);
        popover.setContentViewController(controller);*/
    }

    public void show() {

        NSViewController controller = (NSViewController) new NSViewController().alloc();
        controller = (NSViewController)controller.init();
        controller.setView(view);
        popover.setContentViewController(controller);

        popover.setContentSize(size);

        popover.setAppearance(appearance.ordinal());
        popover.setBehavior(behavior.ordinal());

        popover.showRelativeToRect(target.contentView().frame(), target.getParent().view, prefferedEdge.ordinal());
        layout();
        setVisible(true);
        ((Shell)parent).setWindow(view.window());
        parent.setVisible(true);
    }

    public void setFieldsTabList(Text[] list) {
        this.tabList = list;
        parent.tabList = list;
    }

    @Override
    protected void checkSubclass () {

    }

    @Override
    boolean isTransparent() {
        return true;
    }

    @Override
    void drawBackground (long /*int*/ id, NSGraphicsContext context, NSRect rect) {

    }

    void drawRect (long /*int*/ id, long /*int*/ sel, NSRect rect) {
    //    super.drawRect(id, sel, rect);
    }

    @Override
    void drawWidget (long /*int*/ id, NSGraphicsContext context, NSRect rect) {
      //  super.drawWidget(id, context, rect);
    }

    public void setSize(int width, int height) {
        super.setSize(width, height);
        size.width = width;
        size.height = height;
        popover.setContentSize(size);
    }

    void register() {
        super.register();
        display.addWidget(popover, this);
    }

    public void close() {
        super.dispose();
        popover.close();
    }

    public NSPopover getPopover() {
        return popover;
    }

    public void setPopover(NSPopover popover) {
        this.popover = popover;
    }

    public PopoverBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(PopoverBehavior behavior) {
        this.behavior = behavior;
    }

    public PopoverAppearance getAppearance() {
        return appearance;
    }

    public void setAppearance(PopoverAppearance appearance) {
        this.appearance = appearance;
    }

    public PopoverPrefferedEdge getPrefferedEdge() {
        return prefferedEdge;
    }

    public void setPrefferedEdge(PopoverPrefferedEdge prefferedEdge) {
        this.prefferedEdge = prefferedEdge;
    }

    public boolean isAnimates() {
        return animates;
    }

    public void setAnimates(boolean animates) {
        this.animates = animates;
    }
}
