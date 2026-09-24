package com.example.smartpantrymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class PantryAdapter extends BaseAdapter {
    public interface Listener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private final Context context;
    private final List<PantryItem> items;
    private final Listener listener;

    public PantryAdapter(Context context, List<PantryItem> items, Listener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public void replaceData(List<PantryItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override public int getCount() { return items.size(); }
    @Override public PantryItem getItem(int position) { return items.get(position); }
    @Override public long getItemId(int position) { return items.get(position).getId(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = LayoutInflater.from(context).inflate(R.layout.item_pantry, parent, false);
        PantryItem item = getItem(position);
        TextView name = view.findViewById(R.id.itemName);
        TextView quantity = view.findViewById(R.id.itemQuantity);
        TextView expiry = view.findViewById(R.id.itemExpiry);
        Button edit = view.findViewById(R.id.editButton);
        Button delete = view.findViewById(R.id.deleteButton);

        name.setText(item.getName());
        quantity.setText(String.format(Locale.US, "%.2f %s", item.getQuantity(), UnitConverter.normalizeUnitLabel(item.getUnit())));
        expiry.setText(item.getExpiryDate() == null || item.getExpiryDate().isEmpty() ? "No expiry date" : "Expires: " + item.getExpiryDate());
        edit.setOnClickListener(v -> listener.onEdit(item));
        delete.setOnClickListener(v -> listener.onDelete(item));
        return view;
    }
}
