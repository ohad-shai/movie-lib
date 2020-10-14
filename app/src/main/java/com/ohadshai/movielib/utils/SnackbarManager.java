package com.ohadshai.movielib.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Represents a manager for {@link Snackbar}.
 * Created by Ohad on 1/9/2017.
 */
public class SnackbarManager {

    //region Private Members

    /**
     * Holds the instance of the "SnackbarManager", in order to implement a singleton manner.
     */
    private static SnackbarManager _instance;

    /**
     * Holds the list of all the relations with this "SnackbarManager".
     */
    private ArrayList<Relation> _relations = new ArrayList<>();

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of a manager for {@link Snackbar}.
     */
    private SnackbarManager() {
        // Disables the initialization of a new instance from the outside,
        // in order to implement a singleton manner.
    }

    //endregion

    //region Public Static API

    /**
     * Gets the "SnackbarManager" instance of the application, or creates a new instance if null.
     *
     * @return Returns the "SnackbarManager" instance of the application.
     */
    public static SnackbarManager getInstance() {
        if (_instance == null)
            _instance = new SnackbarManager();

        return _instance;
    }

    /**
     * Gets the "SnackbarManager" instance to help the activity.
     *
     * @param activity The activity owner.
     * @return Returns the "SnackbarManager" instance.
     */
    public static Relation with(@NonNull Activity activity) {
        return SnackbarManager.getInstance().initializeRelation(activity);
    }

    //endregion

    //region Private Methods

    /**
     * Initializes the relation for the "SnackbarManager", to relate the provided activity.
     *
     * @param activity The activity to relate the "SnackbarManager".
     * @return Returns a relation object between the "SnackbarManager" to the activity.
     */
    private Relation initializeRelation(@NonNull Activity activity) {
        // Searches for an existed relation:
        for (Relation relation : _relations) {
            if (relation.getActivity() == activity)
                return relation;
        }

        Relation newRelation = new Relation(activity);
        _relations.add(newRelation);
        return newRelation;
    }

    //endregion

    //region Inner Classes

    /**
     * Represents a relation between the "SnackbarManager" and an activity.
     */
    public class Relation {

        //region Private Members

        /**
         * Holds the activity owner.
         */
        private Activity _activity;

        /**
         * Holds the {@link Snackbar} control.
         */
        private Snackbar _snackbar;

        /**
         * Holds the number of Snackbars currently showing.
         */
        private int _showing;

        //endregion

        //region C'tor

        /**
         * Initializes a new instance of a relation between the search helper and an activity
         *
         * @param activity The activity to relate the "SearchHelper".
         */
        public Relation(@NonNull Activity activity) {
            this._activity = activity;
        }

        //endregion

        //region Public API

        /**
         * Sets a {@link Snackbar} to the relation.
         *
         * @param snackbar The {@link Snackbar} to set the relation.
         * @return Returns the relation object.
         */
        public Relation set(Snackbar snackbar) {
            _snackbar = snackbar;
            return this;
        }

        /**
         * Makes a new {@link Snackbar} to the relation.
         *
         * @param view     The view to find a parent from.
         * @param text     The text to show. Can be formatted text.
         * @param duration How long to display the message. Either LENGTH_SHORT or LENGTH_LONG
         * @return Returns the {@link Relation} object created.
         */
        public Relation make(@NonNull @Nullable View view, @NonNull @Nullable CharSequence text, int duration) {
            _snackbar = Snackbar.make(view, text, duration);
            return this;
        }

        /**
         * Makes a new {@link Snackbar} to the relation.
         *
         * @param view     The view to find a parent from.
         * @param resId    The resource id of the string resource to use. Can be formatted text.
         * @param duration How long to display the message. Either LENGTH_SHORT or LENGTH_LONG
         * @return Returns the {@link Relation} object created.
         */
        public Relation make(@NonNull View view, @StringRes int resId, int duration) {
            _snackbar = Snackbar.make(view, resId, duration);
            return this;
        }

        /**
         * Sets the text color of the snackbar.
         *
         * @param color The color of the snackbar to set.
         * @return Returns the {@link Relation} object.
         */
        public Relation setTextColor(int color) {
            ((TextView) _snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(color);
            return this;
        }

        /**
         * Sets the text style to bold or normal.
         *
         * @param isBold An indicator indicating whether to set the text bold or not.
         * @return Returns the {@link Relation} object.
         */
        public Relation setTextBold(boolean isBold) {
            if (isBold)
                ((TextView) _snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTypeface(null, Typeface.BOLD);
            else
                ((TextView) _snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTypeface(null, Typeface.NORMAL);

            return this;
        }

        /**
         * Shows the {@link Snackbar} to the activity in the relation.
         */
        public void show() {
            if (_snackbar == null)
                throw new NullPointerException("No snackbar was made for the relation.");

            _snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    _showing--;
                }

                @Override
                public void onShown(Snackbar transientBottomBar) {
                    super.onShown(transientBottomBar);
                }
            });

            _snackbar.show();
            _showing++;
        }

        /**
         * Dismisses the {@link Snackbar} from the activity in the relation.
         */
        public void dismiss() {
            if (_snackbar == null)
                throw new NullPointerException("No snackbar was made for the relation.");

            _snackbar.dismiss();
        }

        /**
         * Indicates whether a {@link Snackbar} is showing in the activity or not.
         *
         * @return Returns true if the {@link Snackbar} is showing in the activity, otherwise false.
         */
        public boolean isShowing() {
            return _snackbar != null && _snackbar.isShownOrQueued();
        }

        /**
         * Gets the activity owner.
         *
         * @return Returns the activity owner.
         */
        public Activity getActivity() {
            return _activity;
        }

        /**
         * Removes this relation with the SnackbarManager.
         */
        public void removeRelation() {
            _relations.remove(Relation.this);
        }

        //endregion

    }

    //endregion

}
