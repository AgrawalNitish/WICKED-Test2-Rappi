package test.android.com.rappitexttask.base;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Map;

import test.android.com.rappitexttask.apicalls.APIServiceAsyncTask;
import test.android.com.rappitexttask.apicalls.ApiNames;
import test.android.com.rappitexttask.apicalls.ApiRequest;

/**
 * Created by Nitish on 23/11/16.
 */

public abstract class BaseActivityWithApi extends BaseActivity implements View.OnClickListener{

    @Override
    public void onClick(View view) {

    }

    protected void callApi(ApiRequest call, BaseActivityWithApi fragment){
        callApi(call,fragment,null);
    }

    protected void callApi(ApiRequest call, BaseActivityWithApi fragment, Object map){
        ApiTask mTask = new ApiTask(this,fragment, call,map);
        mTask.execute((Void) null);
    }

    class ApiTask extends APIServiceAsyncTask {

        BaseActivityWithApi fragment;
        public ApiTask(Context mContext) {
            super(mContext);
        }

        public ApiTask(Context mContext, BaseActivityWithApi fragment, ApiRequest apiRequest, Map<String, String> serviceParamsMap) {
            super(mContext,apiRequest,serviceParamsMap);
            this.fragment =fragment;
        }

        public ApiTask(Context mContext, BaseActivityWithApi fragment,
                       ApiRequest apiRequest, Object serviceParamsMap) {
            super(mContext,apiRequest,serviceParamsMap);
            this.fragment =fragment;
        }

        @Override
        protected void success(JSONObject jsonObj, ApiNames serviceTaskType) {
            super.success(jsonObj, serviceTaskType);
            hideSoftKeyboard();
            fragment.responseHandler(jsonObj, serviceTaskType);
        }

        @Override
        protected void failure(JSONObject jsonObj, ApiNames serviceTaskType) {
            super.failure(jsonObj, serviceTaskType);
            errorResponseHandler(jsonObj,serviceTaskType);
            hideSoftKeyboard();
        }

        @Override
        protected void failure(String message) {
            // super.failure(message);
            hideSoftKeyboard();
            Toast.makeText(BaseActivityWithApi.this,"Unknown error in API response",Toast.LENGTH_LONG).show();
        }


    }

   /* private Map<String, String> getParams(String code){
        Map<String, String> params = new HashMap<>();
        switch (code){
            case Test:
                params.put("access_token", getToken() );
                params.put("client_id", getUserId() );
            break;
        }
        return params;
    }*/

    public abstract void errorResponseHandler(JSONObject jsonObject, ApiNames serviceTaskType);

    public abstract void responseHandler(JSONObject jsonObject, ApiNames serviceTaskType);

}
