package com.ohadshai.movielib.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ohadshai.movielib.R;
import com.ohadshai.movielib.entities.PersonInMovieCast;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.ui.activities.PersonDisplayActivity;
import com.ohadshai.movielib.utils.Utils;
import com.ohadshai.movielib.utils.web_services.tmdb_api.TMDBService;

import java.io.File;
import java.util.ArrayList;

/**
 * Represents the "Movie Cast" adapter for RecyclerView.
 * Created by Ohad on 03/27/2017.
 */
public class MovieCastAdapter extends RecyclerView.Adapter<MovieCastAdapter.ViewHolder> {

    //region Private Members

    /**
     * Holds the activity owner of the adapter.
     */
    private Activity _activity;

    /**
     * Holds the list of persons in movie cast to adapt.
     */
    private ArrayList<PersonInMovieCast> _personsInMovieCast;

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of the "Movie Cast" adapter for RecyclerView.
     *
     * @param personsInMovieCast The list of persons in movie cast to adapt.
     * @param activity           The activity owner of the adapter.
     */
    public MovieCastAdapter(ArrayList<PersonInMovieCast> personsInMovieCast, Activity activity) {
        this._personsInMovieCast = personsInMovieCast;
        this._activity = activity;
    }

    //endregion

    //region Events

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflates the custom layout:
        View view = inflater.inflate(R.layout.item_person_in_movie_cast, parent, false);

        return new ViewHolder(view); // Returns a new holder instance.
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bindViewHolder(_personsInMovieCast.get(position)); // Gets the personInMovieCast by the position, and binds it to the view holder.
    }

    @Override
    public int getItemCount() {
        return _personsInMovieCast.size();
    }

    //endregion

    //region Public API

    /**
     * Gets the context of the adapter.
     *
     * @return Returns the context of the adapter.
     */
    public Context getContext() {
        return this._activity.getApplicationContext();
    }

    //endregion

    //region Private Methods

    //endregion

    //region Inner Classes

    class ViewHolder extends RecyclerView.ViewHolder {

        //region Private Members

        private PersonInMovieCast personInMovieCast;

        private CardView cardPerson;
        private ImageView imgPerson;
        private TextView lblPersonName;
        private TextView lblPersonCharacter;

        //endregion

        ViewHolder(final View itemView) {
            super(itemView);

            cardPerson = (CardView) itemView.findViewById(R.id.cardPerson);
            cardPerson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Starts the display activity of the person:
                    Intent displayIntent = new Intent(_activity, PersonDisplayActivity.class);
                    displayIntent.putExtra(UIConsts.Bundles.PERSON_KEY, personInMovieCast.getPerson());
                    _activity.startActivity(displayIntent);
                }
            });

            imgPerson = (ImageView) itemView.findViewById(R.id.imgPerson);

            lblPersonName = (TextView) itemView.findViewById(R.id.lblPersonName);

            lblPersonCharacter = (TextView) itemView.findViewById(R.id.lblPersonCharacter);

        }

        //region Local Methods

        /**
         * Binds a {@link PersonInMovieCast} object to the view holder.
         *
         * @param personInMovieCast The {@link PersonInMovieCast} object to bind.
         */
        void bindViewHolder(PersonInMovieCast personInMovieCast) {
            this.personInMovieCast = personInMovieCast; // Sets the personInMovieCast object to the view holder local.

            // Sets item views, based on the views and the data model:

            //region Movie Image Load

            if (personInMovieCast.getPerson().getProfilePath() != null) {
                File imageFile = new File(Utils.Storage.getInternalStoragePath(getContext()), personInMovieCast.getPerson().getProfilePath());
                if (imageFile.exists()) {
                    Glide.with(_activity).load(imageFile)
                            .placeholder(R.drawable.no_person_image)
                            .override((int) _activity.getResources().getDimension(R.dimen.image_small_width), (int) _activity.getResources().getDimension(R.dimen.image_small_height))
                            .centerCrop()
                            .into(imgPerson);
                } else {
                    Glide.with(_activity).load(TMDBService.Urls.GET_PHOTO_BY_PATH + personInMovieCast.getPerson().getProfilePath())
                            .placeholder(R.drawable.no_person_image)
                            .override((int) _activity.getResources().getDimension(R.dimen.image_small_width), (int) _activity.getResources().getDimension(R.dimen.image_small_height))
                            .centerCrop()
                            .into(imgPerson);
                }
            } else {
                Glide.with(_activity).load(R.drawable.no_person_image)
                        .override((int) _activity.getResources().getDimension(R.dimen.image_small_width), (int) _activity.getResources().getDimension(R.dimen.image_small_height))
                        .centerCrop()
                        .into(imgPerson);
            }

            //endregion

            lblPersonName.setText(personInMovieCast.getPerson().getName());

            lblPersonCharacter.setText(personInMovieCast.getCharacter());
        }

        //endregion

    }

    //endregion

}
