package ro.adlabs.popular_movies_nanodegree.models;


import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import java.util.List;

/**
 * The Model class for out Movie data
 */
public class Movie implements Parcelable {

    private int id;
    private String title;
    private String posterPath;
    private String overview;
    private double voteAverage;
    private String releaseDate;
    private List<Trailer> trailers;

    /**
     * @param title
     * @param releaseDate
     * @param overview
     * @param posterPath
     * @param voteAverage
     */
    public Movie(int id, String title, String posterPath, String overview, double voteAverage, String releaseDate) {
        super();

        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    /**
     *
     * @param id
     * @param title
     */
    public Movie(int id, String title, String posterPath, double voteAverage) {
        super();

        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
    }

    public int getId() {
        return id;
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

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public Movie setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
        return this;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeString(this.overview);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.releaseDate);
        dest.writeTypedList(this.trailers);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.voteAverage = in.readDouble();
        this.releaseDate = in.readString();
        this.trailers = in.createTypedArrayList(Trailer.CREATOR);
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