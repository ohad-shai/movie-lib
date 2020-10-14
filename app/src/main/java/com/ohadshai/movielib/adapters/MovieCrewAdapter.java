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
import com.ohadshai.movielib.entities.PersonInMovieCrew;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.ui.activities.PersonDisplayActivity;
import com.ohadshai.movielib.utils.Utils;
import com.ohadshai.movielib.utils.web_services.tmdb_api.TMDBService;

import java.io.File;
import java.util.ArrayList;

/**
 * Represents the "Movie Crew" adapter for RecyclerView.
 * Created by Ohad on 03/27/2017.
 */
public class MovieCrewAdapter extends RecyclerView.Adapter<MovieCrewAdapter.ViewHolder> {

    //region Private Members

    /**
     * Holds the activity owner of the adapter.
     */
    private Activity _activity;

    /**
     * Holds the list of persons in movie crew to adapt.
     */
    private ArrayList<PersonInMovieCrew> _personsInMovieCrew;

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of the "Movie Crew" adapter for RecyclerView.
     *
     * @param personsInMovieCrew The list of persons in movie crew to adapt.
     * @param activity           The activity owner of the adapter.
     */
    public MovieCrewAdapter(ArrayList<PersonInMovieCrew> personsInMovieCrew, Activity activity) {
        this._personsInMovieCrew = personsInMovieCrew;
        this._activity = activity;
    }

    //endregion

    //region Events

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflates the custom layout:
        View view = inflater.inflate(R.layout.item_person_in_movie_crew, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bindViewHolder(_personsInMovieCrew.get(position)); // Gets the personInMovieCrew by the position, and binds it to the view holder.
    }

    @Override
    public int getItemCount() {
        return _personsInMovieCrew.size();
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

        private PersonInMovieCrew personInMovieCrew;

        private CardView cardPerson;
        private ImageView imgPerson;
        private TextView lblPersonName;
        private TextView lblPersonDepartment;
        private TextView lblPersonJob;

        //endregion

        ViewHolder(final View itemView) {
            super(itemView);

            cardPerson = (CardView) itemView.findViewById(R.id.cardPerson);
            cardPerson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Starts the display activity of the person:
                    Intent displayIntent = new Intent(_activity, PersonDisplayActivity.class);
                    displayIntent.putExtra(UIConsts.Bundles.PERSON_KEY, personInMovieCrew.getPerson());
                    _activity.startActivity(displayIntent);
                }
            });

            imgPerson = (ImageView) itemView.findViewById(R.id.imgPerson);

            lblPersonName = (TextView) itemView.findViewById(R.id.lblPersonName);

            lblPersonDepartment = (TextView) itemView.findViewById(R.id.lblPersonDepartment);

            lblPersonJob = (TextView) itemView.findViewById(R.id.lblPersonJob);

        }

        //region Local Methods

        /**
         * Binds a {@link PersonInMovieCrew} object to the view holder.
         *
         * @param personInMovieCrew The {@link PersonInMovieCrew} object to bind.
         */
        void bindViewHolder(PersonInMovieCrew personInMovieCrew) {
            this.personInMovieCrew = personInMovieCrew; // Sets the personInMovieCrew object to the view holder local.

            // Sets item views, based on the views and the data model:

            //region Movie Image Load

            if (personInMovieCrew.getPerson().getProfilePath() != null) {
                File imageFile = new File(Utils.Storage.getInternalStoragePath(getContext()), personInMovieCrew.getPerson().getProfilePath());
                if (imageFile.exists()) {
                    Glide.with(_activity).load(imageFile)
                            .placeholder(R.drawable.no_person_image)
                            .override((int) _activity.getResources().getDimension(R.dimen.image_small_width), (int) _activity.getResources().getDimension(R.dimen.image_small_height))
                            .centerCrop()
                            .into(imgPerson);
                } else {
                    Glide.with(_activity).load(TMDBService.Urls.GET_PHOTO_BY_PATH + personInMovieCrew.getPerson().getProfilePath())
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

            lblPersonName.setText(personInMovieCrew.getPerson().getName());

            lblPersonDepartment.setText(personInMovieCrew.getDepartment());

            lblPersonJob.setText(personInMovieCrew.getJob());
        }

        //endregion

    }

    //endregion

}
