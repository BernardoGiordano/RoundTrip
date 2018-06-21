package it.roundtrip.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import it.roundtrip.R;
import it.roundtrip.dao.Dao;
import it.roundtrip.pojo.Coordinate;

import static it.roundtrip.util.Constants.LABEL;

public class AddCoordinateActivity extends AppCompatActivity {

    private String cityKey;
    private EditText locationEditText;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private EditText altitudeEditText;
    private Button button;

    private Dao dao = Dao.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coordinate);

        Bundle extras = getIntent().getExtras();
        cityKey = extras.getString(LABEL);

        locationEditText = findViewById(R.id.coordinateEditText);
        latitudeEditText = findViewById(R.id.latitudeEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);
        altitudeEditText = findViewById(R.id.altitudeEditText);
        button = findViewById(R.id.addCoordinateButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = locationEditText.getText().toString();
                String latitudeString = latitudeEditText.getText().toString();
                String longitudeString = longitudeEditText.getText().toString();
                String altitudeString = altitudeEditText.getText().toString();

                if (name.isEmpty()) {
                    locationEditText.setError("Mandatory field!");
                    return;
                } else if (latitudeString.isEmpty()) {
                    latitudeEditText.setError("Mandatory field!");
                    return;
                } else if (longitudeString.isEmpty()) {
                    longitudeEditText.setError("Mandatory field!");
                    return;
                }

                Double latitude = Double.parseDouble(latitudeString);
                Double longitude = Double.parseDouble(longitudeString);
                Double altitude = altitudeString.isEmpty() ? 0 : Double.parseDouble(altitudeString);

                dao.addCoordinate(new Coordinate(name, latitude, longitude, altitude), cityKey);
                Intent intent = new Intent(AddCoordinateActivity.this, CoordinateActivity.class);
                intent.putExtra(LABEL, cityKey);
                startActivity(intent);
            }
        });
    }
}
