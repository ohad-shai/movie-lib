package com.ohadshai.movielib.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a movie in a person crew entity.
 * Created by Ohad on 03/31/2017.
 */
public class MovieInPersonCrew implements Parcelable {

    //region Private Members

    /**
     * Holds the movie in the person crew.
     */
    private Movie movie;

    /**
     * Holds the department of the person in the movie.
     */
    private String department;

    /**
     * Holds the job of the person in the movie.
     */
    private String job;

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of a movie in a person crew entity.
     */
    public MovieInPersonCrew() {
    }

    //endregion

    //region Public API

    /**
     * Gets the movie in the person crew.
     *
     * @return Returns the movie in the person crew.
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * Sets the movie in the person crew.
     *
     * @param movie The movie in the person crew to set.
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    /**
     * Gets the department of the person in the movie.
     *
     * @return Returns the department of the person in the movie.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department of the person in the movie.
     *
     * @param department The department of the person in the movie to set.
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Gets the job of the person in the movie.
     *
     * @return Returns the job of the person in the movie.
     */
    public String getJob() {
        return job;
    }

    /**
     * Sets the job of the person in the movie.
     *
     * @param job The job of the person in the movie to set.
     */
    public void setJob(String job) {
        this.job = job;
    }

    //endregion

    //region [Parcelable]

    protected MovieInPersonCrew(Parcel in) {

        movie = in.readParcelable(Movie.class.getClassLoader());
        department = in.readString();
        job = in.readString();

    }

    public static final Creator<MovieInPersonCrew> CREATOR = new Creator<MovieInPersonCrew>() {
        @Override
        public MovieInPersonCrew createFromParcel(Parcel in) {
            return new MovieInPersonCrew(in);
        }

        @Override
        public MovieInPersonCrew[] newArray(int size) {
            return new MovieInPersonCrew[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(movie, flags);
        dest.writeString(department);
        dest.writeString(job);

    }

    //endregion

}
