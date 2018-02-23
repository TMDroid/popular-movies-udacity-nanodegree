package ro.adlabs.popular_movies_nanodegree.models;


import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * The Model class for out Movie data
 */
public class Movie implements Parcelable {

    private String title;
    private String posterPath;
    private String overview;
    private double voteAverage;
    private String releaseDate;

    /**
     * No arg constructor used in building the object
     */
    public Movie() {
    }

    /**
     * @param title
     * @param releaseDate
     * @param overview
     * @param posterPath
     * @param voteAverage
     */
    public Movie(String title, String posterPath, String overview, double voteAverage, String releaseDate) {
        super();
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeString(this.overview);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.releaseDate);
    }

    protected Movie(Parcel in) {
        this.title = in.readString();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.voteAverage = in.readDouble();
        this.releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}