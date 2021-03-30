package pm.swt.homeAutomation.configurator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pm.swt.homeAutomation.utils.GlobalResources;

public class UILayoutManager
{
    private final String CONFIG_FOLDER = "configuration";
    private final String CONFIG_FILE_NAME = "ui.xml";

    private static final String SENSOR_TYPE_ATTRIBUTE = "sensorType";
    private static final String LOCATION_ICON_ATTRIBUTE = "locationIcon";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String MQTT_TOPIC_ATTRIBUTE = "mqttTopic";

    private static final String AM2320_TYPE = "AM2320";
    private static final String BME280_TYPE = "BME280";

    private List<UISector> sectors;

    public UILayoutManager()
    {
        this.sectors = new ArrayList<>();
    }

    public UISector[] getSectors()
    {
        return this.sectors.toArray(new UISector[0]);
    }

    public void initialize() throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        String jarExecPath = GlobalResources.getExecJarPath();
        String configFilePath = String.format("%s%s%s%s%s", jarExecPath, File.separator, CONFIG_FOLDER, File.separator, CONFIG_FILE_NAME);

        Document doc = builder.parse(configFilePath);

        Element root = doc.getDocumentElement();
        
        NodeList childreList = root.getChildNodes();
        for (int i = 0; i < childreList.getLength(); i++)
        {
            Node child = childreList.item(i);
            if (child.getNodeType() != Node.ELEMENT_NODE)
                continue;

            Element element = (Element)child;

            String icon = element.getAttribute(LOCATION_ICON_ATTRIBUTE);
            String sensor = element.getAttribute(SENSOR_TYPE_ATTRIBUTE);
            String name = element.getAttribute(NAME_ATTRIBUTE);
            String mqttTopic = element.getAttribute(MQTT_TOPIC_ATTRIBUTE);

            this.sectors.add(new UISector(name, mqttTopic, icon, this.getSensorType(sensor)));
        }
    }

    private UISensorType getSensorType(String sensor)
    {
        if (sensor.equals(BME280_TYPE))
            return UISensorType.BME280;

        if (sensor.equals(AM2320_TYPE))
            return UISensorType.AM2320;

        throw new IllegalArgumentException("The sensor type is not supported.");
    }
}