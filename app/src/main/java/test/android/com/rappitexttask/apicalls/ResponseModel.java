package test.android.com.rappitexttask.apicalls;

import org.json.JSONObject;

/**
 * Created by Nitish on 13/12/16.
 */

public class ResponseModel {

    int responseCode;
    JSONObject response;

    public ResponseModel(int responseCode, JSONObject response) {
        this.responseCode = responseCode;
        this.response = response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public JSONObject getResponse() {
        return response;
    }
}
