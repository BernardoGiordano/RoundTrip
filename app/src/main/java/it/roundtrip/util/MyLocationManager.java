package it.roundtrip.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Toast;

import it.roundtrip.pojo.City;
import it.roundtrip.pojo.Coordinate;

import static android.content.Context.LOCATION_SERVICE;

public class MyLocationManager {

    private Context context;

    public MyLocationManager(Context context) {
        this.context = context;
    }

    public void mockLocation(final City city, final Coordinate coordinate) {
        LocationManager lm = (android.location.LocationManager)context.getSystemService(LOCATION_SERVICE);
        String mocLocationProvider = android.location.LocationManager.GPS_PROVIDER;

        lm.addTestProvider(mocLocationProvider, false, false,
                false, false, true, true, true, 0, 5);
        lm.setTestProviderEnabled(mocLocationProvider, true);

        Location mockLocation = new Location(mocLocationProvider);
        mockLocation.setLatitude(coordinate.getLatitude());
        mockLocation.setLongitude(coordinate.getLongitude());
        mockLocation.setAltitude(coordinate.getAltitude());
        mockLocation.setTime(System.currentTimeMillis());
        mockLocation.setAccuracy(5);
        mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        lm.setTestProviderLocation(mocLocationProvider, mockLocation);

        // supposing Toast.LENGTH_SHORT is ~2000ms
        for (int i = 0; i < (city.getCooldown() * 1000) / 2000; i++) {
            final int step = i;
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(context, coordinate.getName() + "... " + Integer.toString(city.getCooldown() - 2*step) + "s", Toast.LENGTH_SHORT).show();
                    }
                }
            );
        }
    }

}
