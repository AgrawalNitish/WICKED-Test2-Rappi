package test.android.com.rappitexttask.module;

import android.os.Parcel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nitish 14/3/17.
 */

public class Data extends AppBaseModel{

    String name;
    Image image;
    String summary;
    String price;
    String contentType;
    String rights;
    String title;
    String link;
    String id;
    String artist;
    String category;
    String releaseDate;

    public Data(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getJSONObject("im:name").getString("label");
            this.summary = jsonObject.getJSONObject("summary").getString("label");
            this.price = jsonObject.getJSONObject("im:price").getString("label");
            this.contentType = jsonObject.getJSONObject("im:contentType").getJSONObject("attributes").getString("label");
            this.rights = jsonObject.getJSONObject("rights").getString("label");
            this.title = jsonObject.getJSONObject("title").getString("label");
            this.link = jsonObject.getJSONObject("link").getJSONObject("attributes").getString("href");
            this.id = jsonObject.getJSONObject("id").getString("label");
            this.artist = jsonObject.getJSONObject("im:artist").getString("label");
            this.category = jsonObject.getJSONObject("category").getJSONObject("attributes").getString("label");
            this.releaseDate = jsonObject.getJSONObject("im:releaseDate").getString("label");
            this.image = new Image(jsonObject.getJSONArray("im:image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

      /*Parcelable section*/


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeParcelable(this.image, flags);
        dest.writeString(this.summary);
        dest.writeString(this.price);
        dest.writeString(this.contentType);
        dest.writeString(this.rights);
        dest.writeString(this.title);
        dest.writeString(this.link);
        dest.writeString(this.id);
        dest.writeString(this.artist);
        dest.writeString(this.category);
        dest.writeString(this.releaseDate);
    }

    protected Data(Parcel in) {
        this.name = in.readString();
        this.image = in.readParcelable(Image.class.getClassLoader());
        this.summary = in.readString();
        this.price = in.readString();
        this.contentType = in.readString();
        this.rights = in.readString();
        this.title = in.readString();
        this.link = in.readString();
        this.id = in.readString();
        this.artist = in.readString();
        this.category = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
}
