package pm.swt.homeAutomation.viewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import pm.swt.homeAutomation.model.BaseModel;
import pm.swt.homeAutomation.model.StatusBar;


public class StatusBarViewModel extends BaseModel
{
    private StatusBar model;

    private PropertyChangeListener listener = new PropertyChangeListener()
    {

        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {

        }
    };



    public StatusBarViewModel(StatusBar model)
    {
        this.model = model;
        this.model.addPropertyChangeListener(this.listener);
    }
}
