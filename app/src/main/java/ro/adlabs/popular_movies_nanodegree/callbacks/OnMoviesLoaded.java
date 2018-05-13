package ro.adlabs.popular_movies_nanodegree.callbacks;

import java.util.List;

import ro.adlabs.popular_movies_nanodegree.models.Movie;

/**
 * Created by danny on 2/21/18.
 */

/**
 * Callback from the API back to the MainActivity containing a list of the parsed Movies
 */
public interface OnMoviesLoaded {
    void onLoad(List<Movie> movies);
}
