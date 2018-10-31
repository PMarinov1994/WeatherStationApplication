package pm.swt.homeAutomation.mqtt;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.eclipse.swt.widgets.Display;

import pm.swt.homeAutomation.model.TempHumSensor;
import pm.swt.homeAutomation.utils.DependencyIndector;
import pm.swt.homeAutomation.utils.GlobalResources;


public class MqttWorker
{
    private static final String MQTT_SERVER_ADDRESS = "tcp://192.168.200.105:1883";
    private static final String[] MQTT_TOPICS = { "bedroom/#", "livingRoom/#" };

    private static final String MQTT_QOS_SUB_FOLDER = "mqttQOS";

    private MqttClient client;
    private MqttWorkerCallback callBack;

    private final String mqttQosFolder;



    public MqttWorker()
    {
        String jarExePath = "";
        try
        {
            jarExePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        this.mqttQosFolder = String.format("%s%s%s", jarExePath, File.separator, MQTT_QOS_SUB_FOLDER);
        
        File dirQos = new File(this.mqttQosFolder);
        if (!dirQos.exists())
            dirQos.mkdirs();
    }



    public void doWork()
    {
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
            e.printStackTrace();
        }
    }



    public void dispose()
    {
        try
        {
            System.out.println("Disconnectiong client...");
            this.client.disconnectForcibly();

            System.out.println("Closing client...");
            this.client.close(true);
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
    }



    public String getMqttQosFolder()
    {
        return mqttQosFolder;
    }



    private class MqttWorkerCallback implements MqttCallback
    {
        private TempHumSensor bedRoomSensorModel;
        private TempHumSensor livingRoomSensorModel;

        private DependencyIndector di;



        public MqttWorkerCallback()
        {
            this.di = DependencyIndector.getInstance();

            this.bedRoomSensorModel = (TempHumSensor) this.di
                    .resolveInstance(GlobalResources.BED_ROOM_INSTANCE_MODEL_NAME);
            this.livingRoomSensorModel = (TempHumSensor) this.di
                    .resolveInstance(GlobalResources.LIVING_ROOM_INSTANCE_MODEL_NAME);
        }



        @Override
        public void connectionLost(Throwable cause)
        {
            System.err.println("MQTT Connection Lost. Reason:");
            System.err.println(cause.getMessage());
        }



        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception
        {
            String messageStr = new String(message.getPayload());

            System.out.println(String.format("MQTT message arrived.\nTopic: %s\nMessage: %s",
                    topic, messageStr));

            Display display = null;
            if (Display.getCurrent() != null)
                display = Display.getCurrent();
            else
                display = Display.getDefault();

            display.asyncExec(new Runnable()
            {
                @Override
                public void run()
                {
                    double humidity;
                    double temperature;

                    switch (topic)
                    {
                    case "bedroom/temperature":
                        temperature = Double.parseDouble(messageStr);
                        bedRoomSensorModel.setTempreture(temperature);

                        break;

                    case "bedroom/humidity":
                        humidity = Double.parseDouble(messageStr);
                        bedRoomSensorModel.setHumidity(humidity);

                        break;

                    case "livingRoom/temperature":
                        temperature = Double.parseDouble(messageStr);
                        livingRoomSensorModel.setTempreture(temperature);

                        break;

                    case "livingRoom/humidity":
                        humidity = Double.parseDouble(messageStr);
                        livingRoomSensorModel.setHumidity(humidity);

                        break;

                    default:
                        break;
                    }
                }
            });
        }



        @Override
        public void deliveryComplete(IMqttDeliveryToken token)
        {
            // TODO Auto-generated method stub
        }
    }
}
