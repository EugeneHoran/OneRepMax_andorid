package h.eugene.com.onerepmax.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import h.eugene.com.onerepmax.R;
import h.eugene.com.onerepmax.util.ORMPreferenceManager;

public class MaxWeightsListAdapter extends ArrayAdapter<String> {
    private Context context;
    private ORMPreferenceManager ormPreferenceManager;
    private ArrayList<String> weights;
    private final String[] percentages = {"100%", "95%", "90%", "88%", "86%", "83%", "80%", "78%", "76%", "75%", "72%", "70%"};

    public MaxWeightsListAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.list_max_weights, values);
        this.context = context;
        ormPreferenceManager = new ORMPreferenceManager(context);
        this.weights = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder.ViewHolderItems viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_max_weights, parent, false);
            viewHolder = new ListViewHolder.ViewHolderItems();
            viewHolder.textWeight = (TextView) convertView.findViewById(R.id.textWeight);
            viewHolder.textMaxReps = (TextView) convertView.findViewById(R.id.textMaxReps);
            viewHolder.textMaxPercentage = (TextView) convertView.findViewById(R.id.textMaxPercentage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ListViewHolder.ViewHolderItems) convertView.getTag();
        }
        // Set weight
        if (weights.get(position) != null) {
            viewHolder.textWeight.setText(String.format("%s %s", weights.get(position), ormPreferenceManager.settingsGetUnits()));
        } else {
            viewHolder.textWeight.setText(String.format("%d %s", 0, ormPreferenceManager.settingsGetUnits()));
        }
        // Set reps number
        viewHolder.textMaxReps.setText(String.format("%d Rep Max", (position + 1)));
        // Set percentage
        viewHolder.textMaxPercentage.setText(percentages[position]);
        return convertView;
    }
}