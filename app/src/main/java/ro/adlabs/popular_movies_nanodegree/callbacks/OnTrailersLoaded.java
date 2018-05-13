package ro.adlabs.popular_movies_nanodegree.callbacks;

import java.util.List;

import ro.adlabs.popular_movies_nanodegree.models.Trailer;

public interface OnTrailersLoaded {
    void onTrailersLoaded(List<Trailer> trailers);
}
