package it.roundtrip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import it.roundtrip.R;
import it.roundtrip.pojo.City;

public class LocationsAdapter extends BaseAdapter {

    private List<City> cities = Collections.emptyList();
    private Context context;

    public LocationsAdapter(Context context) {
        this.context = context;
    }

    public void update(List<City> list) {
        cities = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int position) {
        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.location_row, parent, false);
        }

        TextView locationText = convertView.findViewById(R.id.locationText);

        City city = cities.get(position);
        locationText.setText(city.getName());

        return convertView;
    }
}
