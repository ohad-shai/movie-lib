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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ohadshai.movielib.R;
import com.ohadshai.movielib.db.DBHandler;
import com.ohadshai.movielib.entities.Person;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.ui.activities.PersonDisplayActivity;
import com.ohadshai.movielib.utils.SelectionHelper;
import com.ohadshai.movielib.utils.Utils;
import com.ohadshai.movielib.utils.web_services.tmdb_api.TMDBService;

import java.io.File;
import java.util.ArrayList;

import static com.ohadshai.movielib.utils.SelectionHelper.SELECTED;

/**
 * Represents a directors adapter for RecyclerView.
 * Created by Ohad on 03/20/2017.
 */
public class MyDirectorsAdapter extends RecyclerView.Adapter<MyDirectorsAdapter.ViewHolder> {

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
     * Holds the list of directors to adapt.
     */
    private ArrayList<Person> _directors;

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of a directors adapter for RecyclerView.
     *
     * @param directors The list of directors to adapt.
     * @param fragment  The fragment owner of the adapter.
     */
    public MyDirectorsAdapter(ArrayList<Person> directors, Fragment fragment) {
        this._directors = directors;
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
        View view = inflater.inflate(R.layout.item_person, parent, false);

        return new ViewHolder(view); // Returns a new holder instance.
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bindViewHolder(_directors.get(position)); // Gets the director by the position, and binds it to the view holder.
    }

    @Override
    public int getItemCount() {
        return _directors.size();
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

    class ViewHolder extends RecyclerView.ViewHolder implements SelectionHelper.SelectionItemController<Person> {

        //region Private Members

        private Person _person;

        private CardView cardPerson;
        private TextView lblPersonName;
        private ImageView imgPerson;
        private ImageView imgbtnFavoritePerson;

        private RelativeLayout _rlSelection;
        private CheckBox _chkSelection;

        //endregion

        ViewHolder(final View itemView) {
            super(itemView);

            cardPerson = (CardView) itemView.findViewById(R.id.cardPerson);
            cardPerson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Starts the display activity of the _person:
                    Intent displayIntent = new Intent(_activity, PersonDisplayActivity.class);
                    displayIntent.putExtra(UIConsts.Bundles.PERSON_ID_KEY, _person.getId());
                    _fragment.startActivityForResult(displayIntent, UIConsts.RequestCodes.PERSON_DISPLAY_REQUEST_CODE);
                }
            });
            cardPerson.setOnLongClickListener(new View.OnLongClickListener() {
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

            lblPersonName = (TextView) itemView.findViewById(R.id.lblPersonName);

            imgPerson = (ImageView) itemView.findViewById(R.id.imgPerson);

            imgbtnFavoritePerson = (ImageView) itemView.findViewById(R.id.imgbtnFavoritePerson);
            imgbtnFavoritePerson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (_person.isFavorite()) {
                        imgbtnFavoritePerson.setColorFilter(Color.rgb(136, 136, 136));
                        _person.setFavorite(false);
                        _repository.people.updateFolderRelated(_person);
                    } else {
                        imgbtnFavoritePerson.setColorFilter(Utils.Colors.FAVORITE);
                        _person.setFavorite(true);
                        _repository.people.updateFolderRelated(_person);
                    }
                }
            });
            imgbtnFavoritePerson.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Utils.UI.showInformationToast(imgbtnFavoritePerson, R.string.general_favorite);
                    return true;
                }
            });

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
        public SelectionHelper<Person> getSelectionHelper() {
            return ((SelectionHelper.SelectionController<Person>) _fragment).getSelectionHelper();
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
                if (getSelectionHelper().isItemSelected(_person))
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
                drawSelection(SELECTED);
                getSelectionHelper().itemSelection(getLayoutPosition(), true);
            } else {
                drawSelection(SelectionHelper.UNSELECTED);
                getSelectionHelper().itemSelection(getLayoutPosition(), false);
            }
        }

        //endregion

        //region Local Methods

        /**
         * Binds an person object to the view holder.
         *
         * @param person The person object to bind.
         */
        void bindViewHolder(Person person) {
            this._person = person; // Sets the person object to the view holder local.

            // Sets item views, based on the views and the data model:

            //region Person Image Load

            if (_person.getProfilePath() != null) {
                File imageFile = new File(Utils.Storage.getInternalStoragePath(getContext()), _person.getProfilePath());
                if (imageFile.exists()) {
                    Glide.with(_activity).load(imageFile)
                            .placeholder(R.drawable.no_person_image)
                            .override((int) _activity.getResources().getDimension(R.dimen.image_medium_width), (int) _activity.getResources().getDimension(R.dimen.image_medium_height))
                            .centerCrop()
                            .into(imgPerson);
                } else {
                    Glide.with(_activity).load(TMDBService.Urls.GET_PHOTO_BY_PATH + _person.getProfilePath())
                            .placeholder(R.drawable.no_person_image)
                            .override((int) _activity.getResources().getDimension(R.dimen.image_medium_width), (int) _activity.getResources().getDimension(R.dimen.image_medium_height))
                            .centerCrop()
                            .into(imgPerson);
                }
            } else {
                Glide.with(_activity).load(R.drawable.no_person_image)
                        .override((int) _activity.getResources().getDimension(R.dimen.image_medium_width), (int) _activity.getResources().getDimension(R.dimen.image_medium_height))
                        .centerCrop()
                        .into(imgPerson);
            }

            //endregion

            lblPersonName.setText(person.getName());

            if (person.isFavorite())
                imgbtnFavoritePerson.setColorFilter(Utils.Colors.FAVORITE);
            else
                imgbtnFavoritePerson.setColorFilter(Color.rgb(136, 136, 136));

            this.bindSelection();
        }

        //endregion

    }

    //endregion

}
