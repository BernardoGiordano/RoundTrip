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

import it.roundtrip.R;
import it.roundtrip.adapter.CoordinatesAdapter;
import it.roundtrip.dao.Dao;
import it.roundtrip.util.LocationSwitcher;

import static it.roundtrip.util.Constants.INDEX;
import static it.roundtrip.util.Constants.LABEL;

public class CoordinateActivity extends AppCompatActivity {

    private String cityKey;
    private ListView coordinateList;
    private FloatingActionButton addCoordinateButton;

    private CoordinatesAdapter coordinatesAdapter;

    private Dao dao = Dao.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinate);

        Bundle extras = getIntent().getExtras();
        cityKey = extras.getString(LABEL);

        coordinateList = findViewById(R.id.location_list);
        addCoordinateButton = findViewById(R.id.addCityButton);
        coordinatesAdapter = new CoordinatesAdapter(this);

        dao.beginCoordinatesObservation(new Dao.UpdateListener() {
            @Override
            public void citiesUpdated() {

            }

            @Override
            public void coordinatesUpdated() {
                coordinatesAdapter.update(dao.getCoordinates());
            }
        }, cityKey);

        coordinateList.setAdapter(coordinatesAdapter);

        coordinateList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("What to do with\n" + dao.getCoordinates().get(position).getName() + "?");
                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dao.deleteCoordinate(cityKey, dao.getCoordinates().get(position).getKey());
                    }
                });

                builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(builder.getContext(), LocationSwitcher.class);
                        intent.putExtra(LABEL, cityKey);
                        intent.putExtra(INDEX, position);
                        getApplicationContext().startService(intent);
                    }
                });
                builder.create().show();
                return true;
            }
        });

        addCoordinateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoordinateActivity.this, AddCoordinateActivity.class);
                intent.putExtra(LABEL, cityKey);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.removeCoordinateListener(cityKey);
    }
}
