package com.ohadshai.movielib.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ohadshai.movielib.R;
import com.ohadshai.movielib.db.DBHandler;
import com.ohadshai.movielib.entities.Movie;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.ui.activities.MovieDisplayActivity;
import com.ohadshai.movielib.utils.SelectionHelper;
import com.ohadshai.movielib.utils.Utils;
import com.ohadshai.movielib.utils.web_services.tmdb_api.TMDBService;

import java.io.File;
import java.util.ArrayList;

import static com.ohadshai.movielib.utils.SelectionHelper.SELECTED;

/**
 * Represents the "Wishlist" adapter for RecyclerView.
 * Created by Ohad on 03/30/2017.
 */
public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    //region Private Members

    /**
     * Holds the fragment owner of the adapter.
     */
    private Fragment _fragment;

    /**
     * Holds the activity owner of the adapter.
     */
    private Activity _activity;

    /**
     * Holds the database interactions object.
     */
    private DBHandler _repository;

    /**
     * Holds the list of movies to adapt.
     */
    private ArrayList<Movie> _movies;

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of the "Wishlist" adapter for RecyclerView.
     *
     * @param movies   The list of movies to adapt.
     * @param fragment The fragment owner of the adapter.
     */
    public WishlistAdapter(ArrayList<Movie> movies, Fragment fragment) {
        this._movies = movies;
        this._fragment = fragment;
        this._activity = fragment.getActivity();
        this._repository = DBHandler.getInstance(_activity);
    }

    //endregion

    //region Events

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflates the custom layout:
        View view = inflater.inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(view); // Returns a new holder instance.
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bindViewHolder(_movies.get(position)); // Gets the movie by the position, and binds it to the view holder.
    }

    @Override
    public int getItemCount() {
        return _movies.size();
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

    class ViewHolder extends RecyclerView.ViewHolder implements SelectionHelper.SelectionItemController<Movie> {

        //region Private Members

        private Movie movie;

        private CardView cardMovie;
        private ImageView imgMovie;
        private TextView lblMovieTitle;
        private TextView lblMovieYear;
        private TextView lblMovieGenres;
        private TextView lblMoviePlot;
        private TextView lblIMDbRating;
        private RatingBar ratingUser;
        private ImageButton imgbtnMovieFavorite;
        private ImageButton imgbtnMovieWatched;

        private RelativeLayout _rlSelection;
        private CheckBox _chkSelection;

        //endregion

        ViewHolder(final View itemView) {
            super(itemView);

            cardMovie = (CardView) itemView.findViewById(R.id.cardMovie);
            cardMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Starts the display activity of the movie:
                    Intent displayIntent = new Intent(_activity, MovieDisplayActivity.class);
                    displayIntent.putExtra(UIConsts.Bundles.MOVIE_ID_KEY, movie.getId());
                    _fragment.startActivityForResult(displayIntent, UIConsts.RequestCodes.MOVIE_DISPLAY_REQUEST_CODE);
                }
            });
            cardMovie.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Checks if not already in selection mode:
                    if (!getSelectionHelper().isInSelectionMode()) {
                        getSelectionHelper().enterSelectionMode(); // Enters the selection mode.
                        itemSelection(true); // Selects this item that started the selection mode.
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            imgMovie = (ImageView) itemView.findViewById(R.id.imgMovie);

            lblMovieTitle = (TextView) itemView.findViewById(R.id.lblMovieTitle);

            lblMovieYear = (TextView) itemView.findViewById(R.id.lblMovieYear);

            lblMovieGenres = (TextView) itemView.findViewById(R.id.lblMovieGenres);

            lblMoviePlot = (TextView) itemView.findViewById(R.id.lblMoviePlot);

            lblIMDbRating = (TextView) itemView.findViewById(R.id.lblVoteAverage);

            ratingUser = (RatingBar) itemView.findViewById(R.id.ratingUser);
            ratingUser.setVisibility(View.GONE);

            imgbtnMovieFavorite = (ImageButton) itemView.findViewById(R.id.imgbtnMovieFavorite);
            imgbtnMovieFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (movie.isFavorite()) {
                        imgbtnMovieFavorite.setColorFilter(Color.rgb(136, 136, 136));
                        movie.setFavorite(false);
                        _repository.movies.updateFolderRelated(movie);
                    } else {
                        imgbtnMovieFavorite.setColorFilter(Utils.Colors.FAVORITE);
                        movie.setFavorite(true);
                        _repository.movies.updateFolderRelated(movie);
                    }
                }
            });
            imgbtnMovieFavorite.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Utils.UI.showInformationToast(imgbtnMovieFavorite, R.string.movie_favorite);
                    return true;
                }
            });

            imgbtnMovieWatched = (ImageButton) itemView.findViewById(R.id.imgbtnMovieWatched);
            imgbtnMovieWatched.setVisibility(View.GONE);

            _rlSelection = (RelativeLayout) itemView.findViewById(R.id.rlSelection);
            _rlSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _chkSelection.performClick();
                }
            });

            _chkSelection = (CheckBox) itemView.findViewById(R.id.chkSelection);
            _chkSelection.setChecked(false);
            _chkSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (_chkSelection.isChecked())
                        itemSelection(true);
                    else
                        itemSelection(false);
                }
            });

        }

        //region Selection Events

        @Override
        public SelectionHelper<Movie> getSelectionHelper() {
            return ((SelectionHelper.SelectionController<Movie>) _fragment).getSelectionHelper();
        }

        @Override
        public void drawSelection(int state) {
            if (state == SELECTED) {
                _chkSelection.setChecked(true);
                _rlSelection.setBackgroundResource(R.drawable.selected_background_style);
                _rlSelection.setVisibility(View.VISIBLE);
            } else if (state == SelectionHelper.UNSELECTED) {
                _chkSelection.setChecked(false);
                _rlSelection.setBackgroundResource(R.drawable.select_background_style);
                _rlSelection.setVisibility(View.VISIBLE);
            } else if (state == SelectionHelper.GONE) {
                _rlSelection.setBackgroundResource(R.drawable.select_background_style);
                _rlSelection.setVisibility(View.GONE);
            } else {
                throw new IllegalArgumentException("State is invalid.");
            }
        }

        @Override
        public void bindSelection() {
            // Checks if currently in the selection mode:
            if (getSelectionHelper() != null && getSelectionHelper().isInSelectionMode()) {
                // Checks if the item is selected in the selection list:
                if (getSelectionHelper().isItemSelected(movie))
                    this.drawSelection(SELECTED);
                else
                    this.drawSelection(SelectionHelper.UNSELECTED);
            } else {
                this.drawSelection(SelectionHelper.GONE);
            }
        }

        @Override
        public void itemSelection(boolean isSelected) {
            if (isSelected) {
                drawSelection(SelectionHelper.SELECTED);
                getSelectionHelper().itemSelection(getLayoutPosition(), true);
            } else {
                drawSelection(SelectionHelper.UNSELECTED);
                getSelectionHelper().itemSelection(getLayoutPosition(), false);
            }
        }

        //endregion

        //region Local Methods

        /**
         * Binds a movie object to the view holder.
         *
         * @param movie The movie object to bind.
         */
        void bindViewHolder(Movie movie) {
            this.movie = movie; // Sets the movie object to the view holder local.

            // Sets item views, based on the views and the data model:

            //region Movie Image Load

            if (movie.getPosterPath() != null) {
                File imageFile = new File(Utils.Storage.getInternalStoragePath(getContext()), movie.getPosterPath());
                if (imageFile.exists()) {
                    Glide.with(_activity).load(imageFile)
                            .placeholder(R.drawable.no_movie_image)
                            .override((int) _activity.getResources().getDimension(R.dimen.image_medium_width), (int) _activity.getResources().getDimension(R.dimen.image_medium_height))
                            .centerCrop()
                            .into(imgMovie);
                } else {
                    Glide.with(_activity).load(TMDBService.Urls.GET_PHOTO_BY_PATH + movie.getPosterPath())
                            .placeholder(R.drawable.no_movie_image)
                            .override((int) _activity.getResources().getDimension(R.dimen.image_medium_width), (int) _activity.getResources().getDimension(R.dimen.image_medium_height))
                            .centerCrop()
                            .into(imgMovie);
                }
            } else {
                Glide.with(_activity).load(R.drawable.no_movie_image)
                        .override((int) _activity.getResources().getDimension(R.dimen.image_medium_width), (int) _activity.getResources().getDimension(R.dimen.image_medium_height))
                        .centerCrop()
                        .into(imgMovie);
            }

            //endregion

            lblMovieTitle.setText(movie.getTitle());
            lblMovieYear.setText(movie.displayYear());
            lblMovieGenres.setText(movie.displayGenres());
            lblMoviePlot.setText(movie.getPlot());
            if (movie.getVoteAverage() == null || movie.getVoteAverage() <= 0) {
                lblIMDbRating.setText(_activity.getString(R.string.general_msg_unavailable));
            } else {
                lblIMDbRating.setText(String.valueOf(movie.getVoteAverage()));
            }
            if (movie.isFavorite()) {
                imgbtnMovieFavorite.setColorFilter(Utils.Colors.FAVORITE);
            } else {
                imgbtnMovieFavorite.setColorFilter(Color.rgb(136, 136, 136));
            }

            this.bindSelection();
        }

        //endregion

    }

    //endregion

}
