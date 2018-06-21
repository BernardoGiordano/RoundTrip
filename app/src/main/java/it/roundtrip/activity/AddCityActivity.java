package it.roundtrip.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import it.roundtrip.R;
import it.roundtrip.dao.Dao;
import it.roundtrip.pojo.City;

public class AddCityActivity extends AppCompatActivity {

    private EditText cityNameText;
    private EditText cooldownText;
    private Button button;

    private Dao dao = Dao.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        cityNameText = findViewById(R.id.addCityNameText);
        cooldownText = findViewById(R.id.addCityCooldownText);
        button = findViewById(R.id.addCityButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = cityNameText.getText().toString();
                String cooldownString = cooldownText.getText().toString();

                int cooldown = 60;
                if (name.isEmpty()) {
                    cityNameText.setError("Mandatory field!");
                    return;
                } else if (!cooldownString.isEmpty()) {
                    cooldown = Integer.parseInt(cooldownString);
                }

                dao.addCity(new City(null, name, cooldown));
                Intent intent = new Intent(AddCityActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
