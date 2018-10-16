package pm.swt.homeAutomation.view;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import pm.swt.homeAutomation.viewModel.TempHumSensorViewModel;


public class TempHumSensorView extends Composite
{
    private TempHumSensorViewModel viewModel;

    private CLabel header;
    private CLabel tempLabel;
    private CLabel humLabel;

    private PropertyChangeListener listener = new PropertyChangeListener()
    {

        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            switch (evt.getPropertyName())
            {
            case TempHumSensorViewModel.TEMPERATURE_PROP_NAME:
                tempLabel.setText((String) evt.getNewValue());
                setLabelOptimalFontSize(tempLabel);
                break;
            case TempHumSensorViewModel.HUMIDITY_PROP_NAME:
                humLabel.setText((String) evt.getNewValue());
                setLabelOptimalFontSize(humLabel);
                break;
            default:
                break;
            }
        }
    };



    TempHumSensorView(Composite parent, TempHumSensorViewModel viewModel)
    {
        super(parent, SWT.None);

        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this.listener);
        this.createComposite(this);

        parent.addListener(SWT.Resize, new Listener()
        {

            @Override
            public void handleEvent(Event event)
            {
                changeFont();
            }
        });
    }



    private void createComposite(Composite parent)
    {
        Display display = parent.getDisplay();
        Color foregroundColor = display.getSystemColor(SWT.COLOR_WHITE);
        Color backgroundCOlor = display.getSystemColor(SWT.COLOR_BLACK);

        GridLayout gridLayout = new GridLayout(1, true);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;


        parent.setLayout(gridLayout);

        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);

        header = new CLabel(parent, SWT.CENTER);
        header.setLayoutData(data);
        header.setText(this.viewModel.getHomeSector());
        header.setBackground(backgroundCOlor);
        header.setForeground(foregroundColor);

        tempLabel = new CLabel(parent, SWT.CENTER);
        tempLabel.setLayoutData(data);
        tempLabel.setText(this.viewModel.getTemperature());
        tempLabel.setBackground(backgroundCOlor);
        tempLabel.setForeground(foregroundColor);

        humLabel = new CLabel(parent, SWT.CENTER);
        humLabel.setLayoutData(data);
        humLabel.setText(this.viewModel.getHumidity());
        humLabel.setBackground(backgroundCOlor);
        humLabel.setForeground(foregroundColor);

        parent.pack();
    }



    public void changeFont()
    {
        setLabelOptimalFontSize(header);
        setLabelOptimalFontSize(tempLabel);
        setLabelOptimalFontSize(humLabel);
    }



    @Override
    public void dispose()
    {
        this.header.getFont().dispose();
        this.tempLabel.getFont().dispose();
        this.humLabel.getFont().dispose();

        this.viewModel.removePropertyChangeListener(this.listener);
        this.viewModel.dispose();
        super.dispose();
    }



    private void setLabelOptimalFontSize(CLabel control)
    {
        Point size = control.getSize();

        FontData[] fontData = control.getFont().getFontData();

        int textLen = control.getText().length();
        int charWidth = size.x / textLen;

        fontData[0].setHeight(charWidth);

        Font font = new Font(Display.getCurrent(), fontData[0]);
        control.setFont(font);

        control.addDisposeListener(new DisposeListener()
        {

            @Override
            public void widgetDisposed(DisposeEvent e)
            {
                font.dispose();
            }
        });
    }
}
