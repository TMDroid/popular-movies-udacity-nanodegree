package ro.adlabs.popular_movies_nanodegree.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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
import ro.adlabs.popular_movies_nanodegree.models.MoviesContentProvider;
import ro.adlabs.popular_movies_nanodegree.models.MySQLiteHelper;
import ro.adlabs.popular_movies_nanodegree.util.ApiManager;
import ro.adlabs.popular_movies_nanodegree.util.PreferenceManager;
import ro.adlabs.popular_movies_nanodegree.util.ReviewsAdapter;
import ro.adlabs.popular_movies_nanodegree.util.TrailersAdapter;
import ro.adlabs.popular_movies_nanodegree.util.Utility;

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

    @BindView(R.id.rvTrailers)
    RecyclerView rvTrailers;

    @BindView(R.id.rvReviews)
    RecyclerView rvReviews;


    Unbinder unbinder;
    PreferenceManager preferenceManager;
    ApiManager apiManager;
    Movie movie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        preferenceManager = new PreferenceManager(this);
        apiManager = ApiManager.getInstance();

        //Get the parcel with the selected movie from the intent
        movie = getIntent().hasExtra(KEY_MOVIE) ? (Movie) getIntent().getParcelableExtra(KEY_MOVIE) : null;
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

        TrailersAdapter trailersAdapter = new TrailersAdapter(trailer -> {
            Utility.watchYoutubeVideo(this, trailer.getKey());
        });
        rvTrailers.setAdapter(trailersAdapter);
        rvTrailers.setLayoutManager(new LinearLayoutManager(this));
        rvTrailers.setItemAnimator(new DefaultItemAnimator());

        ReviewsAdapter reviewsAdapter = new ReviewsAdapter();
        rvReviews.setAdapter(reviewsAdapter);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setItemAnimator(new DefaultItemAnimator());


        //load the image into the CollapsingToolbarLayout's ImageView
        //purposefully locked the CollapsingToolbarLayout in the XML
        if (movie.getPosterPath() != null)
            Picasso.with(this)
                    .load(ApiManager.getHighResImageUrl(movie.getPosterPath()))
                    .into(ivPoster);

        if(movie.getReleaseDate() != null) {
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

            releaseDate.setText(String.format(Locale.getDefault(), "%s %d", monthName, year));

        }
        //Setting the rest of the data
        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        averageRating.setText(String.format("Average rating: %s", movie.getVoteAverage()));

        //Showing a snackbar for the first time opening the activity telling the user to click the image to enlarge it
        if (!preferenceManager.hasShownSnackbar()) {
            Snackbar.make(container, "Click on the movie cover to enlarge it", Snackbar.LENGTH_INDEFINITE).show();
            preferenceManager.setShownSnackbar();
        }

        if (Utility.isOnline(this)) {
            apiManager.getMovieTrailers(trailers -> runOnUiThread(() -> trailersAdapter.add(trailers)), movie.getId());

            apiManager.getMovieReviews(reviews -> runOnUiThread(() -> reviewsAdapter.add(reviews)), movie.getId());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing()) unbinder.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem favorite = menu.findItem(R.id.itemSaveToFavs);
        MenuItem unfavorite = menu.findItem(R.id.itemRemoveFromFavs);

        favorite.setVisible(true);
        unfavorite.setVisible(true);

        if (isFavorited()) {
            favorite.setVisible(false);
        } else {
            unfavorite.setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.itemSaveToFavs) {
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COLUMN_ID, movie.getId());
            values.put(MySQLiteHelper.COLUMN_TITLE, movie.getTitle());
            values.put(MySQLiteHelper.COLUMN_IMAGE, movie.getPosterPath());
            values.put(MySQLiteHelper.COLUMN_RATING, movie.getVoteAverage());

            getContentResolver().insert(MoviesContentProvider.CONTENT_URI, values);
        } else if (item.getItemId() == R.id.itemRemoveFromFavs) {
            getContentResolver().delete(buildItemUri(), null, null);
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isFavorited() {
        Uri uri = buildItemUri();

        Cursor c = getContentResolver().query(uri, null, null, null, null);
        if (c != null) {
            int count = c.getCount();
            c.close();

            return count == 1;
        }

        return false;
    }

    private Uri buildItemUri() {
        return Uri.parse(MoviesContentProvider.CONTENT_URI.toString() + "/" + movie.getId());
    }
}
