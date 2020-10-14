package com.ohadshai.movielib.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ohadshai.movielib.entities.Genre;
import com.ohadshai.movielib.utils.Utils;
import com.ohadshai.movielib.utils.web_services.tmdb_api.TMDBService;

/**
 * Represents a helper for database interactions.
 * Created by Ohad on 9/23/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    //region Private Members

    /**
     * Holds the context owner of the DBHelper.
     */
    private Context _context;

    //endregion

    /**
     * Initializes a new instance of a helper for database interactions.
     *
     * @param context The context of the DBHelper owner.
     * @param name    The name of the database.
     * @param factory
     * @param version The version of the database.
     */
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        this._context = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if (!db.isReadOnly())
            db.execSQL("PRAGMA foreign_keys=ON;"); // Enables foreign key constraints.
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Logs the user information, when first initialization of the application:
        //Utils.UserLog.log(_context, Utils.UserLog.LOG_ON_CREATE);

        String tableMovies = " CREATE TABLE IF NOT EXISTS " + DBConsts.Table_Movies.TABLE_NAME + " (" +
                DBConsts.Table_Movies.COLUMN_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                DBConsts.Table_Movies.COLUMN_TMDBId + " INTEGER, " +
                DBConsts.Table_Movies.COLUMN_IMDbId + " TEXT, " +
                DBConsts.Table_Movies.COLUMN_Title + " TEXT NOT NULL, " +
                DBConsts.Table_Movies.COLUMN_OriginalTitle + " TEXT, " +
                DBConsts.Table_Movies.COLUMN_ReleaseDate + " INTEGER, " +
                DBConsts.Table_Movies.COLUMN_Plot + " TEXT, " +
                DBConsts.Table_Movies.COLUMN_PosterPath + " TEXT, " +
                DBConsts.Table_Movies.COLUMN_BackdropPath + " TEXT, " +
                DBConsts.Table_Movies.COLUMN_OriginalLanguage + " TEXT, " +
                DBConsts.Table_Movies.COLUMN_Popularity + " REAL, " +
                DBConsts.Table_Movies.COLUMN_Homepage + " TEXT, " +
                DBConsts.Table_Movies.COLUMN_Budget + " INTEGER, " +
                DBConsts.Table_Movies.COLUMN_Revenue + " INTEGER, " +
                DBConsts.Table_Movies.COLUMN_Runtime + " INTEGER, " +
                DBConsts.Table_Movies.COLUMN_Status + " TEXT, " +
                DBConsts.Table_Movies.COLUMN_Tagline + " TEXT, " +
                DBConsts.Table_Movies.COLUMN_VoteAverage + " REAL, " +
                DBConsts.Table_Movies.COLUMN_VoteCount + " INTEGER, " +
                DBConsts.Table_Movies.COLUMN_IsAdult + " INTEGER DEFAULT 0, " +
                DBConsts.Table_Movies.COLUMN_Folder + " INTEGER NOT NULL, " +
                DBConsts.Table_Movies.COLUMN_Rating + " INTEGER, " +
                DBConsts.Table_Movies.COLUMN_IsWatched + " INTEGER DEFAULT 0, " +
                DBConsts.Table_Movies.COLUMN_IsFavorite + " INTEGER DEFAULT 0, " +
                DBConsts.Table_Movies.COLUMN_TimeAdded + " INTEGER NOT NULL, " +
                " UNIQUE (" + DBConsts.Table_Movies.COLUMN_TMDBId + ") ON CONFLICT REPLACE " +
                "); ";

        String tableGenres = " CREATE TABLE IF NOT EXISTS " + DBConsts.Table_Genres.TABLE_NAME + " (" +
                DBConsts.Table_Genres.COLUMN_TMDBId + " INTEGER NOT NULL, " +
                DBConsts.Table_Genres.COLUMN_GenreName + " TEXT NOT NULL, " +
                " UNIQUE (" + DBConsts.Table_Genres.COLUMN_TMDBId + ") ON CONFLICT REPLACE " +
                "); ";

        // Needs FK from: [Movies], [Genres]:
        String tableMovieVsGenres = " CREATE TABLE IF NOT EXISTS " + DBConsts.Table_MovieVsGenres.TABLE_NAME + " (" +
                DBConsts.Table_MovieVsGenres.COLUMN_MovieId + " INTEGER NOT NULL, " +
                DBConsts.Table_MovieVsGenres.COLUMN_GenreId + " INTEGER NOT NULL, " +
                " UNIQUE (" + DBConsts.Table_MovieVsGenres.COLUMN_MovieId + "," + DBConsts.Table_MovieVsGenres.COLUMN_GenreId + ") ON CONFLICT REPLACE, " +
                " FOREIGN KEY (" + DBConsts.Table_MovieVsGenres.COLUMN_MovieId + ") REFERENCES " + DBConsts.Table_Movies.TABLE_NAME + " (" + DBConsts.Table_Movies.COLUMN_id + ") ON DELETE CASCADE, " +
                " FOREIGN KEY (" + DBConsts.Table_MovieVsGenres.COLUMN_GenreId + ") REFERENCES " + DBConsts.Table_Genres.TABLE_NAME + " (" + DBConsts.Table_Genres.COLUMN_TMDBId + ") ON DELETE CASCADE " +
                "); ";

        String tablePeople = " CREATE TABLE IF NOT EXISTS " + DBConsts.Table_People.TABLE_NAME + " (" +
                DBConsts.Table_People.COLUMN_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                DBConsts.Table_People.COLUMN_TMDBId + " INTEGER, " +
                DBConsts.Table_People.COLUMN_IMDbId + " TEXT, " +
                DBConsts.Table_People.COLUMN_Name + " TEXT NOT NULL, " +
                DBConsts.Table_People.COLUMN_Biography + " TEXT, " +
                DBConsts.Table_People.COLUMN_ProfilePath + " TEXT, " +
                DBConsts.Table_People.COLUMN_Gender + " INTEGER, " +
                DBConsts.Table_People.COLUMN_Birthday + " INTEGER, " +
                DBConsts.Table_People.COLUMN_Deathday + " INTEGER, " +
                DBConsts.Table_People.COLUMN_Homepage + " TEXT, " +
                DBConsts.Table_People.COLUMN_PlaceOfBirth + " TEXT, " +
                DBConsts.Table_People.COLUMN_Popularity + " REAL, " +
                DBConsts.Table_People.COLUMN_Facebook + " TEXT, " +
                DBConsts.Table_People.COLUMN_Instagram + " TEXT, " +
                DBConsts.Table_People.COLUMN_Twitter + " TEXT, " +
                DBConsts.Table_People.COLUMN_Folder + " INTEGER NOT NULL, " +
                DBConsts.Table_People.COLUMN_IsFavorite + " INTEGER DEFAULT 0, " +
                DBConsts.Table_People.COLUMN_TimeAdded + " INTEGER NOT NULL, " +
                " UNIQUE (" + DBConsts.Table_People.COLUMN_TMDBId + ") ON CONFLICT REPLACE " +
                "); ";

        // Executes the queries:
        db.execSQL(tableMovies);
        db.execSQL(tableGenres);
        db.execSQL(tableMovieVsGenres);
        db.execSQL(tablePeople);
        this.insertDefaultGenres(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2)
            this.upgrade1(db);

//        if (oldVersion < 3)
//            this.upgrade2(db);
    }

    //region Private Methods

    /**
     * Inserts the default "Genres" list to the database.
     *
     * @param db The {@link SQLiteDatabase} database object.
     */
    private void insertDefaultGenres(SQLiteDatabase db) {
        db.beginTransaction();

        for (Genre genre : TMDBService.DefaultData.genres) {
            ContentValues values = new ContentValues();
            values.put(DBConsts.Table_Genres.COLUMN_TMDBId, genre.getId());
            values.put(DBConsts.Table_Genres.COLUMN_GenreName, genre.getName());

            db.insert(DBConsts.Table_Genres.TABLE_NAME, null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * Method procedure for database upgrade number: 1.
     *
     * @param db The {@link SQLiteDatabase} database object.
     */
    private void upgrade1(SQLiteDatabase db) {
        String TABLE_NAME_MOVIES = "[Movies]"; // Table name "Movies".
        String TABLE_NAME_MOVIE_GENRES = "[Genres]"; // Table name "Genres".
        String TABLE_NAME_DIRECTORS = "[Directors]"; // Table name "Directors".
        String TABLE_NAME_ACTORS = "[Actors]"; // Table name "Actors".
        String TABLE_NAME_MOVIE_ACTORS = "[MovieActors]"; // Table name "MovieActors".
        String TABLE_NAME_FAV_MOVIES = "[FavoriteMovies]"; // Table name "FavoriteMovies".
        String TABLE_NAME_FAV_ACTORS = "[FavoriteActors]"; // Table name "FavoriteActors".

        // Drops all the tables:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MOVIE_GENRES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DIRECTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ACTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MOVIE_ACTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FAV_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FAV_ACTORS);

        onCreate(db);

        // Logs the user information, when updates the application:
        //Utils.UserLog.log(_context, Utils.UserLog.LOG_ON_UPDATE + "_upgrade1");
    }

    //endregion

}

