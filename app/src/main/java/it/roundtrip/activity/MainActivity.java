package it.roundtrip.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.roundtrip.adapter.LocationsAdapter;
import it.roundtrip.R;
import it.roundtrip.dao.Dao;

import static it.roundtrip.util.Constants.LABEL;

public class MainActivity extends AppCompatActivity {

    private ListView cityList;
    private FloatingActionButton addCityButton;

    private LocationsAdapter locationsAdapter;

    private Dao dao = Dao.getInstance();

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            cityList = findViewById(R.id.location_list);
            addCityButton = findViewById(R.id.addCityButton);
            locationsAdapter = new LocationsAdapter(this);

            dao.beginCitiesObservation(new Dao.UpdateListener() {
                @Override
                public void citiesUpdated() {
                    locationsAdapter.update(dao.getCities());
                }

                @Override
                public void coordinatesUpdated() {

                }
            });

            cityList.setAdapter(locationsAdapter);

            addCityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AddCityActivity.class);
                    startActivity(intent);
                }
            });

            cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, CoordinateActivity.class);
                    intent.putExtra(LABEL, dao.getCities().get(position).getKey());
                    startActivity(intent);
                }
            });

            cityList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("What to do with\n" + dao.getCities().get(position).getName() + "?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dao.deleteCity(dao.getCities().get(position).getKey());
                        }
                    });
                    builder.create().show();
                    return true;
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.removeCityListener();
    }
}
