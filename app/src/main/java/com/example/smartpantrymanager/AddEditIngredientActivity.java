package com.example.smartpantrymanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditIngredientActivity extends Activity {
    private DatabaseHelper databaseHelper;
    private EditText nameInput;
    private EditText quantityInput;
    private Spinner unitSpinner;
    private TextView expiryValue;
    private long ingredientId = -1;

    private final String[] units = {"pieces", "g", "kg", "ml", "L", "tsp", "tbsp", "cups"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);
        databaseHelper = new DatabaseHelper(this);
        nameInput = findViewById(R.id.ingredientNameInput);
        quantityInput = findViewById(R.id.quantityInput);
        unitSpinner = findViewById(R.id.unitSpinner);
        expiryValue = findViewById(R.id.expiryValue);

        unitSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units));
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        findViewById(R.id.expiryButton).setOnClickListener(v -> chooseDate());
        findViewById(R.id.clearExpiryButton).setOnClickListener(v -> expiryValue.setText("No expiry date"));
        findViewById(R.id.saveButton).setOnClickListener(v -> saveIngredient());
        findViewById(R.id.deleteButton).setOnClickListener(v -> deleteIngredient());

        ingredientId = getIntent().getLongExtra("ingredient_id", -1);
        if (ingredientId != -1) {
            ((TextView)findViewById(R.id.screenTitle)).setText("Edit Ingredient");
            findViewById(R.id.deleteButton).setVisibility(TextView.VISIBLE);
            loadExisting();
        } else {
            ((TextView)findViewById(R.id.screenTitle)).setText("Add Ingredient");
            findViewById(R.id.deleteButton).setVisibility(TextView.GONE);
        }
    }

    private void loadExisting() {
        PantryItem item = databaseHelper.getPantryItem(ingredientId);
        if (item == null) { Toast.makeText(this, "Ingredient no longer exists", Toast.LENGTH_SHORT).show(); finish(); return; }
        nameInput.setText(item.getName());
        quantityInput.setText(String.valueOf(item.getQuantity()));
        expiryValue.setText(item.getExpiryDate() == null || item.getExpiryDate().isEmpty() ? "No expiry date" : item.getExpiryDate());
        for (int i = 0; i < units.length; i++) {
            if (units[i].equalsIgnoreCase(UnitConverter.normalizeUnitLabel(item.getUnit()))) {
                unitSpinner.setSelection(i);
                break;
            }
        }
    }

    private void chooseDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, day);
            expiryValue.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(selected.getTime()));
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        Calendar minDay = Calendar.getInstance();
        minDay.set(Calendar.HOUR_OF_DAY, 0);
        minDay.set(Calendar.MINUTE, 0);
        minDay.set(Calendar.SECOND, 0);
        minDay.set(Calendar.MILLISECOND, 0);
        dialog.getDatePicker().setMinDate(minDay.getTimeInMillis());
        dialog.show();
    }

    private void saveIngredient() {
        String name = nameInput.getText().toString().trim();
        String quantityText = quantityInput.getText().toString().trim();
        if (name.isEmpty()) { nameInput.setError("Ingredient name is required"); return; }
        if (quantityText.isEmpty()) { quantityInput.setError("Quantity is required"); return; }

        double quantity;
        try { quantity = Double.parseDouble(quantityText); }
        catch (NumberFormatException e) { quantityInput.setError("Enter a valid number"); return; }
        if (!Double.isFinite(quantity) || quantity <= 0) { quantityInput.setError("Enter a finite quantity greater than zero"); return; }

        String unit = units[unitSpinner.getSelectedItemPosition()];
        String expiry = expiryValue.getText().toString();
        if (expiry.equals("No expiry date")) expiry = "";
        PantryItem item = new PantryItem(
                ingredientId,
                name,
                IngredientNormalizer.canonicalize(name),
                quantity,
                unit,
                expiry);
        long result;
        if (ingredientId == -1) result = databaseHelper.insertPantryItem(item);
        else result = databaseHelper.updatePantryItem(item);

        if (result <= 0) {
            Toast.makeText(this, "Unable to save ingredient", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Ingredient saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void deleteIngredient() {
        if (ingredientId == -1) return;
        new android.app.AlertDialog.Builder(this)
                .setTitle("Delete Ingredient")
                .setMessage("Delete this pantry item?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (databaseHelper.deletePantryItem(ingredientId) > 0) {
                        Toast.makeText(this, "Ingredient deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Ingredient was not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
