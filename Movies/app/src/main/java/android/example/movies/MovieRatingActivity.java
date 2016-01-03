package android.example.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MovieRatingActivity extends AppCompatActivity {
    public final static String TAG = MovieRatingActivity.class.getSimpleName();


TextView movieReview,movieTitile;
    MovieRatingModel movieRatingModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_rating);
        Intent intent = getIntent();
        movieReview=(TextView)findViewById(R.id.movie_review);
        movieTitile=(TextView)findViewById(R.id.movie_review_title);
        getMovieReviewRequest(MovieConstants.BASE_URL + intent.getStringExtra("movieID") + MovieConstants.RATING_KEY);

        movieTitile.setText(intent.getStringExtra("movieTitle"));

    }
    private void getMovieReviewRequest(String url){
        {
            System.out.println("url::"+url);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            JSONArray jsonArray;

                            try {

                                    jsonArray = response.getJSONArray("results");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        movieRatingModel = new MovieRatingModel();
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        movieRatingModel.setAuthor(jsonObject.getString("author"));
                                        movieRatingModel.setContent(jsonObject.getString("content"));
                                        movieReview.setText(movieRatingModel.getContent());
                                        movieRatingModel.setUrl(jsonObject.getString("url"));
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
    }
}
