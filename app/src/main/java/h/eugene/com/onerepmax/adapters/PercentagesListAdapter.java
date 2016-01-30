package h.eugene.com.onerepmax.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import h.eugene.com.onerepmax.R;
import h.eugene.com.onerepmax.util.ORMPreferenceManager;

public class PercentagesListAdapter extends ArrayAdapter<String> {
    private Context context;
    private ORMPreferenceManager ormPreferenceManager;
    private ArrayList<String> percentages;
    private ArrayList<Double> weights;
    private final DecimalFormat df = new DecimalFormat("0.00");

    public PercentagesListAdapter(Context context, ArrayList<String> percentages, ArrayList<Double> weights) {
        super(context, R.layout.list_percentages, percentages);
        this.context = context;
        ormPreferenceManager = new ORMPreferenceManager(context);
        this.percentages = percentages;
        this.weights = weights;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder.ViewHolderItems viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_percentages, parent, false);
            viewHolder = new ListViewHolder.ViewHolderItems();
            viewHolder.textMaxPercentage = (TextView) convertView.findViewById(R.id.textMaxPercentage);
            viewHolder.textWeight = (TextView) convertView.findViewById(R.id.textWeight);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ListViewHolder.ViewHolderItems) convertView.getTag();
        }
        viewHolder.textMaxPercentage.setText(percentages.get(position));
        if (weights.get(position) != 0) {
            viewHolder.textWeight.setText(String.format("%s %s", df.format(weights.get(position)), ormPreferenceManager.settingsGetUnits()));
        } else {
            viewHolder.textWeight.setText(String.format("%d %s", 0, ormPreferenceManager.settingsGetUnits()));
        }
        return convertView;
    }
}