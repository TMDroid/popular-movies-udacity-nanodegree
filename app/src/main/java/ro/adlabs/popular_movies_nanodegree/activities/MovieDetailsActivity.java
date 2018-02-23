package ro.adlabs.popular_movies_nanodegree.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.felix.imagezoom.ImageZoom;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ro.adlabs.popular_movies_nanodegree.R;
import ro.adlabs.popular_movies_nanodegree.models.Movie;
import ro.adlabs.popular_movies_nanodegree.util.ApiManager;
import ro.adlabs.popular_movies_nanodegree.util.PreferenceManager;

/**
 * Created by danny on 2/22/18.
 */

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String KEY_MOVIE = "the_movie";

    public static final String TAG = MovieDetailsActivity.class.getName();

    @BindView(R.id.ivPoster)
    ImageZoom ivPoster;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.tvMovieTitle)
    TextView title;

    @BindView(R.id.tvMovieOverview)
    TextView overview;

    @BindView(R.id.tvReleaseDate)
    TextView releaseDate;

    @BindView(R.id.tvVoteAverage)
    TextView averageRating;

    @BindView(R.id.container)
    CoordinatorLayout container;

    Unbinder unbinder;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        preferenceManager = new PreferenceManager(this);

        //Get the parcel with the selected movie from the intent
        Movie movie = getIntent().hasExtra(KEY_MOVIE) ? (Movie) getIntent().getParcelableExtra(KEY_MOVIE) : null;
        if (movie == null) {
            Log.d(TAG, "Movie not received from the Intent");
            finish();

            return;
        }

        unbinder = ButterKnife.bind(this);

        //setting the toolbar related things
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //load the image into the CollapsingToolbarLayout's ImageView
        //purposefully locked the CollapsingToolbarLayout in the XML
        Picasso.with(this)
                .load(ApiManager.getHighResImageUrl(movie.getPosterPath()))
                .into(ivPoster);

        //Formatting the release date so we can get the month name and year from the Calendar instance
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-mm-dd", Locale.getDefault());
        try {
            calendar.setTime(simpleDateFormat.parse(movie.getReleaseDate()));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not format date");
        }

        String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int year = calendar.get(Calendar.YEAR);

        //Setting the rest of the data
        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        averageRating.setText(String.format("Average rating: %s", movie.getVoteAverage()));
        releaseDate.setText(String.format(Locale.getDefault(), "%s %d", monthName, year));

        //Showing a snackbar for the first time opening the activity telling the user to click the image to enlarge it
        if(!preferenceManager.hasShownSnackbar()) {
            Snackbar.make(container, "Click on the movie cover to enlarge it", Snackbar.LENGTH_INDEFINITE).show();
            preferenceManager.setShownSnackbar();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing()) unbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
