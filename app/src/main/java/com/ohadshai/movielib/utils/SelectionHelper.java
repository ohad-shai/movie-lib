package com.ohadshai.movielib.utils;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Represents a selection helper.
 * Created by Ohad on 3/6/2017.
 *
 * @param <T> The object type of the items.
 */
public class SelectionHelper<T extends Parcelable> {

    //region Constants

    /**
     * Holds a constant for a bundle key: "Is In Selection Mode" (intended to hold a boolean).
     */
    private static final String IS_IN_SELECTION_MODE_KEY = "is_in_selection_mode_key";

    /**
     * Holds a constant for a bundle key: "Selected Positions List" (intended to hold an array of integers represents the items selected positions).
     */
    private static final String SELECTED_POSITIONS_ARRAY_KEY = "selected_positions_array_key";

    /**
     * Holds a constant for a bundle key: "Selected Positions List" (intended to hold an array of integers represents the items selected positions).
     */
    public static final int SELECTED = 36171;

    /**
     * Holds a constant for a bundle key: "Selected Positions List" (intended to hold an array of integers represents the items selected positions).
     */
    public static final int UNSELECTED = 36172;

    /**
     * Holds a constant for a bundle key: "Selected Positions List" (intended to hold an array of integers represents the items selected positions).
     */
    public static final int GONE = 36173;

    //endregion

    //region Private Members

    /**
     * Holds an indicator indicating whether the helper is in selection mode or not.
     */
    private boolean _isInSelectionMode;

    /**
     * Holds an indicator indicating whether currently restoring the selection mode or not.
     */
    private boolean _isRestoring;

    /**
     * Holds the list of all the items can be selected.
     */
    private ArrayList<T> _items;

    /**
     * Holds the list of selected items.
     */
    private ArrayList<T> _selection = new ArrayList<>();

    /**
     * Holds the callback methods for the selection helper.
     */
    private SelectionCallback<T> _callbacks;

    //endregion

    //region C'tors

    /**
     * Initializes a new instance of a selection helper.
     *
     * @param items     The list of items in the selection (NOTE: keep the reference).
     * @param callbacks The callback methods of the selection helper.
     */
    public SelectionHelper(ArrayList<T> items, SelectionCallback<T> callbacks) {
        if (items == null)
            throw new NullPointerException("items");

        this._items = items;
        this._callbacks = callbacks;
    }

    //endregion

    //region Events

    /**
     * Saves the state of the selection helper, to the owner's outState.
     *
     * @param outState The {@link Bundle} outState of the selection helper owner, to save the state in.
     */
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            return;

        // Saves the indicator if in selection mode or not:
        outState.putBoolean(IS_IN_SELECTION_MODE_KEY, _isInSelectionMode);

        // Saves the items selected positions:
        int[] selectedPositions = new int[_selection.size()];
        for (int i = 0; i < _selection.size(); i++)
            selectedPositions[i] = _items.indexOf(_selection.get(i));
        outState.putIntArray(SELECTED_POSITIONS_ARRAY_KEY, selectedPositions);
    }

    /**
     * Restores the state of the selection helper, by the owner's savedInstanceState.
     *
     * @param savedInstanceState The {@link Bundle} savedInstanceState of the selection helper owner, to get the state from.
     */
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null)
            return;

        _isRestoring = true;

        // Gets the indicator if in selection mode or not:
        _isInSelectionMode = savedInstanceState.getBoolean(IS_IN_SELECTION_MODE_KEY, false);

        // Gets the items selected positions:
        int[] selectedPositions = savedInstanceState.getIntArray(SELECTED_POSITIONS_ARRAY_KEY);
        if (selectedPositions != null)
            for (int position : selectedPositions)
                this.itemSelection(position, true);

        // Restores the state:
        if (_isInSelectionMode)
            enterSelectionMode();

        _isRestoring = false;
    }

    //endregion

    //region Public API

    /**
     * Indicates whether the helper is in selection mode or not.
     *
     * @return Returns true if the helper is in selection mode, otherwise false.
     */
    public boolean isInSelectionMode() {
        return _isInSelectionMode;
    }

    /**
     * Indicates whether an item is selected or not.
     *
     * @param item The item to check if selected or not.
     * @return Returns true if the item is selected, otherwise false.
     */
    public boolean isItemSelected(T item) {
        return _selection.contains(item);
    }

    /**
     * Enters the selection mode (if already in selection mode - does nothing).
     */
    public void enterSelectionMode() {
        if (_isInSelectionMode && !_isRestoring)
            return;

        if (_callbacks != null)
            _callbacks.onSelectionEnter();

        _isInSelectionMode = true;

        if (_callbacks != null) {
            // Checks if restoring the selection, then shows the header with no animation:
            if (_isRestoring)
                _callbacks.onShowHeader();
            else
                _callbacks.onAnimateShowHeader();

            _callbacks.onUpdateItemsLayout();
        }

        if (_callbacks != null)
            _callbacks.onSelectionEntered();
    }

    /**
     * Exits the selection mode (if already not in selection mode - does nothing).
     */
    public void exitSelectionMode() {
        if (!_isInSelectionMode)
            return;

        if (_callbacks != null)
            _callbacks.onSelectionExit();

        _isInSelectionMode = false;
        _selection.clear();

        if (_callbacks != null) {
            _callbacks.onAnimateHideHeader();
            _callbacks.onUpdateItemsLayout();
        }

        if (_callbacks != null)
            _callbacks.onSelectionExited();
    }

    /**
     * Exits the selection mode (if already not in selection mode - does nothing).
     */
    public void exitSelectionModeFromAction() {
        if (!_isInSelectionMode)
            return;

        if (_callbacks != null)
            _callbacks.onSelectionExit();

        _isInSelectionMode = false;

        if (_callbacks != null) {
            _callbacks.onAnimateHideHeader();

            // Updates the all items that are not selected:
            for (int i = 0; i < _items.size(); i++)
                if (!_selection.contains(_items.get(i)))
                    _callbacks.onUpdateItemLayout(i);
        }

        _selection.clear();

        if (_callbacks != null)
            _callbacks.onSelectionExited();
    }

    /**
     * Handles the selection of an item, according to the indicator indicating whether the item is selected/unselected.
     *
     * @param position   The position of the item to select / unselect.
     * @param isSelected The select indicator indicating whether the item is selected or unselected.
     */
    public void itemSelection(int position, boolean isSelected) {
        if (position < 0)
            throw new IllegalArgumentException("position");

        T item = _items.get(position); // Gets the item.

        // Checks if the item is selected or unselected:
        if (isSelected)
            _selection.add(item); // Item is selected.
        else
            _selection.remove(item); // Item is unselected.

        // Fires some callback methods:
        if (_callbacks != null) {
            if (isSelected)
                _callbacks.onItemSelected(item);
            else
                _callbacks.onItemUnselected(item);

            _callbacks.onSelectionChanged(_selection);
        }

        // Checks if there are items selected or not:
        if (_selection.size() == 0)
            this.exitSelectionMode(); // No item is selected, then closes the selection mode.
    }

    /**
     * Gets the list of selected items.
     *
     * @return Returns the list of selected items.
     */
    public ArrayList<T> getSelection() {
        return _selection;
    }

    /**
     * Method procedure for menu action: "Select All".
     */
    public void actionSelectAll() {
        if (!_isInSelectionMode)
            throw new IllegalStateException("Selection mode is required.");

        // Checks if there are items not selected:
        if (_items.size() > _selection.size()) {
            _selection.clear();

            for (int position = 0; position < _items.size(); position++)
                this.itemSelection(position, true);

            if (_callbacks != null)
                _callbacks.onUpdateItemsLayout();
        }
    }

    //endregion

    //region Inner Classes

    /**
     * Represents the callbacks for the selection helper.
     */
    public static abstract class SelectionCallback<T extends Parcelable> implements SelectionRequiredCallback {

        /**
         * Event occurs before entering the selection mode.
         */
        public void onSelectionEnter() {
        }

        /**
         * Event occurs after the selection mode is entered.
         */
        public void onSelectionEntered() {
        }

        /**
         * Event occurs when an item is selected.
         *
         * @param item The item selected.
         */
        public void onItemSelected(T item) {
        }

        /**
         * Event occurs when an item is unselected.
         *
         * @param item The item unselected.
         */
        public void onItemUnselected(T item) {
        }

        /**
         * Event occurs when the selection list has changed, caused by an item selection.
         *
         * @param selection The selection list.
         */
        public void onSelectionChanged(ArrayList<T> selection) {
        }

        /**
         * Event occurs before exiting the selection mode.
         */
        public void onSelectionExit() {
        }

        /**
         * Event occurs after the selection mode is exited.
         */
        public void onSelectionExited() {
        }

    }

    /**
     * Represents the required callbacks for the selection helper.
     */
    static interface SelectionRequiredCallback {

        /**
         * Event occurs when the selection helper requests to show the selection header (with no animation).
         */
        void onShowHeader();

        /**
         * Event occurs when the selection helper requests to animate the selection header show.
         */
        void onAnimateShowHeader();

        /**
         * Event occurs when the selection helper requests to update the items layout UI.
         */
        void onUpdateItemsLayout();

        /**
         * Event occurs when the selection helper requests to update an item's layout UI.
         *
         * @param position The position of the item to update.
         */
        void onUpdateItemLayout(int position);

        /**
         * Event occurs when the selection helper requests to hide the selection header (with no animation).
         */
        void onHideHeader();

        /**
         * Event occurs when the selection helper requests to animate the selection header hide.
         */
        void onAnimateHideHeader();

    }

    /**
     * Represents a selection helper controller for a context owner (for example: activity or a fragment).
     *
     * @param <T> The object type of the items.
     */
    public static interface SelectionController<T extends Parcelable> {

        /**
         * Gets the selection helper.
         *
         * @return Returns the selection helper.
         */
        SelectionHelper<T> getSelectionHelper();

    }

    /**
     * Represents a selection helper controller for an item (for example: {@link android.support.v7.widget.RecyclerView.ViewHolder}).
     *
     * @param <T> The object type of the items.
     */
    public static interface SelectionItemController<T extends Parcelable> {

        /**
         * Gets the selection helper, from the owner implementing {@link SelectionController}.
         *
         * @return Returns the selection helper, from the owner implementing {@link SelectionController}.
         */
        SelectionHelper<T> getSelectionHelper();

        /**
         * Draws a selection to the item, according to the provided state.
         * (States: SELECTED, UNSELECTED, GONE).
         *
         * @param state The state to draw the selection to the item.
         */
        void drawSelection(int state);

        /**
         * Binds the selection state to the item (called when the item view is binding - in order to draw the selection state to the item view).
         */
        void bindSelection();

        /**
         * Selects or unselects the item, according to the indicator provided.
         *
         * @param isSelected Indicator indicating whether the item is selected or unselected.
         */
        void itemSelection(boolean isSelected);

    }

    // TODO in the future "HeaderBuilder" is an easier solution, but has less flexibility...

    /**
     * Represents a builder for the selection header layout and actions.
     */
    public static class HeaderBuilder {

        //region Private Members

        /**
         * Holds the menu resource id to inflate.
         */
        private int _menu;

        //endregion

        //region C'tor

        /**
         * Initializes a new instance of a builder for the selection header layout and actions.
         *
         * @param menu The menu resource id to inflate to the header.
         */
        public HeaderBuilder(@MenuRes int menu, @ColorInt int backgroundColor, @ColorInt int statusBarColor) {
            this._menu = menu;
        }

        //endregion

    }

    //endregion

}
