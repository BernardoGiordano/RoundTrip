package it.roundtrip.util;

import java.util.Comparator;

import it.roundtrip.pojo.Coordinate;

public class DistanceFromMeComparator implements Comparator<Coordinate> {
    private Coordinate me;

    public DistanceFromMeComparator(Coordinate me) {
        this.me = me;
    }

    private Double distanceFromMe(Coordinate p) {
        double theta = p.getLatitude() - me.getLongitude();
        double dist = Math.sin(deg2rad(p.getLatitude())) * Math.sin(deg2rad(me.getLatitude()))
                + Math.cos(deg2rad(p.getLatitude())) * Math.cos(deg2rad(me.getLatitude()))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        return dist;
    }

    private double deg2rad(double deg) { return (deg * Math.PI / 180.0); }
    private double rad2deg(double rad) { return (rad * 180.0 / Math.PI); }

    @Override
    public int compare(Coordinate p1, Coordinate p2) {
        return distanceFromMe(p1).compareTo(distanceFromMe(p2));
    }
}