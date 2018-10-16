package pm.swt.homeAutomation.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public abstract class BaseModel
{
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);



    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        changeSupport.addPropertyChangeListener(listener);
    }



    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        changeSupport.removePropertyChangeListener(listener);
    }



    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
}
