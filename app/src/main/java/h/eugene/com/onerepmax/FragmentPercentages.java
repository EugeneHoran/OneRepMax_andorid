package h.eugene.com.onerepmax;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import h.eugene.com.onerepmax.adapters.PercentagesListAdapter;
import h.eugene.com.onerepmax.util.ORMPreferenceManager;

public class FragmentPercentages extends Fragment {
    private EditText editTextAmount;
    private PercentagesListAdapter percentagesListAdapter;
    private ArrayList<String> percentagesString = new ArrayList<>();
    private ArrayList<Double> percentagesWeight = new ArrayList<>();
    private ORMPreferenceManager ormPreferenceManager;
    private DecimalFormat df = new DecimalFormat("0");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_percentages, container, false);
        ormPreferenceManager = new ORMPreferenceManager(getActivity());
        editTextAmount = (EditText) v.findViewById(R.id.editTextAmount);
        ListView listPercentages = (ListView) v.findViewById(R.id.listPercentages);
        percentagesListAdapter = new PercentagesListAdapter(getActivity(), percentagesString, percentagesWeight);
        listPercentages.setAdapter(percentagesListAdapter);
        editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextAmount.getText().toString().trim().length() == 3) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editTextAmount.getWindowToken(), 0);
                }
                ormPreferenceManager.percentageSaveOneRepMax(editTextAmount.getText().toString().trim());
                handleEquation();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editTextAmount.setText(ormPreferenceManager.percentageGetOneRepMax());
        return v;
    }

    /**
     * Handle the equations
     */
    private void handleEquation() {
        percentagesString.clear();
        percentagesWeight.clear();
        double[] percentages = new double[25];
        for (int i = 0; i < percentages.length; i++) {
            if (i == 0) {
                percentages[i] = 1.25;
            } else {
                percentages[i] = percentages[i - 1] - .05;
            }
            if (editTextAmount.getText().toString().trim().length() > 0) {
                percentagesWeight.add(Double.valueOf(editTextAmount.getText().toString()) * percentages[i]);
                percentagesString.add(df.format(percentages[i] * 100) + "%");
            } else {
                percentagesWeight.add(0 * percentages[i]);
                percentagesString.add(df.format(percentages[i] * 100) + "%");
            }
        }
        percentagesListAdapter.notifyDataSetChanged();
    }
}
