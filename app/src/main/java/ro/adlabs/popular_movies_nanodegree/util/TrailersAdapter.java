package ro.adlabs.popular_movies_nanodegree.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ro.adlabs.popular_movies_nanodegree.callbacks.OnTrailerClicked;
import ro.adlabs.popular_movies_nanodegree.R;
import ro.adlabs.popular_movies_nanodegree.models.Trailer;

/**
 * Created by danny on 2/21/18.
 */

/**
 * RecyclerView's adapter class
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    private List<Trailer> theTrailers;
    private OnTrailerClicked onTrailerClicked;

    /**
     *
     * @param allTrailers List of movies to initialize the adapter with
     * @param listener Listener Callback
     */
    public TrailersAdapter(List<Trailer> allTrailers, OnTrailerClicked listener) {
        theTrailers = allTrailers;
        onTrailerClicked = listener;
    }

    /**
     * Initialize the list as empty and pass a callback
     * @param listener
     */
    public TrailersAdapter(OnTrailerClicked listener) {
        this(new ArrayList<Trailer>(), listener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View theView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_trailer_item, parent, false);

        return new ViewHolder(theView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Trailer m = theTrailers.get(position);

        holder.tvTrailerTitle.setText(String.format("Trailer %d", position + 1));
        ((LinearLayout) holder.tvTrailerTitle.getParent()).setOnClickListener(v -> {
            onTrailerClicked.onClick(theTrailers.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return theTrailers.size();
    }

    public void set(List<Trailer> trailers) {
        trailers.clear();
        add(trailers);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrailerTitle;

        ViewHolder(View itemView) {
            super(itemView);

            tvTrailerTitle = itemView.findViewById(R.id.tvTrailerTitle);
        }
    }

    /**
     * Adding items to the existing Movie List
     * @param trailers
     */
    public void add(List<Trailer> trailers) {
        theTrailers.addAll(trailers);
        notifyDataSetChanged();
    }

    /**
     * Clearing the Movie List and adding items to it after that
     * @param trailers
     * @param clear
     */
    public void add(List<Trailer> trailers, boolean clear) {
        if(clear)
            theTrailers = new ArrayList<>();

        add(trailers);
    }
}
