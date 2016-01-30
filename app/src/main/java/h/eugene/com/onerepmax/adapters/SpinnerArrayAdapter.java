package h.eugene.com.onerepmax.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import h.eugene.com.onerepmax.R;

public class SpinnerArrayAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] reps;

    public SpinnerArrayAdapter(Context context, int textViewResourceId, String[] reps) {
        super(context, textViewResourceId, reps);
        this.context = context;
        this.reps = reps;
    }

    // Not expanded
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder.ViewHolderSpinner viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.spinner_state_closed, parent, false);
            viewHolder = new ListViewHolder.ViewHolderSpinner();
            viewHolder.textReps = (TextView) convertView.findViewById(R.id.textReps);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ListViewHolder.ViewHolderSpinner) convertView.getTag();
        }
        viewHolder.textReps.setText(reps[position]);
        return convertView;
    }

    // Expanded
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ListViewHolder.ViewHolderSpinner viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.spinner_state_open, parent, false);
            viewHolder = new ListViewHolder.ViewHolderSpinner();
            viewHolder.textReps = (TextView) convertView.findViewById(R.id.textReps);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ListViewHolder.ViewHolderSpinner) convertView.getTag();
        }
        viewHolder.textReps.setText(reps[position]);
        return convertView;
    }

}