package ro.adlabs.popular_movies_nanodegree.util;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ro.adlabs.popular_movies_nanodegree.callbacks.OnMovieClicked;
import ro.adlabs.popular_movies_nanodegree.R;
import ro.adlabs.popular_movies_nanodegree.models.Movie;

/**
 * Created by danny on 2/21/18.
 */

/**
 * RecyclerView's adapter class
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private List<Movie> theMovies;
    private OnMovieClicked onMovieClicked;

    /**
     *
     * @param allMovies List of movies to initialize the adapter with
     * @param listener Listener Callback
     */
    public MoviesAdapter(List<Movie> allMovies, OnMovieClicked listener) {
        theMovies = allMovies;
        onMovieClicked = listener;
    }

    /**
     * Initialize the list as empty and pass a callback
     * @param listener
     */
    public MoviesAdapter(OnMovieClicked listener) {
        this(new ArrayList<Movie>(), listener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View theView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_movie_item, parent, false);

        return new ViewHolder(theView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie m = theMovies.get(position);
        Log.d("str", String.valueOf(position));

        Picasso.with(holder.poster.getContext())
                .load(m.getPosterPath())
                .into(holder.poster);

        holder.title.setText(m.getTitle());
        holder.rating.setText(String.format("Rating: %s", m.getVoteAverage()));

        ViewParent parent = holder.poster.getParent().getParent(); //getting the card to set an OnClick listener
        if(parent instanceof CardView) {
            CardView cardView = (CardView) parent;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onMovieClicked != null)
                        onMovieClicked.onClick(m);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return theMovies.size();
    }

    public void set(List<Movie> movies) {
        clear();
        add(movies);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView poster;
        public TextView title;
        public TextView rating;

        public ViewHolder(View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.ivMoviePoster);
            title = itemView.findViewById(R.id.tvTitle);
            rating = itemView.findViewById(R.id.tvRating);
        }
    }

    /**
     * Adding items to the existing Movie List
     * @param movies
     */
    public void add(List<Movie> movies) {
        theMovies.addAll(movies);
        notifyDataSetChanged();
    }

    /**
     * Clearing the Movie List and adding items to it after that
     * @param movies
     * @param clear
     */
    public void add(List<Movie> movies, boolean clear) {
        if(clear)
            theMovies = new ArrayList<>();

        add(movies);
    }

    public List<Movie> getTheMovies() {
        return theMovies;
    }

    public void clear() {
        theMovies.clear();
    }
}
