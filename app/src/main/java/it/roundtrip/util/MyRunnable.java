package it.roundtrip.util;

import android.content.Context;

import java.util.List;

import it.roundtrip.dao.Dao;
import it.roundtrip.pojo.Coordinate;

public class MyRunnable implements Runnable {
    private static int i = -1;
    private final int cityIndex;
    private final int size;
    private final List<Coordinate> c;
    private final Context context;

    private Dao dao = Dao.getInstance();

    public MyRunnable(List<Coordinate> c, int cityIndex, Context context) {
        this.c = c;
        this.size = c.size();
        this.cityIndex = cityIndex;
        this.context = context;
    }

    @Override
    public void run() {
        if (!c.isEmpty() && i >= -1) {
            i = (i+1) % size;
            MyLocationManager manager = new MyLocationManager(context);
            manager.mockLocation(dao.getCities().get(cityIndex), c.get(i));
        }
    }
}
