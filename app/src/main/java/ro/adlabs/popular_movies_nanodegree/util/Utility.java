package ro.adlabs.popular_movies_nanodegree.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ro.adlabs.popular_movies_nanodegree.models.Movie;

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

            String title = movieJson.getString(KEY_TITLE);
            String poster = String.format("%s%s%s", BASE_IMAGE_URL, ApiManager.DEFAULT_IMAGE_SIZE, movieJson.getString(KEY_POSTER));
            String overview = movieJson.getString(KEY_OVERVIEW);
            double average = movieJson.getDouble(KEY_VOTE_AVERAGE);
            String release = movieJson.getString(KEY_RELEASE_DATE);

            Movie m = new Movie(title, poster, overview, average, release);
            movies.add(m);
        }

        return movies;

    }
}