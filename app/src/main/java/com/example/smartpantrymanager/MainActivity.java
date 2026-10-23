package com.example.smartpantrymanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private DatabaseHelper databaseHelper;
    private PantryAdapter adapter;
    private TextView emptyMessage;
    private final List<PantryItem> pantryItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        setupRecyclerReplacementList();
        setupNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems();
        showExpiryAlertIfNeeded();
    }

    private void setupRecyclerReplacementList() {
        ListView listView = findViewById(R.id.pantryListView);
        emptyMessage = findViewById(R.id.emptyMessage);
        adapter = new PantryAdapter(this, pantryItems, new PantryAdapter.Listener() {
            @Override public void onEdit(PantryItem item) {
                Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
                intent.putExtra("ingredient_id", item.getId());
                startActivity(intent);
            }
            @Override public void onDelete(PantryItem item) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Ingredient")
                        .setMessage("Delete " + item.getName() + " from your pantry?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            databaseHelper.deletePantryItem(item.getId());
                            loadPantryItems();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        listView.setAdapter(adapter);
    }

    private void loadPantryItems() {
        if (adapter == null) return;
        adapter.replaceData(databaseHelper.getAllPantryItems());
        boolean empty = adapter.getCount() == 0;
        emptyMessage.setVisibility(empty ? View.VISIBLE : View.GONE);
        findViewById(R.id.pantryListView).setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    private void setupNavigation() {
        Button add = findViewById(R.id.addIngredientButton);
        add.setOnClickListener(v -> startActivity(new Intent(this, AddEditIngredientActivity.class)));
        findViewById(R.id.navPantry).setOnClickListener(v -> { });
        findViewById(R.id.navSuggestions).setOnClickListener(v -> startActivity(new Intent(this, SuggestedRecipesActivity.class)));
        findViewById(R.id.navSettings).setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void showExpiryAlertIfNeeded() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        if (!prefs.getBoolean("expiry_alerts", true)) return;
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        String shownDate = prefs.getString("alert_shown_date", "");
        if (today.equals(shownDate)) return;
        int days = prefs.getInt("expiry_days", 3);
        List<String> expiring = databaseHelper.getExpiringSoonItems(days);
        if (expiring.isEmpty()) return;
        StringBuilder message = new StringBuilder("These items are expiring soon:\n\n");
        for (String item : expiring) message.append("• ").append(item).append("\n");
        new AlertDialog.Builder(this)
                .setTitle("Expiring Soon")
                .setMessage(message.toString())
                .setPositiveButton("OK", null)
                .show();
        prefs.edit().putString("alert_shown_date", today).apply();
    }
}
