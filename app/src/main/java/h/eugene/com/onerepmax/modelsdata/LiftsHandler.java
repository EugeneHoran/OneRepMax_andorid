package h.eugene.com.onerepmax.modelsdata;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class LiftsHandler {
    private static final String TAG = "LiftsHandler";
    private static final String FILENAME = "lifts.json";

    private ArrayList<Lifts> mDates;
    private LiftsJSONSerializer mSerializer;

    private static LiftsHandler sCrimeLab;
    private Context mAppContext;

    private LiftsHandler(Context appContext) {
        mAppContext = appContext;
        mSerializer = new LiftsJSONSerializer(mAppContext, FILENAME);

        try {
            mDates = mSerializer.loadLifts();
        } catch (Exception e) {
            mDates = new ArrayList<>();
            Log.e(TAG, "Error loading date: ", e);
        }
    }

    public static LiftsHandler get(Context c) {
        if (sCrimeLab == null) {
            sCrimeLab = new LiftsHandler(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public Lifts getLifts(UUID id) {
        for (Lifts c : mDates) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }

    public void addLift(Lifts c) {
        mDates.add(c);
        saveLifts();
    }

    public ArrayList<Lifts> getLifts() {
        return mDates;
    }

    public void deleteLift(Lifts c) {
        mDates.remove(c);
        saveLifts();
    }

    public boolean saveLifts() {
        try {
            mSerializer.saveLifts(mDates);
            Log.d(TAG, "date saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving date: " + e);
            return false;
        }
    }
}


