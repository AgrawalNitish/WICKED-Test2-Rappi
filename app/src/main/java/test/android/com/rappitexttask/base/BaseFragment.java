package test.android.com.rappitexttask.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

public abstract class BaseFragment extends Fragment {

    public static final String EXTRA_DATA_LAYOUT = "sc_extra_layout";
    public static final String EXTRA_DATA_TAG = "sc_extra_tag";

    public BaseActivity mActivity;
    public Resources resources;
    int _layout = -1;
    public String _extraTag = null;
    public FragmentSelectControl mCallback;

    public interface FragmentSelectControl {

        public void setCurrentFrag(BaseFragment frag);

        public BaseFragment getCurrentFrag();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCallback.setCurrentFrag(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity		=	(BaseActivity) this.getActivity();
        resources = getResources();
        Bundle args = this.getArguments();
        if(args != null) {
            this._layout = args.getInt(EXTRA_DATA_LAYOUT);
            if(args.containsKey(EXTRA_DATA_TAG)) {
                this._extraTag = args.getString(EXTRA_DATA_TAG);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(_layout, container, false);

        initOnCreateView(view);
        return view;
    }

    public abstract void initOnCreateView(View v);


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentSelectControl) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement Interface");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if(mCallback == null)
                mCallback = (FragmentSelectControl) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Interface");
        }
    }

    public void hideSoftKeyboard(){
        try {
            hideSoftKeyboard(getActivity(),getActivity().getCurrentFocus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideSoftKeyboard(View view){
        try {
            hideSoftKeyboard(getActivity(),view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(Activity activity){
        try {
            hideSoftKeyboard(activity,activity.getCurrentFocus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(Activity activity, View view){
        try {
            InputMethodManager imm = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            IBinder token = view.getWindowToken();
            if(token != null)
                imm.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            // imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showSoftKeyboard(){
        try {
            showSoftKeyboard(getView());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showSoftKeyboard(View view){
        try {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            // imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            //imm.showSoftInputFromInputMethod(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
