package com.ohadshai.movielib.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ohadshai.movielib.db.DBHandler;
import com.ohadshai.movielib.entities.Genre;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.utils.Cache;
import com.ohadshai.movielib.utils.Utils;
import com.ohadshai.movielib.utils.web_services.tmdb_api.TMDBService;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for getting the list of genres from the TMDB service to the database.
 */
public class GetGenresListService extends IntentService {

    public GetGenresListService() {
        super("GetGenresListService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DBHandler db = DBHandler.getInstance(getApplicationContext());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = TMDBService.Urls.GET_GENRES_LIST +
                TMDBService.Params.APPEND_LANGUAGE + preferences.getString(UIConsts.Preferences.Keys.LANGUAGE, "");

        try {
            String json = Utils.Networking.sendHttpRequest(url, getApplicationContext());
            ArrayList<Genre> genres = TMDBService.Parse.genres(json);
            db.genres.createList(genres);
            Cache.put(genres, Cache.Keys.GENRES_LIST_FROM_DB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
