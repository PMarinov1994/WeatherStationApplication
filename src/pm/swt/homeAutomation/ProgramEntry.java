package pm.swt.homeAutomation;

import javax.naming.OperationNotSupportedException;

import pm.swt.homeAutomation.configurator.ConfigurationFileManager;
import pm.swt.homeAutomation.model.StatusBar;
import pm.swt.homeAutomation.model.TempHumSensor;
import pm.swt.homeAutomation.model.TempPressureSensor;
import pm.swt.homeAutomation.mqtt.MqttWorker;
import pm.swt.homeAutomation.system.SwtClassLoader;
import pm.swt.homeAutomation.utils.DependencyIndector;
import pm.swt.homeAutomation.utils.GlobalResources;
import pm.swt.homeAutomation.view.HomeAutomationWindow;


public class ProgramEntry
{

    public static void main(String[] args)
    {
        SwtClassLoader swtClassLoader = new SwtClassLoader();
        swtClassLoader.addJarToClasspath();

        DependencyIndector di = DependencyIndector.getInstance();

        TempHumSensor livingRoomSensorModel = new TempHumSensor();
        TempHumSensor bedRoomSensorModel = new TempHumSensor();
        TempPressureSensor outsideSensorModel = new TempPressureSensor();
        StatusBar statusBarModel = new StatusBar();

        di.registerInstance(GlobalResources.LIVING_ROOM_INSTANCE_MODEL_NAME, livingRoomSensorModel);
        di.registerInstance(GlobalResources.BED_ROOM_INSTANCE_MODEL_NAME, bedRoomSensorModel);
        di.registerInstance(GlobalResources.STATUS_BAR_INSTANCE_MODEL_NAME, statusBarModel);
        di.registerInstance(GlobalResources.OUTSIDE_INSTANCE_MODEL_NAME, outsideSensorModel);

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

        MqttWorker mqttWorker = new MqttWorker();
        if (!mqttWorker.connectToBrocker())
            mqttWorker.reconnectToBrocker();

        HomeAutomationWindow mainView = new HomeAutomationWindow();
        mainView.show();

        configManager.dispose();
        mqttWorker.dispose();
    }

}
