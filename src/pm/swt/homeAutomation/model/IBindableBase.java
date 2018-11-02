package pm.swt.homeAutomation.model;

import java.beans.PropertyChangeListener;


public interface IBindableBase
{
    public void addPropertyChangeListener(PropertyChangeListener listener);



    public void removePropertyChangeListener(PropertyChangeListener listener);
}
