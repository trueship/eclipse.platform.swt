package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.internal.C;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.cocoa.NSButton;
import org.eclipse.swt.internal.cocoa.NSButtonCell;
import org.eclipse.swt.internal.cocoa.NSGraphicsContext;
import org.eclipse.swt.internal.cocoa.NSNotification;
import org.eclipse.swt.internal.cocoa.NSPopover;
import org.eclipse.swt.internal.cocoa.NSRect;
import org.eclipse.swt.internal.cocoa.NSSize;
import org.eclipse.swt.internal.cocoa.NSText;
import org.eclipse.swt.internal.cocoa.NSView;
import org.eclipse.swt.internal.cocoa.NSViewController;
import org.eclipse.swt.internal.cocoa.NSWindow;
import org.eclipse.swt.internal.cocoa.OS;
import org.eclipse.swt.internal.cocoa.SWTPopoverDelegate;
import org.eclipse.swt.internal.cocoa.id;
import org.eclipse.swt.widgets.Text;

/**
 * Cocoa popover widget.
 *
 * @author Igor Karpenko, TrueShip LLC
 */
public class Popover extends Composite {
    static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};
    static long /*int*/ delegateClass;
    static Callback Callback3;
    static Callback Callback4;
    protected NSPopover popover;
    protected PopoverBehavior behavior;
    protected PopoverAppearance appearance;
    protected PopoverPrefferedEdge prefferedEdge;
    protected boolean animates;
    protected Composite target;
    protected NSSize size;
    protected int width;
    protected int height;
    protected Text firstResponder;

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

    @Override
    void createHandle () {
        super.createHandle ();


        popover = (NSPopover) new NSPopover().alloc();
        popover = (NSPopover) popover.init();
        popover.setAppearance(0);
        popover.setBehavior(0);
        popover.setAnimates(true);

        size = new NSSize();

        if (delegateClass == 0) {
            Class tokenClass = this.getClass();
            Callback3 = new Callback(tokenClass, "delegateMethod3", 3); //$NON-NLS-1$
            long /*int*/ proc3 = Callback3.getAddress();

            String className = "SWTPopoverDelegate"; //$NON-NLS-1$
            byte[] types = {'*','\0'};
            int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;
            delegateClass = OS.objc_allocateClassPair (OS.class_NSObject, className, 0);

            OS.class_addIvar(delegateClass, SWT_OBJECT, size, (byte)align, types);
            OS.class_addMethod(delegateClass, OS.sel_popoverDidShow_, proc3, "@:@");
            OS.objc_registerClassPair(delegateClass);
        }

        SWTPopoverDelegate delegate = (SWTPopoverDelegate)new SWTPopoverDelegate().alloc().init();
        popover.setDelegate(delegate);
        this.getDisplay().addWidget(delegate, this);

      /*  NSViewController controller = (NSViewController) new NSViewController().alloc();
        controller = (NSViewController)controller.init();
        controller.setView(view);
        popover.setContentViewController(controller);*/
    }

    static long /*int*/ delegateMethod3 (long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
        if (sel == OS.sel_popoverDidShow_) {
            Widget widget = Display.LookupWidget(id, sel);
            if (widget == null || !(widget instanceof Popover)) {
                return 0;
            }

            Popover popover = (Popover) widget;
            if (popover.getFirstResponder() != null) {
                popover.getFirstResponder().forceFocus();
            }

        }

        return 0;
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

    public void showRelativeToRect(int x, int y, int width, int height) {

        NSViewController controller = (NSViewController) new NSViewController().alloc();
        controller = (NSViewController)controller.init();
        controller.setView(view);
        popover.setContentViewController(controller);

        popover.setContentSize(size);

        popover.setAppearance(appearance.ordinal());
        popover.setBehavior(behavior.ordinal());

        NSRect rect = new NSRect();
        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;

        popover.showRelativeToRect(rect, target.view, prefferedEdge.ordinal());
        layout();
        setVisible(true);
        ((Shell)parent).setWindow(view.window());
        parent.setVisible(true);
    }

    public void setFieldsTabList (Text[] list) {
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

    @Override
    void drawRect (long /*int*/ id, long /*int*/ sel, NSRect rect) {
    //    super.drawRect(id, sel, rect);
    }

    @Override
    void drawWidget (long /*int*/ id, NSGraphicsContext context, NSRect rect) {
      //  super.drawWidget(id, context, rect);
    }

    @Override
    public void setSize (int width, int height) {
        super.setSize(width, height);
        size.width = width;
        size.height = height;
        popover.setContentSize(size);
    }

    @Override
    void register () {
        super.register();
        display.addWidget(popover, this);
    }

    public void close () {
        super.dispose();
        popover.close();
    }

    public NSPopover getPopover () {
        return popover;
    }

    public void setPopover (NSPopover popover) {
        this.popover = popover;
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
        return this.firstResponder;
    }

    public void setFirstResponder (Text field) {
        this.firstResponder = field;
    }
}
