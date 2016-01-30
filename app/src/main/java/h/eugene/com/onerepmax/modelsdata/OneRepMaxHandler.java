package h.eugene.com.onerepmax.modelsdata;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class OneRepMaxHandler {
    private static final String TAG = "OneRepMaxHandler";
    private static final String FILENAME = "max.json";

    private ArrayList<OneRepMax> oneRepMaxList;
    private OneRepMaxJSONSerializer mSerializer;

    private static OneRepMaxHandler sCrimeLab;
    private Context mAppContext;

    private OneRepMaxHandler(Context appContext) {
        mAppContext = appContext;
        mSerializer = new OneRepMaxJSONSerializer(mAppContext, FILENAME);

        try {
            oneRepMaxList = mSerializer.loadOneRepMax();
        } catch (Exception e) {
            oneRepMaxList = new ArrayList<>();
            Log.e(TAG, "Error loading date: ", e);
        }
    }

    public static OneRepMaxHandler get(Context c) {
        if (sCrimeLab == null) {
            sCrimeLab = new OneRepMaxHandler(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public OneRepMax getORM(UUID id) {
        for (OneRepMax c : oneRepMaxList) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }

    public void addORM(OneRepMax c) {
        oneRepMaxList.add(c);
        saveDate();
    }

    public ArrayList<OneRepMax> getORM() {
        return oneRepMaxList;
    }

    public void deleteORM(OneRepMax c) {
        oneRepMaxList.remove(c);
        saveDate();
    }

    public boolean saveDate() {
        try {
            mSerializer.saveOneRepMax(oneRepMaxList);
            Log.d(TAG, "date saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving date: " + e);
            return false;
        }
    }
}


