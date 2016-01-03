package android.example.movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ashok on 12/16/2015.
 */
public class MoviesTrailorModel implements Parcelable {
    private String key;
    public MoviesTrailorModel(){}
    public MoviesTrailorModel(String key,String name,String trailorId){
        this.key=key;
        this.trailorName=name;
        this.trailorId=trailorId;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTrailorId() {
        return trailorId;
    }

    public void setTrailorId(String trailorId) {
        this.trailorId = trailorId;
    }

    public String getTrailorName() {
        return trailorName;
    }

    public void setTrailorName(String trailorName) {
        this.trailorName = trailorName;
    }

    public static Creator<MoviesTrailorModel> getCREATOR() {
        return CREATOR;
    }

    private String trailorId;
    private String trailorName;

    protected MoviesTrailorModel(Parcel in) {
        key = in.readString();
        trailorId = in.readString();
        trailorName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(trailorId);
        dest.writeString(trailorName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MoviesTrailorModel> CREATOR = new Parcelable.Creator<MoviesTrailorModel>() {
        @Override
        public MoviesTrailorModel createFromParcel(Parcel in) {
            return new MoviesTrailorModel(in);
        }

        @Override
        public MoviesTrailorModel[] newArray(int size) {
            return new MoviesTrailorModel[size];
        }
    };
}