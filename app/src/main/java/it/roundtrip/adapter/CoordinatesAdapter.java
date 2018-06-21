package it.roundtrip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import android.widget.BaseAdapter;
import it.roundtrip.R;
import it.roundtrip.pojo.Coordinate;

public class CoordinatesAdapter extends BaseAdapter {

    private List<Coordinate> coordinates = Collections.emptyList();
    private Context context;

    public CoordinatesAdapter(Context context) {
        this.context = context;
    }

    public void update(List<Coordinate> list) {
        coordinates = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return coordinates.size();
    }

    @Override
    public Object getItem(int position) {
        return coordinates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.coordinate_row, parent, false);
        }

        TextView locationText = convertView.findViewById(R.id.coordinateNameText);
        TextView latitudeText = convertView.findViewById(R.id.latitudeTextView);
        TextView longitudeText = convertView.findViewById(R.id.longitudeTextView);

        Coordinate c = coordinates.get(position);
        locationText.setText(c.getName());
        latitudeText.setText(Double.toString(c.getLatitude()));
        longitudeText.setText(Double.toString(c.getLongitude()));

        return convertView;
    }
}
