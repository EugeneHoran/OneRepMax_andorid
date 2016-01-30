package h.eugene.com.onerepmax.modelsdata;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class Lifts {
    private static final String JSON_ID = "id";
    private static final String JSON_LIFTS = "lifts";

    private UUID mId;
    private String mLift;

    public Lifts() {
        mId = UUID.randomUUID();
    }

    public Lifts(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        mLift = json.getString(JSON_LIFTS);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_LIFTS, mLift);
        return json;
    }

    public UUID getId() {
        return mId;
    }

    public String getLift() {
        return mLift;
    }

    public void setLift(String lift) {
        mLift = lift;
    }
}