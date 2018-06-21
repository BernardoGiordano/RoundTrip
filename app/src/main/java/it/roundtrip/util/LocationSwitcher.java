package it.roundtrip.util;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import it.roundtrip.dao.Dao;
import it.roundtrip.pojo.Coordinate;
import it.roundtrip.util.MyLocationManager;
import it.roundtrip.util.MyRunnable;

import static it.roundtrip.util.Constants.INDEX;
import static it.roundtrip.util.Constants.LABEL;

public class LocationSwitcher extends Service {

    private Dao dao = Dao.getInstance();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        final String cityKey = intent.getExtras().getString(LABEL);
        final int startIndex = intent.getExtras().getInt(INDEX);

        final int cityIndex = dao.cityIndex(cityKey);
        final Handler handler = new Handler();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                MyLocationManager mlc = new MyLocationManager(getApplicationContext());
                final List<Coordinate> c = dao.getCoordinates();
                handler.post(new MyRunnable(c, cityIndex, getApplicationContext()));
            }
        }, dao.getCities().get(cityIndex).getDelay(), dao.getCities().get(cityIndex).getCooldown() * 1000);

        return Service.START_NOT_STICKY;
    }
}