package pm.swt.homeAutomation.view;


import java.beans.PropertyChangeEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import pm.swt.homeAutomation.viewModel.TempHumSensorViewModel;
import pm.swt.homeAutomation.viewModel.WeatherStationViewModel;


/**
 * @author pmarinov
 *
 */
public class TempHumSensorView extends BaseTempSensorView
{
    private CLabel humLabel;



    TempHumSensorView(Composite parent, TempHumSensorViewModel viewModel)
    {
        super(parent, viewModel);
        this.registerResizableControl(humLabel);
    }



    @Override
    protected void onPropertyChanged(PropertyChangeEvent evt)
    {
        super.onPropertyChanged(evt);

        switch (evt.getPropertyName())
        {
        case TempHumSensorViewModel.HUMIDITY_PROP_NAME:
            humLabel.setText((String) evt.getNewValue());
            this.onCtrlTextChange(humLabel);

            break;
        default:
            break;
        }
    }



    @Override
    protected void createAdditionalComponents(Composite parent, WeatherStationViewModel viewModel)
    {
        TempHumSensorViewModel currViewModel = (TempHumSensorViewModel) viewModel;

        GridData dataValue = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);

        this.humLabel = new CLabel(parent, SWT.CENTER);
        this.humLabel.setLayoutData(dataValue);
        this.humLabel.setText(currViewModel.getHumidity());
        this.humLabel.setBackground(backgroundColor);
        this.humLabel.setForeground(foregroundColor);
    }
}
