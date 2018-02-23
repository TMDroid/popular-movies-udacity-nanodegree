package ro.adlabs.popular_movies_nanodegree.util;


import android.content.Context;
import android.view.View;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ro.adlabs.popular_movies_nanodegree.OnMoviesLoaded;
import ro.adlabs.popular_movies_nanodegree.models.Movie;

/**
 * Created by danny on 2/21/18.
 */

/**
 * A class to manage the API calls
 *
 */
public class ApiManager {

    public static final String API_KEY = "<insert-api-key-here>";
    public static final String BASE_URL = "https://api.themoviedb.org/3";

    public static final String DEFAULT_IMAGE_SIZE = "w342";
    public static final String HIGH_RES_IMAGE_SIZE = "original";

    private static ApiManager instance;

    private OkHttpClient client;

    /**
     * Making the api a singleton
     * @return
     */
    public static ApiManager getInstance() {
        if(instance == null) {
            instance = new ApiManager();
        }

        return instance;
    }

    /**
     * Used to convert the image url to a high resolution image for setting it in the CollapsingToolabarLayout
     */
    public static String getHighResImageUrl(String url) {
        return url.replace(DEFAULT_IMAGE_SIZE, HIGH_RES_IMAGE_SIZE);
    }

    private ApiManager() {
        client = new OkHttpClient();
    }

    /**
     * Used for getting the most popular movies from the api
     *
     * Just setting the url and then calling another method to actually make the request
     *
     * @param cb
     * @param page
     */
    private void getMostPopular(OnMoviesLoaded cb, int page) {
        String path = "/movie/popular";
        String url = buildPath(path, page);

        performRequest(url, cb);
    }

    /**
     * Used for getting the top rated movies from the api
     *
     * Just setting the url and then calling another method to actually make the request
     *
     * @param cb
     * @param page
     */
    private void getTopRated(OnMoviesLoaded cb, int page) {
        String path = "/movie/top_rated";
        String url = buildPath(path, page);

        performRequest(url, cb);
    }

    /**
     * A public method exposed to the clients that allows them to get the Movies for a certain
     * category form the API
     *
     * @param context
     * @param listener
     * @param page
     */
    public void getMovies(Context context, OnMoviesLoaded listener, int page) {
        PreferenceManager manager = new PreferenceManager(context);

        if(manager.isCategoryMostPopular()) {
            getMostPopular(listener, page);
        } else if(manager.isCategoryTopRated()) {
            getTopRated(listener, page);
        }
    }

    /**
     * The method that actually calls the API using the url provided as a parameter
     * and passing the result to a callback
     * @param url
     * @param cb
     */
    private void performRequest(String url, final OnMoviesLoaded cb) {

        Request.Builder builder = new Request.Builder();
        final Request request = builder.url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onLoad(null);
            }

            @Override
            public void onResponse(Call call, Response response)  {
                List<Movie> movies = null;
                try {
                    movies = getMovies(response);
                } catch (IOException e) {
                    onFailure(call, e);
                }

                cb.onLoad(movies);
            }
        });
    }

    /**
     * Used to extract the Movies List from an API response
     * @param response
     * @return
     * @throws IOException
     */
    private List<Movie> getMovies(Response response) throws IOException {
        if(response.body() == null) {
            return null;
        }
        String json = response.body().string();

        try {
            return Utility.parseMovies(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Used to actually build the request url includin the API key and the page number from
     * a base url and the provided path
     *
     * @param path
     * @param page
     * @return
     */
    private String buildPath(String path, int page) {
        return String.format(Locale.getDefault(), "%s%s?api_key=%s&page=%d", BASE_URL, path, API_KEY, page);
    }
}
