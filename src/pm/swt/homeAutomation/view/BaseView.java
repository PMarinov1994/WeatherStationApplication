package pm.swt.homeAutomation.view;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;


public abstract class BaseView extends Composite
{
    private Map<CLabel, Font> resizableControls = new HashMap<>();



    public BaseView(Composite parent, int style)
    {
        super(parent, style);
        
        this.addDisposeListener(new DisposeListener()
        {

            @Override
            public void widgetDisposed(DisposeEvent e)
            {
                dispose();
            }
        });
    }



    public void onResize()
    {
        for (CLabel ctrl : this.resizableControls.keySet())
            this.setNewFont(ctrl);
    }



    @Override
    public void dispose()
    {
        for (Font font : this.resizableControls.values())
            font.dispose();

        super.dispose();
    }



    protected void onCtrlTextChange(CLabel label)
    {
        if (this.resizableControls.containsKey(label))
            this.setNewFont(label);
    }



    protected void registerResizableControl(CLabel control)
    {
        if (!this.resizableControls.containsKey(control))
            this.resizableControls.put(control, null);
    }



    protected abstract void createComposite(Composite parent);



    private void setNewFont(CLabel label)
    {
        Font oldFont = this.resizableControls.get(label);

        Point size = label.getSize();

        FontData[] fontData = label.getFont().getFontData();

        int textLen = label.getText().length();
        int charWidth = size.x / textLen;

        fontData[0].setHeight(charWidth);

        Font newFont = new Font(Display.getCurrent(), fontData[0]);
        label.setFont(newFont);
        this.resizableControls.put(label, newFont);

        if (oldFont != null)
            oldFont.dispose();
    }
}
