package pm.swt.homeAutomation.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import pm.swt.homeAutomation.model.TempHumSensor;
import pm.swt.homeAutomation.utils.DependencyIndector;
import pm.swt.homeAutomation.utils.GlobalResources;
import pm.swt.homeAutomation.viewModel.TempHumSensorViewModel;


public class MainView
{
    public void show()
    {
        Display display = new Display();
        Shell shell = new Shell(display);

        shell.setLayout(new FillLayout(SWT.HORIZONTAL));

        DependencyIndector di = DependencyIndector.getInstance();

        TempHumSensor bedRoomModel = (TempHumSensor) di.resolveInstance(GlobalResources.BED_ROOM_INSTANCE_MODEL_NAME);
        TempHumSensor livingRoomModel = (TempHumSensor) di.resolveInstance(GlobalResources.LIVING_ROOM_INSTANCE_MODEL_NAME);

        new TempHumSensorView(shell, new TempHumSensorViewModel(bedRoomModel, "Bed Room"));
        new TempHumSensorView(shell, new TempHumSensorViewModel(livingRoomModel, "Living Room"));

        shell.open();

        shell.setSize(display.getClientArea().width, display.getClientArea().height);
        shell.setFullScreen(true);
        shell.layout(true, true);

        // run the event loop as long as the window is open
        while (!shell.isDisposed())
        {
            // read the next OS event queue and transfer it to a SWT event
            if (!display.readAndDispatch())
            {
                // if there are currently no other OS event to process
                // sleep until the next OS event is available
                display.sleep();
            }
        }

        // disposes all associated windows and their components
        display.dispose();
    }
}
