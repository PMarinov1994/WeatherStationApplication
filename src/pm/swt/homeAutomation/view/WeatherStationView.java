package pm.swt.homeAutomation.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import pm.swt.homeAutomation.model.BatteryLevel;
import pm.swt.homeAutomation.utils.StationLocation;
import pm.swt.homeAutomation.utils.StationStatus;
import pm.swt.homeAutomation.viewModel.WeatherStationViewModel;


public abstract class WeatherStationView extends BaseView
{
    private static final double DEFAULT_DPI = 96.0;
    private static final int HEIGHT_RATIO = 5;

    private static final String BEDROOM_LOCATION_IMG_PATH = "icons/bedroom.png";
    private static final String LIVINGROOM_LOCATION_IMG_PATH = "icons/livingroom.png";
    private static final String OUTSIDE_LOCATION_IMG_PATH = "icons/outside.png";
    private static final String OK_STATUS_IMG_PATH = "icons/okStatus.png";
    private static final String STANDBY_STATUS_IMG_PATH = "icons/standByStatus.png";
    private static final String ERROR_STATUS_IMG_PATH = "icons/errorStatus.png";

    private static final String BATTERY_LOW_IMG_PATH = "icons/battery_low.png";
    private static final String BATTERY_MEDIUM_IMG_PATH = "icons/battery_medium.png";
    private static final String BATTERY_FULL_IMG_PATH = "icons/battery_full.png";

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
            WeatherStationView.this.onPropertyChanged(evt);
        }
    };



    public WeatherStationView(Composite parent, WeatherStationViewModel viewModel)
    {
        super(parent, SWT.NONE);

        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this.listener);

        this.locationImage = this.getImageLocation(this.viewModel.getHomeSector());
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

        GridLayout gridLayout = new GridLayout(1, true);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 0;
        gridLayout.marginRight = 0;
        gridLayout.marginLeft = 0;

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
                WeatherStationView.this.changeLabelImage(locationLabel, WeatherStationView.this.locationImage);
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
                WeatherStationView.this.changeLabelImage(
                        WeatherStationView.this.statusLabel,
                        WeatherStationView.this.getStatusImage(WeatherStationView.this.viewModel.getStationStatus()));
            }
        });
        this.statusLabel.addDisposeListener(new DisposeListener()
        {
            @Override
            public void widgetDisposed(DisposeEvent e)
            {
                Image image = WeatherStationView.this.statusLabel.getImage();

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
                WeatherStationView.this.changeLabelImage(
                        WeatherStationView.this.batteryLabel,
                        WeatherStationView.this.getBatteryImage(WeatherStationView.this.viewModel.getBatteryLevel()));
            }
        });
        this.batteryLabel.addDisposeListener(new DisposeListener()
        {

            @Override
            public void widgetDisposed(DisposeEvent e)
            {
                Image image = WeatherStationView.this.batteryLabel.getImage();

                if (image != null && !image.isDisposed())
                    image.dispose();
            }
        });

        new Label(imgComp, SWT.None).setBackground(this.backgroundColor);

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
            imageToSet = WeatherStationView.this.getStatusImage((StationStatus) evt.getNewValue());
            WeatherStationView.this.changeLabelImage(statusLabel, imageToSet);
            break;
        case WeatherStationViewModel.BATTERY_LEVEL_PROP_NAME:
            imageToSet = WeatherStationView.this.getBatteryImage((BatteryLevel) evt.getNewValue());
            WeatherStationView.this.changeLabelImage(batteryLabel, imageToSet);
            break;
        default:
            break;
        }
    }



    private Image getImageLocation(StationLocation location)
    {
        switch (location)
        {
        case BED_ROOM:
            return this.getImageFromPath(BEDROOM_LOCATION_IMG_PATH);
        case LIVING_ROOM:
            return this.getImageFromPath(LIVINGROOM_LOCATION_IMG_PATH);
        case OUTSIDE:
            return this.getImageFromPath(OUTSIDE_LOCATION_IMG_PATH);
        default:
            return null;
        }
    }



    /**
     * Creates an <code>Image</code> object from a given absolute or relative path.
     * Note: The returned object must be disposed when not needed anymore!
     *
     * @param path The Path to the image.
     * @return The created image object.
     */
    private Image getImageFromPath(String path)
    {
        InputStream is = null;
        try
        {
            File imgFile = new File(path);
            is = new FileInputStream(imgFile);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        ImageData imgData = new ImageData(is);
        Image img = new Image(this.getDisplay(), imgData);

        Image rescaledImg = this.rescaleImageToDPI(img);
        if (img != rescaledImg)
        {
            img.dispose();
            img = rescaledImg;
        }

        return img;
    }



    /**
     * This method clears any old images that the <code>label</code> may have
     * and created a new image with the appropriate size to fill <code>label</code>.
     * The <code>image</code> will be used only as reference to create the new image
     * with the correct size.
     *
     * @param label The label control to switch the new image will be set.
     * @param image A reference image from which the new image will be created.
     * This image WILL NOT BE DISPOSED!
     */
    private void changeLabelImage(Label label, Image image)
    {
        if (image.isDisposed())
        {
            System.err.println("Error Image was disposed");
            return;
        }

        Rectangle labelSize = label.getBounds();
        if (labelSize.width == 0 || labelSize.height == 0)
            return;

        Image oldImg = label.getImage();
        Image newImg = new Image(WeatherStationView.this.getDisplay(),
                image.getImageData().scaledTo(labelSize.width, labelSize.height));

        label.setImage(newImg);

        if (oldImg != null)
            oldImg.dispose();
    }



    /**
     * Rescale an image object to the correct DPI.
     * NOTE: The returned image must be disposed when not needed anymore.
     *
     * @param image The image to correct.
     * @return The corrected image.
     */
    private Image rescaleImageToDPI(Image image)
    {
        Device display = image.getDevice();
        final double width = image.getBounds().width;
        final double height = image.getBounds().height;
        int currentDPI = display.getDPI().x;
        if (currentDPI <= DEFAULT_DPI)
        {
            return image;
        }
        double multiplier = ((double) currentDPI) / DEFAULT_DPI;
        double newWidth = width * multiplier;
        double newHeight = height * multiplier;

        Image scaledImage = new Image(Display.getCurrent(), image
                .getImageData().scaledTo((int) newWidth, (int) newHeight));

        return scaledImage;
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
