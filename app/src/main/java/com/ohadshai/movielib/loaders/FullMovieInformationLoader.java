package com.ohadshai.movielib.loaders;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.ohadshai.movielib.db.DBHandler;
import com.ohadshai.movielib.entities.Movie;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.utils.Utils;
import com.ohadshai.movielib.utils.web_services.tmdb_api.TMDBService;

/**
 * Represents a loader for a "Full Movie Information".
 * Created by Ohad on 03/28/2017.
 */
public class FullMovieInformationLoader extends AsyncTaskLoader<Movie> {

    //region Private Members

    /**
     * Holds the context owner.
     */
    private Context _context;

    /**
     * Holds an {@link Exception} object if thrown.
     */
    private Exception _exception;

    /**
     * Holds the TMDB id of the movie to get the information.
     */
    private int _tmdbId;

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of a loader for a "Full Movie Information".
     *
     * @param context The context owner.
     * @param tmdbId  The TMDB id of the movie to get the information.
     */
    public FullMovieInformationLoader(@NonNull Context context, int tmdbId) {
        super(context);
        this._context = context;
        this._tmdbId = tmdbId;
    }

    //endregion

    //region Loader Events

    @Override
    public Movie loadInBackground() {
        DBHandler db = DBHandler.getInstance(_context);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String url = TMDBService.Urls.GET_MOVIE_BY_TMDB_ID + _tmdbId + TMDBService.Params.START_API_KEY +
                TMDBService.Params.APPEND_LANGUAGE + preferences.getString(UIConsts.Preferences.Keys.LANGUAGE, "") +
                TMDBService.Params.APPEND_TO_RESPONSE_REVIEWS_AND_CREDITS;

        try {
            String json = Utils.Networking.sendHttpRequest(url, _context);
            return TMDBService.Parse.movieWithReviewsAndCredits(json);
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
