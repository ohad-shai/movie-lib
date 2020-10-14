package com.ohadshai.movielib.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ohadshai.movielib.R;
import com.ohadshai.movielib.entities.Genre;
import com.ohadshai.movielib.ui.activities.MovieCreateActivity;

import java.util.List;

/**
 * Represents a movie genres adapter for RecyclerView.
 * Created by Ohad on 10/3/2016.
 */
public class MovieGenresAdapter extends RecyclerView.Adapter<MovieGenresAdapter.ViewHolder> {

    //region Private Members

    /**
     * Holds the activity owner of the adapter.
     */
    private Activity _activity;

    /**
     * Holds the list of movie genres to adapt.
     */
    private List<Genre> _genres;

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of a movie genres adapter for RecyclerView.
     *
     * @param genres   The list of movie genres to adapt.
     * @param activity The activity owner of the adapter.
     */
    public MovieGenresAdapter(List<Genre> genres, Activity activity) {
        this._genres = genres;
        this._activity = activity;
    }

    //endregion

    //region Events

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflates the custom layout:
        View view = inflater.inflate(R.layout.item_movie_genre, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bindViewHolder(_genres.get(position));
    }

    @Override
    public int getItemCount() {
        return _genres.size();
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

    /**
     * Removes a movie genre from the adapter.
     *
     * @param position The position of the movie genre to remove.
     */
    private void removeMovieGenre(int position) {
        _genres.remove(position);
        notifyItemRemoved(position);

        if (_genres.size() < 1) {
            _genres.clear();
            notifyDataSetChanged();
            ((MovieCreateActivity) _activity).displayMovieGenresListState();
        }
    }

    //endregion

    //region Inner Classes

    class ViewHolder extends RecyclerView.ViewHolder {

        //region Private Members

        Genre _genre;

        TextView _genreName;
        ImageButton _imgbtnRemove;

        //endregion

        ViewHolder(final View itemView) {
            super(itemView);

            _genreName = (TextView) itemView.findViewById(R.id.txtGenreName);

            _imgbtnRemove = (ImageButton) itemView.findViewById(R.id.imgbtnRemove);
            _imgbtnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeMovieGenre(getAdapterPosition());
                }
            });

        }

        //region Local Methods

        /**
         * Binds a movie genre to the view holder.
         *
         * @param genre The genre to bind.
         */
        void bindViewHolder(Genre genre) {
            this._genre = genre;

            // Sets item views, based on the views and the data model:

            _genreName.setText(genre.getName()); // Sets the genre name.
        }

        //endregion

    }

    //endregion

}