package android.example.movies;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.example.moviesdata.MoviesProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ashok on 1/2/2016.
 */
public class MainActivityFragment extends Fragment {
    public final static String TAG = MainActivity.class.getSimpleName();

    private GridView mGridView;

    private GridViewAdapter mMovieGridAdapter;

    JSONObject jsonobject;
    int offset = 0;

    private String tag_json_objects = "results";
    String finalUrl = null;
    public List<MoviesModel> moviesList = new ArrayList<MoviesModel>();

    private static final String SORT_SETTING_KEY = "sort_setting";
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
    private static final String FAVORITE = "favorite";
    private static final String MOVIES_KEY = "movies";

    private String mSortBy = POPULARITY_DESC;

    private ArrayList<MoviesModel> mMovies = null;


    public MainActivityFragment() {
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        void onItemSelected(MoviesModel movie);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem action_sort_by_popularity = menu.findItem(R.id.action_popular);
        MenuItem action_sort_by_rating = menu.findItem(R.id.action_rated);
        MenuItem action_sort_by_favorite = menu.findItem(R.id.action_favorate);

        if (mSortBy.contentEquals(POPULARITY_DESC)) {
            if (!action_sort_by_popularity.isChecked()) {
                action_sort_by_popularity.setChecked(true);
            }
        } else if (mSortBy.contentEquals(RATING_DESC)) {
            if (!action_sort_by_rating.isChecked()) {
                action_sort_by_rating.setChecked(true);
            }
        } else if (mSortBy.contentEquals(FAVORITE)) {
            if (!action_sort_by_popularity.isChecked()) {
                action_sort_by_favorite.setChecked(true);
            }
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView) view.findViewById(R.id.gridview_movies);

        mMovieGridAdapter = new GridViewAdapter(getActivity(), moviesList);
        finalUrl=
                MovieConstants.GIPHY_URL+MovieConstants.GIPHY_API_KEY;
        makeJsonObjReq(finalUrl);
        mGridView.setAdapter(mMovieGridAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MoviesModel movie = moviesList.get(position);
                ((Callback) getActivity()).onItemSelected(movie);
            }
        });


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!mSortBy.contentEquals(POPULARITY_DESC)) {
            outState.putString(SORT_SETTING_KEY, mSortBy);
        }
        if (mMovies != null) {
            outState.putParcelableArrayList(MOVIES_KEY, mMovies);
        }
        super.onSaveInstanceState(outState);
    }


    private void makeJsonObjReq(String url) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        JSONArray jsonArray;
                        moviesList.clear();
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (isOnline(getContext())) {
                                    jsonArray = response.getJSONArray("results");
                                    MoviesModel iModel = null;
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        iModel = new MoviesModel();
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        iModel.setId(jsonObject.getString("id"));
                                        iModel.setMovieImage(jsonObject.getString("poster_path"));
                                        iModel.setOverView(jsonObject.getString("overview"));
                                        iModel.setTitle(jsonObject.getString("original_title"));
                                        iModel.setRating(jsonObject.getString("vote_average"));
                                        iModel.setReleaseDate(jsonObject.getString("release_date"));
                                        moviesList.add(iModel);

                                    }
                                } else {
                                    moviesList.add(new MoviesModel());
                                }
                            }
                            mMovieGridAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());

            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


        };

        // Adding request to request queue
        MoviesController.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_objects);

    }


    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id==R.id.action_rated){
            String finalURL=
                    MovieConstants.GIPHY_URL+MovieConstants.GIPHY_API_KEY;
            makeJsonObjReq(finalUrl);
        }else if(id==R.id.action_popular){
            String finalURL=
                    MovieConstants.GIPHY_URL+MovieConstants.GIPHY_API_KEY;
            makeJsonObjReq(finalUrl);
        }else if(id==R.id.action_favorate){
            moviesList.clear();
            String URL = "content://android.example.movies/movies";

            Uri movies = Uri.parse(URL);
            Cursor cursor = getActivity().managedQuery(movies, null, null, null, MoviesProvider.TITLE);
            while (cursor.moveToNext()) {
                MoviesModel moviesModel=new MoviesModel();
                moviesModel.setMovieImage(cursor.getString(cursor.getColumnIndex(MoviesProvider.POSTER)));
                moviesModel.setOverView(cursor.getString(cursor.getColumnIndex(MoviesProvider.OVERVIEW)));
                moviesModel.setTitle(cursor.getString(cursor.getColumnIndex(MoviesProvider.TITLE)));
                moviesModel.setId(cursor.getString(cursor.getColumnIndex(MoviesProvider._ID)));
                moviesModel.setRating(cursor.getString(cursor.getColumnIndex(MoviesProvider.RATING)));
                moviesModel.setReleaseDate(cursor.getString(cursor.getColumnIndex(MoviesProvider.RELEASE_DATE)));
                moviesList.add(moviesModel);
            }
            mMovieGridAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }


}
