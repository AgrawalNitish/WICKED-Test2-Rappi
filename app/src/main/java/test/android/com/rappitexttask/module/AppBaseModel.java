package test.android.com.rappitexttask.module;

import android.os.Parcelable;

public abstract class AppBaseModel implements Parcelable {

    public AppBaseModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
