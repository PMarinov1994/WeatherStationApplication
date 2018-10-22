# WeatherStationApplication

This project uses SWT to display information from MQTT Broker. This project is made to show data from ESP8266_WeatherStation project found in https://github.com/PMarinov1994/esp8266_weatherStation
The project is build with Eclipse IDE. All the libs that are needed for development are located in the lib folder. The jar build uses a dinamic class loader to load every dependent jar library.
The SWT lib is loaded depending on OS arch.

## TODO
- Implement weather prediction from air pressure data.
- org.eclipse.paho.client.mqtt.jar lib adds some Hash-nameed folders at the root. See if they can be added somewhere else.

## More TODOs
- See the TODOs file found in /src/pm/swt/homeAutomation folder.
