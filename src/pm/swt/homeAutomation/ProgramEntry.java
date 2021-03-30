package pm.swt.homeAutomation;

import java.io.IOException;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pm.swt.homeAutomation.configurator.ConfigurationFileManager;
import pm.swt.homeAutomation.configurator.UILayoutManager;
import pm.swt.homeAutomation.model.StatusBar;
import pm.swt.homeAutomation.mqtt.MqttWorker;
import pm.swt.homeAutomation.system.SwtClassLoader;
import pm.swt.homeAutomation.utils.DependencyIndector;
import pm.swt.homeAutomation.utils.GlobalResources;
import pm.swt.homeAutomation.view.HomeAutomationWindow;


public class ProgramEntry
{

    public static void main(String[] args)
    {
        Thread.currentThread().setName("Main Thread. (UI Thread)");
        
        SwtClassLoader swtClassLoader = new SwtClassLoader();
        swtClassLoader.addJarToClasspath();

        DependencyIndector di = DependencyIndector.getInstance();

        StatusBar statusBarModel = new StatusBar();
        di.registerInstance(GlobalResources.STATUS_BAR_INSTANCE_MODEL_NAME, statusBarModel);

        ConfigurationFileManager configManager = null;
        try
        {
            configManager = new ConfigurationFileManager();
            di.registerInstance(GlobalResources.CONFIGURATION_FILE_MANAGER_NAME, configManager);
        }
        catch (OperationNotSupportedException e)
        {
            e.printStackTrace();
            return;
        }

        UILayoutManager layoutManager = new UILayoutManager();
        try
        {
            layoutManager.initialize();
            di.registerInstance(GlobalResources.UI_LAYOUT_MANAGER_NAME, layoutManager);
        }
        catch (ParserConfigurationException | SAXException | IOException e)
        {
            e.printStackTrace();
            return;
        }

        System.out.println("Creating UI...");
        HomeAutomationWindow mainView = new HomeAutomationWindow();        
        System.out.println("UI Created succesfully!");
        
        // After we create the UI start the MQTT, so that we receive
        // all the data. The PropertyChanged listener will not send the same
        // data twice, so we can miss some initial values.
        MqttWorker mqttWorker = new MqttWorker();
        if (!mqttWorker.connectToBrocker())
            mqttWorker.reconnectToBrocker();

        // Show the UI so that main thread will loop until exit.
        mainView.show();
        
        configManager.dispose();
        mqttWorker.dispose();
    }

}
