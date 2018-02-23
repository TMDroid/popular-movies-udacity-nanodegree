package ro.adlabs.popular_movies_nanodegree.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ro.adlabs.popular_movies_nanodegree.OnMovieClicked;
import ro.adlabs.popular_movies_nanodegree.OnMoviesLoaded;
import ro.adlabs.popular_movies_nanodegree.R;
import ro.adlabs.popular_movies_nanodegree.models.Movie;
import ro.adlabs.popular_movies_nanodegree.util.ApiManager;
import ro.adlabs.popular_movies_nanodegree.util.MoviesAdapter;
import ro.adlabs.popular_movies_nanodegree.util.PaginationScrollListener;
import ro.adlabs.popular_movies_nanodegree.util.PreferenceManager;
import ro.adlabs.popular_movies_nanodegree.util.Utility;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    Unbinder unbinder;

    MoviesAdapter adapter;

    @BindView(R.id.rvMovies)
    RecyclerView rvMovies;

    @BindView(R.id.srlRefresh)
    SwipeRefreshLayout srlRefresh;

    @BindView(R.id.tvNetworkError)
    TextView tvNetworkError;

    boolean isLoading = false;
    int currentApiPage = 1;

    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new MoviesAdapter(new OnMovieClicked() {
            @Override
            public void onClick(Movie movie) {
                Intent movieDetailsIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                movieDetailsIntent.putExtra(MovieDetailsActivity.KEY_MOVIE, movie);
                startActivity(movieDetailsIntent);
            }
        });
        rvMovies.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, Utility.calculateNoOfColumns(this), LinearLayoutManager.VERTICAL, false);
        rvMovies.setLayoutManager(layoutManager);
        rvMovies.setItemAnimator(new DefaultItemAnimator());
        rvMovies.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                refreshList(false);
            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList(true);
            }
        });
        preferenceManager = new PreferenceManager(this);
        setToolbarTitle();

        refreshList(true);
    }

    /**
     * Method to refresh the recycerview's items
     *
     * @param resetPageCounter flag to know if the user changed the category or refreshed the page (we need to go back to page 1)
     *                         if true get the first page
     *                         if false get the next page
     */
    private void refreshList(final boolean resetPageCounter) {
        isLoading = true;

        if (resetPageCounter) {
            currentApiPage = 1;
        }

        if (Utility.isOnline(this)) {
            getMoviesPage(new OnMoviesLoaded() {
                @Override
                public void onLoad(List<Movie> movies) {
                    currentApiPage++;

                    adapter.add(movies, resetPageCounter);
                }
            });
        } else {
            rvMovies.setVisibility(View.GONE);
            tvNetworkError.setVisibility(View.VISIBLE);

            srlRefresh.setRefreshing(false);
        }
    }

    /**
     * Method to actually call the API class and get the page for the current selected category
     *
     * @param listener
     */
    private void getMoviesPage(final OnMoviesLoaded listener) {
        ApiManager apiManager = ApiManager.getInstance();
        apiManager.getMovies(this, new OnMoviesLoaded() {
            @Override
            public void onLoad(final List<Movie> movies) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (movies != null && !movies.isEmpty()) {
                            listener.onLoad(movies);

                            rvMovies.setVisibility(View.VISIBLE);
                            tvNetworkError.setVisibility(View.GONE);
                        } else {
                            rvMovies.setVisibility(View.GONE);
                            tvNetworkError.setVisibility(View.VISIBLE);
                        }

                        srlRefresh.setRefreshing(false);
                        isLoading = false;
                    }
                });
            }
        }, currentApiPage);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) unbinder.unbind();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem mostPopular = menu.findItem(R.id.itemMostPopular);
        MenuItem topRated = menu.findItem(R.id.itemTopRated);

        if (preferenceManager.isCategoryMostPopular()) {
            mostPopular.setVisible(false);
            topRated.setVisible(true);
        } else if (preferenceManager.isCategoryTopRated()) {
            topRated.setVisible(false);
            mostPopular.setVisible(true);
        } else {
            throw new PreferenceManager.IllegalCategoryException("How did you get in here?!?");
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itemMostPopular) {
            preferenceManager.setMoviesCategory(PreferenceManager.CATEGORY_MOST_POPULAR);
        } else if (id == R.id.itemTopRated) {
            preferenceManager.setMoviesCategory(PreferenceManager.CATEGORY_TOP_RATED);
        } else {
            throw new PreferenceManager.IllegalCategoryException("How did you get in here?!?");
        }

        setToolbarTitle();

        invalidateOptionsMenu();
        refreshList(true);

        return super.onOptionsItemSelected(item);
    }

    /**
     * We set the toolbar's title based on the selected category
     * <p>
     * Shouldn't ever get to the last else
     */
    private void setToolbarTitle() {
        if (preferenceManager != null) {
            if (preferenceManager.isCategoryTopRated()) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(R.string.category_top_rated);
                }
            } else if (preferenceManager.isCategoryMostPopular()) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(R.string.category_most_popular);
                }
            } else {
                throw new PreferenceManager.IllegalCategoryException("How did you get in here?!?");
            }
        }
    }
}