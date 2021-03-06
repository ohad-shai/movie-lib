package com.ohadshai.movielib.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.ohadshai.movielib.db.DBHandler;
import com.ohadshai.movielib.entities.Movie;

import java.util.ArrayList;

/**
 * Represents a loader for the "Wishlist" list.
 * Created by Ohad on 03/20/2017.
 */
public class WishlistLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    //region Private Members

    /**
     * Holds the database interactions object.
     */
    private DBHandler _repository;

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of a loader for the "Wishlist" list.
     *
     * @param context The context owner.
     */
    public WishlistLoader(Context context) {
        super(context);
        _repository = DBHandler.getInstance(context);
    }

    //endregion

    //region Loader Events

    @Override
    public ArrayList<Movie> loadInBackground() {
        return _repository.movies.getAllWishlist();
    }

    //endregion

}
