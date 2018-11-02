package pm.swt.homeAutomation.model;

import pm.swt.homeAutomation.utils.StationStatus;


public interface ITrackableStation extends IBindableBase
{
    public static final String REFRESH_INTERVAL_PROP_NAME = "refreshInterval";
    public static final String STATUS_PROP_NAME = "status";



    public int getRefreshInterval();



    public void setRefreshInterval(int refreshInterval);



    public void setStatus(StationStatus status);



    public StationStatus getStatus();
}
