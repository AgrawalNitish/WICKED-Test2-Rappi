package test.android.com.rappitexttask.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;



public abstract class BaseActivity extends AppCompatActivity implements BaseFragment.FragmentSelectControl {

    public Resources resources;
    protected BaseFragment currentFragment = null;

    @Override
    public void setCurrentFrag(BaseFragment frag) {
        this.currentFragment = frag;
    }

    @Override
    public BaseFragment getCurrentFrag(){
        return currentFragment;
    }


    public void callFragment(boolean isBackStackNull, Fragment fragment, int id) {
        this.callFragment(isBackStackNull,fragment,false,id,null);
    }

    public void callFragment(Fragment fragment, int id) {
        this.callFragment(fragment,false,id,null);
    }

    public void callFragment(Fragment fragment, int id, Bundle bundle) {
        this.callFragment(fragment,false,id,bundle);
    }

    public void callFragment(Fragment fragment, boolean isBack, int id) {
        this.callFragment(fragment,isBack,id,null);
    }

    public void callFragment(Fragment fragment, boolean isBack, int id, Bundle bundle) {
        callFragment(false,fragment,isBack,id,bundle);
    }
    public void callFragment(boolean isBackStackNull, Fragment fragment, boolean isBack, int id, Bundle bundle) {
        if(bundle != null) {
            fragment.setArguments(bundle);
        }
        FragmentManager manager = getSupportFragmentManager();
        if(isBackStackNull){
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction fTransaction = manager.beginTransaction();

        fTransaction.replace(id, fragment);
        if (isBack){fTransaction.addToBackStack(null);}
        fTransaction.commit();
    }

    public void addFragment(Fragment fragment, int id) {
        this.addFragment(fragment,id,null);
    }
    public void addFragment(Fragment fragment, int id, Bundle bundle) {
        if(bundle != null) {
            fragment.setArguments(bundle);
        }
        FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
        fTransaction.add(id, fragment);
        fTransaction.commit();
    }

    public void callIntent(Class cls) {
        callIntent(cls,null);
    }

    public void callIntent(Class cls, Bundle bundle) {
        callIntent(cls,bundle,false,this);
    }

    public void callIntent(Class cls, boolean isFinish) {
        callIntent(cls,null,isFinish,this);
    }

    public void callIntent(Class cls, Bundle bundle, boolean isFinish) {
        callIntent(cls,bundle,isFinish,this);
    }
    public static void callIntent(Class cls, Bundle bundle, boolean isFinish, Activity activity) {
        Intent intent = new Intent(activity, cls);
        if(bundle != null)
            intent.putExtras(bundle);
        activity.startActivity(intent);
        if (isFinish)
            activity.finish();
    }

    public Drawable getDrawables(int drawable){
        return getDrawables(drawable,this);
    }

    public static Drawable getDrawables(int drawable, Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return context.getResources().getDrawable(drawable, null);
        else
            return context.getResources().getDrawable(drawable);
    }

    public void setBackgrounds(boolean isSelected, View view, int draUnSelected, int draSelected){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(getDrawables((isSelected) ? draUnSelected : draSelected));
        }else {
            view.setBackgroundDrawable(getDrawables((isSelected) ? draUnSelected : draSelected));
        }
    }

    public String getStr(int id){
        return getStr(id,resources);
    }

    public static String getStr(int id, Resources resources){
        return resources.getString(id);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            FragmentTransaction trans = fm.beginTransaction();
            if(currentFragment != null)
            trans.remove(currentFragment);
            trans.commit();
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public BaseFragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(BaseFragment currentFragment) {
        this.currentFragment = currentFragment;
    }
}
