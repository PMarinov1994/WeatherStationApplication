package pm.swt.homeAutomation.mqtt;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import pm.swt.homeAutomation.configurator.ConfigurationFileManager;
import pm.swt.homeAutomation.configurator.IConfigurationChanged;
import pm.swt.homeAutomation.model.BatteryLevel;
import pm.swt.homeAutomation.model.ConfigurationModel;
import pm.swt.homeAutomation.model.TempHumSensor;
import pm.swt.homeAutomation.model.TempPressureSensor;
import pm.swt.homeAutomation.utils.DependencyIndector;
import pm.swt.homeAutomation.utils.GlobalResources;


public class MqttWorker
{
    private static final char SECTOR_SEPARATOR = '/';

    private static final String BED_ROOM_TOPIC = "bedroom";
    private static final String LIVING_ROOM_TOPIC = "livingRoom";
    private static final String OUTSIDE_TOPIC = "outside";

    private static final String[] MQTT_TOPICS = { "bedroom/#", "livingRoom/#", "outside/#" };
    private static final String MQTT_QOS_SUB_FOLDER = "mqttQOS";

    private final String MQTT_SERVER_ADDRESS;

    private MqttClient client;
    private MqttWorkerCallback callBack;

    private final String mqttQosFolder;

    private volatile boolean isRunning = false;

    private volatile double fullBatteryV;
    private volatile double mediumBatteryV;
    private volatile int mqttReconnectDelaySeconds;
    private volatile boolean recalculateReconnectDelay = false;



    public MqttWorker()
    {
        String jarExePath = GlobalResources.getExecJarPath();

        this.mqttQosFolder = String.format("%s%s%s", jarExePath, File.separator, MQTT_QOS_SUB_FOLDER);
        System.out.println("MqttQosFolder: " + mqttQosFolder);

        File dirQos = new File(this.mqttQosFolder);
        if (!dirQos.exists())
        {
            System.out.println("Creating folder for mqttQos...");
            boolean result = dirQos.mkdirs();
            System.out.println("Result: " + result);
        }

        ConfigurationFileManager configManager = (ConfigurationFileManager) DependencyIndector.getInstance()
                .resolveInstance(GlobalResources.CONFIGURATION_FILE_MANAGER_NAME);
        ConfigurationModel config = configManager.getConfig();

        this.MQTT_SERVER_ADDRESS = String.format("tcp://%s:%s", config.getMqttAddress(), config.getMqttPort());

        this.fullBatteryV = config.getFullBatteryLevel();
        this.mediumBatteryV = config.getMediumBatteryLevel();
        this.mqttReconnectDelaySeconds = config.getMqttReconnectIntervalSeconds();

        configManager.addConfigurationChangeSubscriber(new IConfigurationChanged()
        {

            @Override
            public void configurationChanged(ConfigurationModel newConfig)
            {
                fullBatteryV = newConfig.getFullBatteryLevel();
                mediumBatteryV = newConfig.getMediumBatteryLevel();
                mqttReconnectDelaySeconds = newConfig.getMqttReconnectIntervalSeconds();
                recalculateReconnectDelay = true;

            }
        });
    }



    public boolean connectToBrocker()
    {
        this.isRunning = true;

        try
        {
            this.client = new MqttClient(
                    MQTT_SERVER_ADDRESS,
                    UUID.randomUUID().toString(),
                    new MqttDefaultFilePersistence(this.mqttQosFolder));

            this.callBack = new MqttWorkerCallback();

            System.out.println("Connection to MQTT Server...");
            client.connect();

            System.out.println("Connected!");
            client.setCallback(this.callBack);
            client.subscribe(MQTT_TOPICS);
        }
        catch (MqttException e)
        {
            System.err.println("Connection to Brocker failed.");
            try
            {
                this.client.close(true);
            }
            catch (MqttException e1)
            {
                System.err.println("Closing blocker failed.");
            }

            return false;
        }

        return true;
    }



    public void dispose()
    {
        this.isRunning = false;

        try
        {
            if (this.client.isConnected())
            {
                System.out.println("Disconnectiong client...");
                this.client.disconnectForcibly();

                System.out.println("Closing client...");
            }
            
            this.client.close(true);
        }
        catch (MqttException e)
        {
            System.err.println("Closing MQTT Blocker failed.");
        }
    }



    public String getMqttQosFolder()
    {
        return mqttQosFolder;
    }



    public void reconnectToBrocker()
    {
        System.out.println("Reconnecting to brocker...");

        Thread thread = new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                Date nextCheck = new Date();

                while (isRunning)
                {
                    if (!recalculateReconnectDelay && nextCheck.getTime() - new Date().getTime() > 0)
                        continue;

                    if (connectToBrocker())
                        break;

                    nextCheck = new Date(new Date().getTime() + mqttReconnectDelaySeconds * 1000);
                    recalculateReconnectDelay = false;
                    
                    Thread.yield();
                }
            }
        });

        thread.start();
    }



    private class MqttWorkerCallback implements MqttCallback
    {
        private TempHumSensor bedRoomSensorModel;
        private TempHumSensor livingRoomSensorModel;
        private TempPressureSensor outsideSensorModel;

        private DependencyIndector di;



        public MqttWorkerCallback()
        {
            this.di = DependencyIndector.getInstance();

            this.bedRoomSensorModel = (TempHumSensor) this.di
                    .resolveInstance(GlobalResources.BED_ROOM_INSTANCE_MODEL_NAME);
            this.livingRoomSensorModel = (TempHumSensor) this.di
                    .resolveInstance(GlobalResources.LIVING_ROOM_INSTANCE_MODEL_NAME);
            this.outsideSensorModel = (TempPressureSensor) this.di
                    .resolveInstance(GlobalResources.OUTSIDE_INSTANCE_MODEL_NAME);
        }



        @Override
        public void connectionLost(Throwable cause)
        {
            System.err.println("MQTT Connection Lost. Reason:");
            System.err.println(cause.getMessage());

            try
            {
                client.close();
            }
            catch (MqttException e)
            {
                System.err.println("Closing brocker failed.");
            }

            reconnectToBrocker();
        }



        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception
        {
            String messageStr = new String(message.getPayload());

            System.out.println(String.format("MQTT message arrived.\nTopic: %s\nMessage: %s\n\n",
                    topic, messageStr));

            String messageOrigin = topic.substring(0, topic.indexOf(SECTOR_SEPARATOR));
            String subTopic = topic.substring(topic.indexOf(SECTOR_SEPARATOR) + 1, topic.length());

            switch (messageOrigin)
            {
            case BED_ROOM_TOPIC:
                this.handleBedroomMsg(subTopic, messageStr);
                break;
            case LIVING_ROOM_TOPIC:
                this.handleLivingRoomMsg(subTopic, messageStr);
                break;
            case OUTSIDE_TOPIC:
                this.handleOutsideMsg(subTopic, messageStr);
                break;
            default:
                break;
            }
        }



        @Override
        public void deliveryComplete(IMqttDeliveryToken token)
        {
        }



        private void handleBedroomMsg(String topic, String message)
        {
            switch (topic)
            {
            case "temperature":
                double temp = Double.parseDouble(message);
                this.bedRoomSensorModel.setTempreture(temp);
                break;
            case "humidity":
                double hum = Double.parseDouble(message);
                this.bedRoomSensorModel.setHumidity(hum);
                break;
            case "refreshInterval":
                int refreshInt = Integer.parseInt(message);
                this.bedRoomSensorModel.setRefreshInterval(refreshInt);
                break;
            case "battery":
                double volts = Double.parseDouble(message);
                this.bedRoomSensorModel.setBatteryLevel(this.parseBatteryLevel(volts));
                break;
            }
        }



        private void handleLivingRoomMsg(String topic, String message)
        {
            switch (topic)
            {
            case "temperature":
                double temp = Double.parseDouble(message);
                this.livingRoomSensorModel.setTempreture(temp);
                break;
            case "humidity":
                double hum = Double.parseDouble(message);
                this.livingRoomSensorModel.setHumidity(hum);
                break;
            case "refreshInterval":
                int refreshInt = Integer.parseInt(message);
                this.livingRoomSensorModel.setRefreshInterval(refreshInt);
                break;
            case "battery":
                double volts = Double.parseDouble(message);
                this.livingRoomSensorModel.setBatteryLevel(this.parseBatteryLevel(volts));
                break;
            }
        }



        private void handleOutsideMsg(String topic, String message)
        {
            switch (topic)
            {
            case "temperature":
                double temp = Double.parseDouble(message);
                this.outsideSensorModel.setTempreture(temp);
                break;
            case "pressure":
                double pressure = Double.parseDouble(message);
                this.outsideSensorModel.setPressure(pressure);
                break;
            case "refreshInterval":
                int refreshInt = Integer.parseInt(message);
                this.outsideSensorModel.setRefreshInterval(refreshInt);
                break;
            case "battery":
                double volts = Double.parseDouble(message);
                this.outsideSensorModel.setBatteryLevel(this.parseBatteryLevel(volts));
                break;
            case "altitude":
                double altitude = Double.parseDouble(message);
                this.outsideSensorModel.setAltitude(altitude);
                break;
            }
        }



        private BatteryLevel parseBatteryLevel(double voltage)
        {
            if (voltage > fullBatteryV)
                return BatteryLevel.FULL;

            if (voltage > mediumBatteryV)
                return BatteryLevel.MEDIUM;

            return BatteryLevel.LOW;
        }
    }
}
