package com.ohadshai.movielib.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.ohadshai.movielib.db.DBHandler;
import com.ohadshai.movielib.entities.Person;

import java.util.ArrayList;

/**
 * Represents a loader for the "My Directors" list.
 * Created by Ohad on 03/20/2017.
 */
public class MyDirectorsLoader extends AsyncTaskLoader<ArrayList<Person>> {

    //region Private Members

    /**
     * Holds the database interactions object.
     */
    private DBHandler _repository;

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of a loader for the "My Directors" list.
     *
     * @param context The context owner.
     */
    public MyDirectorsLoader(Context context) {
        super(context);
        _repository = DBHandler.getInstance(context);
    }

    //endregion

    //region Loader Events

    @Override
    public ArrayList<Person> loadInBackground() {
        return _repository.people.getAllMyDirectors();
    }

    //endregion

}
