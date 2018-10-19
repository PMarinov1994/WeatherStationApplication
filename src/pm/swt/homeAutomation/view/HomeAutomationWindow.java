package pm.swt.homeAutomation.view;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import pm.swt.homeAutomation.model.TempHumSensor;
import pm.swt.homeAutomation.utils.DependencyIndector;
import pm.swt.homeAutomation.utils.GlobalResources;
import pm.swt.homeAutomation.viewModel.TempHumSensorViewModel;


public class HomeAutomationWindow
{
    private Display display;
    private Shell shell;

    BaseView statusBarView;
    BaseView bedRoomView;
    BaseView livingRoomView;



    public HomeAutomationWindow()
    {
        this.display = new Display();
        this.shell = new Shell(this.display);
        this.shell.setLayout(new FillLayout());

        this.createContent(this.shell);
        
        this.shell.addListener(SWT.Resize, new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                onResize();
            }
        });
    }



    public void show()
    {
        shell.setMaximized(true);
        shell.setFullScreen(true);
        shell.open();

        this.onResize();

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



    private void createContent(Composite parent)
    {
        final int GRID_COLS = 2;

        Composite mainComp = new Composite(parent, SWT.NONE);

        GridLayout gridLayout = new GridLayout(GRID_COLS, true);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 0;
        mainComp.setLayout(gridLayout);

        statusBarView = new StatusBarView(mainComp);
        GridData statusBarGridData = new GridData(SWT.FILL, SWT.TOP, true, false);
        statusBarGridData.horizontalSpan = GRID_COLS;
        statusBarView.setLayoutData(statusBarGridData);

        DependencyIndector di = DependencyIndector.getInstance();

        TempHumSensor bedRoomModel = (TempHumSensor) di.resolveInstance(GlobalResources.BED_ROOM_INSTANCE_MODEL_NAME);
        bedRoomView = new TempHumSensorView(mainComp, new TempHumSensorViewModel(bedRoomModel, "Bed Room"));
        bedRoomView.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        TempHumSensor livingRoomModel = (TempHumSensor) di.resolveInstance(GlobalResources.LIVING_ROOM_INSTANCE_MODEL_NAME);
        livingRoomView = new TempHumSensorView(mainComp, new TempHumSensorViewModel(livingRoomModel, "Living Room"));
        livingRoomView.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }



    private void onResize()
    {
        bedRoomView.onResize();
        livingRoomView.onResize();
        statusBarView.onResize();

        shell.layout(true, true);
    }
}
