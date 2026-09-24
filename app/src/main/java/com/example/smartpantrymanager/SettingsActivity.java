package com.example.smartpantrymanager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends Activity {
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        Switch expiryAlerts = findViewById(R.id.expiryAlertsSwitch);
        expiryAlerts.setChecked(prefs.getBoolean("expiry_alerts", true));
        expiryAlerts.setOnCheckedChangeListener((buttonView, isChecked) -> prefs.edit().putBoolean("expiry_alerts", isChecked).apply());

        Spinner daysSpinner = findViewById(R.id.warningDaysSpinner);
        String[] days = {"1 day", "3 days", "7 days"};
        daysSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days));
        int stored = prefs.getInt("expiry_days", 3);
        daysSpinner.setSelection(stored == 1 ? 0 : stored == 7 ? 2 : 1);
        daysSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                int daysValue = position == 0 ? 1 : position == 2 ? 7 : 3;
                prefs.edit().putInt("expiry_days", daysValue).apply();
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        Spinner unitSpinner = findViewById(R.id.preferenceUnitSpinner);
        String[] units = {"Metric", "Pieces where appropriate"};
        unitSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units));
        unitSpinner.setSelection(prefs.getInt("preferred_unit", 0));
        unitSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                prefs.edit().putInt("preferred_unit", position).apply();
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        findViewById(R.id.navPantry).setOnClickListener(v -> { startActivity(new android.content.Intent(this, MainActivity.class)); });
        findViewById(R.id.navSuggestions).setOnClickListener(v -> { startActivity(new android.content.Intent(this, SuggestedRecipesActivity.class)); });
        findViewById(R.id.navSettings).setOnClickListener(v -> Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show());
    }
}
