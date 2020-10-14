package com.ohadshai.movielib.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a movie review entity.
 * Created by Ohad on 03/19/2017.
 */
public class MovieReview implements Parcelable {

    //region Private Members

    /**
     * Holds the author of the movie review.
     */
    private String author;

    /**
     * Holds the content of the movie review.
     */
    private String content;

    //endregion

    //region C'tors

    /**
     * Initializes a new instance of a movie review entity.
     *
     * @param author  The author of the movie review to set.
     * @param content The content of the movie review to set.
     */
    public MovieReview(String author, String content) {
        this.author = author;
        this.content = content;
    }

    //endregion

    //region Public API

    /**
     * Gets the author of the movie review.
     *
     * @return Returns the author of the movie review.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the movie review.
     *
     * @param author The author of the movie review to set.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the content of the movie review.
     *
     * @return Returns the content of the movie review.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the movie review.
     *
     * @param content The content of the movie review to set.
     */
    public void setContent(String content) {
        this.content = content;
    }

    //endregion

    //region [Parcelable]

    protected MovieReview(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }

    //endregion

}
