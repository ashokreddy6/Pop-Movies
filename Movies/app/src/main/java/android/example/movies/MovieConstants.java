package android.example.movies;

/**
 * Created by Ashok on 12/27/2015.
 */
public  class MovieConstants {
    public static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String VEDIO_KEY = "/videos?api_key=317b4a606081cf4c687fb4e3ccd796f1";
    public static final String RATING_KEY = "/reviews?api_key=317b4a606081cf4c687fb4e3ccd796f1";
    public static final String IMAGE_URL ="http://image.tmdb.org/t/p/w185/";
    public static final String YOUTUBE_URL="http://www.youtube.com/watch?v=";
    public static final String GIPHY_URL="http://api.themoviedb.org/3/discover/movie?primary_release_date.gte=2015-10-02&primary_release_date.lte=2015-10-02";
    public static final String GIPHY_API_KEY="&api_key=317b4a606081cf4c687fb4e3ccd796f1";
    public static final String RATING_QUERY="&sort_by=vote_average.desc";
    public static final String FAV_QUERY="&sort_by=popularity.desc";
}
