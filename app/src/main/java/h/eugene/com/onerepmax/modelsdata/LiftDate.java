package h.eugene.com.onerepmax.modelsdata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

public class LiftDate implements DateInterface {
    private static final String JSON_ID = "id";
    private static final String JSON_DATE = "date";

    private UUID mId;
    private Date mDate;

    public LiftDate() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public LiftDate(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        mDate = new Date(json.getLong(JSON_DATE));
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_DATE, mDate.getTime());
        return json;
    }

    public UUID getId() {
        return mId;
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
