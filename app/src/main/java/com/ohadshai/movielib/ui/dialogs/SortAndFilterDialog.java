package com.ohadshai.movielib.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.ohadshai.movielib.R;
import com.ohadshai.movielib.utils.Utils;

import java.util.ArrayList;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Represents a dialog for sorting and filtering a list.
 * Created by Ohad on 04/01/2017.
 */
public class SortAndFilterDialog extends DialogFragment implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    //region Constants

    /**
     * Holds a constant for the dialog fragment tag.
     */
    public static final String DIALOG_TAG = "sort_and_filter_dialog_tag";

    /**
     * Holds a constant for the preference key: "Sort Selected Index".
     */
    private static final String SORT_SELECTED_INDEX_KEY = "sort_selected_index_key";

    /**
     * Holds a constant for the preference key: "Sort Is Ascending".
     */
    private static final String SORT_IS_ASC_KEY = "sort_is_ascending_key";

    /**
     * Holds a constant for the preference key: "Filter Selection".
     */
    private static final String FILTER_SELECTION_KEY = "filter_selection_key";

    //endregion

    //region Private Members

    /**
     * Holds the view of this dialog fragment.
     */
    private View _view;

    /**
     * Holds the {@link SharedPreferences} control for the dialog.
     */
    private SharedPreferences _preferences;

    /**
     * Holds the unique key for this sort in the {@link SharedPreferences}.
     */
    private String _key;

    //region [Sort by] Members

    /**
     * Holds the list of all sorts.
     */
    private Sort[] _sorts;

    /**
     * Holds the index of the sort item selected.
     */
    private int _sortSelectedIndex;

    /**
     * Holds an indicator indicating whether the sort is ascending or not (descending).
     */
    private boolean _isSortAscending;

    /**
     * Holds the {@link RadioGroup} control for the list of sorts.
     */
    private RadioGroup _rgSortBy;

    /**
     * Holds the {@link Spinner} control for the sort order (ASC or DESC).
     */
    private Spinner _spinnerSortOrder;

    /**
     * Holds the adapter for the {@link Spinner}.
     */
    private ArrayAdapter<String> _adapter;

    //endregion

    //region [Filter by] Members

    /**
     * Holds the list of all the filters.
     */
    private String[] _filters;

    /**
     * Holds the list of filter selection.
     */
    private ArrayList<String> _filterSelection = new ArrayList<>();

    /**
     * Holds the list of all the {@link AppCompatCheckBox} controls in the filters list.
     */
    private AppCompatCheckBox[] _filtersChecks;

    //endregion

    /**
     * Holds the result listener of the dialog.
     */
    private ResultListener _listener;

    //endregion

    //region Dialog Events

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        _view = inflater.inflate(R.layout.dialog_sort_and_filter, null);

        this.initControls();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(_view);
        return builder.create();
    }

    //region [Sort by] Events

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (_sorts == null)
            return;

        _sortSelectedIndex = checkedId;

        _adapter.clear();
        _adapter.add(_sorts[_sortSelectedIndex].getAscName());
        _adapter.add(_sorts[_sortSelectedIndex].getDescName());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        _isSortAscending = position < 1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //endregion

    //region [Filter by] Events

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String filter = buttonView.getText().toString();

        if (isChecked) {
            if (!_filterSelection.contains(filter))
                _filterSelection.add(filter);
        } else {
            int idx = _filterSelection.indexOf(filter);
            if (idx > -1)
                _filterSelection.remove(idx);
        }
    }

    //endregion

    //endregion

    //region Public Static API

    /**
     * Initializes a new instance of the dialog, and shows it.
     *
     * @param manager               The fragment manager to show the dialog in.
     * @param key                   The unique key for this sort in the {@link SharedPreferences}.
     * @param sorts                 The list of all sorts.
     * @param defaultSortIndex      The default sort index.
     * @param defaultSortOrderIsAsc An indicator indicating whether the default sort order is ASC or not (DESC).
     * @param filters               The list of all the filters.
     * @param listener              The result listener of the dialog.
     * @return Returns the initialized dialog.
     */
    public static SortAndFilterDialog show(@NonNull FragmentManager manager, @NonNull String key, @Nullable Sort[] sorts, int defaultSortIndex, boolean defaultSortOrderIsAsc, @Nullable String[] filters, ResultListener listener) {
        SortAndFilterDialog dialog = new SortAndFilterDialog();
        dialog.setKey(key);
        dialog.setSorts(sorts, defaultSortIndex, defaultSortOrderIsAsc);
        dialog.setFilters(filters);
        dialog.setResultListener(listener);
        dialog.show(manager, DIALOG_TAG);
        return dialog;
    }

    /**
     * Initializes a new instance of the dialog, and shows it.
     *
     * @param manager               The fragment manager to show the dialog in.
     * @param key                   The unique key for this sort in the {@link SharedPreferences}.
     * @param sorts                 The list of all sorts.
     * @param defaultSortIndex      The default sort index.
     * @param defaultSortOrderIsAsc An indicator indicating whether the default sort order is ASC or not (DESC).
     * @param listener              The result listener of the dialog.
     * @return Returns the initialized dialog.
     */
    public static SortAndFilterDialog show(@NonNull FragmentManager manager, @NonNull String key, @NonNull Sort[] sorts, int defaultSortIndex, boolean defaultSortOrderIsAsc, ResultListener listener) {
        SortAndFilterDialog dialog = new SortAndFilterDialog();
        dialog.setKey(key);
        dialog.setSorts(sorts, defaultSortIndex, defaultSortOrderIsAsc);
        dialog.setResultListener(listener);
        dialog.show(manager, DIALOG_TAG);
        return dialog;
    }

    /**
     * Initializes a new instance of the dialog, and shows it.
     *
     * @param manager  The fragment manager to show the dialog in.
     * @param key      The unique key for this sort in the {@link SharedPreferences}.
     * @param filters  The list of all the filters.
     * @param listener The result listener of the dialog.
     * @return Returns the initialized dialog.
     */
    public static SortAndFilterDialog show(@NonNull FragmentManager manager, @NonNull String key, @NonNull String[] filters, ResultListener listener) {
        SortAndFilterDialog dialog = new SortAndFilterDialog();
        dialog.setKey(key);
        dialog.setFilters(filters);
        dialog.setResultListener(listener);
        dialog.show(manager, DIALOG_TAG);
        return dialog;
    }

    /**
     * Gets the {@link Sort} from the preferences.
     *
     * @param preferences      The {@link SharedPreferences} control to get the values from.
     * @param key              The unique key for this sort in the {@link SharedPreferences}.
     * @param sorts            The list of all sorts.
     * @param defaultSortIndex The default sort index.
     * @return Returns the {@link Sort} from the preferences if found, otherwise gets the default.
     */
    public static Sort getSortFromPreferences(@NonNull SharedPreferences preferences, @NonNull String key, @NonNull Sort[] sorts, int defaultSortIndex) {
        return sorts[preferences.getInt(key + SORT_SELECTED_INDEX_KEY, defaultSortIndex)];
    }

    /**
     * Gets the sort order (ascending / descending) from the preferences.
     *
     * @param preferences           The {@link SharedPreferences} control to get the values from.
     * @param key                   The unique key for this sort in the {@link SharedPreferences}.
     * @param defaultSortOrderIsAsc An indicator indicating whether the default sort order is ASC or not (DESC).
     * @return Returns true if the sort order is ascending, otherwise false for descending.
     */
    public static boolean getSortOrderFromPreferences(@NonNull SharedPreferences preferences, @NonNull String key, boolean defaultSortOrderIsAsc) {
        return preferences.getBoolean(key + SORT_IS_ASC_KEY, defaultSortOrderIsAsc);
    }

    /**
     * Gets the selected filters list from the preferences.
     *
     * @param preferences The {@link SharedPreferences} control to get the values from.
     * @param key         The unique key for this sort in the {@link SharedPreferences}.
     * @return Returns the selected filters if found, otherwise null.
     */
    public static ArrayList<String> getFiltersFromPreferences(@NonNull SharedPreferences preferences, @NonNull String key) {
        String filtersStr = preferences.getString(key + FILTER_SELECTION_KEY, null);
        if (filtersStr == null)
            return null;
        else
            return Utils.Strings.toList(filtersStr);
    }

    //endregion

    //region Public API

    /**
     * Sets the unique key for this sort in the {@link SharedPreferences}.
     *
     * @param key The unique key for this sort in the {@link SharedPreferences}.
     * @return Returns this instance of the {@link SortAndFilterDialog}.
     */
    public SortAndFilterDialog setKey(@NonNull String key) {
        this._key = key;
        return this;
    }

    /**
     * Sets the list of all the sorts.
     *
     * @param sorts                 The list of all the sorts to set.
     * @param defaultSortIndex      The default sort index to set.
     * @param defaultSortOrderIsAsc An indicator indicating whether the default sort order is ASC or not (DESC) to set.
     * @return Returns this instance of the {@link SortAndFilterDialog}.
     */
    public SortAndFilterDialog setSorts(@Nullable Sort[] sorts, int defaultSortIndex, boolean defaultSortOrderIsAsc) {
        this._sorts = sorts;
        this._sortSelectedIndex = defaultSortIndex;
        this._isSortAscending = defaultSortOrderIsAsc;
        return this;
    }

    /**
     * Sets the list of all the filters.
     *
     * @param filters The list of all the filters to set.
     * @return Returns this instance of the {@link SortAndFilterDialog}.
     */
    public SortAndFilterDialog setFilters(@Nullable String[] filters) {
        this._filters = filters;
        return this;
    }

    /**
     * Sets the result listener of the dialog.
     *
     * @param listener The result listener of the dialog to set.
     * @return Returns this instance of the {@link SortAndFilterDialog}.
     */
    public SortAndFilterDialog setResultListener(ResultListener listener) {
        this._listener = listener;
        return this;
    }

    //endregion

    //region Private Methods

    /**
     * Initializes all dialog view controls.
     */
    private void initControls() {

        _preferences = PreferenceManager.getDefaultSharedPreferences(_view.getContext());

        final ImageButton imgbtnClose = (ImageButton) _view.findViewById(R.id.imgbtnClose);
        imgbtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        imgbtnClose.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utils.UI.showInformationToast(imgbtnClose, R.string.general_btn_close);
                return true;
            }
        });

        final Button btnOK = (Button) _view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_listener != null)
                    _listener.onPositiveResult((_sorts != null ? _sorts[_sortSelectedIndex] : null), _isSortAscending, (_filters != null ? _filterSelection : null));

                SharedPreferences.Editor editor = _preferences.edit();
                if (_sorts != null) {
                    editor.putInt(_key + SORT_SELECTED_INDEX_KEY, _sortSelectedIndex);
                    editor.putBoolean(_key + SORT_IS_ASC_KEY, _isSortAscending);
                }
                if (_filters != null) {
                    editor.putString(_key + FILTER_SELECTION_KEY, Utils.Strings.fromList(_filterSelection));
                }
                editor.apply();

                dismiss();
            }
        });

        //region Sort by...

        // Checks if sort is enabled:
        if (_sorts != null) {
            _view.findViewById(R.id.llSortBy).setVisibility(View.VISIBLE);

            _rgSortBy = (RadioGroup) _view.findViewById(R.id.rgSortBy);
            _rgSortBy.setOnCheckedChangeListener(this);
            // Populates the radio buttons from the sorts list:
            for (int i = 0; i < _sorts.length; i++) {
                AppCompatRadioButton rbSort = new AppCompatRadioButton(_view.getContext());
                rbSort.setId(i);
                rbSort.setTextColor(Color.parseColor("#cccccc"));
                rbSort.setHighlightColor(Utils.Colors.ACCENT);
                rbSort.setTextSize(COMPLEX_UNIT_SP, 16);
                rbSort.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rbSort.setPadding(25, 25, 25, 25);
                rbSort.setText(_sorts[i].getName());
                _rgSortBy.addView(rbSort);
            }

            _spinnerSortOrder = (Spinner) _view.findViewById(R.id.spinnerSortOrder);
            _adapter = new ArrayAdapter<>(_view.getContext(), R.layout.spinner_item);
            _adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            _spinnerSortOrder.setAdapter(_adapter);
            _spinnerSortOrder.setOnItemSelectedListener(this);

            // Checks the selected sort state from the SharedPreferences:
            _sortSelectedIndex = _preferences.getInt(_key + SORT_SELECTED_INDEX_KEY, _sortSelectedIndex);
            _isSortAscending = _preferences.getBoolean(_key + SORT_IS_ASC_KEY, _isSortAscending);

            // Performs the initialization to the UI:
            _rgSortBy.check(_sortSelectedIndex);
            _spinnerSortOrder.setSelection((_isSortAscending ? 0 : 1), true);
        } else {
            _view.findViewById(R.id.llSortBy).setVisibility(View.GONE);
        }

        //endregion

        //region Filter by...

        // Checks if filter is enabled:
        if (_filters != null) {
            _view.findViewById(R.id.llFilterBy).setVisibility(View.VISIBLE);

            _filtersChecks = new AppCompatCheckBox[_filters.length];

            // Populates the checkboxes from the filters list:
            LinearLayout llFilters = (LinearLayout) _view.findViewById(R.id.llFilters);
            for (int i = 0; i < _filters.length; i++) {
                AppCompatCheckBox chkFilter = new AppCompatCheckBox(_view.getContext());
                chkFilter.setId(i);
                chkFilter.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                chkFilter.setTextColor(Color.parseColor("#cccccc"));
                chkFilter.setTextSize(COMPLEX_UNIT_SP, 16);
                chkFilter.setPadding(25, 25, 25, 25);
                chkFilter.setText(_filters[i]);
                chkFilter.setOnCheckedChangeListener(this);
                llFilters.addView(chkFilter);
                _filtersChecks[i] = chkFilter;
            }

            // Checks the filter selection to initial from the SharedPreferences:
            String filtersStr = _preferences.getString(_key + FILTER_SELECTION_KEY, null);
            if (filtersStr != null) {
                _filterSelection = Utils.Strings.toList(filtersStr);

                // Performs the initialization to the UI:
                for (CheckBox checkBox : _filtersChecks) {
                    if (_filterSelection.contains(checkBox.getText().toString()))
                        checkBox.setChecked(true);
                }
            }
        } else {
            _view.findViewById(R.id.llFilterBy).setVisibility(View.GONE);
            Utils.UI.setMargins(_view.findViewById(R.id.llSortBy), 0, 0, 0, 0);
        }

        //endregion

    }

    //endregion

    //region Inner Classes

    /**
     * Represents a listener for the result from the dialog.
     */
    public interface ResultListener {

        /**
         * Event occurs when there's a positive result from the dialog.
         *
         * @param sort            The sort object selected.
         * @param isSortAsc       An indicator if sort order is ascending or descending.
         * @param selectedFilters The list of selected filters.
         */
        void onPositiveResult(@Nullable Sort sort, boolean isSortAsc, @Nullable ArrayList<String> selectedFilters);

    }

    /**
     * Represents a sort object.
     */
    public static class Sort {

        //region Private Members

        /**
         * Holds the name of the sort.
         */
        private String _name;

        /**
         * Holds the name of the ascending sort.
         */
        private String _ascName;

        /**
         * Holds the name for the descending sort.
         */
        private String _descName;

        //endregion

        //region C'tor

        /**
         * Initializes a new instance of a sort object.
         *
         * @param name     The name of the sort.
         * @param ascName  The name of the ascending sort.
         * @param descName The name for the descending sort.
         */
        public Sort(String name, String ascName, String descName) {
            this._name = name;
            this._ascName = ascName;
            this._descName = descName;
        }

        //endregion

        //region Public API

        /**
         * Gets the name of the sort.
         *
         * @return Returns the name of the sort.
         */
        public String getName() {
            return _name;
        }

        /**
         * Sets the name of the ascending sort.
         *
         * @param name The name of the ascending sort to set.
         */
        public void setName(String name) {
            this._name = name;
        }

        /**
         * Gets the name of the ascending sort.
         *
         * @return The name of the ascending sort to set.
         */
        public String getAscName() {
            return _ascName;
        }

        /**
         * Sets the name of the ascending sort.
         *
         * @param ascName The name of the ascending sort to set.
         */
        public void setAscName(String ascName) {
            this._ascName = ascName;
        }

        /**
         * Gets the name for the descending sort.
         *
         * @return Returns the name for the descending sort.
         */
        public String getDescName() {
            return _descName;
        }

        /**
         * Sets the name for the descending sort.
         *
         * @param descName The name for the descending sort to set.
         */
        public void setDescName(String descName) {
            this._descName = descName;
        }

        //endregion

    }

    //endregion

}
