package h.eugene.com.onerepmax.modelsdata;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class LiftDateHandler {
    private static final String TAG = "LiftDateHandler";
    private static final String FILENAME = "date.json";

    private ArrayList<LiftDate> mDates;
    private LiftDateJSONSerializer mSerializer;

    private static LiftDateHandler sCrimeLab;
    private Context mAppContext;

    private LiftDateHandler(Context appContext) {
        mAppContext = appContext;
        mSerializer = new LiftDateJSONSerializer(mAppContext, FILENAME);

        try {
            mDates = mSerializer.loadLiftDates();
        } catch (Exception e) {
            mDates = new ArrayList<>();
            Log.e(TAG, "Error loading date: ", e);
        }
    }

    public static LiftDateHandler get(Context c) {
        if (sCrimeLab == null) {
            sCrimeLab = new LiftDateHandler(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public LiftDate getDate(UUID id) {
        for (LiftDate c : mDates) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }

    public void addDate(LiftDate c) {
        mDates.add(c);
        saveDate();
    }

    public ArrayList<LiftDate> getDate() {
        return mDates;
    }

    public void deleteDate(LiftDate c) {
        mDates.remove(c);
        saveDate();
    }

    public boolean saveDate() {
        try {
            mSerializer.saveLiftDates(mDates);
            Log.d(TAG, "date saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving date: " + e);
            return false;
        }
    }
}

