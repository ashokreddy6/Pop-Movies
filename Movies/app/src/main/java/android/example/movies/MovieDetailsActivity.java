package android.example.movies;

import android.content.ContentValues;
import android.content.Intent;
import android.example.moviesdata.MoviesProvider;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MovieDetailsActivity extends AppCompatActivity {
    public final static String TAG = MovieDetailsActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailActivityFragment.DETAIL_MOVIE,
                    getIntent().getParcelableExtra(MovieDetailActivityFragment.DETAIL_MOVIE));

            MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }
    public void markAsFavorate(View view) {
      Button favorateButton = (Button) findViewById(R.id.fav_button);
      ImageView starIcon=(ImageView)findViewById(R.id.star_icon);
        ContentValues values = new ContentValues();
        values.put(MoviesProvider._ID, (((TextView) findViewById(R.id.movie_id)).getText()).toString());
        values.put(MoviesProvider.POSTER,   (((TextView) findViewById(R.id.movie_image_hidden)).getText()).toString());
        values.put(MoviesProvider.TITLE, (((TextView) findViewById(R.id.movie_title)).getText()).toString());
        values.put(MoviesProvider.OVERVIEW, (((TextView) findViewById(R.id.movie_overview)).getText()).toString());
        values.put(MoviesProvider.RATING, (((TextView) findViewById(R.id.movie_rating)).getText()).toString());
        values.put(MoviesProvider.RELEASE_DATE, (((TextView) findViewById(R.id.movie_releasedate)).getText()).toString());
        Uri uri = getContentResolver().insert(MoviesProvider.CONTENT_URI, values);
        if(uri!=null) {
            Toast.makeText(this,
                    "Movie marked as favorate",
                    Toast.LENGTH_SHORT).show();
            favorateButton.setVisibility(View.GONE);
            starIcon.setVisibility(View.VISIBLE);
        }

    }
    public void playVedio(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(MovieConstants.YOUTUBE_URL + view.getContentDescription()));
        startActivity(intent);
    }
    public void getReview(View view){
        String movieId=(((TextView) findViewById(R.id.movie_id)).getText()).toString();
        String movieTitle= (((TextView) findViewById(R.id.movie_title)).getText()).toString();
        Intent intent=new Intent(this,MovieRatingActivity.class);
        intent.putExtra("movieID", movieId);
        intent.putExtra("movieTitle",movieTitle);
        startActivity(intent);
    }

}
