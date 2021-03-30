package pm.swt.homeAutomation.serializer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;

import pm.swt.homeAutomation.model.ConfigurationModel;


public class ConfigurationSerializer
{
    private static final String PARAMETER_SEPARATOR_CHAR = ":";
    private static final String COMMENT_LINE_STARTER = "#";
    private static final String START_SEQUENCE = "/!#$%!@$%@@#%";

    private final File file;



    public ConfigurationSerializer(File file)
    {
        this.file = file;
    }



    public ConfigurationModel deserialize()
    {
        ConfigurationModel model = new ConfigurationModel();

        try (FileReader fileReader = new FileReader(this.file);
                BufferedReader br = new BufferedReader(fileReader);)
        {
            String line;

            while ((line = br.readLine()) != null)
            {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                if (line.equals(START_SEQUENCE))
                    continue;

                if (line.startsWith(COMMENT_LINE_STARTER))
                    continue;

                String[] keyValue = line.split(PARAMETER_SEPARATOR_CHAR);
                if (keyValue.length != 2)
                    throw new InvalidParameterException("Wrong configuration line!");

                switch (keyValue[0])
                {
                case ConfigurationModel.FULL_BATTERY_LEVEL:
                    model.setFullBatteryLevel(Double.parseDouble(keyValue[1]));
                    break;
                case ConfigurationModel.MEDIUM_BATTERY_LEVEL:
                    model.setMediumBatteryLevel(Double.parseDouble(keyValue[1]));
                    break;
                case ConfigurationModel.LOW_BATTERY_LEVEL:
                    model.setLowBatteryLevel(Double.parseDouble(keyValue[1]));
                    break;
                case ConfigurationModel.MISSED_REPORTS_COUNT:
                    model.setMissedReportsCount(Integer.parseInt(keyValue[1]));
                    break;
                case ConfigurationModel.MQTT_ADDRESS:
                    model.setMqttAddress(keyValue[1]);
                    break;
                case ConfigurationModel.MQTT_PORT:
                    model.setMqttPort(keyValue[1]);
                    break;
                case ConfigurationModel.MQTT_RECONNECT_INTERVAL_SECONDS:
                    model.setMqttReconnectIntervalSeconds(Integer.parseInt(keyValue[1]));
                    break;
                case ConfigurationModel.APPLICATION_HOT_PARAMETERS_CHANGE_ENABLE:
                    model.setApplicationHotParametersChangeEnable(Boolean.parseBoolean(keyValue[1]));
                    break;

                default:
                    model.putAdditionalParamer(keyValue[0], keyValue[1]);
                    break;
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }


        return model;
    }



    public void serialize(ConfigurationModel model)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(START_SEQUENCE + "\n");
        sb.append("#########################################\n");
        sb.append("#\n");
        sb.append("# This a configuration file for WeatherStation HomeAutomation App\n");
        sb.append("#\n");
        sb.append("#########################################\n");
        sb.append("\n\n");
        sb.append("### General usage of this file: ###\n");
        sb.append("#This is simple comment. Lines that start with '#' will be ingnored when parsed.\n");
        sb.append("#All parameters are key-value based and are in no order, i.e. the order can be changed.\n");
        sb.append("#All the paramerts are hot-plugged. This means that the application can be running and any change\n");
        sb.append("#to this file will result in immediate change of the program behavior\n");
        sb.append("#Empty lines will be ignored. Invalid paramerts and/or keys will lead to unexpected behavior.\n");
        sb.append("#No validation is made as of now.\n");
        sb.append("#/* Unused */ - The first line of this file confirms that this is the file that the program can use. DO NOT CHANGE IT.\n");
        sb.append("\n\n");

        sb.append("#Somethimes the stations fail to send data. This is how many consecutive fails are OK. Any more than this number and\n");
        sb.append("#the error alarm will be raised.\n");
        sb.append(String.format("%s%s%s\n\n", ConfigurationModel.MISSED_REPORTS_COUNT,
                PARAMETER_SEPARATOR_CHAR, model.getMissedReportsCount()));

        sb.append("#If battery is above this value, we consider it full.\n");
        sb.append(String.format("%s%s%s\n\n", ConfigurationModel.FULL_BATTERY_LEVEL,
                PARAMETER_SEPARATOR_CHAR, model.getFullBatteryLevel()));

        sb.append("#If battery is above this value and below the previous one, we consider it medium. Anything else is low battery\n");
        sb.append(String.format("%s%s%s\n\n", ConfigurationModel.MEDIUM_BATTERY_LEVEL,
                PARAMETER_SEPARATOR_CHAR, model.getMediumBatteryLevel()));

        sb.append("#This value is not used.\n");
        sb.append(String.format("%s%s%s\n\n", ConfigurationModel.LOW_BATTERY_LEVEL,
                PARAMETER_SEPARATOR_CHAR, model.getLowBatteryLevel()));

        sb.append("#This is the MQTT Brocker Address from which we get the data.\n");
        sb.append(String.format("%s%s%s\n\n", ConfigurationModel.MQTT_ADDRESS,
                PARAMETER_SEPARATOR_CHAR, model.getMqttAddress()));

        sb.append("#This is the MQTT Brocker Port to which we connect.\n");
        sb.append(String.format("%s%s%s\n\n", ConfigurationModel.MQTT_PORT,
                PARAMETER_SEPARATOR_CHAR, model.getMqttPort()));

        sb.append("#In case we get disconnected from the brocker, we will try to reconnect. This is the amount to wait before every try.\n");
        sb.append(String.format("%s%s%s\n\n", ConfigurationModel.MQTT_RECONNECT_INTERVAL_SECONDS,
                PARAMETER_SEPARATOR_CHAR, model.getMqttReconnectIntervalSeconds()));
        
        sb.append("#Application supports hot parameter chaning. Its is disabled by default to save CPU usage. false - off, true - on.\n");
        sb.append(String.format("%s%s%s\n\n", ConfigurationModel.APPLICATION_HOT_PARAMETERS_CHANGE_ENABLE,
                PARAMETER_SEPARATOR_CHAR, model.isApplicationHotParametersChangeEnable()));

        sb.append("#The margin between each Sector.\n");
        sb.append("sectorUiMargin:20\n\n");

        try (FileWriter fw = new FileWriter(this.file);
                BufferedWriter bw = new BufferedWriter(fw))
        {
            bw.write(sb.toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }



    public File getFile()
    {
        return file;
    }
}
