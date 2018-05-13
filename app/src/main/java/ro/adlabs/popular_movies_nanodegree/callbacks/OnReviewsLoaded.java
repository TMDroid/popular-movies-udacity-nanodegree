package ro.adlabs.popular_movies_nanodegree.callbacks;

import java.util.List;

import ro.adlabs.popular_movies_nanodegree.models.Review;

public interface OnReviewsLoaded {
    void onReviewsLoad(List<Review> reviews);
}
