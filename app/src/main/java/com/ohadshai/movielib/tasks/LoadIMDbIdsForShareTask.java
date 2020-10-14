package com.ohadshai.movielib.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.ohadshai.movielib.R;
import com.ohadshai.movielib.entities.Movie;
import com.ohadshai.movielib.utils.Utils;
import com.ohadshai.movielib.utils.web_services.IMDbConsts;
import com.ohadshai.movielib.utils.web_services.tmdb_api.TMDBService;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Represents a task for loading the IMDb id of movies from the TMDB service (in a "Share" action).
 * Created by Ohad on 4/6/2017.
 */
public class LoadIMDbIdsForShareTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

    //region Private Members

    /**
     * Holds the activity owner.
     */
    private Activity _activity;

    /**
     * Holds the {@link ProgressDialog} control.
     */
    private ProgressDialog _progressDialog;

    /**
     * Holds the selection list of {@link Movie}.
     */
    private ArrayList<Movie> _selection;

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of a task for loading the IMDb id of movies from the TMDB service (in a "Share" action).
     *
     * @param activity  The activity owner.
     * @param selection The selection list of {@link Movie}.
     */
    public LoadIMDbIdsForShareTask(@NonNull Activity activity, @NonNull ArrayList<Movie> selection) {
        this._activity = activity;
        this._selection = new ArrayList<>(selection);
    }

    //endregion

    //region Task Events

    @Override
    protected void onPreExecute() {
        // Checks if there's no network connection:
        if (!Utils.Networking.isNetworkAvailable(_activity)) {
            Utils.UI.showNoNetworkConnectionDialog(_activity);
            this.cancel(true);
        }

        // Shows a progress dialog to the user:
        _progressDialog = new ProgressDialog(_activity);
        _progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                cancel(true);
            }
        });
        _progressDialog.setMessage(_activity.getString(R.string.general_msg_please_wait));
        _progressDialog.show();
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {
        try {
            for (Movie movie : _selection) {
                String json = Utils.Networking.sendHttpRequest(TMDBService.Urls.GET_MOVIE_BY_TMDB_ID + movie.getTMDBId() + TMDBService.Params.START_API_KEY, _activity);
                String imdbId = new JSONObject(json).getString(TMDBService.Responses.Movie.VALUE_IMDB_ID);
                if (imdbId != null && !imdbId.trim().equals("") && !imdbId.equals("null"))
                    movie.setIMDbId(imdbId);
            }
            return _selection;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        _progressDialog.dismiss();

        if (movies == null) {
            Toast.makeText(_activity, R.string.general_msg_something_went_wrong, Toast.LENGTH_SHORT).show();
            return;
        }

        // Shares the selection of movies:
        StringBuilder sb = new StringBuilder();
        for (Movie movie : movies) {
            sb.append(movie.getTitle()).append(" ").append(movie.displayYear());
            if (movie.getIMDbId() != null)
                sb.append("\n").append(IMDbConsts.IMDB_MOVIE_PAGE).append(movie.getIMDbId());
            sb.append("\n\n");
        }

        if (sb.length() >= 2)
            Utils.Intents.share(sb.substring(0, sb.length() - 2), R.string.general_share_via, _activity);
        else
            Toast.makeText(_activity, R.string.general_msg_something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCancelled() {
        try {
            // Hides the progress dialog if shown:
            if (_progressDialog != null)
                _progressDialog.dismiss();
        } catch (Exception ignored) {
        }

        _activity = null;
        _progressDialog = null;
        _selection = null;
    }

    //endregion

}
