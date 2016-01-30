package h.eugene.com.onerepmax.util;

import android.content.Context;
import android.content.SharedPreferences;

public class ORMPreferenceManager {
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;

    private static final String TAG_PREFERENCE = "save_options_for_fragments";

    public ORMPreferenceManager(Context context) {
        mPreferences = context.getSharedPreferences(TAG_PREFERENCE, Context.MODE_PRIVATE);
        editor = context.getSharedPreferences(TAG_PREFERENCE, Context.MODE_PRIVATE).edit();
    }

    /**
     * Max Fragment
     */
    private static final String PREF_ORM_SAVE_ONE_REP_MAX = "save_orm_weight";

    public void ormSaveOneRepMax(String oneRepMax) {
        editor.putString(PREF_ORM_SAVE_ONE_REP_MAX, oneRepMax);
        editor.commit();
    }

    public String ormGetOneRepMax() {
        return mPreferences.getString(PREF_ORM_SAVE_ONE_REP_MAX, null);
    }

    private static final String PREF_ORM_SAVE_REPS = "save_orm_reps";

    public void ormSaveReps(int oneRepMax) {
        editor.putInt(PREF_ORM_SAVE_REPS, oneRepMax);
        editor.commit();
    }

    public int ormGetReps() {
        return mPreferences.getInt(PREF_ORM_SAVE_REPS, 0);
    }


    /**
     * Percentage Fragment
     */
    private static final String PREF_PERCENTAGE_SAVE_ONE_REP_MAX = "save_percentage_weight";

    public void percentageSaveOneRepMax(String oneRepMax) {
        editor.putString(PREF_PERCENTAGE_SAVE_ONE_REP_MAX, oneRepMax);
        editor.commit();
    }

    public String percentageGetOneRepMax() {
        return mPreferences.getString(PREF_PERCENTAGE_SAVE_ONE_REP_MAX, null);
    }

    /**
     * Settings
     */

    /**
     * Percentage Fragment
     */
    private static final String PREF_SAVE_LIFT_POSITION = "save_lift_position";

    public void saveLiftPosition(int position) {
        editor.putInt(PREF_SAVE_LIFT_POSITION, position);
        editor.commit();
    }

    public Integer getLiftPosition() {
        return mPreferences.getInt(PREF_SAVE_LIFT_POSITION, 0);
    }

    /**
     * Settings
     */

    private static final String PREF_SETTINGS_UNITS = "selected_units";

    public void settingsUnits(int oneRepMax) {
        editor.putInt(PREF_SETTINGS_UNITS, oneRepMax);
        editor.commit();
    }

    public String settingsGetUnits() {
        if (mPreferences.getInt(PREF_SETTINGS_UNITS, 0) == 0) {
            return "lbs";
        } else {
            return "kg";
        }
    }

    public void resetToDefault() {
        editor.putInt(PREF_SETTINGS_UNITS, 0);
        editor.commit();
    }
}
