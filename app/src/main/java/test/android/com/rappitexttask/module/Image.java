package test.android.com.rappitexttask.module;

import android.os.Parcel;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by rails-dev on 14/3/17.
 */

public class Image extends AppBaseModel{

    String image53;
    String image75;
    String image100;

    public Image(JSONArray jsonArray) {
        try {
            this.image53 = jsonArray.getJSONObject(0).getString("label");
            this.image75 = jsonArray.getJSONObject(1).getString("label");
            this.image100 = jsonArray.getJSONObject(2).getString("label");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getImage53() {
        return image53;
    }

    public void setImage53(String image53) {
        this.image53 = image53;
    }

    public String getImage75() {
        return image75;
    }

    public void setImage75(String image75) {
        this.image75 = image75;
    }

    public String getImage100() {
        return image100;
    }

    public void setImage100(String image100) {
        this.image100 = image100;
    }

      /*Parcelable section*/


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.image53);
        dest.writeString(this.image75);
        dest.writeString(this.image100);
    }

    protected Image(Parcel in) {
        this.image53 = in.readString();
        this.image75 = in.readString();
        this.image100 = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
