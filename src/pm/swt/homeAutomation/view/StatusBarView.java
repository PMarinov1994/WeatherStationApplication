package pm.swt.homeAutomation.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Supplier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import pm.swt.homeAutomation.viewModel.StatusBarViewModel;


public class StatusBarView extends BaseView
{
    private static final String CAMERA_ON_IMAGE_NAME = "icons/camOn.png";
    private static final String CAMERA_OFF_IMAGE_NAME = "icons/camOff.png";

    private static final String SWITCH_POWER_ON = "icons/switchOn.png";
    private static final String SWITCH_POWER_OFF = "icons/switchOff.png";

    private final int HEIGHT_RATIO = 7;

    private CLabel timeLabel;
    private CLabel messageLabel;

    private Label cam1StatusLabel;
    private Label relaySwitchOneLabel;
    private Label relaySwitchTwoLabel;

    private StatusBarViewModel viewModel;

    private Image cameraOnImage;
    private Image cameraOffImage;

    private Image switchPowerOnImage;
    private Image switchPowerOffImage;

    private PropertyChangeListener listener = new PropertyChangeListener()
    {

        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            switch (evt.getPropertyName())
            {
            case StatusBarViewModel.TIME_PROP_NAME:
                timeLabel.setText((String) evt.getNewValue());
                break;
            case StatusBarViewModel.MESSAGE_PROP_NAME:
                messageLabel.setText((String) evt.getNewValue());
                break;
            
            case StatusBarViewModel.CAM_1_PROP_NAME:
                StatusBarView.this.changeLabelImage(cam1StatusLabel, getCam1StatusImage());
                break;

            case StatusBarViewModel.RELAY_ONE_STATE:
                StatusBarView.this.changeLabelImage(relaySwitchOneLabel, getPowerSwitchImage((boolean) evt.getNewValue()));
                break;

            case StatusBarViewModel.RELAY_TWO_STATE:
                StatusBarView.this.changeLabelImage(relaySwitchTwoLabel, getPowerSwitchImage((boolean) evt.getNewValue()));
                break;

            default:
                break;
            }
        }
    };



    public StatusBarView(Composite parent, StatusBarViewModel viewModel)
    {
        super(parent, SWT.NONE);

        this.cameraOnImage = this.getImageFromPath(CAMERA_ON_IMAGE_NAME);
        this.cameraOffImage = this.getImageFromPath(CAMERA_OFF_IMAGE_NAME);

        this.switchPowerOnImage = this.getImageFromPath(SWITCH_POWER_ON);
        this.switchPowerOffImage = this.getImageFromPath(SWITCH_POWER_OFF);

        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this.listener);
        this.createComposite(this);
    }



    @Override
    protected void createComposite(Composite parent)
    {
        GridLayout layout = new GridLayout(8, false);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);

        parent.setLayout(layout);

        Color backgroundColor = parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
        Color foregroundColor = parent.getDisplay().getSystemColor(SWT.COLOR_WHITE);

        parent.setBackground(backgroundColor);
        parent.setForeground(foregroundColor);

        // CLOCK LABEL
        timeLabel = new CLabel(parent, SWT.NONE);
        timeLabel.setBackground(backgroundColor);
        timeLabel.setForeground(foregroundColor);
        timeLabel.setLayoutData(data);
        
        this.registerResizableControl(timeLabel);
        timeLabel.setText("00:00");

        // CAM 1 LABEL
        CLabel cam1StatusLabelText = new CLabel(parent, SWT.NONE);
        cam1StatusLabelText.setBackground(backgroundColor);
        cam1StatusLabelText.setForeground(foregroundColor);
        cam1StatusLabelText.setLayoutData(data);
        
        this.registerResizableControl(cam1StatusLabelText);
        cam1StatusLabelText.setText("Cam1:");

        // CAM 1 IMAGE
        this.cam1StatusLabel = new Label(parent, SWT.NONE);
        this.cam1StatusLabel.setBackground(backgroundColor);
        this.cam1StatusLabel.setLayoutData(data);

        this.addLabelImageResizeHandle(this.cam1StatusLabel, () -> getCam1StatusImage());
        this.addLabelImageDisposeHandle(this.cam1StatusLabel);        

        // RELAY ONE LABEL
        CLabel relaySwitchOneLabelText = new CLabel(parent, SWT.NONE);
        relaySwitchOneLabelText.setBackground(backgroundColor);
        relaySwitchOneLabelText.setForeground(foregroundColor);
        relaySwitchOneLabelText.setLayoutData(data);

        this.registerResizableControl(relaySwitchOneLabelText);
        relaySwitchOneLabelText.setText("SW1:");

        // RELAY ONE IMAGE
        this.relaySwitchOneLabel = new Label(parent, SWT.NONE);
        this.relaySwitchOneLabel.setBackground(backgroundColor);
        this.relaySwitchOneLabel.setLayoutData(data);

        this.addLabelImageResizeHandle(this.relaySwitchOneLabel, () -> getPowerSwitchImage(viewModel.getRelayOneState()));
        this.addLabelImageDisposeHandle(this.relaySwitchOneLabel);


        // RELAY TWO LABEL
        CLabel relaySwitchTwoLabelText = new CLabel(parent, SWT.NONE);
        relaySwitchTwoLabelText.setBackground(backgroundColor);
        relaySwitchTwoLabelText.setForeground(foregroundColor);
        relaySwitchTwoLabelText.setLayoutData(data);

        this.registerResizableControl(relaySwitchTwoLabelText);
        relaySwitchTwoLabelText.setText("SW2:");

        // RELAY TWO IMAGE
        this.relaySwitchTwoLabel = new Label(parent, SWT.NONE);
        this.relaySwitchTwoLabel.setBackground(backgroundColor);
        this.relaySwitchTwoLabel.setLayoutData(data);

        this.addLabelImageResizeHandle(this.relaySwitchTwoLabel, () -> getPowerSwitchImage(viewModel.getRelayTwoState()));
        this.addLabelImageDisposeHandle(this.relaySwitchTwoLabel);

        // STATUS MESSAGE TEXT
        this.messageLabel = new CLabel(parent, SWT.None);
        this.messageLabel.setBackground(backgroundColor);
        this.messageLabel.setForeground(foregroundColor);
        this.messageLabel.setLayoutData(data);
        
        this.registerResizableControl(this.messageLabel);
        this.messageLabel.setText("");
    }



    @Override
    public void onResize()
    {
        Object layoutData = this.getLayoutData();
        if (layoutData instanceof GridData)
        {
            GridData gridData = (GridData) layoutData;
            gridData.heightHint = this.getParent().getBounds().height / HEIGHT_RATIO;
        }

        super.onResize();
    }



    @Override
    public void dispose()
    {
        this.viewModel.removePropertyChangeListener(this.listener);
        this.viewModel.dispose();

        this.cameraOnImage.dispose();
        this.cameraOffImage.dispose();

        this.switchPowerOffImage.dispose();
        this.switchPowerOnImage.dispose();

        super.dispose();
    }



    private void addLabelImageResizeHandle(Label label, Supplier<Image> getImage)
    {
        label.addListener(SWT.Resize, new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                changeLabelImage(label, getImage.get());
            }
        });
    }



    private void addLabelImageDisposeHandle(Label label)
    {
        label.addDisposeListener(new DisposeListener()
        {
            @Override
            public void widgetDisposed(DisposeEvent e)
            {
                Image image = label.getImage();

                if (image != null && !image.isDisposed())
                    image.dispose();
            }
        });
    }



    private Image getCam1StatusImage()
    {
        if (this.viewModel.getCam1Status())
            return this.cameraOnImage;
        else
            return this.cameraOffImage;
    }



    private Image getPowerSwitchImage(boolean powerSwitch)
    {
        if (powerSwitch)
            return this.switchPowerOnImage;
        else
            return this.switchPowerOffImage;
    }
}
