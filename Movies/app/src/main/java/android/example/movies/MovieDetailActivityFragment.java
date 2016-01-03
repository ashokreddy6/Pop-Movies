package android.example.movies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.example.moviesdata.MoviesProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

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
public class MovieDetailActivityFragment extends Fragment {

    public static final String TAG = MovieDetailActivityFragment.class.getSimpleName();

    static final String DETAIL_MOVIE = "DETAIL_MOVIE";

    private MoviesModel mMovie;

    ImageView moviePoster,starIcon;
    TextView title, overview, rating, releaseDate, movieID, movieImage,poster;
    Button favorateButton,reviewButton;
    List<MoviesTrailorModel> trailorModelList = new ArrayList<MoviesTrailorModel>();
    ListView trailorListView;
    MovieRatingModel reviewModel=null;
    ScrollView detailLayout;
    public MovieDetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mMovie != null) {
            inflater.inflate(R.menu.menu_main, menu);

            final MenuItem action_favorite = menu.findItem(R.id.action_favorate);

        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        return super.onOptionsItemSelected(item);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(MovieDetailActivityFragment.DETAIL_MOVIE);
        }
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        detailLayout=(ScrollView)rootView.findViewById(R.id.movieDetailsLayout);
        movieID = (TextView) rootView.findViewById(R.id.movie_id);
        favorateButton = (Button)rootView.findViewById(R.id.fav_button);
        reviewButton=(Button)rootView.findViewById(R.id.review_button);
        movieImage = (TextView) rootView.findViewById(R.id.movie_image_hidden);
        starIcon = (ImageView) rootView.findViewById(R.id.star_icon);
        moviePoster = (ImageView) rootView.findViewById(R.id.movie_big_image);
        title = (TextView) rootView.findViewById(R.id.movie_title);
        overview = (TextView)rootView. findViewById(R.id.movie_overview);
        rating = (TextView) rootView.findViewById(R.id.movie_rating);
        releaseDate = (TextView)rootView. findViewById(R.id.movie_releasedate);
        trailorListView = (ListView) rootView.findViewById(R.id.trailors_list);
        final ImageView starIcon=(ImageView)rootView.findViewById(R.id.star_icon);
         poster=(TextView) rootView.findViewById(R.id.movie_image_hidden);
        Intent intent = getActivity().getIntent();

        if (intent != null) {

               if(mMovie==null)
                   mMovie = intent.getParcelableExtra(MovieDetailActivityFragment.DETAIL_MOVIE);

               if(mMovie!=null) {
                   detailLayout.setVisibility(View.VISIBLE);
                   trailorRequest(MovieConstants.BASE_URL + mMovie.getId() + MovieConstants.VEDIO_KEY);
                   Picasso.with(this.getActivity()).load(MovieConstants.IMAGE_URL + mMovie.getMovieImage()).into(moviePoster);
                   movieID.setText(mMovie.getId());
                   movieImage.setText(mMovie.getMovieImage());
                   title.setText(mMovie.getTitle());
                   overview.setText(mMovie.getOverView());
                   rating.setText(mMovie.getRating());
                   releaseDate.setText(mMovie.getReleaseDate());
                   if (isFavorate(mMovie.getId())) {
                       favorateButton.setVisibility(View.GONE);
                       starIcon.setVisibility(View.VISIBLE);
                   }
               }

        }
        favorateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ContentValues values = new ContentValues();
                values.put(MoviesProvider._ID, (movieID.getText()).toString());
                values.put(MoviesProvider.POSTER,   (poster.getText()).toString());
                values.put(MoviesProvider.TITLE, (title.getText()).toString());
                values.put(MoviesProvider.OVERVIEW, (overview.getText()).toString());
                values.put(MoviesProvider.RATING, (rating.getText()).toString());
                values.put(MoviesProvider.RELEASE_DATE, (releaseDate.getText()).toString());
                Uri uri = getActivity().getContentResolver().insert(MoviesProvider.CONTENT_URI, values);
                if(uri!=null) {
                    Toast.makeText(getActivity(),
                            "Movie marked as favorate",
                            Toast.LENGTH_SHORT).show();
                    favorateButton.setVisibility(View.GONE);
                    starIcon.setVisibility(View.VISIBLE);
                }
            }
        });
        reviewButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String movieId=(movieID.getText()).toString();
                String movieTitle= (title.getText()).toString();
                Intent intent=new Intent(getActivity(),MovieRatingActivity.class);
                intent.putExtra("movieID", movieId);
                intent.putExtra("movieTitle", movieTitle);
                startActivity(intent);
            }
        });
        trailorListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView linearListView, View view,
                                    int position, long id) {
                MoviesTrailorModel trailer = trailorModelList.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(MovieConstants.YOUTUBE_URL + trailer.getKey()));
                startActivity(intent);
            }
        });
        return rootView;
    }
    private boolean isFavorate(String movieID) {
        String URL = "content://android.example.movies/movies";
        Uri movies = Uri.parse(URL + "/" + movieID);
        Cursor cursor =getActivity(). managedQuery(movies, null, null, null, MoviesProvider.TITLE);
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }
    private void trailorRequest(String url) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        JSONArray jsonArray;
                        trailorModelList.clear();
                        try {
                            if (isOnline(getContext())) {
                                jsonArray = response.getJSONArray("results");
                                MoviesTrailorModel iModel = null;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    iModel = new MoviesTrailorModel();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    iModel.setTrailorId(jsonObject.getString("id"));
                                    iModel.setKey(jsonObject.getString("key"));
                                    iModel.setTrailorName(jsonObject.getString("name"));

                                    trailorModelList.add(iModel);

                                }
                                trailorListView.setAdapter(new TrailorListAdapter(getActivity(), trailorModelList));
                            } else {
                                trailorModelList.add(new MoviesTrailorModel());
                            }


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
                "results");

    }
    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


}