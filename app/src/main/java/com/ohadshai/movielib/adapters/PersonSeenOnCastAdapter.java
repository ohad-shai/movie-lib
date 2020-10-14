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
import com.ohadshai.movielib.entities.MovieInPersonCast;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.ui.activities.MovieDisplayActivity;
import com.ohadshai.movielib.utils.Utils;
import com.ohadshai.movielib.utils.web_services.tmdb_api.TMDBService;

import java.io.File;
import java.util.ArrayList;

/**
 * Represents the "Person Seen On Cast" adapter for RecyclerView.
 * Created by Ohad on 03/31/2017.
 */
public class PersonSeenOnCastAdapter extends RecyclerView.Adapter<PersonSeenOnCastAdapter.ViewHolder> {

    //region Private Members

    /**
     * Holds the activity owner of the adapter.
     */
    private Activity _activity;

    /**
     * Holds the list of movies seen on person cast to adapt.
     */
    private ArrayList<MovieInPersonCast> _movieInPersonCasts;

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of the "Person Seen On Cast" adapter for RecyclerView.
     *
     * @param movieInPersonCasts The list of movies seen on person cast to adapt.
     * @param activity           The activity owner of the adapter.
     */
    public PersonSeenOnCastAdapter(ArrayList<MovieInPersonCast> movieInPersonCasts, Activity activity) {
        this._movieInPersonCasts = movieInPersonCasts;
        this._activity = activity;
    }

    //endregion

    //region Events

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflates the custom layout:
        View view = inflater.inflate(R.layout.item_movie_in_person_cast, parent, false);

        return new ViewHolder(view); // Returns a new holder instance.
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bindViewHolder(_movieInPersonCasts.get(position)); // Gets the movieInPersonCast by the position, and binds it to the view holder.
    }

    @Override
    public int getItemCount() {
        return _movieInPersonCasts.size();
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

        private MovieInPersonCast movieInPersonCast;

        private CardView cardMovie;
        private ImageView imgMovie;
        private TextView lblMovieTitle;
        private TextView lblMovieYear;
        private TextView lblPersonCharacter;

        //endregion

        ViewHolder(final View itemView) {
            super(itemView);

            cardMovie = (CardView) itemView.findViewById(R.id.cardMovie);
            cardMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Starts the display activity of the movie:
                    Intent displayIntent = new Intent(_activity, MovieDisplayActivity.class);
                    displayIntent.putExtra(UIConsts.Bundles.MOVIE_KEY, movieInPersonCast.getMovie());
                    _activity.startActivity(displayIntent);
                }
            });

            imgMovie = (ImageView) itemView.findViewById(R.id.imgMovie);

            lblMovieTitle = (TextView) itemView.findViewById(R.id.lblMovieTitle);

            lblMovieYear = (TextView) itemView.findViewById(R.id.lblMovieYear);

            lblPersonCharacter = (TextView) itemView.findViewById(R.id.lblPersonCharacter);

        }

        //region Local Methods

        /**
         * Binds a {@link MovieInPersonCast} object to the view holder.
         *
         * @param movieInPersonCast The {@link MovieInPersonCast} object to bind.
         */
        void bindViewHolder(MovieInPersonCast movieInPersonCast) {
            this.movieInPersonCast = movieInPersonCast; // Sets the movieInPersonCast object to the view holder local.

            // Sets item views, based on the views and the data model:

            //region Movie Image Load

            if (movieInPersonCast.getMovie().getPosterPath() != null) {
                File imageFile = new File(Utils.Storage.getInternalStoragePath(getContext()), movieInPersonCast.getMovie().getPosterPath());
                if (imageFile.exists()) {
                    Glide.with(_activity).load(imageFile)
                            .placeholder(R.drawable.no_movie_image)
                            .override((int) _activity.getResources().getDimension(R.dimen.image_small_width), (int) _activity.getResources().getDimension(R.dimen.image_small_height))
                            .centerCrop()
                            .into(imgMovie);
                } else {
                    Glide.with(_activity).load(TMDBService.Urls.GET_PHOTO_BY_PATH + movieInPersonCast.getMovie().getPosterPath())
                            .placeholder(R.drawable.no_movie_image)
                            .override((int) _activity.getResources().getDimension(R.dimen.image_small_width), (int) _activity.getResources().getDimension(R.dimen.image_small_height))
                            .centerCrop()
                            .into(imgMovie);
                }
            } else {
                Glide.with(_activity).load(R.drawable.no_movie_image)
                        .override((int) _activity.getResources().getDimension(R.dimen.image_small_width), (int) _activity.getResources().getDimension(R.dimen.image_small_height))
                        .centerCrop()
                        .into(imgMovie);
            }

            //endregion

            lblMovieTitle.setText(movieInPersonCast.getMovie().getTitle());

            lblMovieYear.setText(movieInPersonCast.getMovie().displayYear());

            lblPersonCharacter.setText(movieInPersonCast.getCharacter());
        }

        //endregion

    }

    //endregion

}
