package com.ohadshai.movielib.ui;

/**
 * Holds all the constants for the UI.
 * Created by Ohad on 10/12/2016.
 */
public interface UIConsts {

    /**
     * Holds a constant for the application package name.
     */
    public static final String APP_PACKAGE_NAME = "com.ohadshai.movielib";

    /**
     * Holds all the constants for the Fragments.
     */
    public interface Fragments {

        /**
         * Holds a constant for the "In Theaters" fragment tag.
         */
        public static final String IN_THEATERS_FRAGMENT_TAG = "in_theaters_frag_tag";

        /**
         * Holds a constant for the "Popular Movies" fragment tag.
         */
        public static final String POPULAR_MOVIES_FRAGMENT_TAG = "popular_movies_frag_tag";

        /**
         * Holds a constant for the "Upcoming Movies" fragment tag.
         */
        public static final String UPCOMING_MOVIES_FRAGMENT_TAG = "upcoming_movies_frag_tag";

        /**
         * Holds a constant for the "Wishlist" fragment tag.
         */
        public static final String WISHLIST_FRAGMENT_TAG = "wishlist_frag_tag";

        /**
         * Holds a constant for the "My Movies" fragment tag.
         */
        public static final String MY_MOVIES_FRAGMENT_TAG = "my_movies_frag_tag";

        /**
         * Holds a constant for the "My Actors" fragment tag.
         */
        public static final String MY_ACTORS_FRAGMENT_TAG = "my_actors_frag_tag";

        /**
         * Holds a constant for the "Directors" fragment tag.
         */
        public static final String MY_DIRECTORS_FRAGMENT_TAG = "my_directors_frag_tag";

        /**
         * Holds a constant for the "Search Movies" fragment tag.
         */
        public static final String SEARCH_MOVIES_FRAGMENT_TAG = "search_movies_frag_tag";

        /**
         * Holds a constant for the "Search People" fragment tag.
         */
        public static final String SEARCH_PEOPLE_FRAGMENT_TAG = "search_people_frag_tag";

    }

    /**
     * Holds all the constants for the SharedPreferences.
     */
    public interface Preferences {

        /**
         * Represents preference keys in the {@link android.content.SharedPreferences}.
         */
        public static final class Keys {

            /**
             * Holds a constant for the "Language" preference key.
             */
            public static final String LANGUAGE = "pref_language_key";

            /**
             * Holds a constant for the "Region" preference key.
             */
            public static final String REGION = "pref_region_key";

            /**
             * Holds a constant for the "Auto-play trailers" preference key.
             */
            public static final String AUTOPLAY_TRAILERS = "pref_autoplay_trailers_key";

            /**
             * Holds a constant for the "Delete all data" preference key.
             */
            public static final String DELETE_ALL_DATA = "pref_delete_all_data_key";

            /**
             * Holds a constant for the "Wishlist Deleted" indicator key preference.
             */
            public static final String WISHLIST_DELETED = "pref_wishlist_deleted_key";

            /**
             * Holds a constant for the "My Movies Deleted" indicator key preference.
             */
            public static final String MY_MOVIES_DELETED = "pref_my_movies_deleted_key";

            /**
             * Holds a constant for the "My Actors Deleted" indicator key preference.
             */
            public static final String MY_ACTORS_DELETED = "pref_my_actors_deleted_key";

            /**
             * Holds a constant for the "My Directors Deleted" indicator key preference.
             */
            public static final String MY_DIRECTORS_DELETED = "pref_my_directors_deleted_key";

            /**
             * Holds a constant for the "Tell a friend" preference key.
             */
            public static final String TELL_A_FRIEND = "pref_tell_a_friend_key";

            /**
             * Holds a constant for the "Rate app" preference key.
             */
            public static final String RATE_APP = "pref_rate_app_key";

            /**
             * Holds a constant for the "About app" preference key.
             */
            public static final String ABOUT_APP = "pref_about_app_key";

        }

        /**
         * Represents preference values in the {@link android.content.SharedPreferences}.
         */
        public static final class Values {

            /**
             * Holds a constant for the "Delete all data" value preference: "Wishlist".
             */
            public static final String DELETE_ALL_DATA_WISHLIST = "Wishlist";

            /**
             * Holds a constant for the "Delete all data" value preference: "My Movies".
             */
            public static final String DELETE_ALL_DATA_MY_MOVIES = "My Movies";

            /**
             * Holds a constant for the "Delete all data" value preference: "My Actors".
             */
            public static final String DELETE_ALL_DATA_MY_ACTORS = "My Actors";

            /**
             * Holds a constant for the "Delete all data" value preference: "My Directors".
             */
            public static final String DELETE_ALL_DATA_MY_DIRECTORS = "My Directors";

        }

    }

    /**
     * Holds all the constants for bundles {@link android.os.Bundle}.
     */
    public interface Bundles {

        /**
         * Holds a constant for a "Movies" list key name (intended to send a list of {@link com.ohadshai.movielib.entities.Movie}).
         */
        public static final String MOVIES_LIST_KEY = "movies_list_key";

        /**
         * Holds a constant for a "Movies Raw" list key name (intended to send a list of {@link com.ohadshai.movielib.entities.Movie}).
         */
        public static final String MOVIES_RAW_LIST_KEY = "movies_raw_list_key";

        /**
         * Holds a constant for the "movie_id" key name (holds the id of the movie).
         */
        public static final String MOVIE_ID_KEY = "movie_id_key";

        /**
         * Holds a constant for a "Movie" object key name (intended to send an object of a {@link com.ohadshai.movielib.entities.Movie}).
         */
        public static final String MOVIE_KEY = "movie_key";

        /**
         * Holds a constant for a "People" list key name (intended to send a list of {@link com.ohadshai.movielib.entities.Person}).
         */
        public static final String PEOPLE_LIST_KEY = "people_list_key";

        /**
         * Holds a constant for a "People Raw" list key name (intended to send a list of {@link com.ohadshai.movielib.entities.Person}).
         */
        public static final String PEOPLE_RAW_LIST_KEY = "people_raw_list_key";

        /**
         * Holds a constant for a "Movie Name" key name (intended to send a String holds a movie name).
         */
        public static final String MOVIE_NAME_KEY = "movie_name_key";

        /**
         * Holds a constant for a "Movie Cast" list key name (intended to send a list of {@link com.ohadshai.movielib.entities.PersonInMovieCast}).
         */
        public static final String MOVIE_CAST_LIST_KEY = "movie_cast_list_key";

        /**
         * Holds a constant for a "Movie Crew" list key name (intended to send a list of {@link com.ohadshai.movielib.entities.PersonInMovieCrew}).
         */
        public static final String MOVIE_CREW_LIST_KEY = "movie_crew_list_key";

        /**
         * Holds a constant for a "Movie Folder" key name (intended to send an integer holds a movie folder).
         */
        public static final String MOVIE_FOLDER_KEY = "movie_folder_key";

        /**
         * Holds a constant for a {@link com.ohadshai.movielib.entities.Person} object key name (intended to send an object of a {@link com.ohadshai.movielib.entities.Person}).
         */
        public static final String PERSON_KEY = "person_key";

        /**
         * Holds a constant for the "Person Id" key name (holds the id of a {@link com.ohadshai.movielib.entities.Person}).
         */
        public static final String PERSON_ID_KEY = "person_id_key";

        /**
         * Holds a constant for a "Person Name" key name (intended to send a String holds a person name).
         */
        public static final String PERSON_NAME_KEY = "person_name_key";

        /**
         * Holds a constant for a "Person Seen On Cast" list key name (intended to send a list of {@link com.ohadshai.movielib.entities.MovieInPersonCast}).
         */
        public static final String PERSON_SEEN_ON_CAST_LIST_KEY = "person_seen_on_cast_list_key";

        /**
         * Holds a constant for a "Person Seen On Crew" list key name (intended to send a list of {@link com.ohadshai.movielib.entities.MovieInPersonCrew}).
         */
        public static final String PERSON_SEEN_ON_CREW_LIST_KEY = "person_seen_on_crew_list_key";

        /**
         * Holds all the constants that indicates a mode (like create, read, update, delete, select, etc...).
         */
        public interface MODE {

            /**
             * Holds a constant for the key name.
             */
            public static final String KEY_NAME = "intent_mode_key";

            /**
             * Holds a constant for the "Mode" value: "Unspecified".
             */
            public static final int UNSPECIFIED = 0;

            /**
             * Holds a constant for the "Mode" value: "Create".
             */
            public static final int CREATE = 1;

            /**
             * Holds a constant for the "Mode" value: "Read".
             */
            public static final int READ = 2;

            /**
             * Holds a constant for the "Mode" value: "Update".
             */
            public static final int UPDATE = 3;

            /**
             * Holds a constant for the "Mode" value: "Delete".
             */
            public static final int DELETE = 4;

            /**
             * Holds a constant for the "Mode" value: "Select".
             */
            public static final int SELECT = 5;

            /**
             * Holds a constant for the "Mode" value: "Display".
             */
            public static final int DISPLAY = 6;

        }

    }

    /**
     * Holds all the constants for request codes.
     */
    public interface RequestCodes {

        /**
         * Holds the request code for the image load from gallery activity.
         */
        public final static int IMAGE_GALLERY_REQUEST_CODE = 3000;

        /**
         * Holds the request code for the image load from camera activity.
         */
        public final static int IMAGE_CAMERA_REQUEST_CODE = 3001;

        /**
         * Holds the request code for the {@link com.ohadshai.movielib.ui.activities.FullMovieCastActivity}.
         */
        public final static int FULL_MOVIE_CAST_REQUEST_CODE = 3002;

        /**
         * Holds the request code for the {@link com.ohadshai.movielib.ui.activities.FullMovieCrewActivity}.
         */
        public final static int FULL_MOVIE_CREW_REQUEST_CODE = 3003;

        /**
         * Holds the request code for the {@link com.ohadshai.movielib.ui.activities.MovieCreateActivity}.
         */
        public final static int MOVIE_CREATE_REQUEST_CODE = 3004;

        /**
         * Holds the request code for the {@link com.ohadshai.movielib.ui.activities.MovieDisplayActivity}.
         */
        public final static int MOVIE_DISPLAY_REQUEST_CODE = 3005;

        /**
         * Holds the request code for the {@link com.ohadshai.movielib.ui.activities.MovieDisplayActivity}.
         */
        public final static int PERSON_DISPLAY_REQUEST_CODE = 3006;

        /**
         * Holds the request code for the {@link com.ohadshai.movielib.ui.activities.FullPersonSeenOnCastActivity}.
         */
        public final static int FULL_PERSON_SEEN_ON_CAST_REQUEST_CODE = 3007;

        /**
         * Holds the request code for the {@link com.ohadshai.movielib.ui.activities.FullPersonSeenOnCrewActivity}.
         */
        public final static int FULL_PERSON_SEEN_ON_CREW_REQUEST_CODE = 3008;

    }

    /**
     * Holds all the constants for unique keys.
     */
    public interface Keys {

        /**
         * Holds the unique key for the {@link com.ohadshai.movielib.ui.dialogs.SortAndFilterDialog} of {@link com.ohadshai.movielib.ui.fragments.InTheatersFragment}.
         */
        public final static String SORT_IN_THEATERS_KEY = "sort_in_theaters_key";

        /**
         * Holds the unique key for the {@link com.ohadshai.movielib.ui.dialogs.SortAndFilterDialog} of {@link com.ohadshai.movielib.ui.fragments.PopularMoviesFragment}.
         */
        public final static String SORT_POPULAR_MOVIES_KEY = "sort_popular_movies_key";

        /**
         * Holds the unique key for the {@link com.ohadshai.movielib.ui.dialogs.SortAndFilterDialog} of {@link com.ohadshai.movielib.ui.fragments.UpcomingMoviesFragment}.
         */
        public final static String SORT_UPCOMING_MOVIES_KEY = "sort_upcoming_movies_key";

        /**
         * Holds the unique key for the {@link com.ohadshai.movielib.ui.dialogs.SortAndFilterDialog} of {@link com.ohadshai.movielib.ui.fragments.WishlistFragment}.
         */
        public final static String SORT_WISHLIST_KEY = "sort_wishlist_key";

        /**
         * Holds the unique key for the {@link com.ohadshai.movielib.ui.dialogs.SortAndFilterDialog} of {@link com.ohadshai.movielib.ui.fragments.MyMoviesFragment}.
         */
        public final static String SORT_MY_MOVIES_KEY = "sort_my_movies_key";

        /**
         * Holds the unique key for the {@link com.ohadshai.movielib.ui.dialogs.SortAndFilterDialog} of {@link com.ohadshai.movielib.ui.fragments.MyActorsFragment}.
         */
        public final static String SORT_MY_ACTORS_KEY = "sort_my_actors_key";

        /**
         * Holds the unique key for the {@link com.ohadshai.movielib.ui.dialogs.SortAndFilterDialog} of {@link com.ohadshai.movielib.ui.fragments.MyDirectorsFragment}.
         */
        public final static String SORT_MY_DIRECTORS_KEY = "sort_my_directors_key";

    }

}
