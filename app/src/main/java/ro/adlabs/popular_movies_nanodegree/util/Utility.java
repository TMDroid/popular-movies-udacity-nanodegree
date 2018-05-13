package ro.adlabs.popular_movies_nanodegree.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ro.adlabs.popular_movies_nanodegree.models.Movie;
import ro.adlabs.popular_movies_nanodegree.models.Review;
import ro.adlabs.popular_movies_nanodegree.models.Trailer;

/**
 * Created by danny on 2/22/18.
 */

/**
 * Utility class with some static methods
 */
public class Utility {

    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";


    private static final String KEY_RESULTS = "results";
    private static final String KEY_TITLE = "title";
    private static final String KEY_POSTER = "poster_path";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_ID = "id";


    /**
     * Calculates the number of Movie columns to have in the recyclewview
     * @param context
     * @return
     */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        return (int) (dpWidth / 150);
    }

    /**
     * Used to check if the phone has an Internet connection
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Parses the Movies json got from the server
     *
     * @param json
     * @return
     * @throws JSONException
     */
    public static List<Movie> parseMovies(String json) throws JSONException {
        List<Movie> movies = new ArrayList<>();

        JSONObject main = new JSONObject(json);
        JSONArray allMovies = main.getJSONArray(KEY_RESULTS);

        for (int i = 0; i < allMovies.length(); i++) {
            JSONObject movieJson = allMovies.getJSONObject(i);

            int id = movieJson.optInt(KEY_ID);
            String title = movieJson.getString(KEY_TITLE);
            String poster = String.format("%s%s%s", BASE_IMAGE_URL, ApiManager.DEFAULT_IMAGE_SIZE, movieJson.getString(KEY_POSTER));
            String overview = movieJson.getString(KEY_OVERVIEW);
            double average = movieJson.getDouble(KEY_VOTE_AVERAGE);
            String release = movieJson.getString(KEY_RELEASE_DATE);

            Movie m = new Movie(id, title, poster, overview, average, release);
            movies.add(m);
        }

        return movies;

    }

    public static List<Trailer> parseTrailers(String json) throws JSONException {
        List<Trailer> theTrailers = new ArrayList<>();

        JSONObject main = new JSONObject(json);
        JSONArray allTrailers = main.getJSONArray("results");

        for(int i = 0; i < allTrailers.length(); i++) {
            JSONObject t = allTrailers.getJSONObject(i);

            String id = t.optString("id");
            String name = t.optString("name");
            String key = t.optString("key");

            Trailer trailer = new Trailer(id, name, key);
            theTrailers.add(trailer);
        }


        return theTrailers;
    }

    public static List<Review> parseReviews(String json) throws JSONException {
        List<Review> theReviews = new ArrayList<>();

        JSONObject main = new JSONObject(json);
        JSONArray allReviews = main.getJSONArray("results");

        for(int i = 0; i < allReviews.length(); i++) {
            JSONObject t = allReviews.getJSONObject(i);

            String author = t.optString("author");
            String content = t.optString("content");

            Review Review = new Review(author, content);
            theReviews.add(Review);
        }


        return theReviews;
    }

    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}