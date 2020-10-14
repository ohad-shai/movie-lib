package com.ohadshai.movielib.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ohadshai.movielib.entities.Genre;
import com.ohadshai.movielib.entities.Movie;
import com.ohadshai.movielib.entities.Person;
import com.ohadshai.movielib.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Represents a handler for database interactions.
 * Created by Ohad on 9/23/2016.
 */
public class DBHandler {

    //region Private Members

    /**
     * Holds the instance of the DBHandler for all the application.
     */
    private static DBHandler _instance;

    /**
     * Holds the _helper of the database interactions.
     */
    private DBHelper _helper;

    //endregion

    //region (Database Interactions) Public Members

    /**
     * Holds the database interaction with: "Movies".
     */
    public Movies movies;

    /**
     * Holds the database interaction with: "Movie Genres".
     */
    public MovieGenres movieGenres;

    /**
     * Holds the database interaction with: "Genres".
     */
    public Genres genres;

    /**
     * Holds the database interaction with: "People".
     */
    public People people;

    //endregion

    //region C'tors

    /**
     * C'tor
     * Initializes a new instance of a handler for database interactions.
     *
     * @param context The context of the handler owner.
     */
    private DBHandler(Context context) {
        _helper = new DBHelper(context, DBConsts.DB_NAME, null, DBConsts.DB_VERSION);

        // Initializes the database interaction objects:
        this.movies = new Movies();
        this.movieGenres = new MovieGenres();
        this.genres = new Genres();
        this.people = new People();
    }

    //endregion

    //region Public Static API

    /**
     * Gets the DBHandler instance of the application, or creates a new instance if null.
     *
     * @param context The context of the DBHandler owner.
     * @return Returns the DBHandler instance of the application.
     */
    public static DBHandler getInstance(Context context) {
        if (_instance == null)
            _instance = new DBHandler(context.getApplicationContext());

        return _instance;
    }

    //endregion

    //region Inner Classes

    /**
     * Represents the database interaction with: "Movies".
     */
    public class Movies {

        /**
         * Creates a new {@link Movie} in the database.
         *
         * @param movie The {@link Movie} object to create.
         * @return Returns the {@link Movie} id created in the database.
         */
        public int create(Movie movie) {
            if (movie == null)
                throw new NullPointerException("movie");

            SQLiteDatabase db = _helper.getWritableDatabase();
            db.beginTransaction();

            ContentValues values = new ContentValues();
            values.put(DBConsts.Table_Movies.COLUMN_TMDBId, movie.getTMDBId());
            values.put(DBConsts.Table_Movies.COLUMN_IMDbId, movie.getIMDbId());
            values.put(DBConsts.Table_Movies.COLUMN_Title, movie.getTitle());
            values.put(DBConsts.Table_Movies.COLUMN_OriginalTitle, movie.getOriginalTitle());
            if (movie.getReleaseDate() != null)
                values.put(DBConsts.Table_Movies.COLUMN_ReleaseDate, movie.getReleaseDate().getTimeInMillis());
            values.put(DBConsts.Table_Movies.COLUMN_Plot, movie.getPlot());
            values.put(DBConsts.Table_Movies.COLUMN_PosterPath, movie.getPosterPath());
            values.put(DBConsts.Table_Movies.COLUMN_BackdropPath, movie.getBackdropPath());
            values.put(DBConsts.Table_Movies.COLUMN_OriginalLanguage, movie.getOriginalTitle());
            values.put(DBConsts.Table_Movies.COLUMN_Popularity, movie.getPopularity());
            values.put(DBConsts.Table_Movies.COLUMN_Homepage, movie.getHomepage());
            values.put(DBConsts.Table_Movies.COLUMN_Budget, movie.getBudget());
            values.put(DBConsts.Table_Movies.COLUMN_Revenue, movie.getRevenue());
            values.put(DBConsts.Table_Movies.COLUMN_Runtime, movie.getRuntime());
            values.put(DBConsts.Table_Movies.COLUMN_Status, movie.getStatus());
            values.put(DBConsts.Table_Movies.COLUMN_Tagline, movie.getTagline());
            values.put(DBConsts.Table_Movies.COLUMN_VoteAverage, movie.getVoteAverage());
            values.put(DBConsts.Table_Movies.COLUMN_VoteCount, movie.getVoteCount());
            values.put(DBConsts.Table_Movies.COLUMN_IsAdult, movie.isAdult());
            values.put(DBConsts.Table_Movies.COLUMN_Folder, movie.getFolder());
            values.put(DBConsts.Table_Movies.COLUMN_Rating, movie.getRating());
            values.put(DBConsts.Table_Movies.COLUMN_IsWatched, movie.isWatched());
            values.put(DBConsts.Table_Movies.COLUMN_IsFavorite, movie.isFavorite());
            values.put(DBConsts.Table_Movies.COLUMN_TimeAdded, movie.getTimeAdded().getTimeInMillis());

            int movieId = (int) db.insert(DBConsts.Table_Movies.TABLE_NAME, null, values);

            //region Inserts the movie genres...

            for (Genre genre : movie.getGenres()) {
                values = new ContentValues();
                values.put(DBConsts.Table_MovieVsGenres.COLUMN_MovieId, movieId);
                values.put(DBConsts.Table_MovieVsGenres.COLUMN_GenreId, genre.getId());
                db.insert(DBConsts.Table_MovieVsGenres.TABLE_NAME, null, values);
            }

            //endregion

            db.setTransactionSuccessful();
            db.endTransaction();

            return movieId;
        }

        /**
         * Updates a {@link Movie} from the database.
         *
         * @param movie The {@link Movie} entity to update from the database, contains the new values.
         */
        public void update(Movie movie) {
            if (movie == null)
                throw new NullPointerException("movie");

            SQLiteDatabase db = _helper.getWritableDatabase();
            db.beginTransaction();

            ContentValues values = new ContentValues();
            values.put(DBConsts.Table_Movies.COLUMN_TMDBId, movie.getTMDBId());
            values.put(DBConsts.Table_Movies.COLUMN_IMDbId, movie.getIMDbId());
            values.put(DBConsts.Table_Movies.COLUMN_Title, movie.getTitle());
            values.put(DBConsts.Table_Movies.COLUMN_OriginalTitle, movie.getOriginalTitle());
            if (movie.getReleaseDate() != null)
                values.put(DBConsts.Table_Movies.COLUMN_ReleaseDate, movie.getReleaseDate().getTimeInMillis());
            values.put(DBConsts.Table_Movies.COLUMN_Plot, movie.getPlot());
            values.put(DBConsts.Table_Movies.COLUMN_PosterPath, movie.getPosterPath());
            values.put(DBConsts.Table_Movies.COLUMN_BackdropPath, movie.getBackdropPath());
            values.put(DBConsts.Table_Movies.COLUMN_OriginalLanguage, movie.getOriginalTitle());
            values.put(DBConsts.Table_Movies.COLUMN_Popularity, movie.getPopularity());
            values.put(DBConsts.Table_Movies.COLUMN_Homepage, movie.getHomepage());
            values.put(DBConsts.Table_Movies.COLUMN_Budget, movie.getBudget());
            values.put(DBConsts.Table_Movies.COLUMN_Revenue, movie.getRevenue());
            values.put(DBConsts.Table_Movies.COLUMN_Runtime, movie.getRuntime());
            values.put(DBConsts.Table_Movies.COLUMN_Status, movie.getStatus());
            values.put(DBConsts.Table_Movies.COLUMN_Tagline, movie.getTagline());
            values.put(DBConsts.Table_Movies.COLUMN_VoteAverage, movie.getVoteAverage());
            values.put(DBConsts.Table_Movies.COLUMN_VoteCount, movie.getVoteCount());
            values.put(DBConsts.Table_Movies.COLUMN_IsAdult, movie.isAdult());
            values.put(DBConsts.Table_Movies.COLUMN_Folder, movie.getFolder());
            values.put(DBConsts.Table_Movies.COLUMN_Rating, movie.getRating());
            values.put(DBConsts.Table_Movies.COLUMN_IsWatched, movie.isWatched());
            values.put(DBConsts.Table_Movies.COLUMN_IsFavorite, movie.isFavorite());
            values.put(DBConsts.Table_Movies.COLUMN_TimeAdded, movie.getTimeAdded().getTimeInMillis());

            db.update(DBConsts.Table_Movies.TABLE_NAME, values, DBConsts.Table_Movies.COLUMN_id + "=" + movie.getId(), null);

            // Deletes all the movie genres:
            db.delete(DBConsts.Table_MovieVsGenres.TABLE_NAME, DBConsts.Table_MovieVsGenres.COLUMN_MovieId + "=" + movie.getId(), null);

            //region Inserts the movie genres...

            for (Genre genre : movie.getGenres()) {
                values = new ContentValues();
                values.put(DBConsts.Table_MovieVsGenres.COLUMN_MovieId, movie.getId());
                values.put(DBConsts.Table_MovieVsGenres.COLUMN_GenreId, genre.getId());
                db.insert(DBConsts.Table_MovieVsGenres.TABLE_NAME, null, values);
            }

            //endregion

            db.setTransactionSuccessful();
            db.endTransaction();
        }

        /**
         * Updates a folder related of a {@link Movie} from the database.
         *
         * @param movie The {@link Movie} entity to update folder related from the database, contains the new values.
         */
        public void updateFolderRelated(Movie movie) {
            if (movie == null)
                throw new NullPointerException("movie");

            SQLiteDatabase db = _helper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DBConsts.Table_Movies.COLUMN_Folder, movie.getFolder());
            values.put(DBConsts.Table_Movies.COLUMN_Rating, movie.getRating());
            values.put(DBConsts.Table_Movies.COLUMN_IsWatched, movie.isWatched());
            values.put(DBConsts.Table_Movies.COLUMN_IsFavorite, movie.isFavorite());
            values.put(DBConsts.Table_Movies.COLUMN_TimeAdded, movie.getTimeAdded().getTimeInMillis());

            db.update(DBConsts.Table_Movies.TABLE_NAME, values, DBConsts.Table_Movies.COLUMN_id + "=" + movie.getId(), null);
        }

        /**
         * Deletes a {@link Movie} from the database.
         *
         * @param movieId The {@link Movie} id to delete.
         */
        public void delete(int movieId) {
            if (movieId < 1)
                throw new IllegalArgumentException("movieId: " + movieId);

            SQLiteDatabase db = _helper.getWritableDatabase();
            db.delete(DBConsts.Table_Movies.TABLE_NAME, DBConsts.Table_Movies.COLUMN_id + "=" + movieId, null);
        }

        /**
         * Gets all the movies in the "My Movies" folder.
         *
         * @return Returns a list of all the movies in the "My Movies" folder.
         */
        public ArrayList<Movie> getAllMyMovies() {
            ArrayList<Movie> list = new ArrayList<>();
            SQLiteDatabase db = _helper.getReadableDatabase();

            Cursor cursor = db.query(DBConsts.Table_Movies.TABLE_NAME, null, DBConsts.Table_Movies.COLUMN_Folder + "=" + Movie.Folders.MY_MOVIES, null, null, null, null);

            while (cursor.moveToNext())
                list.add(this.createMovieFromCursor(cursor));

            cursor.close();
            return list;
        }

        /**
         * Gets all the movies in the "Wishlist" folder.
         *
         * @return Returns a list of all the movies in the "Wishlist" folder.
         */
        public ArrayList<Movie> getAllWishlist() {
            ArrayList<Movie> list = new ArrayList<>();
            SQLiteDatabase db = _helper.getReadableDatabase();

            Cursor cursor = db.query(DBConsts.Table_Movies.TABLE_NAME, null, DBConsts.Table_Movies.COLUMN_Folder + "=" + Movie.Folders.WISHLIST, null, null, null, null);

            while (cursor.moveToNext())
                list.add(this.createMovieFromCursor(cursor));

            cursor.close();
            return list;
        }

        /**
         * Gets a {@link Movie} by the id from the database.
         *
         * @param id The id of the {@link Movie} to get.
         * @return Returns the {@link Movie} object if found, otherwise null.
         */
        public Movie getById(int id) {
            if (id < 1)
                throw new IllegalArgumentException("id");

            SQLiteDatabase db = _helper.getReadableDatabase();

            Cursor cursor = db.query(DBConsts.Table_Movies.TABLE_NAME, null, DBConsts.Table_Movies.COLUMN_id + "=" + id, null, null, null, null);

            Movie movie = null;
            if (cursor.moveToFirst())
                movie = this.createMovieFromCursor(cursor);

            cursor.close();
            return movie;
        }

        /**
         * Gets a {@link Movie} by the TMDB id from the database.
         *
         * @param tmdbId The TMDB id of the {@link Movie} to get.
         * @return Returns the {@link Movie} object if found, otherwise null.
         */
        public Movie getByTMDBId(int tmdbId) {
            if (tmdbId < 1)
                throw new IllegalArgumentException("tmdbId");

            SQLiteDatabase db = _helper.getReadableDatabase();

            Cursor cursor = db.query(DBConsts.Table_Movies.TABLE_NAME, null, DBConsts.Table_Movies.COLUMN_TMDBId + "=" + tmdbId, null, null, null, null);

            Movie movie = null;
            if (cursor.moveToFirst())
                movie = this.createMovieFromCursor(cursor);

            cursor.close();
            return movie;
        }

        /**
         * Deletes all the data associated with "Wishlist" folder in the database.
         */
        public void deleteAllWishlist() {
            SQLiteDatabase db = _helper.getWritableDatabase();
            db.delete(DBConsts.Table_Movies.TABLE_NAME, DBConsts.Table_Movies.COLUMN_Folder + "=" + Movie.Folders.WISHLIST, null);
        }

        /**
         * Deletes all the data associated with "My Movies" folder in the database.
         */
        public void deleteAllMyMovies() {
            SQLiteDatabase db = _helper.getWritableDatabase();
            db.delete(DBConsts.Table_Movies.TABLE_NAME, DBConsts.Table_Movies.COLUMN_Folder + "=" + Movie.Folders.MY_MOVIES, null);
        }

        //region Private Methods

        /**
         * Creates a {@link Movie} object from the cursor.
         *
         * @param cursor The cursor to get the movie values from.
         * @return Returns the movie object created from the cursor.
         */
        private Movie createMovieFromCursor(Cursor cursor) {
            Movie movie = new Movie();

            int id = cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_id));
            movie.setId(id);
            movie.setTMDBId(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_TMDBId)));
            if (movie.getTMDBId() != null && movie.getTMDBId() == 0)
                movie.setTMDBId(null);
            movie.setIMDbId(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_IMDbId)));
            movie.setTitle(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_Title)));
            movie.setOriginalTitle(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_OriginalTitle)));

            if (!cursor.isNull(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_ReleaseDate))) {
                movie.setReleaseDate(Calendar.getInstance());
                movie.getReleaseDate().setTimeInMillis(cursor.getLong(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_ReleaseDate)));
            }

            movie.setPlot(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_Plot)));
            movie.setPosterPath(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_PosterPath)));
            movie.setBackdropPath(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_BackdropPath)));
            movie.setOriginalLanguage(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_OriginalLanguage)));
            movie.setPopularity(cursor.getFloat(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_Popularity)));
            movie.setHomepage(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_Homepage)));
            movie.setBudget(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_Budget)));
            movie.setRevenue(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_Revenue)));
            movie.setRuntime(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_Runtime)));
            movie.setStatus(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_Status)));
            movie.setTagline(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_Tagline)));
            movie.setVoteAverage(cursor.getFloat(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_VoteAverage)));
            movie.setVoteCount(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_VoteCount)));
            movie.setAdult(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_IsAdult)) > 0);
            movie.setFolder(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_Folder)));
            if (!cursor.isNull(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_Rating)))
                movie.setRating(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_Rating)));
            movie.setWatched(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_IsWatched)) > 0);
            movie.setFavorite(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_IsFavorite)) > 0);

            movie.setTimeAdded(Calendar.getInstance());
            movie.getTimeAdded().setTimeInMillis(cursor.getLong(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Movies.COLUMN_TimeAdded)));

            movie.setGenres(movieGenres.getGenresForMovieById(id));

            return movie;
        }

        //endregion

    }

    /**
     * Represents the database interaction with: "Movie Genres".
     */
    public class MovieGenres {

        /**
         * Gets a list of movie genres for a movie by the movie id.
         *
         * @param movieId The id of the movie to get it's genres.
         * @return Returns the list of the movie genres.
         */
        public ArrayList<Genre> getGenresForMovieById(int movieId) {
            if (movieId < 1)
                throw new IllegalArgumentException("movieId");

            SQLiteDatabase db = _helper.getReadableDatabase();

            String query = "SELECT * FROM " + DBConsts.Table_MovieVsGenres.TABLE_NAME + " mg " +
                    " INNER JOIN " + DBConsts.Table_Genres.TABLE_NAME + " g ON mg." + DBConsts.Table_MovieVsGenres.COLUMN_GenreId + "=g." + DBConsts.Table_Genres.COLUMN_TMDBId +
                    " WHERE mg." + DBConsts.Table_MovieVsGenres.COLUMN_MovieId + "=" + movieId;
            Cursor cursor = db.rawQuery(query, null);

            ArrayList<Genre> genres = new ArrayList<>();
            while (cursor.moveToNext())
                genres.add(this.createGenreFromCursor(cursor));

            cursor.close();
            return genres;
        }

        //region Private Methods

        /**
         * Creates a {@link Genre} object from the cursor.
         *
         * @param cursor The cursor to get the genre values from.
         * @return Returns the genre object created from the cursor.
         */
        private Genre createGenreFromCursor(Cursor cursor) {
            return new Genre(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Genres.COLUMN_TMDBId)),
                    cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Genres.COLUMN_GenreName)));
        }

        //endregion

    }

    /**
     * Represents the database interaction with: "Genres".
     */
    public class Genres {

        /**
         * Create a genres list in the database.
         *
         * @param genres The genres list to create in the database.
         */
        public void createList(ArrayList<Genre> genres) {
            if (genres == null)
                throw new NullPointerException("genres");

            SQLiteDatabase db = _helper.getWritableDatabase();
            db.beginTransaction();

            for (Genre genre : genres) {
                ContentValues values = new ContentValues();
                values.put(DBConsts.Table_Genres.COLUMN_TMDBId, genre.getId());
                values.put(DBConsts.Table_Genres.COLUMN_GenreName, genre.getName());

                db.insert(DBConsts.Table_Genres.TABLE_NAME, null, values);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
        }

        /**
         * Gets all the genres list in the database.
         *
         * @return Returns a list of all the genres in the database.
         */
        public ArrayList<Genre> getAll() {
            ArrayList<Genre> list = new ArrayList<>();
            SQLiteDatabase db = _helper.getReadableDatabase();

            Cursor cursor = db.query(DBConsts.Table_Genres.TABLE_NAME, null, null, null, null, null, null);

            while (cursor.moveToNext())
                list.add(this.createGenreFromCursor(cursor));

            return list;
        }

        //region Private Methods

        /**
         * Creates a {@link Genre} object from the cursor.
         *
         * @param cursor The cursor to get the genre values from.
         * @return Returns the genre object created from the cursor.
         */
        private Genre createGenreFromCursor(Cursor cursor) {
            return new Genre(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Genres.COLUMN_TMDBId)),
                    cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_Genres.COLUMN_GenreName)));
        }

        //endregion

    }

    /**
     * Represents the database interaction with: "People".
     */
    public class People {

        /**
         * Creates a new {@link Person} in the database.
         *
         * @param person The {@link Person} object to create.
         * @return Returns the {@link Person} id created in the database.
         */
        public int create(Person person) {
            if (person == null)
                throw new NullPointerException("person");

            SQLiteDatabase db = _helper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DBConsts.Table_People.COLUMN_TMDBId, person.getTMDBId());
            values.put(DBConsts.Table_People.COLUMN_IMDbId, person.getIMDbId());
            values.put(DBConsts.Table_People.COLUMN_Name, person.getName());
            values.put(DBConsts.Table_People.COLUMN_Biography, person.getBiography());
            values.put(DBConsts.Table_People.COLUMN_ProfilePath, person.getProfilePath());
            values.put(DBConsts.Table_People.COLUMN_Gender, person.getGender());
            if (person.getBirthday() != null)
                values.put(DBConsts.Table_People.COLUMN_Birthday, person.getBirthday().getTimeInMillis());
            if (person.getDeathday() != null)
                values.put(DBConsts.Table_People.COLUMN_Deathday, person.getDeathday().getTimeInMillis());
            values.put(DBConsts.Table_People.COLUMN_Homepage, person.getHomepage());
            values.put(DBConsts.Table_People.COLUMN_PlaceOfBirth, person.getPlaceOfBirth());
            values.put(DBConsts.Table_People.COLUMN_Popularity, person.getPopularity());
            values.put(DBConsts.Table_People.COLUMN_Facebook, person.getFacebook());
            values.put(DBConsts.Table_People.COLUMN_Instagram, person.getInstagram());
            values.put(DBConsts.Table_People.COLUMN_Twitter, person.getTwitter());
            values.put(DBConsts.Table_People.COLUMN_Folder, person.getFolder());
            values.put(DBConsts.Table_People.COLUMN_IsFavorite, person.isFavorite());
            values.put(DBConsts.Table_People.COLUMN_TimeAdded, person.getTimeAdded().getTimeInMillis());

            return (int) db.insert(DBConsts.Table_People.TABLE_NAME, null, values);
        }

        /**
         * Updates a {@link Person} from the database.
         *
         * @param person The {@link Person} entity to update from the database, contains the new values.
         */
        public void update(Person person) {
            if (person == null)
                throw new NullPointerException("person");

            SQLiteDatabase db = _helper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DBConsts.Table_People.COLUMN_TMDBId, person.getTMDBId());
            values.put(DBConsts.Table_People.COLUMN_IMDbId, person.getIMDbId());
            values.put(DBConsts.Table_People.COLUMN_Name, person.getName());
            values.put(DBConsts.Table_People.COLUMN_Biography, person.getBiography());
            values.put(DBConsts.Table_People.COLUMN_ProfilePath, person.getProfilePath());
            values.put(DBConsts.Table_People.COLUMN_Gender, person.getGender());
            if (person.getBirthday() != null)
                values.put(DBConsts.Table_People.COLUMN_Birthday, person.getBirthday().getTimeInMillis());
            if (person.getDeathday() != null)
                values.put(DBConsts.Table_People.COLUMN_Deathday, person.getDeathday().getTimeInMillis());
            values.put(DBConsts.Table_People.COLUMN_Homepage, person.getHomepage());
            values.put(DBConsts.Table_People.COLUMN_PlaceOfBirth, person.getPlaceOfBirth());
            values.put(DBConsts.Table_People.COLUMN_Popularity, person.getPopularity());
            values.put(DBConsts.Table_People.COLUMN_Facebook, person.getFacebook());
            values.put(DBConsts.Table_People.COLUMN_Instagram, person.getInstagram());
            values.put(DBConsts.Table_People.COLUMN_Twitter, person.getTwitter());
            values.put(DBConsts.Table_People.COLUMN_Folder, person.getFolder());
            values.put(DBConsts.Table_People.COLUMN_IsFavorite, person.isFavorite());
            values.put(DBConsts.Table_People.COLUMN_TimeAdded, person.getTimeAdded().getTimeInMillis());

            db.update(DBConsts.Table_People.TABLE_NAME, values, DBConsts.Table_People.COLUMN_id + "=" + person.getId(), null);
        }

        /**
         * Updates a folder related of a {@link Person} from the database.
         *
         * @param person The {@link Person} entity to update folder related from the database, contains the new values.
         */
        public void updateFolderRelated(Person person) {
            if (person == null)
                throw new NullPointerException("person");

            SQLiteDatabase db = _helper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DBConsts.Table_People.COLUMN_Folder, person.getFolder());
            values.put(DBConsts.Table_People.COLUMN_IsFavorite, person.isFavorite());
            values.put(DBConsts.Table_People.COLUMN_TimeAdded, person.getTimeAdded().getTimeInMillis());

            db.update(DBConsts.Table_People.TABLE_NAME, values, DBConsts.Table_People.COLUMN_id + "=" + person.getId(), null);
        }

        /**
         * Deletes a {@link Person} from the database.
         *
         * @param personId The {@link Person id to delete.
         */
        public void delete(int personId) {
            if (personId < 1)
                throw new IllegalArgumentException("personId: " + personId);

            SQLiteDatabase db = _helper.getWritableDatabase();
            db.delete(DBConsts.Table_People.TABLE_NAME, DBConsts.Table_People.COLUMN_id + "=" + personId, null);
        }

        /**
         * Gets all the people in the "My Actors" folder.
         *
         * @return Returns a list of all the people in the "My Actors" folder.
         */
        public ArrayList<Person> getAllMyActors() {
            ArrayList<Person> list = new ArrayList<>();
            SQLiteDatabase db = _helper.getReadableDatabase();

            Cursor cursor = db.query(DBConsts.Table_People.TABLE_NAME, null, DBConsts.Table_People.COLUMN_Folder + "=" + Person.Folders.MY_ACTORS, null, null, null, null);

            while (cursor.moveToNext())
                list.add(this.createPersonFromCursor(cursor));

            cursor.close();
            return list;
        }

        /**
         * Gets all the people in the "My Directors" folder.
         *
         * @return Returns a list of all the people in the "My Directors" folder.
         */
        public ArrayList<Person> getAllMyDirectors() {
            ArrayList<Person> list = new ArrayList<>();
            SQLiteDatabase db = _helper.getReadableDatabase();

            Cursor cursor = db.query(DBConsts.Table_People.TABLE_NAME, null, DBConsts.Table_People.COLUMN_Folder + "=" + Person.Folders.MY_DIRECTORS, null, null, null, null);

            while (cursor.moveToNext())
                list.add(this.createPersonFromCursor(cursor));

            cursor.close();
            return list;
        }

        /**
         * Gets a {@link Person} by the id from the database.
         *
         * @param id The id of the {@link Person} to get.
         * @return Returns the {@link Person} object if found, otherwise null.
         */
        public Person getById(int id) {
            if (id < 1)
                throw new IllegalArgumentException("id");

            SQLiteDatabase db = _helper.getReadableDatabase();

            Cursor cursor = db.query(DBConsts.Table_People.TABLE_NAME, null, DBConsts.Table_People.COLUMN_id + "=" + id, null, null, null, null);

            Person person = null;
            if (cursor.moveToFirst())
                person = this.createPersonFromCursor(cursor);

            cursor.close();
            return person;
        }

        /**
         * Gets a {@link Person} by the TMDB id from the database.
         *
         * @param tmdbId The TMDB id of the {@link Person} to get.
         * @return Returns the {@link Person} object if found, otherwise null.
         */
        public Person getByTMDBId(int tmdbId) {
            if (tmdbId < 1)
                throw new IllegalArgumentException("tmdbId");

            SQLiteDatabase db = _helper.getReadableDatabase();

            Cursor cursor = db.query(DBConsts.Table_People.TABLE_NAME, null, DBConsts.Table_People.COLUMN_TMDBId + "=" + tmdbId, null, null, null, null);

            Person person = null;
            if (cursor.moveToFirst())
                person = this.createPersonFromCursor(cursor);

            cursor.close();
            return person;
        }

        /**
         * Deletes all the data associated with "My Actors" folder in the database.
         */
        public void deleteAllMyActors() {
            SQLiteDatabase db = _helper.getWritableDatabase();
            db.delete(DBConsts.Table_People.TABLE_NAME, DBConsts.Table_People.COLUMN_Folder + "=" + Person.Folders.MY_ACTORS, null);
        }

        /**
         * Deletes all the data associated with "My Directors" folder in the database.
         */
        public void deleteAllMyDirectors() {
            SQLiteDatabase db = _helper.getWritableDatabase();
            db.delete(DBConsts.Table_People.TABLE_NAME, DBConsts.Table_People.COLUMN_Folder + "=" + Person.Folders.MY_DIRECTORS, null);
        }

        //region Private Methods

        /**
         * Creates a {@link Person} object from the cursor.
         *
         * @param cursor The cursor to get the person values from.
         * @return Returns the person object created from the cursor.
         */
        private Person createPersonFromCursor(Cursor cursor) {
            Person person = new Person();

            int id = cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_id));
            person.setId(id);
            person.setTMDBId(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_TMDBId)));
            if (person.getTMDBId() != null && person.getTMDBId() == 0)
                person.setTMDBId(null);
            person.setIMDbId(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_IMDbId)));
            person.setName(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_Name)));
            person.setBiography(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_Biography)));
            person.setProfilePath(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_ProfilePath)));
            person.setGender(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_Gender)));

            if (!cursor.isNull(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_Birthday))) {
                person.setBirthday(Calendar.getInstance());
                person.getBirthday().setTimeInMillis(cursor.getLong(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_Birthday)));
            }

            if (!cursor.isNull(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_Deathday))) {
                person.setDeathday(Calendar.getInstance());
                person.getDeathday().setTimeInMillis(cursor.getLong(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_Deathday)));
            }

            person.setHomepage(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_Homepage)));
            person.setPlaceOfBirth(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_PlaceOfBirth)));
            person.setPopularity(cursor.getFloat(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_Popularity)));
            person.setFacebook(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_Facebook)));
            person.setInstagram(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_Instagram)));
            person.setTwitter(cursor.getString(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_Twitter)));
            person.setFolder(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_Folder)));
            person.setFavorite(cursor.getInt(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_IsFavorite)) > 0);

            person.setTimeAdded(Calendar.getInstance());
            person.getTimeAdded().setTimeInMillis(cursor.getLong(Utils.Cursors.getColumnIndex(cursor, DBConsts.Table_People.COLUMN_TimeAdded)));

            return person;
        }

        //endregion

    }

    //endregion

}
