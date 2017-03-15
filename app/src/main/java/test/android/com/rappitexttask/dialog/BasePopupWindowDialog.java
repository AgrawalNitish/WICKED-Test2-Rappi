package test.android.com.rappitexttask.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import test.android.com.rappitexttask.R;
import test.android.com.rappitexttask.base.BaseFragment;


public abstract class BasePopupWindowDialog implements View.OnClickListener {

    protected Activity activity;
    protected TextView cancel;

    protected int layout = 0;
    protected PopupWindow popup;

    public BasePopupWindowDialog(Activity activity) {
        this.activity = activity;
        this.layout = getLayout();
    }

    public void show() {
        try {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layoutView = layoutInflater.from(activity).inflate(this.layout, null);
            popup = new PopupWindow(activity);
            popup.setContentView(layoutView);

            handleChildViews(layoutView, popup);

            //popup.setAnimationStyle(R.style.MyCustomPopupTheme);
            popup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
            popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);

            popup.setFocusable(true);
            //popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            //popup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popup.showAtLocation(layoutView, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void handleChildViews(View layoutView, final PopupWindow popup) {
        cancel = (TextView) layoutView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
            }
        });

    }

    protected void closePopup() {
        popup.dismiss();
    }

    public abstract int getLayout();

}
