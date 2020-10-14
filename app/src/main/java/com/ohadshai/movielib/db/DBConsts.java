package com.ohadshai.movielib.db;

/**
 * Holds all the constants for the database.
 * Created by Ohad on 9/23/2016.
 */
public interface DBConsts {

    //region Constants

    /**
     * Holds a constant for the database name.
     */
    public final static String DB_NAME = "MovieLib_DB.db";

    /**
     * Holds a constant for the database version.
     */
    public final static int DB_VERSION = 1;

    //endregion

    /**
     * Holds all the constants for the "Movies" table.
     */
    public interface Table_Movies {

        /**
         * Holds a constant for the table name "Movies".
         */
        public final static String TABLE_NAME = "[Movies]";

        /**
         * Holds a constant for the [Movie Id] column name, in the "Movies" table.
         */
        public final static String COLUMN_id = "[_id]";

        /**
         * Holds a constant for the [Movie TMDBId] column name, in the "Movies" table.
         */
        public final static String COLUMN_TMDBId = "[TMDBId]";

        /**
         * Holds a constant for the [Movie IMDbId] column name, in the "Movies" table.
         */
        public final static String COLUMN_IMDbId = "[IMDbId]";

        /**
         * Holds a constant for the [Movie Title] column name, in the "Movies" table.
         */
        public final static String COLUMN_Title = "[Title]";

        /**
         * Holds a constant for the [Movie Original Title] column name, in the "Movies" table.
         */
        public final static String COLUMN_OriginalTitle = "[OriginalTitle]";

        /**
         * Holds a constant for the [Movie Release Date] column name, in the "Movies" table.
         */
        public final static String COLUMN_ReleaseDate = "[ReleaseDate]";

        /**
         * Holds a constant for the [Movie Plot] column name, in the "Movies" table.
         */
        public final static String COLUMN_Plot = "[Plot]";

        /**
         * Holds a constant for the [Movie Poster Path] column name, in the "Movies" table.
         */
        public final static String COLUMN_PosterPath = "[PosterPath]";

        /**
         * Holds a constant for the [Movie Backdrop Path] column name, in the "Movies" table.
         */
        public final static String COLUMN_BackdropPath = "[BackdropPath]";

        /**
         * Holds a constant for the [Movie Original Language] column name, in the "Movies" table.
         */
        public final static String COLUMN_OriginalLanguage = "[OriginalLanguage]";

        /**
         * Holds a constant for the [Movie Popularity] column name, in the "Movies" table.
         */
        public final static String COLUMN_Popularity = "[Popularity]";

        /**
         * Holds a constant for the [Movie Homepage] column name, in the "Movies" table.
         */
        public final static String COLUMN_Homepage = "[Homepage]";

        /**
         * Holds a constant for the [Movie Budget] column name, in the "Movies" table.
         */
        public final static String COLUMN_Budget = "[Budget]";

        /**
         * Holds a constant for the [Movie Revenue] column name, in the "Movies" table.
         */
        public final static String COLUMN_Revenue = "[Revenue]";

        /**
         * Holds a constant for the [Movie Runtime] column name, in the "Movies" table.
         */
        public final static String COLUMN_Runtime = "[Runtime]";

        /**
         * Holds a constant for the [Movie Status] column name, in the "Movies" table.
         */
        public final static String COLUMN_Status = "[Status]";

        /**
         * Holds a constant for the [Movie Tagline] column name, in the "Movies" table.
         */
        public final static String COLUMN_Tagline = "[Tagline]";

        /**
         * Holds a constant for the [Movie Vote Average] column name, in the "Movies" table.
         */
        public final static String COLUMN_VoteAverage = "[VoteAverage]";

        /**
         * Holds a constant for the [Movie Vote Count] column name, in the "Movies" table.
         */
        public final static String COLUMN_VoteCount = "[VoteCount]";

        /**
         * Holds a constant for the [Movie Is Adult] column name, in the "Movies" table.
         */
        public final static String COLUMN_IsAdult = "[IsAdult]";

        /**
         * Holds a constant for the [Movie Folder] column name, in the "Movies" table.
         */
        public final static String COLUMN_Folder = "[Folder]";

        /**
         * Holds a constant for the [Movie Rating] column name, in the "Movies" table.
         */
        public final static String COLUMN_Rating = "[Rating]";

        /**
         * Holds a constant for the [Movie Is Watched] column name, in the "Movies" table.
         */
        public final static String COLUMN_IsWatched = "[IsWatched]";

        /**
         * Holds a constant for the [Movie Is Favorite] column name, in the "Movies" table.
         */
        public final static String COLUMN_IsFavorite = "[IsFavorite]";

        /**
         * Holds a constant for the [Movie Time Added] column name, in the "Movies" table.
         */
        public final static String COLUMN_TimeAdded = "[TimeAdded]";

    }

    /**
     * Holds all the constants for the "Genres" table.
     */
    public interface Table_Genres {

        /**
         * Holds a constant for the table name "Genres".
         */
        public final static String TABLE_NAME = "[Genres]";

        /**
         * Holds a constant for the [Genre TMDBId] column name, in the "Genres" table.
         */
        public final static String COLUMN_TMDBId = "[TMDBId]";

        /**
         * Holds a constant for the [Genre Name] column name, in the "Genres" table.
         */
        public final static String COLUMN_GenreName = "[GenreName]";

    }

    /**
     * Holds all the constants for the "Movie Vs Genres" table.
     */
    public interface Table_MovieVsGenres {

        /**
         * Holds a constant for the table name "Movie Vs Genres".
         */
        public final static String TABLE_NAME = "[MovieVsGenres]";

        /**
         * Holds a constant for the [Movie Id] column name, in the "Movie Vs Genres" table.
         */
        public final static String COLUMN_MovieId = "[MovieId]";

        /**
         * Holds a constant for the [Genre Id] column name, in the "Movie Vs Genres" table.
         */
        public final static String COLUMN_GenreId = "[GenreId]";

    }

    /**
     * Holds all the constants for the "People" table.
     */
    public interface Table_People {

        /**
         * Holds a constant for the table name "People".
         */
        public final static String TABLE_NAME = "[People]";

        /**
         * Holds a constant for the [Person Id] column name, in the "People" table.
         */
        public final static String COLUMN_id = "[_id]";

        /**
         * Holds a constant for the [Person TMDBId] column name, in the "People" table.
         */
        public final static String COLUMN_TMDBId = "[TMDBId]";

        /**
         * Holds a constant for the [Person IMDbId] column name, in the "People" table.
         */
        public final static String COLUMN_IMDbId = "[IMDbId]";

        /**
         * Holds a constant for the [Person Name] column name, in the "People" table.
         */
        public final static String COLUMN_Name = "[Name]";

        /**
         * Holds a constant for the [Person Biography] column name, in the "People" table.
         */
        public final static String COLUMN_Biography = "[Biography]";

        /**
         * Holds a constant for the [Person Profile Path] column name, in the "People" table.
         */
        public final static String COLUMN_ProfilePath = "[ProfilePath]";

        /**
         * Holds a constant for the [Person Gender] column name, in the "People" table.
         */
        public final static String COLUMN_Gender = "[Gender]";

        /**
         * Holds a constant for the [Person Birthday] column name, in the "People" table.
         */
        public final static String COLUMN_Birthday = "[Birthday]";

        /**
         * Holds a constant for the [Person Deathday] column name, in the "People" table.
         */
        public final static String COLUMN_Deathday = "[Deathday]";

        /**
         * Holds a constant for the [Person Homepage] column name, in the "People" table.
         */
        public final static String COLUMN_Homepage = "[Homepage]";

        /**
         * Holds a constant for the [Person Place of Birth] column name, in the "People" table.
         */
        public final static String COLUMN_PlaceOfBirth = "[PlaceOfBirth]";

        /**
         * Holds a constant for the [Person Popularity] column name, in the "People" table.
         */
        public final static String COLUMN_Popularity = "[Popularity]";

        /**
         * Holds a constant for the [Person Facebook] column name, in the "People" table.
         */
        public final static String COLUMN_Facebook = "[Facebook]";

        /**
         * Holds a constant for the [Person Instagram] column name, in the "People" table.
         */
        public final static String COLUMN_Instagram = "[Instagram]";

        /**
         * Holds a constant for the [Person Twitter] column name, in the "People" table.
         */
        public final static String COLUMN_Twitter = "[Twitter]";

        /**
         * Holds a constant for the [Person Folder] column name, in the "People" table.
         */
        public final static String COLUMN_Folder = "[Folder]";

        /**
         * Holds a constant for the [Person Is Favorite] column name, in the "People" table.
         */
        public final static String COLUMN_IsFavorite = "[IsFavorite]";

        /**
         * Holds a constant for the [Person Time Added] column name, in the "People" table.
         */
        public final static String COLUMN_TimeAdded = "[TimeAdded]";

    }

}
