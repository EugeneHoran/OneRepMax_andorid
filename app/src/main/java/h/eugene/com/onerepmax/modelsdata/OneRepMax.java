package h.eugene.com.onerepmax.modelsdata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

public class OneRepMax implements DateInterface {

    private static final String JSON_ID = "id";
    private static final String JSON_LIFT = "lift";
    private static final String JSON_WEIGHT_LIFTED = "weight";
    private static final String JSON_REPS = "reps";
    private static final String JSON_MAX = "max";
    private static final String JSON_DATE = "date";

    private UUID mId;
    private String mLiftName;
    private String mWeightLifted;
    private String mRepsPerformed;
    private String mOneRepMax;
    private Date mDate;

    public OneRepMax() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public OneRepMax(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        mLiftName = json.getString(JSON_LIFT);
        mWeightLifted = json.getString(JSON_WEIGHT_LIFTED);
        mRepsPerformed = json.getString(JSON_REPS);
        mOneRepMax = json.getString(JSON_MAX);
        mDate = new Date(json.getLong(JSON_DATE));
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_LIFT, mLiftName);
        json.put(JSON_WEIGHT_LIFTED, mWeightLifted);
        json.put(JSON_REPS, mRepsPerformed);
        json.put(JSON_MAX, mOneRepMax);
        json.put(JSON_DATE, mDate.getTime());
        return json;
    }

    @Override
    public String toString() {
        return mLiftName;
    }

    public UUID getId() {
        return mId;
    }

    public String getLiftName() {
        return mLiftName;
    }

    public void setLiftName(String title) {
        mLiftName = title;
    }

    public String getWeightLifted() {
        return mWeightLifted;
    }

    public void setWeightLifted(String solved) {
        mWeightLifted = solved;
    }

    public String getRepsPerformed() {
        return mRepsPerformed;
    }

    public void setWeightsPerformed(String suspect) {
        mRepsPerformed = suspect;
    }

    public String getOneRepMax() {
        return mOneRepMax;
    }

    public void setOneRepMax(String mOneRepMax) {
        this.mOneRepMax = mOneRepMax;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    @Override
    public Date getModelDate() {
        return mDate;
    }
}