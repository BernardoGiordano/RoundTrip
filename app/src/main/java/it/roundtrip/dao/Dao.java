package it.roundtrip.dao;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.roundtrip.util.DistanceFromMeComparator;
import it.roundtrip.pojo.City;
import it.roundtrip.pojo.Coordinate;

public class Dao {

    private static final String KEY_CITIES = "cities";
    private static final String KEY_COORDINATES = "coordinates";

    private List<City> cities;
    private List<Coordinate> coordinates;

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static Dao dao = null;

    private ValueEventListener cityListener;
    private ValueEventListener coordinateListener;

    public interface UpdateListener {
        void citiesUpdated();
        void coordinatesUpdated();
    }

    private Dao() {
        cities = new ArrayList<>();
        coordinates = new ArrayList<>();
    }

    public static Dao getInstance() {
        if (dao == null) {
            dao = new Dao();
            database.setPersistenceEnabled(true);
        }
        return dao;
    }

    public void beginCitiesObservation(final UpdateListener notification) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = database.getReference(user.getUid()).child(KEY_CITIES);

        cityListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cities.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    City city = new City();

                    city.setKey(item.getKey());
                    city.setName(item.child("name").getValue(String.class));
                    city.setCooldown(item.child("cooldown").getValue(Integer.class));

                    cities.add(city);
                }

                Collections.sort(cities, new Comparator<City>() {
                    @Override
                    public int compare(City o1, City o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                notification.citiesUpdated();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref.addValueEventListener(cityListener);
    }

    public void beginCoordinatesObservation(final UpdateListener notification, final String cityKey) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = database.getReference(user.getUid()).child(KEY_COORDINATES).child(cityKey);

        coordinateListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                coordinates.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Coordinate c = new Coordinate();

                    c.setKey(item.getKey());
                    c.setName(item.child("name").getValue(String.class));
                    c.setLatitude(item.child("latitude").getValue(Double.class));
                    c.setLongitude(item.child("longitude").getValue(Double.class));
                    c.setAltitude(item.child("altitude").getValue(Double.class));

                    coordinates.add(c);
                }

                if (!coordinates.isEmpty()) {
                    final Coordinate me = coordinates.get(0);
                    Collections.sort(coordinates, new DistanceFromMeComparator(me));
                }

                notification.coordinatesUpdated();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref.addValueEventListener(coordinateListener);
    }

    public void removeCityListener() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (cityListener != null) {
            FirebaseDatabase.getInstance().getReference(user.getUid()).child(KEY_CITIES).removeEventListener(cityListener);
        }
    }

    public void removeCoordinateListener(String cityKey) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (cityListener != null) {
            FirebaseDatabase.getInstance().getReference(user.getUid()).child(KEY_COORDINATES).child(cityKey).removeEventListener(cityListener);
        }
    }

    public void addCity(City city) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String key = database.getReference(user.getUid())
                .child(KEY_CITIES)
                .push()
                .getKey();
        city.setKey(key);

        DatabaseReference ref = database.getReference(user.getUid())
                .child(KEY_CITIES)
                .child(key);
        ref.setValue(city);
    }

    public void deleteCity(final String key) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = database.getReference(user.getUid())
                .child(KEY_CITIES)
                .child(key);
        ref.removeValue();

        ref = database.getReference(user.getUid())
                .child(KEY_COORDINATES)
                .child(key);
        ref.removeValue();
    }

    public void deleteCoordinate(final String cityKey, final String coordinateKey) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = database.getReference(user.getUid())
                .child(KEY_COORDINATES)
                .child(cityKey)
                .child(coordinateKey);
        ref.removeValue();
    }

    public void addCoordinate(Coordinate coordinate, String cityKey) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String key = database.getReference(user.getUid())
                .child(KEY_COORDINATES)
                .child(cityKey)
                .push()
                .getKey();
        coordinate.setKey(key);

        DatabaseReference ref = database.getReference(user.getUid())
                .child(KEY_COORDINATES)
                .child(cityKey)
                .child(key);
        ref.setValue(coordinate);
    }

    public List<City> getCities() {
        return cities;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public int cityIndex(String key) {
        int i = 0;
        while (i < cities.size()) {
            if (cities.get(i).getKey().equals(key))
                return i;
            i++;
        }
        return -1;
    }

}
