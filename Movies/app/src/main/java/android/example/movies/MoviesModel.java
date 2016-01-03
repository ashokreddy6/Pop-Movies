package android.example.movies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Ashok on 11/11/2015.
 */
public class MoviesModel implements Parcelable {
    private String movieImage;
    private String overView;
    private String releaseDate;
    private String rating;
    private String title;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    public MoviesModel(){

    }
    public MoviesModel(String id, String mTitle, String mRating, String mOverView, String mReleaseDate, String mImage){
        this.movieImage=mImage;
        this.overView=mOverView;
        this.releaseDate=mReleaseDate;
        this.rating=mRating;
        this.title=mTitle;
        this.id=id;
        // this.trailorModelList = trailorModelList;
    }
    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }





    protected MoviesModel(Parcel in) {
        movieImage = in.readString();
        overView = in.readString();
        releaseDate = in.readString();
        rating = in.readString();
        title = in.readString();
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieImage);
        dest.writeString(overView);
        dest.writeString(releaseDate);
        dest.writeString(rating);
        dest.writeString(title);
        dest.writeString(id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MoviesModel> CREATOR = new Parcelable.Creator<MoviesModel>() {
        @Override
        public MoviesModel createFromParcel(Parcel in) {
            return new MoviesModel(in);
        }

        @Override
        public MoviesModel[] newArray(int size) {
            return new MoviesModel[size];
        }
    };
}