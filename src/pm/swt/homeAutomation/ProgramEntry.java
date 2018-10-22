package pm.swt.homeAutomation;

import pm.swt.homeAutomation.model.StatusBar;
import pm.swt.homeAutomation.model.TempHumSensor;
import pm.swt.homeAutomation.mqtt.MqttWorker;
import pm.swt.homeAutomation.utils.DependencyIndector;
import pm.swt.homeAutomation.utils.GlobalResources;
import pm.swt.homeAutomation.utils.SwtClassLoader;
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
        StatusBar statusBarModel = new StatusBar();

        di.registerInstance(GlobalResources.LIVING_ROOM_INSTANCE_MODEL_NAME, livingRoomSensorModel);
        di.registerInstance(GlobalResources.BED_ROOM_INSTANCE_MODEL_NAME, bedRoomSensorModel);
        di.registerInstance(GlobalResources.STATUS_BAR_INSTANCE_MODEL_NAME, statusBarModel);

        MqttWorker worker = new MqttWorker();
        worker.doWork();

        HomeAutomationWindow mainView = new HomeAutomationWindow();
        mainView.show();

        worker.dispose();
    }

}
