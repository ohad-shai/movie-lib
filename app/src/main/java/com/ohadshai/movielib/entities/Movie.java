package com.ohadshai.movielib.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Represents a movie entity.
 * Created by Ohad on 03/19/2017.
 */
public class Movie implements Parcelable {

    //region Private Members

    /**
     * Holds the id of the movie.
     */
    private Integer id;

    /**
     * Holds the TMDB id of the movie.
     */
    private Integer tmdbId;

    /**
     * Holds the imdb id of the movie.
     */
    private String imdbID;

    /**
     * Holds the title of the movie.
     */
    private String title;

    /**
     * Holds the original title of the movie.
     */
    private String originalTitle;

    /**
     * Holds the release date of the movie.
     */
    private Calendar releaseDate;

    /**
     * Holds the plot of the movie.
     */
    private String plot;

    /**
     * Holds the poster path of the movie.
     */
    private String posterPath;

    /**
     * Holds the backdrop path of the movie.
     */
    private String backdropPath;

    /**
     * Holds the original language of the movie.
     */
    private String originalLanguage;

    /**
     * Holds the popularity of the movie.
     */
    private Float popularity;

    /**
     * Holds the homepage of the movie.
     */
    private String homepage;

    /**
     * Holds the budget of the movie.
     */
    private Integer budget;

    /**
     * Holds the revenue of the movie.
     */
    private Integer revenue;

    /**
     * Holds the runtime of the movie.
     */
    private Integer runtime;

    /**
     * Holds the status of the movie.
     */
    private String status;

    /**
     * Holds the tagline of the movie.
     */
    private String tagline;

    /**
     * Holds the vote average of the movie.
     */
    private Float voteAverage;

    /**
     * Holds the vote count of the movie.
     */
    private Integer voteCount;

    /**
     * Holds an indicator indicating whether the movie is for adults or not.
     */
    private boolean isAdult;

    //region Folder Members

    /**
     * Holds the folder the movie is in, like: "Wishlist" or "My Movies".
     */
    private Integer folder;

    /**
     * Holds the user rating of the movie.
     */
    private Integer rating;

    /**
     * Holds an indicator if the movie was watched by the user or not.
     */
    private boolean isWatched;

    /**
     * Holds an indicator if the movie is favorite or not by the user.
     */
    private boolean isFavorite;

    /**
     * Holds the time the movie added.
     */
    private Calendar timeAdded;

    //endregion

    /**
     * Holds the list of the movie genres.
     */
    private ArrayList<Genre> genres;

    /**
     * Holds the list of the movie reviews.
     */
    private ArrayList<MovieReview> reviews;

    /**
     * Holds the list of movie cast.
     */
    private ArrayList<PersonInMovieCast> cast;

    /**
     * Holds the list of movie crew.
     */
    private ArrayList<PersonInMovieCrew> crew;

    /**
     * Holds an indicator indicating whether the extra information for the movie is loaded or not.
     */
    private boolean isExtraInfoLoaded = false;

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of a movie entity.
     */
    public Movie() {
    }

    //endregion

    //region Public API

    /**
     * Gets the id of the movie.
     *
     * @return Returns the id of the movie.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the movie.
     *
     * @param id The id of the movie to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the TMDB id of the movie.
     *
     * @return Returns the TMDB id of the movie.
     */
    public Integer getTMDBId() {
        return tmdbId;
    }

    /**
     * Sets the TMDB id of the movie.
     *
     * @param tmdbId The TMDB id of the movie to set.
     */
    public void setTMDBId(Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    /**
     * Gets the imdb id of the movie.
     *
     * @return Returns the imdb id of the movie.
     */
    public String getIMDbId() {
        return imdbID;
    }

    /**
     * Sets the imdb id of the movie.
     *
     * @param imdbID The imdb id of the movie to set.
     */
    public void setIMDbId(String imdbID) {
        this.imdbID = imdbID;
    }

    /**
     * Gets the title of the movie.
     *
     * @return Returns the title of the movie.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the movie.
     *
     * @param title The title of the movie to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the original title of the movie.
     *
     * @return Returns the original title of the movie.
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     * Sets the original title of the movie.
     *
     * @param originalTitle The original title of the movie to set.
     */
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    /**
     * Gets the release date of the movie.
     *
     * @return Returns the release date of the movie.
     */
    public Calendar getReleaseDate() {
        return releaseDate;
    }

    /**
     * Sets the release date of the movie.
     *
     * @param releaseDate The release date of the movie to set.
     */
    public void setReleaseDate(Calendar releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Gets the plot of the movie.
     *
     * @return Returns the plot of the movie.
     */
    public String getPlot() {
        return plot;
    }

    /**
     * Sets the plot of the movie.
     *
     * @param plot The plot of the movie to set.
     */
    public void setPlot(String plot) {
        this.plot = plot;
    }

    /**
     * Gets the poster path of the movie.
     *
     * @return Returns the poster path of the movie.
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * Sets the poster path of the movie.
     *
     * @param posterPath The poster path of the movie to set.
     */
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    /**
     * Gets the backdrop path of the movie.
     *
     * @return Returns the backdrop path of the movie.
     */
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     * Sets the backdrop path of the movie.
     *
     * @param backdropPath The backdrop path of the movie to set.
     */
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    /**
     * Gets the original language of the movie.
     *
     * @return Returns the original language of the movie.
     */
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    /**
     * Sets the original language of the movie.
     *
     * @param originalLanguage The original language of the movie to set.
     */
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    /**
     * Gets the popularity of the movie.
     *
     * @return Returns the popularity of the movie.
     */
    public Float getPopularity() {
        return popularity;
    }

    /**
     * Sets the popularity of the movie.
     *
     * @param popularity The popularity of the movie to set.
     */
    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    /**
     * Gets the homepage of the movie.
     *
     * @return Returns the homepage of the movie.
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * Sets the homepage of the movie.
     *
     * @param homepage The homepage of the movie to set.
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * Gets the budget of the movie.
     *
     * @return Returns the budget of the movie.
     */
    public Integer getBudget() {
        return budget;
    }

    /**
     * Sets the budget of the movie.
     *
     * @param budget The budget of the movie to set.
     */
    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    /**
     * Gets the revenue of the movie.
     *
     * @return Returns the revenue of the movie.
     */
    public Integer getRevenue() {
        return revenue;
    }

    /**
     * Sets the revenue of the movie.
     *
     * @param revenue The revenue of the movie to set.
     */
    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    /**
     * Gets the runtime of the movie.
     *
     * @return Returns the runtime of the movie.
     */
    public Integer getRuntime() {
        return runtime;
    }

    /**
     * Sets the runtime of the movie.
     *
     * @param runtime The runtime of the movie to set.
     */
    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    /**
     * Gets the status of the movie.
     *
     * @return Returns the status of the movie.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the movie.
     *
     * @param status The status of the movie to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the tagline of the movie.
     *
     * @return Returns the tagline of the movie.
     */
    public String getTagline() {
        return tagline;
    }

    /**
     * Sets the tagline of the movie.
     *
     * @param tagline The tagline of the movie to set.
     */
    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    /**
     * Gets the vote average of the movie.
     *
     * @return Returns the vote average of the movie.
     */
    public Float getVoteAverage() {
        return voteAverage;
    }

    /**
     * Sets the vote average of the movie.
     *
     * @param voteAverage The vote average of the movie to set.
     */
    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }

    /**
     * Gets the vote count of the movie.
     *
     * @return Returns the vote count of the movie.
     */
    public Integer getVoteCount() {
        return voteCount;
    }

    /**
     * Sets the vote count of the movie.
     *
     * @param voteCount The vote count of the movie to set.
     */
    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    /**
     * Indicates whether the movie is for adults or not.
     *
     * @return Returns true if the movie is for adults, otherwise false.
     */
    public boolean isAdult() {
        return isAdult;
    }

    /**
     * Sets an indicator indicating whether the movie is for adults or not.
     *
     * @param adult An indicator indicating whether the movie is for adults to set.
     */
    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    //region Folder Methods

    /**
     * Gets the folder the movie is in, like: "Wishlist" or "My Movies".
     *
     * @return Returns the folder the movie is in, like: "Wishlist" or "My Movies".
     */
    public Integer getFolder() {
        return folder;
    }

    /**
     * Sets the folder the movie is in, like: "Wishlist" or "My Movies".
     *
     * @param folder The folder the movie is in to set.
     */
    public void setFolder(Integer folder) {
        this.folder = folder;
    }

    /**
     * Gets the user rating of the movie.
     *
     * @return Returns the user rating of the movie.
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * Sets the user rating of the movie.
     *
     * @param rating The user rating of the movie to set.
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    /**
     * Indicates whether the movie is watched or not.
     *
     * @return Returns an indicator if the movie is watched or not.
     */
    public boolean isWatched() {
        return isWatched;
    }

    /**
     * Sets an indicator if the movie was watched or not.
     *
     * @param watched An indicator if the movie was watched or not to set.
     */
    public void setWatched(boolean watched) {
        isWatched = watched;
    }

    /**
     * Indicates whether the movie is favorite or not by the user.
     *
     * @return Returns an indicator if the movie is favorite or not by the user.
     */
    public boolean isFavorite() {
        return isFavorite;
    }

    /**
     * Sets an indicator if the movie is favorite or not by the user.
     *
     * @param favorite An indicator if the movie is favorite or not by the user to set.
     */
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    /**
     * Gets the time the movie added.
     *
     * @return Returns the time the movie added.
     */
    public Calendar getTimeAdded() {
        return timeAdded;
    }

    /**
     * Sets the time the movie added.
     *
     * @param timeAdded The time the movie added to set.
     */
    public void setTimeAdded(Calendar timeAdded) {
        this.timeAdded = timeAdded;
    }

    //endregion

    /**
     * Gets the list of the movie genres.
     *
     * @return Returns the list of the movie genres.
     */
    public ArrayList<Genre> getGenres() {
        return genres;
    }

    /**
     * Sets the list of the movie genres.
     *
     * @param genres The list of the movie genres to set.
     */
    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    /**
     * Gets the list of the movie reviews.
     *
     * @return Returns the list of the movie reviews.
     */
    public ArrayList<MovieReview> getReviews() {
        return reviews;
    }

    /**
     * Sets the list of the movie reviews.
     *
     * @param reviews The list of the movie reviews to set.
     */
    public void setReviews(ArrayList<MovieReview> reviews) {
        this.reviews = reviews;
    }

    /**
     * Gets the list of movie cast.
     *
     * @return Returns the list of movie cast.
     */
    public ArrayList<PersonInMovieCast> getCast() {
        return cast;
    }

    /**
     * Sets the list of movie cast.
     *
     * @param cast The list of movie cast to set.
     */
    public void setCast(ArrayList<PersonInMovieCast> cast) {
        this.cast = cast;
    }

    /**
     * Gets the list of movie crew.
     *
     * @return Returns the list of movie crew.
     */
    public ArrayList<PersonInMovieCrew> getCrew() {
        return crew;
    }

    /**
     * Sets the list of movie crew.
     *
     * @param crew The list of movie crew to set.
     */
    public void setCrew(ArrayList<PersonInMovieCrew> crew) {
        this.crew = crew;
    }

    /**
     * Indicates whether the extra information for the movie is loaded or not.
     *
     * @return Returns true if the extra information for the movie is loaded, otherwise false.
     */
    public boolean isExtraInfoLoaded() {
        return isExtraInfoLoaded;
    }

    /**
     * Sets an indicator indicating whether the extra information for the movie is loaded or not.
     *
     * @param extraInfoLoaded An indicator indicating whether the extra information for the movie is loaded or not.
     */
    public void setExtraInfoLoaded(boolean extraInfoLoaded) {
        isExtraInfoLoaded = extraInfoLoaded;
    }

    /**
     * Gets the movie year to display in the correct format for the UI.
     *
     * @return Returns the correct string to display of the movie year.
     */
    public String displayYear() {
        if (releaseDate == null) {
            return "";
        } else {
            return "(" + releaseDate.get(Calendar.YEAR) + ")";
        }
    }

    /**
     * Gets the year of the movie.
     *
     * @return Returns the year of the movie if found, otherwise null.
     */
    public Integer getYear() {
        if (releaseDate == null)
            return null;
        else
            return releaseDate.get(Calendar.YEAR);
    }

    /**
     * Gets the movie genres to display in the correct format for the UI.
     *
     * @return Returns the correct string to display of the movie genres.
     */
    public String displayGenres() {
        if (genres == null)
            return "";

        String display = "";
        for (Genre genre : genres)
            if (genre.getName() != null && !genre.getName().equals("null"))
                display += (genre.getName() + " | ");

        if (display.length() > 0)
            return display.substring(0, display.length() - 3);
        else
            return display;
    }

    /**
     * Gets the movie user rating to display in the correct format for the UI.
     *
     * @return Returns the correct float value to display the movie user rating.
     */
    public float displayRating() {
        if (rating != null)
            return (float) rating / 2;
        else
            return 0;
    }

    //endregion

    //region [Parcelable]

    protected Movie(Parcel in) {
        int idVal = in.readInt();
        if (idVal != -1)
            id = idVal;

        int tmdbIdVal = in.readInt();
        if (tmdbIdVal != -1)
            tmdbId = tmdbIdVal;

        imdbID = in.readString();
        title = in.readString();
        originalTitle = in.readString();

        long releaseDateVal = in.readLong();
        if (releaseDateVal != -1) {
            releaseDate = Calendar.getInstance();
            releaseDate.setTimeInMillis(releaseDateVal);
        }

        plot = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        originalLanguage = in.readString();

        float popularityVal = in.readFloat();
        if (popularityVal != -1)
            popularity = popularityVal;

        homepage = in.readString();

        int budgetVal = in.readInt();
        if (budgetVal != -1)
            budget = budgetVal;

        int revenueVal = in.readInt();
        if (revenueVal != -1)
            revenue = revenueVal;

        int runtimeVal = in.readInt();
        if (runtimeVal != -1)
            runtime = runtimeVal;

        status = in.readString();
        tagline = in.readString();

        float voteAverageVal = in.readFloat();
        if (voteAverageVal != -1)
            voteAverage = voteAverageVal;

        int voteCountVal = in.readInt();
        if (voteCountVal != -1)
            voteCount = voteCountVal;

        isAdult = in.readByte() != 0;

        int folderVal = in.readInt();
        if (folderVal != -1)
            folder = folderVal;

        int ratingVal = in.readInt();
        if (ratingVal != -1)
            rating = ratingVal;

        isWatched = in.readByte() != 0;
        isFavorite = in.readByte() != 0;

        long timeAddedVal = in.readLong();
        if (timeAddedVal != -1) {
            timeAdded = Calendar.getInstance();
            timeAdded.setTimeInMillis(timeAddedVal);
        }

        genres = in.createTypedArrayList(Genre.CREATOR);
        reviews = in.createTypedArrayList(MovieReview.CREATOR);
        cast = in.createTypedArrayList(PersonInMovieCast.CREATOR);
        crew = in.createTypedArrayList(PersonInMovieCrew.CREATOR);

        isExtraInfoLoaded = in.readByte() != 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
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
        dest.writeString(title);
        dest.writeString(originalTitle);

        if (releaseDate == null)
            dest.writeLong(-1);
        else
            dest.writeLong(releaseDate.getTimeInMillis());

        dest.writeString(plot);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(originalLanguage);

        if (popularity == null)
            dest.writeFloat(-1);
        else
            dest.writeFloat(popularity);

        dest.writeString(homepage);

        if (budget == null)
            dest.writeInt(-1);
        else
            dest.writeInt(budget);

        if (revenue == null)
            dest.writeInt(-1);
        else
            dest.writeInt(revenue);

        if (runtime == null)
            dest.writeInt(-1);
        else
            dest.writeInt(runtime);

        dest.writeString(status);
        dest.writeString(tagline);

        if (voteAverage == null)
            dest.writeFloat(-1);
        else
            dest.writeFloat(voteAverage);

        if (voteCount == null)
            dest.writeInt(-1);
        else
            dest.writeInt(voteCount);

        dest.writeByte((byte) (isAdult ? 1 : 0));

        if (folder == null)
            dest.writeInt(-1);
        else
            dest.writeInt(folder);

        if (rating == null)
            dest.writeInt(-1);
        else
            dest.writeInt(rating);

        dest.writeByte((byte) (isWatched ? 1 : 0));
        dest.writeByte((byte) (isFavorite ? 1 : 0));

        if (timeAdded == null)
            dest.writeLong(-1);
        else
            dest.writeLong(timeAdded.getTimeInMillis());

        dest.writeTypedList(genres);
        dest.writeTypedList(reviews);
        dest.writeTypedList(cast);
        dest.writeTypedList(crew);

        dest.writeByte((byte) (isExtraInfoLoaded ? 1 : 0));
    }

    //endregion

    //region Inner Classes

    /**
     * Represents movie folders.
     */
    public static interface Folders {

        //region Constants

        /**
         * Holds a constant for folder: "Wishlist".
         */
        public static final int WISHLIST = 1;

        /**
         * Holds a constant for folder: "My Movies".
         */
        public static final int MY_MOVIES = 2;

        //endregion

    }

    /**
     * Represents a comparator to sort a list of {@link Movie} by title.
     */
    public static class TitleComparator implements Comparator<Movie> {
        @Override
        public int compare(Movie o1, Movie o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    }

    /**
     * Represents a comparator to sort a list of {@link Movie} by year.
     */
    public static class YearComparator implements Comparator<Movie> {
        @Override
        public int compare(Movie o1, Movie o2) {
            return o1.getYear().compareTo(o2.getYear());
        }
    }

    /**
     * Represents a comparator to sort a list of {@link Movie} by rating (vote average).
     */
    public static class RatingComparator implements Comparator<Movie> {
        @Override
        public int compare(Movie o1, Movie o2) {
            return o1.getVoteAverage().compareTo(o2.getVoteAverage());
        }
    }

    /**
     * Represents a comparator to sort a list of {@link Movie} by user rating.
     */
    public static class UserRatingComparator implements Comparator<Movie> {
        @Override
        public int compare(Movie o1, Movie o2) {
            return o1.getRating().compareTo(o2.getRating());
        }
    }

    /**
     * Represents a comparator to sort a list of {@link Movie} by date added.
     */
    public static class DateAddedComparator implements Comparator<Movie> {
        @Override
        public int compare(Movie o1, Movie o2) {
            return o1.getTimeAdded().compareTo(o2.getTimeAdded());
        }
    }

    /**
     * Represents a comparator to sort a list of {@link Movie} by popularity.
     */
    public static class PopularityComparator implements Comparator<Movie> {
        @Override
        public int compare(Movie o1, Movie o2) {
            return o1.getPopularity().compareTo(o2.getPopularity());
        }
    }

    //endregion

}
