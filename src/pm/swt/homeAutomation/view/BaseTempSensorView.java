package pm.swt.homeAutomation.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import pm.swt.homeAutomation.configurator.ConfigurationFileManager;
import pm.swt.homeAutomation.model.BatteryLevel;
import pm.swt.homeAutomation.model.ConfigurationModel;
import pm.swt.homeAutomation.utils.DependencyIndector;
import pm.swt.homeAutomation.utils.GlobalResources;
import pm.swt.homeAutomation.utils.StationStatus;
import pm.swt.homeAutomation.viewModel.WeatherStationViewModel;


public abstract class BaseTempSensorView extends BaseView
{
    private static final int HEIGHT_RATIO = 4;

    private static final String ICONS_FOLDER_PATH = "icons/";

    private static final String OK_STATUS_IMG_PATH = "icons/okStatus.png";
    private static final String STANDBY_STATUS_IMG_PATH = "icons/standByStatus.png";
    private static final String ERROR_STATUS_IMG_PATH = "icons/errorStatus.png";

    private static final String BATTERY_LOW_IMG_PATH = "icons/battery_low_croped.png";
    private static final String BATTERY_MEDIUM_IMG_PATH = "icons/battery_medium_croped.png";
    private static final String BATTERY_FULL_IMG_PATH = "icons/battery_full_croped.png";

    private static final String UNKNOWN_IMG_PATH = "icons/info.png";

    private WeatherStationViewModel viewModel;

    private Label statusLabel;
    private Label batteryLabel;
    private CLabel tempLabel;

    private Image locationImage;

    private Image okStatusImage;
    private Image standByImage;
    private Image errorImage;

    private Image batteryFullImage;
    private Image batteryMediumImage;
    private Image batteryLowImage;

    private Image infoImage;

    protected Color backgroundColor;
    protected Color foregroundColor;

    private PropertyChangeListener listener = new PropertyChangeListener()
    {

        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            BaseTempSensorView.this.onPropertyChanged(evt);
        }
    };



    public BaseTempSensorView(Composite parent, WeatherStationViewModel viewModel)
    {
        super(parent, SWT.NONE);

        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this.listener);

        this.locationImage = this.getImageFromPath(ICONS_FOLDER_PATH + this.viewModel.getHomeSectorIcon());
        this.okStatusImage = this.getImageFromPath(OK_STATUS_IMG_PATH);
        this.standByImage = this.getImageFromPath(STANDBY_STATUS_IMG_PATH);
        this.errorImage = this.getImageFromPath(ERROR_STATUS_IMG_PATH);

        this.batteryFullImage = this.getImageFromPath(BATTERY_FULL_IMG_PATH);
        this.batteryMediumImage = this.getImageFromPath(BATTERY_MEDIUM_IMG_PATH);
        this.batteryLowImage = this.getImageFromPath(BATTERY_LOW_IMG_PATH);
        this.infoImage = this.getImageFromPath(UNKNOWN_IMG_PATH);

        this.createComposite(this);

        this.registerResizableControl(tempLabel);
    }



    @Override
    public void dispose()
    {
        this.viewModel.removePropertyChangeListener(this.listener);
        this.viewModel.dispose();

        this.locationImage.dispose();

        this.okStatusImage.dispose();
        this.standByImage.dispose();
        this.errorImage.dispose();

        this.batteryFullImage.dispose();
        this.batteryMediumImage.dispose();
        this.batteryLowImage.dispose();

        this.infoImage.dispose();

        super.dispose();
    }



    @Override
    protected void createComposite(Composite parent)
    {
        Display display = parent.getDisplay();
        this.foregroundColor = display.getSystemColor(SWT.COLOR_WHITE);
        this.backgroundColor = display.getSystemColor(SWT.COLOR_BLACK);

        this.setBackground(this.backgroundColor);

        DependencyIndector di = DependencyIndector.getInstance();
        ConfigurationFileManager cFileManager = (ConfigurationFileManager)di.resolveInstance(GlobalResources.CONFIGURATION_FILE_MANAGER_NAME);
        ConfigurationModel config = cFileManager.getConfig();

        String marginStr = config.getAdditionalParamer("sectorUiMargin");
        
        int margin = 0;
        if (!marginStr.isEmpty())
          margin= Integer.parseInt(marginStr);

        System.out.println(String.format("Layout margin is: %d", margin));

        GridLayout gridLayout = new GridLayout(1, true);
        gridLayout.marginWidth = margin;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 0;

        parent.setLayout(gridLayout);

        Composite imgComp = new Composite(parent, SWT.NONE);
        imgComp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        imgComp.setLayout(new FillLayout(SWT.HORIZONTAL));

        Label locationLabel = new Label(imgComp, SWT.NONE);
        locationLabel.setBackground(this.backgroundColor);
        locationLabel.addListener(SWT.Resize, new Listener()
        {

            @Override
            public void handleEvent(Event event)
            {
                BaseTempSensorView.this.changeLabelImage(locationLabel, BaseTempSensorView.this.locationImage);
            }
        });
        locationLabel.addDisposeListener(new DisposeListener()
        {
            @Override
            public void widgetDisposed(DisposeEvent e)
            {
                Image image = locationLabel.getImage();

                if (image != null && !image.isDisposed())
                    image.dispose();
            }
        });

        this.statusLabel = new Label(imgComp, SWT.None);
        this.statusLabel.setBackground(this.backgroundColor);
        this.statusLabel.addListener(SWT.Resize, new Listener()
        {

            @Override
            public void handleEvent(Event event)
            {
                BaseTempSensorView.this.changeLabelImage(
                        BaseTempSensorView.this.statusLabel,
                        BaseTempSensorView.this.getStatusImage(BaseTempSensorView.this.viewModel.getStationStatus()));
            }
        });
        this.statusLabel.addDisposeListener(new DisposeListener()
        {
            @Override
            public void widgetDisposed(DisposeEvent e)
            {
                Image image = BaseTempSensorView.this.statusLabel.getImage();

                if (image != null && !image.isDisposed())
                    image.dispose();
            }
        });

        this.batteryLabel = new Label(imgComp, SWT.None);
        this.batteryLabel.setBackground(this.backgroundColor);
        this.batteryLabel.addListener(SWT.Resize, new Listener()
        {

            @Override
            public void handleEvent(Event event)
            {
                BaseTempSensorView.this.changeLabelImage(
                        BaseTempSensorView.this.batteryLabel,
                        BaseTempSensorView.this.getBatteryImage(BaseTempSensorView.this.viewModel.getBatteryLevel()));
            }
        });
        this.batteryLabel.addDisposeListener(new DisposeListener()
        {

            @Override
            public void widgetDisposed(DisposeEvent e)
            {
                Image image = BaseTempSensorView.this.batteryLabel.getImage();

                if (image != null && !image.isDisposed())
                    image.dispose();
            }
        });

        imgComp.addListener(SWT.Resize, new Listener()
        {

            @Override
            public void handleEvent(Event event)
            {
                Object layoutData = imgComp.getLayoutData();
                if (layoutData instanceof GridData)
                {
                    GridData gridData = (GridData) layoutData;
                    gridData.heightHint = imgComp.getParent().getBounds().height / HEIGHT_RATIO;
                }
            }
        });


        GridData dataValue = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);

        this.tempLabel = new CLabel(parent, SWT.CENTER);
        this.tempLabel.setLayoutData(dataValue);
        this.tempLabel.setText(this.viewModel.getTemperature());
        this.tempLabel.setBackground(this.backgroundColor);
        this.tempLabel.setForeground(this.foregroundColor);

        this.createAdditionalComponents(parent, this.viewModel);

        parent.pack();
    }



    protected abstract void createAdditionalComponents(Composite parent, WeatherStationViewModel viewModel);



    protected void onPropertyChanged(PropertyChangeEvent evt)
    {
        Image imageToSet = null;

        switch (evt.getPropertyName())
        {
        case WeatherStationViewModel.TEMPERATURE_PROP_NAME:
            tempLabel.setText((String) evt.getNewValue());
            onCtrlTextChange(tempLabel);
            break;
        case WeatherStationViewModel.STATION_STATUS_PROP_NAME:
            imageToSet = BaseTempSensorView.this.getStatusImage((StationStatus) evt.getNewValue());
            BaseTempSensorView.this.changeLabelImage(statusLabel, imageToSet);
            break;
        case WeatherStationViewModel.BATTERY_LEVEL_PROP_NAME:
            imageToSet = BaseTempSensorView.this.getBatteryImage((BatteryLevel) evt.getNewValue());
            BaseTempSensorView.this.changeLabelImage(batteryLabel, imageToSet);
            break;
        default:
            break;
        }
    }



    /**
     * Returns an image based on the status.
     * NOTE: The returned image will be disposed when this class
     * is no longer needed in its <code>Disposed()</code> method.
     * DO NOT DISPOSE THIS IMAGE.
     *
     * @param status
     * @return
     */
    private Image getStatusImage(StationStatus status)
    {
        switch (status)
        {
        case ERROR_STATUS:
            return this.errorImage;
        case OK_STATUS:
            return this.okStatusImage;
        case STANDBY_STATUS:
            return this.standByImage;
        default:
            return null;

        }
    }



    /**
     * Returns an image based on the batteryLevel.
     * NOTE: The returned image will be disposed when this class
     * is no longer needed in its <code>Disposed()</code> method.
     * DO NOT DISPOSE THIS IMAGE.
     *
     * @param batteryLevel
     * @return
     */
    private Image getBatteryImage(BatteryLevel batteryLevel)
    {
        switch (batteryLevel)
        {
        case FULL:
            return this.batteryFullImage;
        case LOW:
            return this.batteryLowImage;
        case MEDIUM:
            return this.batteryMediumImage;
        case UNKNOWN:
            return this.infoImage;
        default:
            return null;

        }
    }
}
