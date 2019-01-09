package pm.swt.homeAutomation.configurator;

import pm.swt.homeAutomation.model.ConfigurationModel;


public interface IConfigurationChanged
{
    public void configurationChanged(ConfigurationModel newConfig);
}
