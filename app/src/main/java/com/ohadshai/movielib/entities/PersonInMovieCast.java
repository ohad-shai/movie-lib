package com.ohadshai.movielib.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a person in a movie cast.
 * Created by Ohad on 3/20/2017.
 */
public class PersonInMovieCast implements Parcelable {

    //region Private Members

    /**
     * Holds the {@link Person} object in the movie cast.
     */
    private Person person;

    /**
     * Holds the character of the person in the movie cast.
     */
    private String character;

    //endregion

    //region C'tors

    /**
     * Initializes a new instance of a person in a movie cast.
     */
    public PersonInMovieCast() {
    }

    /**
     * Initializes a new instance of a person in a movie cast.
     *
     * @param person    The {@link Person} object in the movie cast.
     * @param character The character of the person in the movie cast.
     */
    public PersonInMovieCast(Person person, String character) {
        this.person = person;
        this.character = character;
    }

    //endregion

    //region Public API

    /**
     * Gets the {@link Person} object in the movie cast.
     *
     * @return Returns the {@link Person} object in the movie cast.
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Sets the {@link Person} object in the movie cast.
     *
     * @param person The {@link Person} object in the movie cast to set.
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * Gets the character of the person in the movie cast.
     *
     * @return Returns the character of the person in the movie cast.
     */
    public String getCharacter() {
        return character;
    }

    /**
     * Sets the character of the person in the movie cast.
     *
     * @param character The character of the person in the movie cast to set.
     */
    public void setCharacter(String character) {
        this.character = character;
    }

    //endregion

    //region [Parcelable]

    protected PersonInMovieCast(Parcel in) {
        person = in.readParcelable(Person.class.getClassLoader());
        character = in.readString();
    }

    public static final Creator<PersonInMovieCast> CREATOR = new Creator<PersonInMovieCast>() {
        @Override
        public PersonInMovieCast createFromParcel(Parcel in) {
            return new PersonInMovieCast(in);
        }

        @Override
        public PersonInMovieCast[] newArray(int size) {
            return new PersonInMovieCast[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(person, flags);
        dest.writeString(character);
    }

    //endregion

}
