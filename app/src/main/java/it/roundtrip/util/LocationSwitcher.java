package it.roundtrip.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import it.roundtrip.dao.Dao;
import it.roundtrip.pojo.Coordinate;

import static it.roundtrip.util.Constants.INDEX;
import static it.roundtrip.util.Constants.LABEL;

public class LocationSwitcher extends Service {

    private boolean active = false;
    private Dao dao = Dao.getInstance();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!active) {
            final String cityKey = intent.getExtras().getString(LABEL);
            final int startIndex = intent.getExtras().getInt(INDEX);

            final int cityIndex = dao.cityIndex(cityKey);
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                private int i = startIndex;
                public void run() {
                    final List<Coordinate> coordinates = dao.getCoordinates();
                    MyLocationManager mlc = new MyLocationManager(getApplicationContext());
                    mlc.mockLocation(dao.getCities().get(cityIndex), coordinates.get(i));
                    i = (i+1) % coordinates.size();
                }
            }, dao.getCities().get(cityIndex).getDelay(), dao.getCities().get(cityIndex).getCooldown() * 1000);

            active = true;
        } else {
            Toast.makeText(getApplicationContext(), "Service already started, kill the app.", Toast.LENGTH_LONG).show();
        }

        return Service.START_NOT_STICKY;
    }

}