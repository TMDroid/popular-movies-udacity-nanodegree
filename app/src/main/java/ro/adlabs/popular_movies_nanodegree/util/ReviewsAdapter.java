package ro.adlabs.popular_movies_nanodegree.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ro.adlabs.popular_movies_nanodegree.R;
import ro.adlabs.popular_movies_nanodegree.models.Review;

/**
 * Created by danny on 2/21/18.
 */

/**
 * RecyclerView's adapter class
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<Review> theReviews;

    /**
     *
     * @param allReviews List of movies to initialize the adapter with
     */
    public ReviewsAdapter(List<Review> allReviews) {
        theReviews = allReviews;
    }

    public ReviewsAdapter() {
        theReviews = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View theView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_review_item, parent, false);

        return new ViewHolder(theView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Review r = theReviews.get(position);

        holder.tvReviewAuthor.setText(r.getAuthor());
        holder.tvReviewContent.setText(r.getContent());
    }

    @Override
    public int getItemCount() {
        return theReviews.size();
    }

    public void set(List<Review> Reviews) {
        Reviews.clear();
        add(Reviews);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReviewAuthor;
        TextView tvReviewContent;

        ViewHolder(View itemView) {
            super(itemView);

            tvReviewAuthor = itemView.findViewById(R.id.tvReviewAuthor);
            tvReviewContent = itemView.findViewById(R.id.tvReviewContent);
        }
    }

    /**
     * Adding items to the existing Movie List
     * @param reviews
     */
    public void add(List<Review> reviews) {
        theReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    /**
     * Clearing the Movie List and adding items to it after that
     * @param reviews
     * @param clear
     */
    public void add(List<Review> reviews, boolean clear) {
        if(clear)
            theReviews = new ArrayList<>();

        add(reviews);
    }
}
