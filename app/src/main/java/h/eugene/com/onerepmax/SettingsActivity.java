package h.eugene.com.onerepmax;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import h.eugene.com.onerepmax.util.ORMPreferenceManager;

public class SettingsActivity extends AppCompatActivity {
    private ORMPreferenceManager ormPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ormPreferenceManager = new ORMPreferenceManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        findViews();
        updateSettings();
    }

    private LinearLayout changeUnits;
    private TextView textCurrentUnits;
    private LinearLayout clearHistory;
    private String[] unitsList = {"Imperial (lbs)", "Metric (kg)"};

    private void findViews() {
        changeUnits = (LinearLayout) findViewById(R.id.changeUnits);
        textCurrentUnits = (TextView) findViewById(R.id.textCurrentUnits);
        clearHistory = (LinearLayout) findViewById(R.id.clearHistory);
    }

    private int selectedUnits = 0;

    private void updateSettings() {
        /**
         * Handle Units
         */
        String units;
        if (ormPreferenceManager.settingsGetUnits() == "lbs") {
            selectedUnits = 0;
            units = "Imperial (";
        } else {
            selectedUnits = 1;
            units = "Metric (";
        }
        textCurrentUnits.setText(String.format("Current units: %s%s)", units, ormPreferenceManager.settingsGetUnits()));
        changeUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Units")
                        .setSingleChoiceItems(unitsList, selectedUnits, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ormPreferenceManager.settingsUnits(which);
                                updateSettings();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        /**
         * Clear History
         */
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Reset To Default")
                        .setMessage("Are you sure you want to reset the app to default settings and delete history?")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ormPreferenceManager.resetToDefault();
                                Toast.makeText(SettingsActivity.this, "Restored", Toast.LENGTH_SHORT).show();
                                updateSettings();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
    }
}
