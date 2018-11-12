package pm.swt.homeAutomation.view;

import java.beans.PropertyChangeEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import pm.swt.homeAutomation.viewModel.TempPressureSensorViewModel;
import pm.swt.homeAutomation.viewModel.WeatherStationViewModel;


public class TempPressureSensorView extends WeatherStationView
{
    private CLabel pressureLabel;



    public TempPressureSensorView(Composite parent, TempPressureSensorViewModel viewModel)
    {
        super(parent, viewModel);
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
        default:
            break;
        }
    }



    @Override
    protected void createAdditionalComponents(Composite parent, WeatherStationViewModel viewModel)
    {
        TempPressureSensorViewModel currViewModel = (TempPressureSensorViewModel) viewModel;

        GridData dataValue = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);

        this.pressureLabel = new CLabel(parent, SWT.CENTER);
        this.pressureLabel.setLayoutData(dataValue);
        this.pressureLabel.setText(currViewModel.getPressure());
        this.pressureLabel.setBackground(backgroundColor);
        this.pressureLabel.setForeground(foregroundColor);
    }
}
