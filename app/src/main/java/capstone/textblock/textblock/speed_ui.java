package capstone.textblock.textblock;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
/**
 * Created by Remzorz on 1/19/2017.
 */

public class speed_ui {

    public interface IBaseGpsListener extends LocationListener, GpsStatus.Listener {

        public void onLocationChanged(Location location);

        public void onProviderDisabled(String provider);

        public void onProviderEnabled(String provider);

        public void onStatusChanged(String provider, int status, Bundle extras);

        public void onGpsStatusChanged(int event);

    }
}
