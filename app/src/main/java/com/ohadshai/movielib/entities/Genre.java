package com.ohadshai.movielib.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Represents a genre entity.
 * Created by Ohad on 3/23/2017.
 */
public class Genre implements Parcelable {

    //region Private Members

    /**
     * Holds the id of the genre.
     */
    private Integer id;

    /**
     * Holds the name of the genre.
     */
    private String name;

    //endregion

    //region C'tors

    /**
     * Initializes a new instance of a genre entity.
     */
    public Genre() {
    }

    /**
     * Initializes a new instance of a genre entity.
     *
     * @param id   The id of the genre.
     * @param name The name of the genre.
     */
    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    //endregion

    //region Public Static API

    /**
     * Gets the genre name in a list of genres by the genre id.
     *
     * @param id     The genre id to get it's name in the list.
     * @param genres The genres list to search in.
     * @return Returns the name of the genre associated with the id.
     */
    public static String getGenreNameInListById(int id, ArrayList<Genre> genres) {
        for (Genre genre : genres) {
            if (genre.getId().equals(id))
                return genre.getName();
        }

        return null;
    }

    /**
     * Indicates whether a list of genres contains a genre name or not.
     *
     * @param genreName The genre name to check if it's in the list.
     * @param genres    The genres list to check on.
     * @return Returns true if the list contains the genre name, otherwise false.
     */
    public static boolean isListContainsGenreName(@NonNull String genreName, ArrayList<Genre> genres) {
        for (Genre genre : genres) {
            if (genre.getName().equals(genreName))
                return true;
        }

        return false;
    }

    //endregion

    //region Public API

    /**
     * Gets the id of the genre.
     *
     * @return Returns the id of the genre.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the genre.
     *
     * @param id The id of the genre to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the genre.
     *
     * @return Returns the name of the genre.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the genre.
     *
     * @param name The the name of the genre to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    //endregion

    //region [Parcelable]

    protected Genre(Parcel in) {
        int idVal = in.readInt();
        if (idVal != -1)
            id = idVal;

        name = in.readString();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null)
            dest.writeInt(-1);
        else
            dest.writeInt(id);

        dest.writeString(name);
    }

    //endregion

}
