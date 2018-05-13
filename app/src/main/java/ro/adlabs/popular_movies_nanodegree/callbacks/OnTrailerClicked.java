package ro.adlabs.popular_movies_nanodegree.callbacks;

import ro.adlabs.popular_movies_nanodegree.models.Trailer;

/**
 * Created by danny on 2/22/18.
 */

/**
 * Callback from the RecyclerView Adapter back to the MainActivity when a movies is clicked
 */
public interface OnTrailerClicked {
    void onClick(Trailer trailer);
}
