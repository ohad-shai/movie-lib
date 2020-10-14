package com.ohadshai.movielib.utils.web_services.tmdb_api;

import android.annotation.SuppressLint;

import com.ohadshai.movielib.entities.Genre;
import com.ohadshai.movielib.entities.Movie;
import com.ohadshai.movielib.entities.MovieInPersonCast;
import com.ohadshai.movielib.entities.MovieInPersonCrew;
import com.ohadshai.movielib.entities.MovieReview;
import com.ohadshai.movielib.entities.Person;
import com.ohadshai.movielib.entities.PersonInMovieCast;
import com.ohadshai.movielib.entities.PersonInMovieCrew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Represents a documentation and holds all the constants for the "The Movie DB API" web service.
 * Created by Ohad on 03/18/2017.
 */
public class TMDBService {

    /**
     * Holds all the constants for the service urls.
     */
    public interface Urls {

        /**
         * Holds a constant for a service url for: getting a photo by a poster path / backdrop path.
         */
        public static final String GET_PHOTO_BY_PATH = "https://image.tmdb.org/t/p/w500/";

        /**
         * Holds a constant for a service url for: getting now playing movies.
         * [Associated params: Page, Language, Region].
         * [Response: Movies {@link Responses.Movies}].
         */
        public static final String GET_NOW_PLAYING_MOVIES = "https://api.themoviedb.org/3/movie/now_playing" + Params.START_API_KEY;

        /**
         * Holds a constant for a service url for: getting popular movies.
         * [Associated params: Page, Language, Region].
         * [Response: Movies {@link Responses.Movies}].
         */
        public static final String GET_POPULAR_MOVIES = "https://api.themoviedb.org/3/movie/popular" + Params.START_API_KEY;

        /**
         * Holds a constant for a service url for: getting upcoming movies.
         * [Associated params: Page, Language, Region].
         * [Response: Movies {@link Responses.Movies}].
         */
        public static final String GET_UPCOMING_MOVIES = "https://api.themoviedb.org/3/movie/upcoming" + Params.START_API_KEY;

        /**
         * Holds a constant for a service url for: searching movies by a query (title).
         * [Associated params: Page, Language, Region, Include Adult, Year].
         * [Response: Movies {@link Responses.Movies}].
         */
        public static final String SEARCH_MOVIES_BY_TITLE = "https://api.themoviedb.org/3/search/movie" + Params.START_API_KEY + "&query=";

        /**
         * Holds a constant for a service url for: getting the primary information about a movie.
         * [Required params: API key].
         * [Associated params: Language, Append to Response].
         * [Response: Movie Object {@link Responses.Movie}].
         */
        public static final String GET_MOVIE_BY_TMDB_ID = "https://api.themoviedb.org/3/movie/";

        /**
         * Holds a constant for a service url for: searching people by a query (name).
         * [Associated params: Page, Language, Region, Include Adult].
         * [Response: Persons List {@link Responses.People}].
         */
        public static final String SEARCH_PEOPLE_BY_NAME = "https://api.themoviedb.org/3/search/person" + Params.START_API_KEY + "&query=";

        /**
         * Holds a constant for a service url for: getting the primary information about a person.
         * [Required params: API key].
         * [Associated params: Language, Append to Response].
         * [Response: Person Object {@link Responses.Person}].
         */
        public static final String GET_PERSON_BY_TMDB_ID = "https://api.themoviedb.org/3/person/";

        /**
         * Holds a constant for a service url for: getting the list of all the movie genres.
         * [Associated params: Language].
         * [Response: Person Object {@link Responses.Genres}].
         */
        public static final String GET_GENRES_LIST = "https://api.themoviedb.org/3/genre/movie/list" + Params.START_API_KEY;

    }

    /**
     * Holds all the constants for url query parameters.
     */
    public interface Params {

        /**
         * Holds a constant for a query start parameter: "API key".
         */
        public static final String START_API_KEY = "?api_key=" + TMDBCredentials.AUTH_KEY;

        /**
         * Holds a constant for a query append parameter: "Page" (integer).
         */
        public static final String APPEND_PAGE = "&page=";

        /**
         * Holds a constant for a query append parameter: "Include Adult" (boolean).
         */
        public static final String APPEND_INCLUDE_ADULT = "&include_adult=";

        /**
         * Holds a constant for a query append parameter: "Language" (ISO 639-1, like: "en-US").
         */
        public static final String APPEND_LANGUAGE = "&language=";

        /**
         * Holds a constant for a query append parameter: "Region" (ISO 3166-1, like: "US").
         */
        public static final String APPEND_REGION = "&region=";

        /**
         * Holds a constant for a query append parameter: "Year" (integer).
         */
        public static final String APPEND_YEAR = "&year=";

        /**
         * Holds a constant for a query append parameter: "Append to Response".
         */
        public static final String APPEND_APPEND_TO_RESPONSE = "&append_to_response=";

        /**
         * Holds a constant for a query append parameter: "Append to Response: Reviews and Credits".
         */
        public static final String APPEND_TO_RESPONSE_REVIEWS_AND_CREDITS = APPEND_APPEND_TO_RESPONSE + "reviews,credits";

        /**
         * Holds a constant for a query append parameter: "Append to Response: External Ids & Movie Credits".
         */
        public static final String APPEND_TO_RESPONSE_EXTERNAL_IDS_AND_CREDITS = APPEND_APPEND_TO_RESPONSE + "external_ids,movie_credits";

    }

    /**
     * Represents a documentation for the responses from the TMDB service.
     */
    public static class Responses {

        /**
         * Holds all the constants for the service response base.
         */
        public interface Base {

            /**
             * Holds a constant for a response JSON value: "Page" (holds an integer value).
             */
            public static final String VALUE_PAGE = "page";

            /**
             * Holds a constant for a response JSON array: "Results" (holds a JSON array of objects).
             */
            public static final String ARRAY_RESULTS = "results";

            /**
             * Holds a constant for a response JSON value: "Total Results" (holds an integer value).
             */
            public static final String VALUE_TOTAL_RESULTS = "total_results";

            /**
             * Holds a constant for a response JSON value: "Total Pages" (holds an integer value).
             */
            public static final String VALUE_TOTAL_PAGES = "total_pages";

        }

        /**
         * Holds all the constants for the service response associated with: "A list of movies".
         */
        public interface Movies extends Base {

            /**
             * Holds a constant for a response JSON value: "Poster Path" (holds a string / null value).
             */
            public static final String VALUE_POSTER_PATH = "poster_path";

            /**
             * Holds a constant for a response JSON value: "Adult" (holds a boolean value).
             */
            public static final String VALUE_ADULT = "adult";

            /**
             * Holds a constant for a response JSON value: "Overview" (holds a string value).
             */
            public static final String VALUE_OVERVIEW = "overview";

            /**
             * Holds a constant for a response JSON value: "Release Date" (holds a date as a string value / null).
             */
            public static final String VALUE_RELEASE_DATE = "release_date";

            /**
             * Holds a constant for a response JSON array: "Genres Ids" (holds an integer array).
             */
            public static final String ARRAY_GENRE_IDS = "genre_ids";

            /**
             * Holds a constant for a response JSON value: "ID" (holds an integer value).
             */
            public static final String VALUE_ID = "id";

            /**
             * Holds a constant for a response JSON value: "Original Title" (holds a string value).
             */
            public static final String VALUE_ORIGINAL_TITLE = "original_title";

            /**
             * Holds a constant for a response JSON value: "Original Language" (holds a string value).
             */
            public static final String VALUE_ORIGINAL_LANGUAGE = "original_language";

            /**
             * Holds a constant for a response JSON value: "Title" (holds a string value).
             */
            public static final String VALUE_TITLE = "title";

            /**
             * Holds a constant for a response JSON value: "Backdrop Path" (holds a string / null value).
             */
            public static final String VALUE_BACKDROP_PATH = "backdrop_path";

            /**
             * Holds a constant for a response JSON value: "Popularity" (holds a number (float) value).
             */
            public static final String VALUE_POPULARITY = "popularity";

            /**
             * Holds a constant for a response JSON value: "Vote Count" (holds an integer value).
             */
            public static final String VALUE_VOTE_COUNT = "vote_count";

            /**
             * Holds a constant for a response JSON value: "Vote Average" (holds a number (float) value).
             */
            public static final String VALUE_VOTE_AVERAGE = "vote_average";

        }

        /**
         * Holds all the constants for the service response associated with: "Movie Object".
         */
        public interface Movie extends Base {

            /**
             * Holds a constant for a response JSON value: "ID" (holds an integer value).
             */
            public static final String VALUE_ID = "id";

            /**
             * Holds a constant for a response JSON value: "Budget" (holds an integer value).
             */
            public static final String VALUE_BUDGET = "budget";

            /**
             * Holds a constant for a response JSON array: "Genres" (holds a JSON array of id (integer) & name (string)).
             */
            public static final String ARRAY_GENRES = "genres";

            /**
             * Holds a constant for a response JSON value: "Name" (holds a string value).
             */
            public static final String VALUE_NAME = "name";

            /**
             * Holds a constant for a response JSON value: "Homepage" (holds a string value).
             */
            public static final String VALUE_HOMEPAGE = "homepage";

            /**
             * Holds a constant for a response JSON value: "IMDb Id" (holds a string value).
             */
            public static final String VALUE_IMDB_ID = "imdb_id";

            /**
             * Holds a constant for a response JSON value: "Revenue" (holds an integer value).
             */
            public static final String VALUE_REVENUE = "revenue";

            /**
             * Holds a constant for a response JSON value: "Runtime" (holds an integer value).
             */
            public static final String VALUE_RUNTIME = "runtime";

            /**
             * Holds a constant for a response JSON value: "Status" (holds a string value).
             */
            public static final String VALUE_STATUS = "status";

            /**
             * Holds a constant for a response JSON value: "Tagline" (holds a string value).
             */
            public static final String VALUE_TAGLINE = "tagline";

            /**
             * Holds a constant for a response JSON object: "Reviews" (holds a JSON object contains a response of {@link Review}).
             */
            public static final String OBJECT_REVIEWS = "reviews";

            /**
             * Holds a constant for a response JSON object: "Credits" (holds a JSON object contains a response of {@link MovieCredits}).
             */
            public static final String OBJECT_CREDITS = "credits";

        }

        /**
         * Holds all the constants for the service response associated with: "Reviews".
         */
        public interface Review {

            /**
             * Holds a constant for a response JSON value: "Author" (holds a string value).
             */
            public static final String VALUE_AUTHOR = "author";

            /**
             * Holds a constant for a response JSON value: "Content" (holds a string value).
             */
            public static final String VALUE_CONTENT = "content";

        }

        /**
         * Holds all the constants for the service response associated with: "Movie Credits".
         */
        public interface MovieCredits {

            /**
             * Holds a constant for a response JSON value: "ID" (holds an integer value).
             */
            public static final String VALUE_ID = "id";

            /**
             * Holds a constant for a response JSON value: "Name" (holds a string value).
             */
            public static final String VALUE_NAME = "name";

            /**
             * Holds a constant for a response JSON array: "Cast" (holds a JSON array contains: character, id, name, profile path).
             */
            public static final String ARRAY_CAST = "cast";

            /**
             * Holds a constant for a response JSON value: "Character" (holds a string value).
             */
            public static final String VALUE_CHARACTER = "character";

            /**
             * Holds a constant for a response JSON value: "Profile Path" (holds a string / null value).
             */
            public static final String VALUE_PROFILE_PATH = "profile_path";

            /**
             * Holds a constant for a response JSON array: "Crew" (holds a JSON array contains: department, id, job, name, profile path).
             */
            public static final String ARRAY_CREW = "crew";

            /**
             * Holds a constant for a response JSON value: "Department" (holds a string value).
             */
            public static final String VALUE_DEPARTMENT = "department";

            /**
             * Holds a constant for a response JSON value: "Job" (holds a string value).
             */
            public static final String VALUE_JOB = "job";

        }

        /**
         * Holds all the constants for the service response associated with: "Genres".
         */
        public interface Genres {

            /**
             * Holds a constant for a response JSON array: "Genres" (holds a JSON array contains: id, name).
             */
            public static final String ARRAY_GENRES = "genres";

            /**
             * Holds a constant for a response JSON value: "ID" (holds an integer value).
             */
            public static final String VALUE_ID = "id";

            /**
             * Holds a constant for a response JSON value: "Name" (holds a string value).
             */
            public static final String VALUE_NAME = "name";

        }

        /**
         * Holds all the constants for the service response associated with: "A list of people".
         */
        public interface People extends Base {

            /**
             * Holds a constant for a response JSON value: "Profile Path" (holds a string / null value).
             */
            public static final String VALUE_PROFILE_PATH = "profile_path";

            /**
             * Holds a constant for a response JSON value: "Id" (holds an integer value).
             */
            public static final String VALUE_ID = "id";

            /**
             * Holds a constant for a response JSON array: "Known For" (holds a JSON array contains: list of movies {@link Movies}).
             */
            public static final String ARRAY_KNOWN_FOR = "known_for";

            /**
             * Holds a constant for a response JSON value: "Name" (holds a string value).
             */
            public static final String VALUE_NAME = "name";

            /**
             * Holds a constant for a response JSON value: "Popularity" (holds a number (float) value).
             */
            public static final String VALUE_POPULARITY = "popularity";

        }

        /**
         * Holds all the constants for the service response associated with: "Person Object".
         */
        public interface Person extends Base {

            /**
             * Holds a constant for a response JSON value: "Biography" (holds a string value).
             */
            public static final String VALUE_BIOGRAPHY = "biography";

            /**
             * Holds a constant for a response JSON value: "Birthday" (holds a string value).
             */
            public static final String VALUE_BIRTHDAY = "birthday";

            /**
             * Holds a constant for a response JSON value: "Deathday" (holds a string value).
             */
            public static final String VALUE_DEATHDAY = "deathday";

            /**
             * Holds a constant for a response JSON value: "Gender" (holds an integer value).
             */
            public static final String VALUE_GENDER = "gender";

            /**
             * Holds a constant for a response JSON value: "Homepage" (holds a string value).
             */
            public static final String VALUE_HOMEPAGE = "homepage";

            /**
             * Holds a constant for a response JSON value: "Id" (holds an integer value).
             */
            public static final String VALUE_ID = "id";

            /**
             * Holds a constant for a response JSON value: "IMDb Id" (holds a string value).
             */
            public static final String VALUE_IMDB_ID = "imdb_id";

            /**
             * Holds a constant for a response JSON value: "Name" (holds a string value).
             */
            public static final String VALUE_NAME = "name";

            /**
             * Holds a constant for a response JSON value: "Place of Birth" (holds a string value).
             */
            public static final String VALUE_PLACE_OF_BIRTH = "place_of_birth";

            /**
             * Holds a constant for a response JSON value: "Popularity" (holds a number (float) value).
             */
            public static final String VALUE_POPULARITY = "popularity";

            /**
             * Holds a constant for a response JSON value: "Profile Path" (holds a string / null value).
             */
            public static final String VALUE_PROFILE_PATH = "profile_path";

            /**
             * Holds a constant for a response JSON object: "External Ids" (holds a JSON object contains a response of {@link ExternalIds}).
             */
            public static final String OBJECT_EXTERNAL_IDS = "external_ids";

            /**
             * Holds a constant for a response JSON object: "Movie Credits" (holds a JSON object contains a response of {@link PersonCredits}).
             */
            public static final String OBJECT_MOVIE_CREDITS = "movie_credits";

        }

        /**
         * Holds all the constants for the service response associated with: "External Ids".
         */
        public interface ExternalIds {

            /**
             * Holds a constant for a response JSON value: "Facebook Id" (holds a string / null value).
             */
            public static final String VALUE_FACEBOOK_ID = "facebook_id";

            /**
             * Holds a constant for a response JSON value: "Instagram Id" (holds a string / null value).
             */
            public static final String VALUE_INSTAGRAM_ID = "instagram_id";

            /**
             * Holds a constant for a response JSON value: "Twitter Id" (holds a string / null value).
             */
            public static final String VALUE_TWITTER_ID = "twitter_id";

        }

        /**
         * Holds all the constants for the service response associated with: "Person Credits".
         */
        public interface PersonCredits {

            /**
             * Holds a constant for a response JSON value: "ID" (holds an integer value).
             */
            public static final String VALUE_ID = "id";

            /**
             * Holds a constant for a response JSON array: "Cast" (holds a JSON array contains: character, id, name, profile path).
             */
            public static final String ARRAY_CAST = "cast";

            /**
             * Holds a constant for a response JSON value: "Character" (holds a string value).
             */
            public static final String VALUE_CHARACTER = "character";

            /**
             * Holds a constant for a response JSON value: "Release Date" (holds a date as a string value / null).
             */
            public static final String VALUE_RELEASE_DATE = "release_date";

            /**
             * Holds a constant for a response JSON value: "Poster Path" (holds a string / null value).
             */
            public static final String VALUE_POSTER_PATH = "poster_path";

            /**
             * Holds a constant for a response JSON value: "Original Title" (holds a string value).
             */
            public static final String VALUE_ORIGINAL_TITLE = "original_title";

            /**
             * Holds a constant for a response JSON value: "Title" (holds a string value).
             */
            public static final String VALUE_TITLE = "title";

            /**
             * Holds a constant for a response JSON array: "Crew" (holds a JSON array contains: department, id, job, name, profile path).
             */
            public static final String ARRAY_CREW = "crew";

            /**
             * Holds a constant for a response JSON value: "Department" (holds a string value).
             */
            public static final String VALUE_DEPARTMENT = "department";

            /**
             * Holds a constant for a response JSON value: "Job" (holds a string value).
             */
            public static final String VALUE_JOB = "job";

        }

        /**
         * Holds all the constants for the service response associated with: "TV Series".
         */
        public interface TV {

            /**
             * Holds a constant for a response JSON value: "Name" (holds a string value).
             */
            public static final String VALUE_NAME = "name";

            /**
             * Holds a constant for a response JSON value: "Original Name" (holds a string value).
             */
            public static final String VALUE_ORIGINAL_NAME = "original_name";

            /**
             * Holds a constant for a response JSON value: "First Air Date" (holds a string value).
             */
            public static final String VALUE_FIRST_AIR_DATE = "first_air_date";

        }

    }

    /**
     * Represents a parser for JSON responses from the service.
     */
    public static class Parse {

        /**
         * Parses a JSON to a {@link Movie} list.
         *
         * @param json The JSON string to parse.
         * @return Returns the list of {@link Movie} parsed from the JSON.
         */
        public static ArrayList<Movie> movieList(String json) throws JSONException {
            ArrayList<Movie> movies = new ArrayList<>();
            JSONObject jo = new JSONObject(json);
            JSONArray results = jo.getJSONArray(Responses.Base.ARRAY_RESULTS);

            for (int i = 0; i < results.length(); i++) {
                try {
                    JSONObject movieJSON = results.getJSONObject(i);
                    Movie movie = new Movie();

                    String posterPath = movieJSON.getString(Responses.Movies.VALUE_POSTER_PATH);
                    if (posterPath != null)
                        movie.setPosterPath(posterPath.substring(posterPath.lastIndexOf("/") + 1));

                    if (!movieJSON.isNull(Responses.Movies.VALUE_ADULT))
                        movie.setAdult(movieJSON.getBoolean(Responses.Movies.VALUE_ADULT));

                    movie.setPlot(movieJSON.getString(Responses.Movies.VALUE_OVERVIEW));

                    try {
                        String releaseDate = movieJSON.getString(Responses.Movies.VALUE_RELEASE_DATE);
                        if (releaseDate != null) {
                            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            movie.setReleaseDate(Calendar.getInstance());
                            movie.getReleaseDate().setTime(dateFormat.parse(releaseDate));
                        } else {
                            movie.setReleaseDate(null);
                        }
                    } catch (ParseException e) {
                        movie.setReleaseDate(null);
                    }

                    movie.setGenres(new ArrayList<Genre>());
                    JSONArray genreIds = movieJSON.getJSONArray(Responses.Movies.ARRAY_GENRE_IDS);
                    for (int idx = 0; idx < genreIds.length(); idx++)
                        movie.getGenres().add(new Genre(genreIds.getInt(idx), null));

                    if (!movieJSON.isNull(Responses.Movies.VALUE_ID)) {
                        movie.setTMDBId(movieJSON.getInt(Responses.Movies.VALUE_ID));
                    }
                    if (!movieJSON.isNull(Responses.Movies.VALUE_ORIGINAL_TITLE)) {
                        movie.setOriginalTitle(movieJSON.getString(Responses.Movies.VALUE_ORIGINAL_TITLE));
                    }
                    if (!movieJSON.isNull(Responses.Movies.VALUE_ORIGINAL_LANGUAGE)) {
                        movie.setOriginalLanguage(movieJSON.getString(Responses.Movies.VALUE_ORIGINAL_LANGUAGE));
                    }
                    if (!movieJSON.isNull(Responses.Movies.VALUE_TITLE)) {
                        movie.setTitle(movieJSON.getString(Responses.Movies.VALUE_TITLE));
                    }

                    String backdropPath = movieJSON.getString(Responses.Movies.VALUE_BACKDROP_PATH);
                    if (backdropPath != null)
                        movie.setBackdropPath(backdropPath.substring(backdropPath.lastIndexOf("/") + 1));

                    if (!movieJSON.isNull(Responses.Movies.VALUE_POPULARITY))
                        movie.setPopularity((float) movieJSON.getDouble(Responses.Movies.VALUE_POPULARITY));
                    if (!movieJSON.isNull(Responses.Movies.VALUE_VOTE_AVERAGE))
                        movie.setVoteAverage((float) movieJSON.getDouble(Responses.Movies.VALUE_VOTE_AVERAGE));
                    if (!movieJSON.isNull(Responses.Movies.VALUE_VOTE_COUNT))
                        movie.setVoteCount(movieJSON.getInt(Responses.Movies.VALUE_VOTE_COUNT));

                    movies.add(movie);
                } catch (Exception ignored) {
                }
            }

            return movies;
        }

        /**
         * Parses a JSON to a {@link Movie} object.
         *
         * @param json The JSON string to parse.
         * @return Returns the {@link Movie} object parsed from the JSON.
         */
        public static Movie movie(String json) throws JSONException {
            Movie movie = new Movie();
            JSONObject movieJSON = new JSONObject(json);

            if (!movieJSON.isNull(Responses.Movies.VALUE_ADULT)) {
                movie.setAdult(movieJSON.getBoolean(Responses.Movies.VALUE_ADULT));
            }

            if (!movieJSON.isNull(Responses.Movies.VALUE_BACKDROP_PATH)) {
                String backdropPath = movieJSON.getString(Responses.Movies.VALUE_BACKDROP_PATH);
                if (backdropPath != null)
                    movie.setBackdropPath(backdropPath.substring(backdropPath.lastIndexOf("/") + 1));
            }

            if (!movieJSON.isNull(Responses.Movie.VALUE_BUDGET)) {
                movie.setBudget(movieJSON.getInt(Responses.Movie.VALUE_BUDGET));
            }

            if (!movieJSON.isNull(Responses.Movie.VALUE_HOMEPAGE)) {
                movie.setHomepage(movieJSON.getString(Responses.Movie.VALUE_HOMEPAGE));
                if (movie.getHomepage() != null && movie.getHomepage().trim().equals(""))
                    movie.setHomepage(null);
            }

            if (!movieJSON.isNull(Responses.Movies.VALUE_ID)) {
                movie.setTMDBId(movieJSON.getInt(Responses.Movies.VALUE_ID));
            }
            if (!movieJSON.isNull(Responses.Movie.VALUE_IMDB_ID)) {
                movie.setIMDbId(movieJSON.getString(Responses.Movie.VALUE_IMDB_ID));
            }
            if (!movieJSON.isNull(Responses.Movies.VALUE_ORIGINAL_LANGUAGE)) {
                movie.setOriginalLanguage(movieJSON.getString(Responses.Movies.VALUE_ORIGINAL_LANGUAGE));
            }
            if (!movieJSON.isNull(Responses.Movies.VALUE_ORIGINAL_TITLE)) {
                movie.setOriginalTitle(movieJSON.getString(Responses.Movies.VALUE_ORIGINAL_TITLE));
            }
            if (!movieJSON.isNull(Responses.Movies.VALUE_OVERVIEW)) {
                movie.setPlot(movieJSON.getString(Responses.Movies.VALUE_OVERVIEW));
            }
            if (!movieJSON.isNull(Responses.Movies.VALUE_POPULARITY)) {
                movie.setPopularity((float) movieJSON.getDouble(Responses.Movies.VALUE_POPULARITY));
            }

            if (!movieJSON.isNull(Responses.Movies.VALUE_POSTER_PATH)) {
                String posterPath = movieJSON.getString(Responses.Movies.VALUE_POSTER_PATH);
                if (posterPath != null)
                    movie.setPosterPath(posterPath.substring(posterPath.lastIndexOf("/") + 1));
            }

            try {
                String releaseDate = movieJSON.getString(Responses.Movies.VALUE_RELEASE_DATE);
                if (releaseDate != null) {
                    @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    movie.setReleaseDate(Calendar.getInstance());
                    movie.getReleaseDate().setTime(dateFormat.parse(releaseDate));
                } else {
                    movie.setReleaseDate(null);
                }
            } catch (ParseException e) {
                movie.setReleaseDate(null);
            }

            movie.setGenres(genres(json));

            if (!movieJSON.isNull(Responses.Movie.VALUE_REVENUE)) {
                movie.setRevenue(movieJSON.getInt(Responses.Movie.VALUE_REVENUE));
            }
            if (!movieJSON.isNull(Responses.Movie.VALUE_RUNTIME)) {
                movie.setRuntime(movieJSON.getInt(Responses.Movie.VALUE_RUNTIME));
            }
            if (!movieJSON.isNull(Responses.Movie.VALUE_STATUS)) {
                movie.setStatus(movieJSON.getString(Responses.Movie.VALUE_STATUS));
            }
            if (!movieJSON.isNull(Responses.Movie.VALUE_TAGLINE)) {
                movie.setTagline(movieJSON.getString(Responses.Movie.VALUE_TAGLINE));
            }
            if (!movieJSON.isNull(Responses.Movies.VALUE_TITLE)) {
                movie.setTitle(movieJSON.getString(Responses.Movies.VALUE_TITLE));
            }

            if (!movieJSON.isNull(Responses.Movies.VALUE_VOTE_AVERAGE)) {
                movie.setVoteAverage((float) movieJSON.getDouble(Responses.Movies.VALUE_VOTE_AVERAGE));
            }
            if (!movieJSON.isNull(Responses.Movies.VALUE_VOTE_COUNT)) {
                movie.setVoteCount(movieJSON.getInt(Responses.Movies.VALUE_VOTE_COUNT));
            }

            return movie;
        }

        /**
         * Parses a JSON to a {@link Movie} object, contains the {@link MovieReview} list, and the movie credits.
         *
         * @param json The JSON string to parse.
         * @return Returns the {@link Movie} object parsed from the JSON.
         */
        public static Movie movieWithReviewsAndCredits(String json) throws JSONException {
            Movie movie = movie(json);

            JSONObject reviewsJSON = new JSONObject(json).getJSONObject(Responses.Movie.OBJECT_REVIEWS);
            movie.setReviews(movieReviews(reviewsJSON.toString()));

            JSONObject creditsJSON = new JSONObject(json).getJSONObject(Responses.Movie.OBJECT_CREDITS);
            movie.setCast(movieCast(creditsJSON.toString()));
            movie.setCrew(movieCrew(creditsJSON.toString()));

            return movie;
        }

        /**
         * Parses a JSON to a {@link MovieReview} list.
         *
         * @param json The JSON string to parse.
         * @return Returns the list of {@link MovieReview} parsed from the JSON.
         */
        public static ArrayList<MovieReview> movieReviews(String json) throws JSONException {
            ArrayList<MovieReview> reviews = new ArrayList<>();
            JSONObject jo = new JSONObject(json);
            JSONArray results = jo.getJSONArray(Responses.Base.ARRAY_RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject reviewJSON = results.getJSONObject(i);
                reviews.add(new MovieReview(reviewJSON.getString(Responses.Review.VALUE_AUTHOR), reviewJSON.getString(Responses.Review.VALUE_CONTENT)));
            }

            return reviews;
        }

        /**
         * Parses a JSON to a {@link PersonInMovieCast} list.
         *
         * @param json The JSON string to parse.
         * @return Returns the list of {@link PersonInMovieCast} parsed from the JSON.
         */
        public static ArrayList<PersonInMovieCast> movieCast(String json) throws JSONException {
            ArrayList<PersonInMovieCast> cast = new ArrayList<>();
            JSONObject jo = new JSONObject(json);
            JSONArray castJSON = jo.getJSONArray(Responses.MovieCredits.ARRAY_CAST);

            for (int i = 0; i < castJSON.length(); i++) {
                try {
                    JSONObject personJSON = castJSON.getJSONObject(i);
                    PersonInMovieCast personInCast = new PersonInMovieCast();

                    Person person = new Person();
                    person.setTMDBId(personJSON.getInt(Responses.MovieCredits.VALUE_ID));
                    person.setName(personJSON.getString(Responses.MovieCredits.VALUE_NAME));
                    String profilePath = personJSON.getString(Responses.MovieCredits.VALUE_PROFILE_PATH);
                    if (profilePath != null)
                        person.setProfilePath(profilePath.substring(profilePath.lastIndexOf("/") + 1));
                    personInCast.setPerson(person);

                    personInCast.setCharacter(personJSON.getString(Responses.MovieCredits.VALUE_CHARACTER));

                    cast.add(personInCast);
                } catch (Exception ignored) {
                }
            }

            return cast;
        }

        /**
         * Parses a JSON to a {@link PersonInMovieCrew} list.
         *
         * @param json The JSON string to parse.
         * @return Returns the list of {@link PersonInMovieCrew} parsed from the JSON.
         */
        public static ArrayList<PersonInMovieCrew> movieCrew(String json) throws JSONException {
            ArrayList<PersonInMovieCrew> crew = new ArrayList<>();
            JSONObject jo = new JSONObject(json);
            JSONArray crewJSON = jo.getJSONArray(Responses.MovieCredits.ARRAY_CREW);

            for (int i = 0; i < crewJSON.length(); i++) {
                try {
                    JSONObject personJSON = crewJSON.getJSONObject(i);
                    PersonInMovieCrew personInCrew = new PersonInMovieCrew();

                    Person person = new Person();
                    person.setTMDBId(personJSON.getInt(Responses.MovieCredits.VALUE_ID));
                    person.setName(personJSON.getString(Responses.MovieCredits.VALUE_NAME));
                    String profilePath = personJSON.getString(Responses.MovieCredits.VALUE_PROFILE_PATH);
                    if (profilePath != null)
                        person.setProfilePath(profilePath.substring(profilePath.lastIndexOf("/") + 1));
                    personInCrew.setPerson(person);

                    personInCrew.setDepartment(personJSON.getString(Responses.MovieCredits.VALUE_DEPARTMENT));
                    personInCrew.setJob(personJSON.getString(Responses.MovieCredits.VALUE_JOB));

                    crew.add(personInCrew);
                } catch (Exception ignored) {
                }
            }

            return crew;
        }

        /**
         * Parses a JSON to a {@link Genre} list.
         *
         * @param json The JSON string to parse.
         * @return Returns the list of {@link Genre} parsed from the JSON.
         */
        public static ArrayList<Genre> genres(String json) throws JSONException {
            ArrayList<Genre> genres = new ArrayList<>();
            JSONObject jo = new JSONObject(json);
            JSONArray genresJSON = jo.getJSONArray(Responses.Genres.ARRAY_GENRES);

            for (int i = 0; i < genresJSON.length(); i++) {
                try {
                    JSONObject genreJSON = genresJSON.getJSONObject(i);
                    genres.add(new Genre(genreJSON.getInt(Responses.Genres.VALUE_ID), genreJSON.getString(Responses.Genres.VALUE_NAME)));
                } catch (Exception ignored) {
                }
            }

            return genres;
        }

        /**
         * Parses a JSON to a {@link Person} list.
         *
         * @param json The JSON string to parse.
         * @return Returns the list of {@link Person} parsed from the JSON.
         */
        public static ArrayList<Person> personList(String json) throws JSONException {
            ArrayList<Person> persons = new ArrayList<>();
            JSONObject jo = new JSONObject(json);
            JSONArray results = jo.getJSONArray(Responses.Base.ARRAY_RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject personJSON = results.getJSONObject(i);
                Person person = new Person();

                String profilePath = personJSON.getString(Responses.People.VALUE_PROFILE_PATH);
                if (profilePath != null)
                    person.setProfilePath(profilePath.substring(profilePath.lastIndexOf("/") + 1));

                person.setTMDBId(personJSON.getInt(Responses.People.VALUE_ID));

                person.setName(personJSON.getString(Responses.People.VALUE_NAME));

                if (!personJSON.isNull(Responses.People.VALUE_POPULARITY))
                    person.setPopularity((float) personJSON.getDouble(Responses.People.VALUE_POPULARITY));

                persons.add(person);
            }

            return persons;
        }

        /**
         * Parses a JSON to a {@link Person} object.
         *
         * @param json The JSON string to parse.
         * @return Returns the {@link Person} object parsed from the JSON.
         */
        public static Person person(String json) throws JSONException {
            Person person = new Person();
            JSONObject personJSON = new JSONObject(json);

            if (!personJSON.isNull(Responses.Person.VALUE_BIOGRAPHY)) {
                person.setBiography(personJSON.getString(Responses.Person.VALUE_BIOGRAPHY));
                if (person.getBiography() != null && person.getBiography().equals("null"))
                    person.setBiography(null);
            }

            try {
                String birthday = personJSON.getString(Responses.Person.VALUE_BIRTHDAY);
                if (birthday != null) {
                    @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    person.setBirthday(Calendar.getInstance());
                    person.getBirthday().setTime(dateFormat.parse(birthday));
                } else {
                    person.setBirthday(null);
                }
            } catch (Exception e) {
                person.setBirthday(null);
            }

            try {
                String deathday = personJSON.getString(Responses.Person.VALUE_DEATHDAY);
                if (deathday != null) {
                    @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    person.setDeathday(Calendar.getInstance());
                    person.getDeathday().setTime(dateFormat.parse(deathday));
                } else {
                    person.setDeathday(null);
                }
            } catch (Exception e) {
                person.setDeathday(null);
            }

            try {
                person.setGender(personJSON.getInt(Responses.Person.VALUE_GENDER));
            } catch (Exception ignored) {
            }

            if (!personJSON.isNull(Responses.Person.VALUE_HOMEPAGE)) {
                person.setHomepage(personJSON.getString(Responses.Person.VALUE_HOMEPAGE));
                if (person.getHomepage() != null && person.getHomepage().trim().equals(""))
                    person.setHomepage(null);
            }

            if (!personJSON.isNull(Responses.Person.VALUE_ID)) {
                person.setTMDBId(personJSON.getInt(Responses.Person.VALUE_ID));
            }
            if (!personJSON.isNull(Responses.Person.VALUE_IMDB_ID)) {
                person.setIMDbId(personJSON.getString(Responses.Person.VALUE_IMDB_ID));
            }
            if (!personJSON.isNull(Responses.Person.VALUE_NAME)) {
                person.setName(personJSON.getString(Responses.Person.VALUE_NAME));
            }

            if (!personJSON.isNull(Responses.Person.VALUE_PLACE_OF_BIRTH)) {
                person.setPlaceOfBirth(personJSON.getString(Responses.Person.VALUE_PLACE_OF_BIRTH));
                if (person.getPlaceOfBirth() != null && person.getPlaceOfBirth().equals("null"))
                    person.setPlaceOfBirth(null);
            }

            if (!personJSON.isNull(Responses.Person.VALUE_POPULARITY)) {
                person.setPopularity((float) personJSON.getDouble(Responses.Person.VALUE_POPULARITY));
            }

            if (!personJSON.isNull(Responses.Person.VALUE_PROFILE_PATH)) {
                String profilePath = personJSON.getString(Responses.Person.VALUE_PROFILE_PATH);
                if (profilePath != null)
                    person.setProfilePath(profilePath.substring(profilePath.lastIndexOf("/") + 1));
            }

            return person;
        }

        /**
         * Parses a JSON to a {@link Person} object, contains the external ids, and the {@link MovieInPersonCast} list.
         *
         * @param json The JSON string to parse.
         * @return Returns the {@link Person} object parsed from the JSON.
         */
        public static Person personWithExternalIdsAndCredits(String json) throws JSONException {
            Person person = person(json);

            JSONObject externalIdsJSON = new JSONObject(json).getJSONObject(Responses.Person.OBJECT_EXTERNAL_IDS);
            person.setFacebook(externalIdsJSON.getString(Responses.ExternalIds.VALUE_FACEBOOK_ID));
            if (person.getFacebook() != null && (person.getFacebook().equals("") || person.getFacebook().equals("null")))
                person.setFacebook(null);
            person.setTwitter(externalIdsJSON.getString(Responses.ExternalIds.VALUE_TWITTER_ID));
            if (person.getTwitter() != null && (person.getTwitter().equals("") || person.getTwitter().equals("null")))
                person.setTwitter(null);
            person.setInstagram(externalIdsJSON.getString(Responses.ExternalIds.VALUE_INSTAGRAM_ID));
            if (person.getInstagram() != null && (person.getInstagram().equals("") || person.getInstagram().equals("null")))
                person.setInstagram(null);

            JSONObject creditsJSON = new JSONObject(json).getJSONObject(Responses.Person.OBJECT_MOVIE_CREDITS);
            person.setSeenOnCast(personMovieCast(creditsJSON.toString()));
            person.setSeenOnCrew(personMovieCrew(creditsJSON.toString()));

            return person;
        }

        /**
         * Parses a JSON to a {@link MovieInPersonCast} list.
         *
         * @param json The JSON string to parse.
         * @return Returns the list of {@link MovieInPersonCast} parsed from the JSON.
         */
        public static ArrayList<MovieInPersonCast> personMovieCast(String json) throws JSONException {
            ArrayList<MovieInPersonCast> movies = new ArrayList<>();
            JSONObject jo = new JSONObject(json);
            JSONArray castJSON = jo.getJSONArray(Responses.PersonCredits.ARRAY_CAST);

            for (int i = 0; i < castJSON.length(); i++) {
                try {
                    JSONObject movieJSON = castJSON.getJSONObject(i);
                    MovieInPersonCast movieInPersonCast = new MovieInPersonCast();
                    movieInPersonCast.setMovie(new Movie());

                    movieInPersonCast.getMovie().setTMDBId(movieJSON.getInt(Responses.PersonCredits.VALUE_ID));
                    movieInPersonCast.getMovie().setTitle(movieJSON.getString(Responses.PersonCredits.VALUE_TITLE));
                    movieInPersonCast.getMovie().setOriginalTitle(movieJSON.getString(Responses.PersonCredits.VALUE_ORIGINAL_TITLE));

                    try {
                        movieInPersonCast.setCharacter(movieJSON.getString(Responses.PersonCredits.VALUE_CHARACTER));
                    } catch (Exception e) {
                        movieInPersonCast.setCharacter(" ");
                    }

                    try {
                        String releaseDate = movieJSON.getString(Responses.PersonCredits.VALUE_RELEASE_DATE);
                        if (releaseDate != null) {
                            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            movieInPersonCast.getMovie().setReleaseDate(Calendar.getInstance());
                            movieInPersonCast.getMovie().getReleaseDate().setTime(dateFormat.parse(releaseDate));
                        } else {
                            movieInPersonCast.getMovie().setReleaseDate(null);
                        }
                    } catch (Exception e) {
                        movieInPersonCast.getMovie().setReleaseDate(null);
                    }

                    String posterPath = movieJSON.getString(Responses.PersonCredits.VALUE_POSTER_PATH);
                    if (posterPath != null)
                        movieInPersonCast.getMovie().setPosterPath(posterPath.substring(posterPath.lastIndexOf("/") + 1));

                    movies.add(movieInPersonCast);
                } catch (Exception ignored) {
                }
            }

            return movies;
        }

        /**
         * Parses a JSON to a {@link MovieInPersonCrew} list.
         *
         * @param json The JSON string to parse.
         * @return Returns the list of {@link MovieInPersonCrew} parsed from the JSON.
         */
        public static ArrayList<MovieInPersonCrew> personMovieCrew(String json) throws JSONException {
            ArrayList<MovieInPersonCrew> movies = new ArrayList<>();
            JSONObject jo = new JSONObject(json);
            JSONArray crewJSON = jo.getJSONArray(Responses.PersonCredits.ARRAY_CREW);

            for (int i = 0; i < crewJSON.length(); i++) {
                try {
                    JSONObject movieJSON = crewJSON.getJSONObject(i);
                    MovieInPersonCrew movieInPersonCrew = new MovieInPersonCrew();
                    movieInPersonCrew.setMovie(new Movie());

                    movieInPersonCrew.getMovie().setTMDBId(movieJSON.getInt(Responses.PersonCredits.VALUE_ID));
                    movieInPersonCrew.getMovie().setTitle(movieJSON.getString(Responses.PersonCredits.VALUE_TITLE));
                    movieInPersonCrew.getMovie().setOriginalTitle(movieJSON.getString(Responses.PersonCredits.VALUE_ORIGINAL_TITLE));
                    movieInPersonCrew.setDepartment(movieJSON.getString(Responses.PersonCredits.VALUE_DEPARTMENT));
                    movieInPersonCrew.setJob(movieJSON.getString(Responses.PersonCredits.VALUE_JOB));

                    try {
                        String releaseDate = movieJSON.getString(Responses.PersonCredits.VALUE_RELEASE_DATE);
                        if (releaseDate != null) {
                            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            movieInPersonCrew.getMovie().setReleaseDate(Calendar.getInstance());
                            movieInPersonCrew.getMovie().getReleaseDate().setTime(dateFormat.parse(releaseDate));
                        } else {
                            movieInPersonCrew.getMovie().setReleaseDate(null);
                        }
                    } catch (ParseException e) {
                        movieInPersonCrew.getMovie().setReleaseDate(null);
                    }

                    String posterPath = movieJSON.getString(Responses.PersonCredits.VALUE_POSTER_PATH);
                    if (posterPath != null)
                        movieInPersonCrew.getMovie().setPosterPath(posterPath.substring(posterPath.lastIndexOf("/") + 1));

                    movies.add(movieInPersonCrew);
                } catch (Exception ignored) {
                }
            }

            return movies;
        }

    }

    /**
     * Represents the default data of the service.
     */
    public static class DefaultData {

        /**
         * Holds the default data for the "Genres" list.
         */
        public static final Genre[] genres = new Genre[]{
                new Genre(28, "Action"),
                new Genre(12, "Adventure"),
                new Genre(16, "Animation"),
                new Genre(35, "Comedy"),
                new Genre(80, "Crime"),
                new Genre(99, "Documentary"),
                new Genre(18, "Drama"),
                new Genre(10751, "Family"),
                new Genre(14, "Fantasy"),
                new Genre(36, "History"),
                new Genre(27, "Horror"),
                new Genre(10402, "Music"),
                new Genre(9648, "Mystery"),
                new Genre(10749, "Romance"),
                new Genre(878, "Science Fiction"),
                new Genre(10770, "TV Movie"),
                new Genre(53, "Thriller"),
                new Genre(10752, "War"),
                new Genre(37, "Western")
        };

    }

}
