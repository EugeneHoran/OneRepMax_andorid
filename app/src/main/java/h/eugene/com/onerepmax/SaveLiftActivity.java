package h.eugene.com.onerepmax;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import h.eugene.com.onerepmax.modelsdata.LiftDate;
import h.eugene.com.onerepmax.modelsdata.LiftDateHandler;
import h.eugene.com.onerepmax.modelsdata.Lifts;
import h.eugene.com.onerepmax.modelsdata.LiftsHandler;
import h.eugene.com.onerepmax.modelsdata.OneRepMax;
import h.eugene.com.onerepmax.modelsdata.OneRepMaxHandler;
import h.eugene.com.onerepmax.util.DateCompare;
import h.eugene.com.onerepmax.util.ORMPreferenceManager;

public class SaveLiftActivity extends AppCompatActivity {
    private String liftWeightLifted;
    private String liftRepsPerformed;
    private String liftOneRepMax;
    private ORMPreferenceManager ormPreferenceManager;
    private String selectedLift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_lift);
        ormPreferenceManager = new ORMPreferenceManager(this);
        Bundle bundleLift = getIntent().getExtras();
        if (bundleLift != null) {
            liftWeightLifted = bundleLift.getString(MainActivity.LIFT_WEIGHT_LIFTED);
            liftRepsPerformed = bundleLift.getString(MainActivity.LIFT_REPS_PERFORMED);
            liftOneRepMax = bundleLift.getString(MainActivity.LIFT_ONE_REP_MAX);
        }
        findViews();
    }


    private void findViews() {
        ORMPreferenceManager ormPreferenceManager = new ORMPreferenceManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Save Lift");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLift();
            }
        });
        ImageView addLift = (ImageView) findViewById(R.id.addLift);
        TextView editTextAmount = (TextView) findViewById(R.id.editTextAmount);
        TextView amountReps = (TextView) findViewById(R.id.amountReps);
        TextView orm = (TextView) findViewById(R.id.orm);
        editTextAmount.setText(String.format("%s %s", liftWeightLifted, ormPreferenceManager.settingsGetUnits()));
        amountReps.setText(String.format("%s Reps", liftRepsPerformed));
        orm.setText(String.format("%s %s", liftOneRepMax, ormPreferenceManager.settingsGetUnits()));

        addLift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.dialog_add_lift, null);
                final EditText enterLift = (EditText) view.findViewById(R.id.enterLift);

                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Create New Lift");
                dialog.setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (enterLift.getText().length() > 0) {
                            saveNewLift(enterLift.getText().toString().trim());
                        } else {
                            Toast.makeText(SaveLiftActivity.this, "Lift not saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.setNegativeButton("cancel", null);
                dialog.setView(view);
                dialog.show();
            }
        });
        setupLiftListAdapter();
    }

    private void setupLiftListAdapter() {
        final ArrayList<Lifts> savedLifts = LiftsHandler.get(this).getLifts();
        String[] originalLifts = getResources().getStringArray(R.array.lifts);
        final ArrayList<String> allLiftOptions = new ArrayList<>();
        Collections.addAll(allLiftOptions, originalLifts);
        for (int i = 0; i < savedLifts.size(); i++) {
            allLiftOptions.add(savedLifts.get(i).getLift());
        }

        final ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, android.R.id.text1, allLiftOptions);
        final ListView listSavedLifts = (ListView) findViewById(R.id.listSavedLifts);
        listSavedLifts.setAdapter(listAdapter);
        listSavedLifts.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listSavedLifts.setItemChecked(ormPreferenceManager.getLiftPosition(), true);
        listSavedLifts.smoothScrollToPosition(ormPreferenceManager.getLiftPosition());
        listSavedLifts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedLift = listAdapter.getItem(position);
                ormPreferenceManager.saveLiftPosition(position);
            }
        });
        listSavedLifts.performItemClick(listSavedLifts.getAdapter().getView(ormPreferenceManager.getLiftPosition(), null, null), ormPreferenceManager.getLiftPosition(), ormPreferenceManager.getLiftPosition());
        listSavedLifts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position > 2) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Lifts oneRepMax = savedLifts.get(position - 3);
                                    allLiftOptions.remove(position);
                                    LiftsHandler.get(SaveLiftActivity.this).deleteLift(oneRepMax);
                                    listAdapter.notifyDataSetChanged();
                                    listSavedLifts.performItemClick(listSavedLifts.getAdapter().getView(0, null, null), 0, 0);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void saveNewLift(String name) {
        Lifts lift = new Lifts();
        lift.setLift(name);
        LiftsHandler.get(this).addLift(lift);
        LiftsHandler.get(this).saveLifts();
        setupLiftListAdapter();
        Toast.makeText(this, "Lift Saved", Toast.LENGTH_SHORT).show();
    }

    private void saveLift() {
        if (LiftDateHandler.get(this).getDate().size() == 0) {
            LiftDate liftDate = new LiftDate();
            liftDate.setDate(new Date());
            LiftDateHandler.get(this).addDate(liftDate);
            LiftDateHandler.get(this).saveDate();
        } else {
            if (!DateCompare.areDatesEqual(new Date(), LiftDateHandler.get(this).getDate().get(LiftDateHandler.get(this).getDate().size() - 1).getDate())) {
                LiftDate liftDate = new LiftDate();
                liftDate.setDate(new Date());
                LiftDateHandler.get(this).addDate(liftDate);
                LiftDateHandler.get(this).saveDate();
            }
        }
        OneRepMax oneRepMax = new OneRepMax();
        oneRepMax.setLiftName(selectedLift);
        oneRepMax.setWeightLifted(liftWeightLifted);
        oneRepMax.setWeightsPerformed(liftRepsPerformed);
        oneRepMax.setOneRepMax(liftOneRepMax);
        oneRepMax.setDate(new Date());
        OneRepMaxHandler.get(this).addORM(oneRepMax);
        OneRepMaxHandler.get(this).saveDate();
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
    }
}
