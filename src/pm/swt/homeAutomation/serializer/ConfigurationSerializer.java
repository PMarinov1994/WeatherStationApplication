package pm.swt.homeAutomation.serializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;

import pm.swt.homeAutomation.model.ConfigurationModel;


public class ConfigurationSerializer
{
    private static final String BATTERY_FULL_LEVEL_KEY = "batteryFullLevelKey";
    private static final String BATTERY_MEDUIM_LEVEL_KEY = "batteryMediumLevelKey";
    private static final String BATTERY_LOW_LEVEL_KEY = "batteryLowLevelKey";
    private static final String MISSED_REPORTS_COUNT_KEY = "missedReportsCountKey";

    private static final String PARAMETER_SEPARATOR_CHAR = ":";
    private static final String COMMENT_LINE_STARTER = "#";

    private final File file;



    public ConfigurationSerializer(String file)
    {
        this.file = new File(file);

        if (!this.file.exists())
            throw new InvalidParameterException("File does not exists");
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
                if (line.startsWith(COMMENT_LINE_STARTER))
                    continue;

                String[] keyValue = line.split(PARAMETER_SEPARATOR_CHAR);
                if (keyValue.length != 2)
                    throw new InvalidParameterException("Wrong configuration line!");

                switch (keyValue[0])
                {
                case BATTERY_FULL_LEVEL_KEY:
                    model.setFullBatteryLevel(Double.parseDouble(keyValue[1]));
                    break;
                case BATTERY_MEDUIM_LEVEL_KEY:
                    model.setMediumBatteryLevel(Double.parseDouble(keyValue[1]));
                    break;
                case BATTERY_LOW_LEVEL_KEY:
                    model.setLowBatteryLevel(Double.parseDouble(keyValue[1]));
                    break;
                case MISSED_REPORTS_COUNT_KEY:
                    model.setMissedReportsCount(Integer.parseInt(keyValue[1]));
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
}
