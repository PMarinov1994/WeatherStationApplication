package pm.swt.homeAutomation.view;

import java.beans.PropertyChangeEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import pm.swt.homeAutomation.viewModel.TempPressureSensorViewModel;
import pm.swt.homeAutomation.viewModel.WeatherStationViewModel;


public class TempPressureSensorView extends BaseTempSensorView
{
    private CLabel pressureLabel;
    private CLabel humidityLabel;



    public TempPressureSensorView(Composite parent, TempPressureSensorViewModel viewModel)
    {
        super(parent, viewModel);
        this.registerResizableControl(humidityLabel);
        this.registerResizableControl(pressureLabel);
    }



    @Override
    protected void onPropertyChanged(PropertyChangeEvent evt)
    {
        super.onPropertyChanged(evt);

        switch (evt.getPropertyName())
        {
        case TempPressureSensorViewModel.PRESSURE_PROP_NAME:
            pressureLabel.setText((String) evt.getNewValue());
            this.onCtrlTextChange(pressureLabel);

            break;
            
        case TempPressureSensorViewModel.HUMIDITY_PROP_NAME:
            humidityLabel.setText((String) evt.getNewValue());
            this.onCtrlTextChange(humidityLabel);
            
            break;
        default:
            break;
        }
    }



    @Override
    protected void createAdditionalComponents(Composite parent, WeatherStationViewModel viewModel)
    {
        TempPressureSensorViewModel currViewModel = (TempPressureSensorViewModel) viewModel;

        GridData dataValue = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);

        this.humidityLabel = new CLabel(parent, SWT.CENTER);
        this.humidityLabel.setLayoutData(dataValue);
        this.humidityLabel.setText(currViewModel.getHumidity());
        this.humidityLabel.setBackground(backgroundColor);
        this.humidityLabel.setForeground(foregroundColor);
        
        this.pressureLabel = new CLabel(parent, SWT.CENTER);
        this.pressureLabel.setLayoutData(dataValue);
        this.pressureLabel.setText(currViewModel.getPressure());
        this.pressureLabel.setBackground(backgroundColor);
        this.pressureLabel.setForeground(foregroundColor);
    }
}
