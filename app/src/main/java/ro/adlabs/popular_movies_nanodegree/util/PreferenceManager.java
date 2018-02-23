package ro.adlabs.popular_movies_nanodegree.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by danny on 2/22/18.
 */

/**
 * A class to manage working with shared preferences
 *
 * Could've tried to make it a Singleton also but it would've been a little bit more complicated
 * to store the static reference in the Application object or somewhere that would make sense to
 * have it in and also to have access to a Context
 */
public class PreferenceManager {

    public static final String KEY_CATEGORY = "movies_category";
    public static final String KEY_SNACKBAR = "snackbar_shown";

    public static final String CATEGORY_TOP_RATED = "top_rated";
    public static final String CATEGORY_MOST_POPULAR = "most_popular";

    private SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);

        //Setting the default category on first app start to most popular movies
        if(!isCategoryTopRated() && !isCategoryMostPopular()) {
            setMoviesCategory(CATEGORY_MOST_POPULAR);
        }
    }

    /**
     * Used to set a specific category
     *
     * If it's not one of the two categories, it throws a RuntimeException so I don't have to catch it specifically anywhere
     * @param category
     */
    public void setMoviesCategory(String category) {
        if(!category.equals(CATEGORY_MOST_POPULAR) && !category.equals(CATEGORY_TOP_RATED))
            throw new IllegalCategoryException("The movie specified is unknown");

        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(KEY_CATEGORY, category);
        e.apply();
    }

    /**
     * Checks if the current selected Category is Top Rated
     * @return
     */
    public boolean isCategoryTopRated() {
        String category = sharedPreferences.getString(KEY_CATEGORY, null);
        return category != null && category.equals(CATEGORY_TOP_RATED);

    }


    /**
     * Checks if the current selected Category is Most Popular
     * @return
     */
    public boolean isCategoryMostPopular() {
        String category = sharedPreferences.getString(KEY_CATEGORY, null);
        return category != null && category.equals(CATEGORY_MOST_POPULAR);

    }

    /**
     * Set the Snackbar as shown so we don't show it again
     */
    public void setShownSnackbar() {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putBoolean(KEY_SNACKBAR, true);
        e.apply();
    }

    /**
     * Checks to see if we shown the Snackbar before
     * @return
     */
    public boolean hasShownSnackbar() {
        return sharedPreferences.getBoolean(KEY_SNACKBAR, false);
    }


    public static class IllegalCategoryException extends RuntimeException {
        public IllegalCategoryException(String msg) {
            super(msg);
        }
    }

}
