package ro.adlabs.popular_movies_nanodegree;

import ro.adlabs.popular_movies_nanodegree.models.Movie;

/**
 * Created by danny on 2/22/18.
 */

/**
 * Callback from the RecyclerView Adapter back to the MainActivity when a movies is clicked
 */
public interface OnMovieClicked {
    void onClick(Movie movie);
}
