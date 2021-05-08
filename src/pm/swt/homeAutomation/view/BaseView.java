package pm.swt.homeAutomation.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;


public abstract class BaseView extends Composite
{
    private Map<CLabel, Font> resizableCLabels = new HashMap<>();



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
        for (CLabel ctrl : this.resizableCLabels.keySet())
            this.setNewFont(ctrl);
    }



    @Override
    public void dispose()
    {
        for (Font font : this.resizableCLabels.values())
        {
            if (font != null)
                font.dispose();
        }

        super.dispose();
    }



    protected void onCtrlTextChange(CLabel label)
    {
        if (this.resizableCLabels.containsKey(label))
            this.setNewFont(label);
    }



    protected void registerResizableControl(CLabel control)
    {
        if (!this.resizableCLabels.containsKey(control))
            this.resizableCLabels.put(control, null);
    }



    /**
     * This method clears any old images that the <code>label</code> may have
     * and created a new image with the appropriate size to fill <code>label</code>.
     * The <code>image</code> will be used only as reference to create the new image
     * with the correct size.
     *
     * @param label The label control to switch the new image will be set.
     * @param image A reference image from which the new image will be created.
     * This image WILL NOT BE DISPOSED!
     */
    protected void changeLabelImage(Label label, Image image)
    {
        if (image.isDisposed())
        {
            System.err.println("Error Image was disposed");
            return;
        }

        Rectangle labelSize = label.getBounds();
        if (labelSize.width == 0 || labelSize.height == 0)
            return;

        int size = Math.min(labelSize.width, labelSize.height);

        Image oldImg = label.getImage();
        Image newImg = new Image(this.getDisplay(),
                image.getImageData().scaledTo(size, size));

        label.setImage(newImg);

        if (oldImg != null)
            oldImg.dispose();
    }


    /**
     * Creates an <code>Image</code> object from a given absolute or relative path.
     * Note: The returned object must be disposed when not needed anymore!
     *
     * @param path The Path to the image.
     * @return The created image object.
     */
    protected Image getImageFromPath(String path)
    {
        InputStream is = null;
        try
        {
            File imgFile = new File(path);
            is = new FileInputStream(imgFile);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        ImageData imgData = new ImageData(is);
        Image img = new Image(this.getDisplay(), imgData);

        return img;
    }



    protected abstract void createComposite(Composite parent);



    private void setNewFont(CLabel label)
    {
        // Point size = label.getSize();
        Rectangle size = label.getClientArea();
        FontData[] fontData = label.getFont().getFontData();

        int textLen = label.getText().length();
        if (textLen == 0)
            return;

        GC gc = new GC(label);
        int stringWidth = gc.stringExtent(label.getText()).x;

        double widthRatio = (double) size.width / (double) stringWidth;
        
        // Sometimes for unknow reason, fontData's height is 0. Setting it to 1 as initial value seems to work.
        int fontHeight = fontData[0].getHeight() == 0 ? size.height : fontData[0].getHeight();
        double newFontSize = fontHeight * widthRatio;

        /*
        System.out.println(String.format("TEXT: %s --- SIZE %d --- GC SIZE %d --- RATIO %f --- FONT HEIGHT %d --- NEW FONT SIZE %f",
            label.getText(), 
            size.width, 
            stringWidth,
            widthRatio,
            fontHeight,
            newFontSize));
        */

        fontData[0].setHeight((int)newFontSize);

        Font newFont = new Font(Display.getCurrent(), fontData[0]);
        label.setFont(newFont);

        // Clean up the old font, if we have created one.
        Font oldFont = this.resizableCLabels.get(label);
        if (oldFont != null)
            oldFont.dispose();

        this.resizableCLabels.put(label, newFont);

        gc.dispose();
    }
}
