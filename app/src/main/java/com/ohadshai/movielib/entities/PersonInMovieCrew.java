package com.ohadshai.movielib.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a person in a movie crew.
 * Created by Ohad on 3/20/2017.
 */
public class PersonInMovieCrew implements Parcelable {

    //region Private Members

    /**
     * Holds the {@link Person} object in the movie crew.
     */
    private Person person;

    /**
     * Holds the department of the person in the movie crew.
     */
    private String department;

    /**
     * Holds the job of the person in the movie crew.
     */
    private String job;

    //endregion

    //region C'tors

    /**
     * Initializes a new instance of a person in a movie crew.
     */
    public PersonInMovieCrew() {
    }

    /**
     * Initializes a new instance of a person in a movie crew.
     *
     * @param person     The {@link Person} object in the movie crew.
     * @param department The department of the person in the movie crew.
     * @param job        The job of the person in the movie crew.
     */
    public PersonInMovieCrew(Person person, String department, String job) {
        this.person = person;
        this.department = department;
        this.job = job;
    }

    //endregion

    //region Public API

    /**
     * Gets the {@link Person} object in the movie crew.
     *
     * @return Returns the {@link Person} object in the movie crew.
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Sets the {@link Person} object in the movie crew.
     *
     * @param person The {@link Person} object in the movie crew to set.
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * Gets the department of the person in the movie crew.
     *
     * @return Returns the department of the person in the movie crew.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department of the person in the movie crew.
     *
     * @param department The department of the person in the movie crew to set.
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Gets the job of the person in the movie crew.
     *
     * @return Returns the job of the person in the movie crew.
     */
    public String getJob() {
        return job;
    }

    /**
     * Sets the job of the person in the movie crew.
     *
     * @param job The job of the person in the movie crew to set.
     */
    public void setJob(String job) {
        this.job = job;
    }

    //endregion

    //region [Parcelable]

    protected PersonInMovieCrew(Parcel in) {
        person = in.readParcelable(Person.class.getClassLoader());
        department = in.readString();
        job = in.readString();
    }

    public static final Creator<PersonInMovieCrew> CREATOR = new Creator<PersonInMovieCrew>() {
        @Override
        public PersonInMovieCrew createFromParcel(Parcel in) {
            return new PersonInMovieCrew(in);
        }

        @Override
        public PersonInMovieCrew[] newArray(int size) {
            return new PersonInMovieCrew[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(person, flags);
        dest.writeString(department);
        dest.writeString(job);
    }

    //endregion

}
