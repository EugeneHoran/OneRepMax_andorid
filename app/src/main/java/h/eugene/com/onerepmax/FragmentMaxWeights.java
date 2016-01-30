package h.eugene.com.onerepmax;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import h.eugene.com.onerepmax.adapters.MaxWeightsListAdapter;
import h.eugene.com.onerepmax.adapters.SpinnerArrayAdapter;
import h.eugene.com.onerepmax.util.Equations;
import h.eugene.com.onerepmax.util.ListView;
import h.eugene.com.onerepmax.util.ORMPreferenceManager;
import h.eugene.com.onerepmax.util.OnDetectScrollListener;

public class FragmentMaxWeights extends Fragment {
    private ORMPreferenceManager ormPreferenceManager;

    private TextView title;
    private EditText editTextAmount;
    private Spinner spinnerReps;
    private MaxWeightsListAdapter maxWeightsListAdapter;
    private ArrayList<String> maxWeights = new ArrayList<>();

    private DecimalFormat df = new DecimalFormat("0.00");
    private int repsPerformed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_max_weights, container, false);
        ormPreferenceManager = new ORMPreferenceManager(getActivity());
        ListView listMaxWeights = (ListView) v.findViewById(R.id.listMaxWeights);
        maxWeightsListAdapter = new MaxWeightsListAdapter(getActivity(), maxWeights);
        listMaxWeights.setAdapter(maxWeightsListAdapter);
        listMaxWeights.setOnDetectScrollListener(new OnDetectScrollListener() { // Listen for scrolls
            @Override
            public void onUpScrolling() {
                mCallbacks.slideUp();
            }

            @Override
            public void onDownScrolling() {
                mCallbacks.slideDown();
            }
        });
        title = (TextView) v.findViewById(R.id.title);
        editTextAmount = (EditText) v.findViewById(R.id.editTextAmount);
        spinnerReps = (Spinner) v.findViewById(R.id.spinnerReps);
        String[] selectedReps = getResources().getStringArray(R.array.reps_array);
        SpinnerArrayAdapter recentTransactionsSpinnerAdapter = new SpinnerArrayAdapter(getActivity(), 0, selectedReps);
        spinnerReps.setAdapter(recentTransactionsSpinnerAdapter);

        spinnerReps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ormPreferenceManager.ormSaveReps(position);
                repsPerformed = position + 2;
                handleEquation();
                title.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextAmount.getText().toString().trim().length() == 3) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editTextAmount.getWindowToken(), 0);
                    title.requestFocus();
                }
                ormPreferenceManager.ormSaveOneRepMax(editTextAmount.getText().toString().trim());
                handleEquation();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editTextAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editTextAmount.getWindowToken(), 0);
                    title.requestFocus();
                    return true;
                }
                return false;
            }
        });
        editTextAmount.setText(ormPreferenceManager.ormGetOneRepMax());
        spinnerReps.setSelection(ormPreferenceManager.ormGetReps());
        return v;
    }

    /**
     * MainActivity Fab pressed
     *
     * @return Weight Lifted, Reps Performed, One Rep Max
     */
    public String[] getWeight() {
        if (editTextAmount.getText().toString().trim().length() > 0) {
            return new String[]{editTextAmount.getText().toString().trim(), String.valueOf(repsPerformed), maxWeights.get(0)};
        } else {
            return new String[]{null, null, null};
        }
    }

    /**
     * History Item Clicked, Set the weight and reps to the saved item
     *
     * @param weight weight lifted
     * @param reps   reps performed
     */
    public void fromHistory(String weight, int reps) {
        editTextAmount.setText(weight);
        spinnerReps.setSelection(reps);
    }

    /**
     * Handle the equations
     */
    private void handleEquation() {
        maxWeights.clear();
        for (int i = 0; i < Equations.percentages.length; i++) {
            if (editTextAmount.getText().toString().trim().length() > 0) {
                maxWeights.add(df.format(Equations.getPercentages(Equations.BrzyckiEquation(Double.valueOf(editTextAmount.getText().toString()), repsPerformed))[i]));
            } else {
                maxWeights.add(df.format(Equations.getPercentages(Equations.BrzyckiEquation(0, repsPerformed))[i]));
            }
        }
        maxWeightsListAdapter.notifyDataSetChanged();
    }

    /**
     * Interface
     */
    private FragmentCallback mCallbacks;

    public interface FragmentCallback {
        void slideUp();

        void slideDown();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (FragmentCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement FragmentMaxWeights");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
