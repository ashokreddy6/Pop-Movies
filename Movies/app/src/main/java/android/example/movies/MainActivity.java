    package android.example.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


    public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

        private boolean mTwoPane;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            if (findViewById(R.id.movie_detail_container) != null) {
                mTwoPane = true;
                if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, new MovieDetailActivityFragment(),
                                    MovieDetailActivityFragment.TAG)
                            .commit();
                }
            } else {
                mTwoPane = false;
            }
            System.out.println("mTwoPane::"+mTwoPane);
        }

        @Override
        public void onItemSelected(MoviesModel movie) {
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(MovieDetailActivityFragment.DETAIL_MOVIE, movie);

                MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
                fragment.setArguments(arguments);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, fragment, MovieDetailActivityFragment.TAG)
                        .commit();
            } else {
                Intent intent = new Intent(this, MovieDetailsActivity.class)
                        .putExtra(MovieDetailActivityFragment.DETAIL_MOVIE, movie);
                startActivity(intent);
            }
        }
    }