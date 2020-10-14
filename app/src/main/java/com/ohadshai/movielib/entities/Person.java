package com.ohadshai.movielib.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Represents a person entity.
 * Created by Ohad on 3/20/2017.
 */
public class Person implements Parcelable {

    //region Constants

    /**
     * Holds a constant for a person gender: female.
     */
    public static final int FEMALE = 1;

    /**
     * Holds a constant for a person gender: male.
     */
    public static final int MALE = 2;

    //endregion

    //region Private Members

    /**
     * Holds the id of the person.
     */
    private Integer id;

    /**
     * Holds the TMDB id of the person.
     */
    private Integer tmdbId;

    /**
     * Holds the imdb id of the person.
     */
    private String imdbID;

    /**
     * Holds the name of the person.
     */
    private String name;

    /**
     * Holds the biography of the person.
     */
    private String biography;

    /**
     * Holds the profile path of the person.
     */
    private String profilePath;

    /**
     * Holds the gender of the person.
     */
    private Integer gender;

    /**
     * Holds the birthday of the person.
     */
    private Calendar birthday;

    /**
     * Holds the deathday of the person.
     */
    private Calendar deathday;

    /**
     * Holds the homepage of the person.
     */
    private String homepage;

    /**
     * Holds the place of birth of the person.
     */
    private String placeOfBirth;

    /**
     * Holds the popularity of the person.
     */
    private Float popularity;

    /**
     * Holds the facebook of the person.
     */
    private String facebook;

    /**
     * Holds the instagram of the person.
     */
    private String instagram;

    /**
     * Holds the twitter of the person.
     */
    private String twitter;

    //region Folder Members

    /**
     * Holds the folder the person is in, like: "My Actors" or "My Directors".
     */
    private Integer folder;

    /**
     * Holds an indicator indicating whether the person is favorite by the user or not.
     */
    private boolean isFavorite;

    /**
     * Holds the time the person added.
     */
    private Calendar timeAdded;

    //endregion

    /**
     * Holds the list of movies the person is seen on cast.
     */
    private ArrayList<MovieInPersonCast> seenOnCast;

    /**
     * Holds the list of movies the person is seen on crew.
     */
    private ArrayList<MovieInPersonCrew> seenOnCrew;

    /**
     * Holds an indicator indicating whether the extra information for the person is loaded or not.
     */
    private boolean isExtraInfoLoaded = false;

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of a person entity.
     */
    public Person() {
    }

    //endregion

    //region Public API

    /**
     * Gets the id of the person.
     *
     * @return Returns the id of the person.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the person.
     *
     * @param id The id of the person to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the TMDB id of the person.
     *
     * @return Returns the TMDB id of the person.
     */
    public Integer getTMDBId() {
        return tmdbId;
    }

    /**
     * Sets the TMDB id of the person.
     *
     * @param tmdbId The TMDB id of the person to set.
     */
    public void setTMDBId(Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    /**
     * Gets the imdb id of the person.
     *
     * @return Returns the imdb id of the person.
     */
    public String getIMDbId() {
        return imdbID;
    }

    /**
     * Sets the imdb id of the person.
     *
     * @param imdbID The imdb id of the person to set.
     */
    public void setIMDbId(String imdbID) {
        this.imdbID = imdbID;
    }

    /**
     * Gets the name of the person.
     *
     * @return Returns the name of the person.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the person.
     *
     * @param name The name of the person to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the biography of the person.
     *
     * @return Returns the biography of the person.
     */
    public String getBiography() {
        return biography;
    }

    /**
     * Sets the biography of the person.
     *
     * @param biography The biography of the person to set.
     */
    public void setBiography(String biography) {
        this.biography = biography;
    }

    /**
     * Gets the profile path of the person.
     *
     * @return Returns the profile path of the person.
     */
    public String getProfilePath() {
        return profilePath;
    }

    /**
     * Sets the profile path of the person.
     *
     * @param profilePath The profile path of the person to set.
     */
    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    /**
     * Gets the gender of the person.
     *
     * @return Returns the gender of the person.
     */
    public Integer getGender() {
        return gender;
    }

    /**
     * Sets the gender of the person.
     *
     * @param gender The gender of the person to set.
     */
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    /**
     * Gets the birthday of the person.
     *
     * @return Returns the birthday of the person.
     */
    public Calendar getBirthday() {
        return birthday;
    }

    /**
     * Sets the birthday of the person.
     *
     * @param birthday The birthday of the person to set.
     */
    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }

    /**
     * Gets the deathday of the person.
     *
     * @return Returns the deathday of the person.
     */
    public Calendar getDeathday() {
        return deathday;
    }

    /**
     * Sets the deathday of the person.
     *
     * @param deathday The deathday of the person to set.
     */
    public void setDeathday(Calendar deathday) {
        this.deathday = deathday;
    }

    /**
     * Gets the homepage of the person.
     *
     * @return Returns the homepage of the person.
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * Sets the homepage of the person.
     *
     * @param homepage The homepage of the person to set.
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * Gets the place of birth of the person.
     *
     * @return Returns the place of birth of the person.
     */
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    /**
     * Sets the place of birth of the person.
     *
     * @param placeOfBirth The place of birth of the person to set.
     */
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    /**
     * Gets the popularity of the person.
     *
     * @return Returns the popularity of the person.
     */
    public Float getPopularity() {
        return popularity;
    }

    /**
     * Sets the popularity of the person.
     *
     * @param popularity The popularity of the person to set.
     */
    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    /**
     * Gets the facebook of the person.
     *
     * @return Returns the facebook of the person.
     */
    public String getFacebook() {
        return facebook;
    }

    /**
     * Sets the facebook of the person.
     *
     * @param facebook The facebook of the person to set.
     */
    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    /**
     * Gets the instagram of the person.
     *
     * @return Returns the instagram of the person.
     */
    public String getInstagram() {
        return instagram;
    }

    /**
     * Sets the instagram of the person.
     *
     * @param instagram The instagram of the person to set.
     */
    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    /**
     * Gets the twitter of the person.
     *
     * @return Returns the twitter of the person.
     */
    public String getTwitter() {
        return twitter;
    }

    /**
     * Sets the twitter of the person.
     *
     * @param twitter The twitter of the person to set.
     */
    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    //region Folder Methods

    /**
     * Gets the folder the person is in, like: "My Actors" or "My Directors".
     *
     * @return Returns the folder the person is in, like: "My Actors" or "My Directors".
     */
    public Integer getFolder() {
        return folder;
    }

    /**
     * Sets the folder the person is in, like: "My Actors" or "My Directors".
     *
     * @param folder The folder the person is in to set.
     */
    public void setFolder(Integer folder) {
        this.folder = folder;
    }

    /**
     * Indicates whether the person is favorite by the user or not.
     *
     * @return Returns true if the person is favorite by the user, otherwise false.
     */
    public boolean isFavorite() {
        return isFavorite;
    }

    /**
     * Sets an indicator indicating whether the person is favorite by the user or not.
     *
     * @param favorite An indicator indicating whether the person is favorite by the user or not.
     */
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    /**
     * Gets the time the person added.
     *
     * @return Returns the time the person added.
     */
    public Calendar getTimeAdded() {
        return timeAdded;
    }

    /**
     * Sets the time the person added.
     *
     * @param timeAdded The time the person added to set.
     */
    public void setTimeAdded(Calendar timeAdded) {
        this.timeAdded = timeAdded;
    }

    //endregion

    /**
     * Gets the list of movies the person is seen on cast.
     *
     * @return Returns the list of movies the person is seen on cast.
     */
    public ArrayList<MovieInPersonCast> getSeenOnCast() {
        return seenOnCast;
    }

    /**
     * Sets the list of movies the person is seen on cast.
     *
     * @param seenOnCast The list of movies the person is seen on cast. to set.
     */
    public void setSeenOnCast(ArrayList<MovieInPersonCast> seenOnCast) {
        this.seenOnCast = seenOnCast;
    }

    /**
     * Gets the list of movies the person is seen on crew.
     *
     * @return Returns the list of movies the person is seen on crew.
     */
    public ArrayList<MovieInPersonCrew> getSeenOnCrew() {
        return seenOnCrew;
    }

    /**
     * Sets the list of movies the person is seen on crew.
     *
     * @param seenOnCrew The list of movies the person is seen on crew to set.
     */
    public void setSeenOnCrew(ArrayList<MovieInPersonCrew> seenOnCrew) {
        this.seenOnCrew = seenOnCrew;
    }

    /**
     * Indicates whether the extra information for the person is loaded or not.
     *
     * @return Returns true if the extra information for the person is loaded, otherwise false.
     */
    public boolean isExtraInfoLoaded() {
        return isExtraInfoLoaded;
    }

    /**
     * Sets an indicator indicating whether the extra information for the person is loaded or not.
     *
     * @param extraInfoLoaded An indicator indicating whether the extra information for the person is loaded or not.
     */
    public void setExtraInfoLoaded(boolean extraInfoLoaded) {
        isExtraInfoLoaded = extraInfoLoaded;
    }

    /**
     * Display the age of the person in the correct format.
     *
     * @return Returns the age of the person in the correct format.
     */
    public String displayAge() {
        if (birthday == null)
            return "";

        StringBuilder sb = new StringBuilder();
        sb.append(birthday.get(Calendar.YEAR));

        if (deathday != null)
            sb.append(" - ").append(deathday.get(Calendar.YEAR));

        sb.append(" (Age: ");
        if (deathday == null)
            sb.append(Calendar.getInstance().get(Calendar.YEAR) - birthday.get(Calendar.YEAR));
        else
            sb.append(deathday.get(Calendar.YEAR) - birthday.get(Calendar.YEAR));

        sb.append(")");
        return sb.toString();
    }

    //endregion

    //region [Parcelable]

    protected Person(Parcel in) {
        int idVal = in.readInt();
        if (idVal != -1)
            id = idVal;

        int tmdbIdVal = in.readInt();
        if (tmdbIdVal != -1)
            tmdbId = tmdbIdVal;

        imdbID = in.readString();
        name = in.readString();
        biography = in.readString();
        profilePath = in.readString();

        int genderVal = in.readInt();
        if (genderVal != -1)
            gender = genderVal;

        long birthdayVal = in.readLong();
        if (birthdayVal != -1) {
            birthday = Calendar.getInstance();
            birthday.setTimeInMillis(birthdayVal);
        }

        long deathdayVal = in.readLong();
        if (deathdayVal != -1) {
            deathday = Calendar.getInstance();
            deathday.setTimeInMillis(deathdayVal);
        }

        homepage = in.readString();
        placeOfBirth = in.readString();

        float popularityVal = in.readFloat();
        if (popularityVal != -1)
            popularity = popularityVal;

        facebook = in.readString();
        instagram = in.readString();
        twitter = in.readString();

        int folderVal = in.readInt();
        if (folderVal != -1)
            folder = folderVal;

        isFavorite = in.readByte() != 0;

        long timeAddedVal = in.readLong();
        if (timeAddedVal != -1) {
            timeAdded = Calendar.getInstance();
            timeAdded.setTimeInMillis(timeAddedVal);
        }

        seenOnCast = in.createTypedArrayList(MovieInPersonCast.CREATOR);
        seenOnCrew = in.createTypedArrayList(MovieInPersonCrew.CREATOR);

        isExtraInfoLoaded = in.readByte() != 0;
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
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

        if (tmdbId == null)
            dest.writeInt(-1);
        else
            dest.writeInt(tmdbId);

        dest.writeString(imdbID);
        dest.writeString(name);
        dest.writeString(biography);
        dest.writeString(profilePath);

        if (gender == null || gender == 0)
            dest.writeInt(-1);
        else
            dest.writeInt(gender);

        if (birthday == null)
            dest.writeLong(-1);
        else
            dest.writeLong(birthday.getTimeInMillis());

        if (deathday == null)
            dest.writeLong(-1);
        else
            dest.writeLong(deathday.getTimeInMillis());

        dest.writeString(homepage);
        dest.writeString(placeOfBirth);

        if (popularity == null)
            dest.writeFloat(-1);
        else
            dest.writeFloat(popularity);

        dest.writeString(facebook);
        dest.writeString(instagram);
        dest.writeString(twitter);

        if (folder == null)
            dest.writeInt(-1);
        else
            dest.writeInt(folder);

        dest.writeByte((byte) (isFavorite ? 1 : 0));

        if (timeAdded == null)
            dest.writeLong(-1);
        else
            dest.writeLong(timeAdded.getTimeInMillis());

        dest.writeTypedList(seenOnCast);
        dest.writeTypedList(seenOnCrew);

        dest.writeByte((byte) (isExtraInfoLoaded ? 1 : 0));
    }

    //endregion

    //region Inner Classes

    /**
     * Represents movie folders.
     */
    public static interface Folders {

        /**
         * Holds a constant for folder: "My Actors".
         */
        public static final int MY_ACTORS = 1;

        /**
         * Holds a constant for folder: "My Directors".
         */
        public static final int MY_DIRECTORS = 2;

    }

    /**
     * Represents a comparator to sort a list of {@link Person} by name.
     */
    public static class NameComparator implements Comparator<Person> {
        @Override
        public int compare(Person o1, Person o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    /**
     * Represents a comparator to sort a list of {@link Person} by age.
     */
    public static class AgeComparator implements Comparator<Person> {
        @Override
        public int compare(Person o1, Person o2) {
            if (o1.getBirthday() == null)
                return 0;
            else if (o2.getBirthday() == null)
                return 0;

            return o2.getBirthday().compareTo(o1.getBirthday());
        }
    }

    /**
     * Represents a comparator to sort a list of {@link Person} by date added.
     */
    public static class DateAddedComparator implements Comparator<Person> {
        @Override
        public int compare(Person o1, Person o2) {
            return o1.getTimeAdded().compareTo(o2.getTimeAdded());
        }
    }

    /**
     * Represents a comparator to sort a list of {@link Person} by popularity.
     */
    public static class PopularityComparator implements Comparator<Person> {
        @Override
        public int compare(Person o1, Person o2) {
            return o1.getPopularity().compareTo(o2.getPopularity());
        }
    }

    //endregion

}
