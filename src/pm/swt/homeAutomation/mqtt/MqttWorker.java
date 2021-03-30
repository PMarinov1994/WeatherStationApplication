package pm.swt.homeAutomation.mqtt;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import pm.swt.homeAutomation.configurator.ConfigurationFileManager;
import pm.swt.homeAutomation.configurator.IConfigurationChanged;
import pm.swt.homeAutomation.configurator.UILayoutManager;
import pm.swt.homeAutomation.configurator.UISector;
import pm.swt.homeAutomation.model.BatteryLevel;
import pm.swt.homeAutomation.model.ConfigurationModel;
import pm.swt.homeAutomation.model.TempHumSensor;
import pm.swt.homeAutomation.model.TempPressureSensor;
import pm.swt.homeAutomation.utils.DependencyIndector;
import pm.swt.homeAutomation.utils.GlobalResources;


public class MqttWorker
{
    private static final char SECTOR_SEPARATOR = '/';
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
            
            DependencyIndector di = DependencyIndector.getInstance();
            UILayoutManager layoutManager = (UILayoutManager)di.resolveInstance(GlobalResources.UI_LAYOUT_MANAGER_NAME);

            List<String> topics = new ArrayList<>();
            for (UISector sector : layoutManager.getSectors())
                topics.add(sector.getMqttTopic() + "/#");

            int[] qos = new int[topics.size()];
            Arrays.fill(qos, 0);

            client.subscribe(topics.toArray(new String[0]), qos);
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

                    try
                    {
                        Thread.sleep(20);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }


    private class MqttWorkerCallback implements MqttCallback
    {
        private UISector[] sectors;

        public MqttWorkerCallback()
        {
            DependencyIndector di = DependencyIndector.getInstance();
            UILayoutManager layoutManager = (UILayoutManager)di.resolveInstance(GlobalResources.UI_LAYOUT_MANAGER_NAME);

            this.sectors = layoutManager.getSectors();
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
            System.out.println("Mqtt thread: " + Thread.currentThread().getName());

            String messageStr = new String(message.getPayload());

            System.out.println(String.format("MQTT message arrived.\nTopic: %s\nMessage: %s\n\n",
                    topic, messageStr));

            String messageOrigin = topic.substring(0, topic.indexOf(SECTOR_SEPARATOR));
            String subTopic = topic.substring(topic.indexOf(SECTOR_SEPARATOR) + 1, topic.length());

            for (UISector uiSector : sectors)
            {
                String sectorTopic = uiSector.getMqttTopic();
                if (!sectorTopic.equals(messageOrigin))
                    continue;

                switch (uiSector.getSensorType()) {
                    case BME280:
                        this.handleBME280(subTopic, messageStr, uiSector.getModel(TempPressureSensor.class));
                        break;                
                    case AM2320:
                        this.handleAM2320(subTopic, messageStr, uiSector.getModel(TempHumSensor.class));
                        break;
                }
            }
        }

        private void handleAM2320(String topic, String message, TempHumSensor model)
        {
            switch (topic)
            {
            case "temperature":
                double temp = Double.parseDouble(message);
                model.setTempreture(temp);
                break;
            case "humidity":
                double hum = Double.parseDouble(message);
                model.setHumidity(hum);
                break;
            case "refreshInterval":
                int refreshInt = Integer.parseInt(message);
                model.setRefreshInterval(refreshInt);
                break;
            case "battery":
                double volts = Double.parseDouble(message);
                model.setBatteryLevel(this.parseBatteryLevel(volts));
                break;
            }
        }

        private void handleBME280(String topic, String message, TempPressureSensor model)
        {
            switch (topic)
            {
            case "temperature":
                double temp = Double.parseDouble(message);
                model.setTempreture(temp);
                break;
            case "pressure":
                double pressure = Double.parseDouble(message);
                model.setPressure(pressure);
                break;
            case "refreshInterval":
                int refreshInt = Integer.parseInt(message);
                model.setRefreshInterval(refreshInt);
                break;
            case "battery":
                double volts = Double.parseDouble(message);
                model.setBatteryLevel(this.parseBatteryLevel(volts));
                break;
            case "altitude":
                double altitude = Double.parseDouble(message);
                model.setAltitude(altitude);
                break;
            case "humidity":
                double humidity = Double.parseDouble(message);
                model.setHumidity(humidity);
                break;
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token)
        {
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
