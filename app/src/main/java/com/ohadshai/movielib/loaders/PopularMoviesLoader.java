package com.ohadshai.movielib.loaders;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

import com.ohadshai.movielib.db.DBHandler;
import com.ohadshai.movielib.entities.Genre;
import com.ohadshai.movielib.entities.Movie;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.utils.Cache;
import com.ohadshai.movielib.utils.Utils;
import com.ohadshai.movielib.utils.web_services.tmdb_api.TMDBService;

import java.util.ArrayList;

/**
 * Represents a loader for the "Popular Movies" list.
 * Created by Ohad on 03/21/2017.
 */
public class PopularMoviesLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    //region Private Members

    /**
     * Holds the context owner.
     */
    private Context _context;

    /**
     * Holds an {@link Exception} object if thrown.
     */
    private Exception _exception;

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of a loader for the "Popular Movies" list.
     *
     * @param context The context owner.
     */
    public PopularMoviesLoader(Context context) {
        super(context);
        this._context = context;
    }

    //endregion

    //region Loader Events

    @Override
    public ArrayList<Movie> loadInBackground() {
        DBHandler db = DBHandler.getInstance(_context);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String url = TMDBService.Urls.GET_POPULAR_MOVIES +
                TMDBService.Params.APPEND_PAGE + "1" +
                TMDBService.Params.APPEND_LANGUAGE + preferences.getString(UIConsts.Preferences.Keys.LANGUAGE, "") +
                TMDBService.Params.APPEND_REGION + preferences.getString(UIConsts.Preferences.Keys.REGION, "");

        try {
            String json = Utils.Networking.sendHttpRequest(url, _context); // Sends an HTTP request, and gets the JSON.
            ArrayList<Movie> movies = TMDBService.Parse.movieList(json); // Parses the movies list from the JSON.
            ArrayList<Genre> genres = Cache.getOtherwisePut(Cache.Keys.GENRES_LIST_FROM_DB, db.genres.getAll()); // Gets all the genres in the database.
            for (Movie movie : movies) {
                // Gets the name of each genre by it's id:
                for (Genre genre : movie.getGenres())
                    genre.setName(Genre.getGenreNameInListById(genre.getId(), genres));
            }
            return movies;
        } catch (Exception e) {
            this._exception = e;
            return null;
        }
    }

    //endregion

    //region Public API

    /**
     * Gets an {@link Exception} object if thrown.
     *
     * @return Returns an {@link Exception} object if thrown.
     */
    public Exception getException() {
        return _exception;
    }

    //endregion

}
