package pm.swt.homeAutomation.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

import pm.swt.homeAutomation.viewModel.StatusBarViewModel;


public class StatusBarView extends BaseView
{
    private final int HEIGHT_RATIO = 6;

    private CLabel timeLabel;
    private StatusBarViewModel viewModel;

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
            default:
                break;
            }
        }
    };



    public StatusBarView(Composite parent, StatusBarViewModel viewModel)
    {
        super(parent, SWT.NONE);

        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this.listener);
        this.createComposite(this);
    }



    @Override
    protected void createComposite(Composite parent)
    {
        parent.setLayout(new RowLayout());

        Color backgroundColor = parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
        Color foregroundColor = parent.getDisplay().getSystemColor(SWT.COLOR_WHITE);

        parent.setBackground(backgroundColor);
        parent.setForeground(foregroundColor);

        timeLabel = new CLabel(parent, SWT.None);
        timeLabel.setBackground(backgroundColor);
        timeLabel.setForeground(foregroundColor);

        this.registerResizableControl(timeLabel);
        timeLabel.setText("00:00");
    }



    @Override
    public void onResize()
    {
        Object layoutData = this.getLayoutData();
        if (layoutData instanceof GridData)
        {
            GridData gridData = (GridData) layoutData;
            gridData.heightHint = this.getParent().getBounds().height / HEIGHT_RATIO;

            Point newSize = this.timeLabel.getSize();
            newSize.y = gridData.heightHint;
            newSize.x = gridData.heightHint * 3;
            this.timeLabel.setSize(newSize);
        }

        super.onResize();
    }



    @Override
    public void dispose()
    {
        this.viewModel.removePropertyChangeListener(this.listener);
        this.viewModel.dispose();
        super.dispose();
    }
}
