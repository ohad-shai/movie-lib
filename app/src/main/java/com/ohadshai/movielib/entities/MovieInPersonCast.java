package com.ohadshai.movielib.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a movie in a person cast entity.
 * Created by Ohad on 03/31/2017.
 */
public class MovieInPersonCast implements Parcelable {

    //region Private Members

    /**
     * Holds the movie in the person cast.
     */
    private Movie movie;

    /**
     * Holds the character of the person in the movie.
     */
    private String character;

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of a movie in a person cast entity.
     */
    public MovieInPersonCast() {
    }

    //endregion

    //region Public API

    /**
     * Gets the movie in the person cast.
     *
     * @return Returns the movie in the person cast.
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * Sets the movie in the person cast.
     *
     * @param movie The movie in the person cast to set.
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    /**
     * Gets the character of the person in the movie.
     *
     * @return Returns the character of the person in the movie.
     */
    public String getCharacter() {
        return character;
    }

    /**
     * Sets the character of the person in the movie.
     *
     * @param character The character of the person in the movie to set.
     */
    public void setCharacter(String character) {
        this.character = character;
    }

    //endregion

    //region [Parcelable]

    protected MovieInPersonCast(Parcel in) {

        movie = in.readParcelable(Movie.class.getClassLoader());
        character = in.readString();

    }

    public static final Creator<MovieInPersonCast> CREATOR = new Creator<MovieInPersonCast>() {
        @Override
        public MovieInPersonCast createFromParcel(Parcel in) {
            return new MovieInPersonCast(in);
        }

        @Override
        public MovieInPersonCast[] newArray(int size) {
            return new MovieInPersonCast[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(movie, flags);
        dest.writeString(character);

    }

    //endregion

}
