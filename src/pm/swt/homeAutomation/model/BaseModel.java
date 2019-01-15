package pm.swt.homeAutomation.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public abstract class BaseModel implements IBindableBase
{
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);



    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        this.changeSupport.addPropertyChangeListener(listener);
    }



    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        this.changeSupport.addPropertyChangeListener(propertyName, listener);
    }



    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        this.changeSupport.removePropertyChangeListener(listener);
    }



    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        this.changeSupport.removePropertyChangeListener(propertyName, listener);
    }



    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        this.changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
}
