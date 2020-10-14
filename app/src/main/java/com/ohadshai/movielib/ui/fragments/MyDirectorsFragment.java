package com.ohadshai.movielib.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ohadshai.movielib.R;
import com.ohadshai.movielib.adapters.MyDirectorsAdapter;
import com.ohadshai.movielib.db.DBHandler;
import com.ohadshai.movielib.entities.Person;
import com.ohadshai.movielib.loaders.MyDirectorsLoader;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.ui.dialogs.SortAndFilterDialog;
import com.ohadshai.movielib.utils.RevealHelper;
import com.ohadshai.movielib.utils.SelectionHelper;
import com.ohadshai.movielib.utils.SnackbarManager;
import com.ohadshai.movielib.utils.Utils;
import com.ohadshai.movielib.utils.web_services.IMDbConsts;

import java.util.ArrayList;
import java.util.Collections;

import static android.support.design.widget.Snackbar.make;

/**
 * A simple {@link Fragment} subclass for "My Directors".
 */
public class MyDirectorsFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener, SelectionHelper.SelectionController<Person>, MenuItem.OnMenuItemClickListener {

    //region Constants

    /**
     * Holds a constant for the {@link com.ohadshai.movielib.loaders.MyDirectorsLoader} loader id.
     */
    private static final int MY_DIRECTORS_LOADER_ID = 173203;

    /**
     * Holds a constant for the default sort index.
     */
    private static final int DEFAULT_SORT_INDEX = 3;

    /**
     * Holds a constant for the indicator indicating whether the default sort order is ascending or not (descending).
     */
    private static final boolean DEFAULT_SORT_ORDER_IS_ASC = false;

    //endregion

    //region Private Members

    /**
     * Holds the view of this fragment.
     */
    private View _view;

    /**
     * Holds the database interactions object.
     */
    private DBHandler _repository;

    /**
     * Holds the {@link SharedPreferences} control for the fragment.
     */
    private SharedPreferences _preferences;

    /**
     * Holds the raw list of directors (the list without any manipulations like sort or filters).
     */
    private ArrayList<Person> _peopleRaw = new ArrayList<>();

    /**
     * Holds the list of directors.
     */
    private ArrayList<Person> _people = new ArrayList<>();

    /**
     * Holds the adapter of the directors list for the {@link RecyclerView}.
     */
    private MyDirectorsAdapter _adapter;

    /**
     * Holds the {@link RecyclerView} control of the directors list.
     */
    private RecyclerView _rvDirectors;

    /**
     * Holds the main {@link ProgressBar} control of the fragment.
     */
    private ProgressBar _progressBar;

    /**
     * Holds the {@link Button} control to show for empty directors list.
     */
    private Button _btnDirectorsNotFound;

    /**
     * Holds the {@link RevealHelper} for the search.
     */
    private RevealHelper _revealSearch;

    /**
     * Holds an array of {@link com.ohadshai.movielib.ui.dialogs.SortAndFilterDialog.Sort}, which represents the sort options.
     */
    private SortAndFilterDialog.Sort[] _sorts;

    /**
     * Holds the array of filters for the list.
     */
    private String[] _filters;

    //region [ Selection ]

    /**
     * Holds the {@link Toolbar} control for the selection header.
     */
    private Toolbar _toolbarSelection;

    /**
     * Holds the {@link TextView} control for the selection title.
     */
    private TextView _txtSelectionTitle;

    /**
     * Holds the selection helper for the fragment.
     */
    private SelectionHelper<Person> _selectionHelper = new SelectionHelper<>(_people, new SelectionHelper.SelectionCallback<Person>() {

        @Override
        public void onShowHeader() {
            _revealSelection.show();
        }

        @Override
        public void onAnimateShowHeader() {
            _revealSelection.reveal();
        }

        @Override
        public void onSelectionChanged(ArrayList<Person> selection) {
            if (selection.size() > 0)
                _txtSelectionTitle.setText(String.valueOf(selection.size()) + " " + getString(R.string.general_selected));
        }

        @Override
        public void onUpdateItemsLayout() {
            if (_adapter != null)
                _adapter.notifyDataSetChanged();
        }

        @Override
        public void onUpdateItemLayout(int position) {
            if (_adapter != null)
                _adapter.notifyItemChanged(position);
        }

        @Override
        public void onHideHeader() {
            _revealSelection.hide();
        }

        @Override
        public void onAnimateHideHeader() {
            _revealSelection.conceal();
        }

    });

    /**
     * Holds the {@link RevealHelper} for the selection.
     */
    private RevealHelper _revealSelection;

    //endregion

    //endregion

    /**
     * Initializes a new instance of simple {@link Fragment} subclass for "My Directors".
     */
    public MyDirectorsFragment() {
        // Required empty public constructor.
    }

    //region Fragment Events

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.fragment_my_directors, container, false);

        getActivity().setTitle(R.string.directors_fragment_title);

        _repository = DBHandler.getInstance(getActivity());

        _preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        _preferences.registerOnSharedPreferenceChangeListener(this);

        _sorts = new SortAndFilterDialog.Sort[]{
                new SortAndFilterDialog.Sort(getString(R.string.general_name), getString(R.string.dialog_sort_a_to_z), getString(R.string.dialog_sort_z_to_a)),
                new SortAndFilterDialog.Sort(getString(R.string.general_age), getString(R.string.dialog_sort_low_to_high), getString(R.string.dialog_sort_high_to_low)),
                new SortAndFilterDialog.Sort(getString(R.string.general_popularity), getString(R.string.dialog_sort_most_unpopular), getString(R.string.dialog_sort_most_popular)),
                new SortAndFilterDialog.Sort(getString(R.string.general_date_added), getString(R.string.dialog_sort_oldest_first), getString(R.string.dialog_sort_most_recent))
        };

        _filters = new String[]{
                getString(R.string.general_favorites)
        };

        final FloatingActionButton fabAdd = (FloatingActionButton) getActivity().findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _revealSearch.reveal();
            }
        });
        // Animates the fab on start:
        fabAdd.hide();
        fabAdd.show();

        _progressBar = (ProgressBar) _view.findViewById(R.id.progressBar);
        _progressBar.setVisibility(View.VISIBLE);

        _rvDirectors = (RecyclerView) _view.findViewById(R.id.rvDirectors);
        _rvDirectors.setLayoutManager(new LinearLayoutManager(getActivity()));
        _adapter = new MyDirectorsAdapter(_people, this);
        _rvDirectors.setAdapter(_adapter);

        _btnDirectorsNotFound = (Button) _view.findViewById(R.id.btnDirectorsNotFound);
        _btnDirectorsNotFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _revealSearch.reveal();
            }
        });

        //region Search Related

        final View flSearchContainer = getActivity().findViewById(R.id.flSearchContainer);
        _revealSearch = RevealHelper.create(getActivity(), flSearchContainer, "_revealSearch_MyDirectors", new RevealHelper.RevealHelperCallback() {
            @Override
            public void onReveal() {
                // Checks if any snackbar is showing in this area, then closes it:
                if (SnackbarManager.with(getActivity()).isShowing())
                    SnackbarManager.with(getActivity()).dismiss();

                // Locks the navigation drawer:
                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                fabAdd.hide();
            }

            @Override
            public void onRevealed() {
                // Disables the main window:
                getActivity().findViewById(R.id.llMainWindow).setVisibility(View.INVISIBLE);
            }

            @Override
            public void initLayoutControls() {

                final ImageButton imgbtnExitSearch = (ImageButton) flSearchContainer.findViewById(R.id.imgbtnExitSearch);
                imgbtnExitSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _revealSearch.conceal();
                    }
                });
                imgbtnExitSearch.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Utils.UI.showInformationToast(imgbtnExitSearch, R.string.general_btn_exit_search);
                        return true;
                    }
                });

                final SearchView searchMain = (SearchView) flSearchContainer.findViewById(R.id.searchMain);
                searchMain.setIconified(false);
                searchMain.findViewById(android.support.v7.appcompat.R.id.search_plate).setBackgroundColor(Color.TRANSPARENT); // Hides the search underline.
                searchMain.setQueryHint(getText(R.string.general_search_people_hint));
                searchMain.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searchMain.clearFocus();
                        flSearchContainer.findViewById(R.id.toolbarSearch).requestFocus();

                        SearchPeopleFragment fragment = (SearchPeopleFragment) getActivity().getSupportFragmentManager().findFragmentByTag(UIConsts.Fragments.SEARCH_PEOPLE_FRAGMENT_TAG);
                        if (fragment != null)
                            fragment.search(query);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        SearchPeopleFragment fragment = (SearchPeopleFragment) getActivity().getSupportFragmentManager().findFragmentByTag(UIConsts.Fragments.SEARCH_PEOPLE_FRAGMENT_TAG);
                        if (fragment != null)
                            fragment.search(newText);
                        return true;
                    }
                });
                searchMain.setQuery("", false);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flSearchContent, new SearchPeopleFragment(), UIConsts.Fragments.SEARCH_PEOPLE_FRAGMENT_TAG).commit();

            }

            @Override
            public void onConceal() {
                // Enables the main window:
                getActivity().findViewById(R.id.llMainWindow).setVisibility(View.VISIBLE);

                // Checks if any snackbar is showing in this area, then closes it:
                if (SnackbarManager.with(getActivity()).isShowing())
                    SnackbarManager.with(getActivity()).dismiss();

                // Unlocks the navigation drawer:
                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                // Reloads the list:
                loadMyDirectorsList();
            }

            @Override
            public void onConcealed() {
                // Removes the SearchPeopleFragment:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                SearchPeopleFragment fragment = (SearchPeopleFragment) fragmentManager.findFragmentByTag(UIConsts.Fragments.SEARCH_PEOPLE_FRAGMENT_TAG);
                if (fragment != null)
                    fragmentManager.beginTransaction().remove(fragment).commit();

                fabAdd.show();
            }
        }).setRevealInterpolator(new AccelerateInterpolator()).setConcealInterpolator(new DecelerateInterpolator())
                .setStatusBarTransition(Utils.Colors.PRIMARY_DARK, Utils.Colors.SEARCH, 600, 300);

        final ViewTreeObserver viewTreeObserver = getActivity().getWindow().getDecorView().getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Checks if the activity owner is not null:
                if (getActivity() == null) {
                    // Removes the listener:
                    if (viewTreeObserver.isAlive()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                            viewTreeObserver.removeOnGlobalLayoutListener(this);
                        else
                            viewTreeObserver.removeGlobalOnLayoutListener(this);
                    }
                    return;
                }

                View actionSearch = getActivity().findViewById(R.id.actionSearch);

                // Must check for null, view could be called when it's not there yet:
                if (actionSearch != null) {
                    _revealSearch.setRevealFrom(actionSearch);

                    // Removes the listener:
                    if (viewTreeObserver.isAlive()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                            viewTreeObserver.removeOnGlobalLayoutListener(this);
                        else
                            viewTreeObserver.removeGlobalOnLayoutListener(this);
                    }
                }
            }
        });

        //endregion

        //region Selection Related...

        _toolbarSelection = (Toolbar) getActivity().findViewById(R.id.toolbarSelection);

        _txtSelectionTitle = (TextView) _toolbarSelection.findViewById(R.id.txtSelectionTitle);

        _revealSelection = RevealHelper.create(getActivity(), _toolbarSelection, "_revealSelection_MyDirectors", new RevealHelper.RevealHelperCallback() {
            @Override
            public void onReveal() {
                int revealX = _toolbarSelection.getWidth() / 2;
                int revealY = _toolbarSelection.getHeight() + 500;
                _revealSelection.setRevealFrom(revealX, revealY)
                        .setRevealRadius(500, (float) Math.hypot(revealX, revealY))
                        .setConcealTo(revealX, -500)
                        .setConcealRadius((float) Math.hypot(revealX, -500), 500);

                // Checks if any snackbar is showing in this area, then closes it:
                if (SnackbarManager.with(getActivity()).isShowing())
                    SnackbarManager.with(getActivity()).dismiss();

                // Locks the navigation drawer:
                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                fabAdd.hide();
            }

            @Override
            public void initLayoutControls() {

                _toolbarSelection.getMenu().clear();
                _toolbarSelection.inflateMenu(R.menu.menu_selection);

                _toolbarSelection.getMenu().findItem(R.id.actionEdit).setVisible(false);

                MenuItem actionShare = _toolbarSelection.getMenu().findItem(R.id.actionShare);
                actionShare.setOnMenuItemClickListener(MyDirectorsFragment.this);

                MenuItem actionDelete = _toolbarSelection.getMenu().findItem(R.id.actionDelete);
                actionDelete.setOnMenuItemClickListener(MyDirectorsFragment.this);

                MenuItem actionSelectAll = _toolbarSelection.getMenu().findItem(R.id.actionSelectAll);
                actionSelectAll.setOnMenuItemClickListener(MyDirectorsFragment.this);

                final ImageButton imgbtnClose = (ImageButton) _toolbarSelection.findViewById(R.id.imgbtnClose);
                imgbtnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _selectionHelper.exitSelectionMode();
                    }
                });
                imgbtnClose.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Utils.UI.showInformationToast(imgbtnClose, R.string.general_hint_exit_selection);
                        return true;
                    }
                });

            }

            @Override
            public void onConceal() {
                int revealX = _toolbarSelection.getWidth() / 2;
                int revealY = _toolbarSelection.getHeight() + 500;
                _revealSelection.setRevealFrom(revealX, revealY)
                        .setRevealRadius(500, (float) Math.hypot(revealX, revealY))
                        .setConcealTo(revealX, -500)
                        .setConcealRadius((float) Math.hypot(revealX, -500), 500);

                // Checks if any snackbar is showing in this area, then closes it:
                if (SnackbarManager.with(getActivity()).isShowing())
                    SnackbarManager.with(getActivity()).dismiss();

                // Unlocks the navigation drawer:
                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onConcealed() {
                fabAdd.show();
            }
        }).setStatusBarTransition(Utils.Colors.PRIMARY_DARK, Utils.Colors.SELECTION_STATUS, 500, 0, 500, 200);

        //endregion

        if (savedInstanceState == null)
            this.loadMyDirectorsList();

        return _view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSearch:
                _revealSearch.reveal();
                return true;
            case R.id.actionSortAndFilter:
                this.actionSortAndFilter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // Validates that in selection mode:
        if (_selectionHelper == null || !_selectionHelper.isInSelectionMode())
            return false;

        if (item.getItemId() == R.id.actionShare) {
            this.actionSelectionShare(_selectionHelper.getSelection());
            return true;
        } else if (item.getItemId() == R.id.actionDelete) {
            this.actionSelectionDelete(_selectionHelper.getSelection());
            return true;
        } else if (item.getItemId() == R.id.actionSelectAll) {
            _selectionHelper.actionSelectAll();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SelectionHelper<Person> getSelectionHelper() {
        return _selectionHelper;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UIConsts.RequestCodes.PERSON_DISPLAY_REQUEST_CODE)
            this.loadMyDirectorsList();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Checks if the changed preference is "My Directors Deleted":
        if (key.equals(UIConsts.Preferences.Keys.MY_DIRECTORS_DELETED) && sharedPreferences.getBoolean(UIConsts.Preferences.Keys.MY_DIRECTORS_DELETED, false)) {
            _people.clear();
            _peopleRaw.clear();
            _adapter.notifyDataSetChanged();
            this.displayListState();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Saves the people list:
        outState.putParcelableArrayList(UIConsts.Bundles.PEOPLE_RAW_LIST_KEY, _peopleRaw);
        outState.putParcelableArrayList(UIConsts.Bundles.PEOPLE_LIST_KEY, _people);

        // Saves the search state:
        _revealSearch.onSaveInstanceState(outState);

        // Lets the selection helper handle the save instance state:
        _selectionHelper.onSaveInstanceState(outState);
        _revealSelection.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState == null)
            return;

        // Restores the directors list:
        _peopleRaw = savedInstanceState.getParcelableArrayList(UIConsts.Bundles.PEOPLE_RAW_LIST_KEY);
        ArrayList<Person> directors = savedInstanceState.getParcelableArrayList(UIConsts.Bundles.PEOPLE_LIST_KEY);
        if (directors != null) {
            _people.clear();
            for (Person director : directors)
                _people.add(director);
        }
        this.displayListState();

        // Restores the search state:
        _revealSearch.onRestoreInstanceState(savedInstanceState);

        // Lets the selection helper handle the restore instance state:
        _selectionHelper.onRestoreInstanceState(savedInstanceState);
        _revealSelection.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Handles a back press.
     *
     * @return Returns true if back press was handled, otherwise false.
     */
    public boolean onBackPressed() {
        if (_revealSearch.onBackPress())
            return true;
        if (_selectionHelper.isInSelectionMode()) {
            _selectionHelper.exitSelectionMode();
            return true;
        }

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Checks to close any snackbar if showing:
        if (SnackbarManager.with(getActivity()).isShowing())
            SnackbarManager.with(getActivity()).dismiss();

        _preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    //endregion

    //region Public API

    @Override
    public Context getContext() {
        if (_view != null)
            return _view.getContext();
        else if (getActivity() != null)
            return getActivity().getApplicationContext();
        else
            return super.getContext();
    }

    /**
     * Displays the state of the adapter (list / empty message), according to the items in the list.
     */
    public void displayListState() {
        _progressBar.setVisibility(View.GONE);

        if (this._people.size() < 1) {
            _rvDirectors.setVisibility(View.GONE);
            _btnDirectorsNotFound.setVisibility(View.VISIBLE);
        } else {
            _btnDirectorsNotFound.setVisibility(View.GONE);
            _rvDirectors.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Loads the "My Directors" list from the DB.
     */
    public void loadMyDirectorsList() {
        getLoaderManager().initLoader(MY_DIRECTORS_LOADER_ID, null, new LoaderManager.LoaderCallbacks<ArrayList<Person>>() {
            @Override
            public Loader<ArrayList<Person>> onCreateLoader(int id, Bundle args) {
                return new MyDirectorsLoader(getContext());
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<Person>> loader, ArrayList<Person> data) {
                if (data == null)
                    return;

                _people.clear();
                for (Person director : data)
                    _people.add(director);
                _peopleRaw = new ArrayList<>(_people);
                filter(SortAndFilterDialog.getFiltersFromPreferences(_preferences, UIConsts.Keys.SORT_MY_DIRECTORS_KEY));
                sort(SortAndFilterDialog.getSortFromPreferences(_preferences, UIConsts.Keys.SORT_MY_DIRECTORS_KEY, _sorts, DEFAULT_SORT_INDEX),
                        SortAndFilterDialog.getSortOrderFromPreferences(_preferences, UIConsts.Keys.SORT_MY_DIRECTORS_KEY, DEFAULT_SORT_ORDER_IS_ASC));
                _adapter.notifyDataSetChanged();
                displayListState();
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<Person>> loader) {
            }
        }).forceLoad();
    }

    //endregion

    //region Private Methods

    /**
     * Method procedure for menu action: "Sort & Filter".
     */
    private void actionSortAndFilter() {
        SortAndFilterDialog.show(getActivity().getFragmentManager(), UIConsts.Keys.SORT_MY_DIRECTORS_KEY, _sorts, DEFAULT_SORT_INDEX, DEFAULT_SORT_ORDER_IS_ASC, _filters, new SortAndFilterDialog.ResultListener() {
            @Override
            public void onPositiveResult(@Nullable SortAndFilterDialog.Sort sort, boolean isSortAsc, @Nullable ArrayList<String> selectedFilters) {
                // Checks if filter is enabled:
                if (selectedFilters != null)
                    filter(selectedFilters);

                // Checks if sort is enabled:
                if (sort != null)
                    sort(sort, isSortAsc);

                // Notify the sort and filter:
                _adapter.notifyDataSetChanged();
                displayListState();
            }
        });
    }

    /**
     * Filters the list of {@link Person}.
     *
     * @param selectedFilters The list of all the selected filters.
     */
    private void filter(ArrayList<String> selectedFilters) {
        if (selectedFilters == null)
            return;

        _people.clear();
        for (Person person : _peopleRaw) {
            // Checks if to filter by "Favorites" is requested, and also the person is not favorite (to exclude it from the list):
            if (selectedFilters.contains(_filters[0]) && !person.isFavorite())
                continue;

            // Person qualifies the filter, so it can be added to the list:
            _people.add(person);
        }

        // Checks if there are people qualify the filters:
        if (_people.size() > 0) {
            if (SnackbarManager.with(getActivity()).isShowing())
                SnackbarManager.with(getActivity()).dismiss();
        } else {
            // No people qualify the filters, so notifies the user:
            Snackbar snackbar = make(getActivity().findViewById(R.id.coordinator), R.string.general_msg_no_directors_answer_filter, Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.general_filter).toUpperCase(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            actionSortAndFilter();
                        }
                    }).setActionTextColor(Utils.Colors.ACCENT);
            SnackbarManager.with(getActivity()).set(snackbar).show();
        }
    }

    /**
     * Sorts the list of {@link Person}.
     *
     * @param sort      The sort option to apply.
     * @param isSortAsc An indicator indicating whether the sort order is ascending or not (descending).
     */
    private void sort(@NonNull SortAndFilterDialog.Sort sort, boolean isSortAsc) {
        if (sort.getName().equals(_sorts[0].getName()))
            Collections.sort(_people, new Person.NameComparator()); // Sort by "Name".
        else if (sort.getName().equals(_sorts[1].getName()))
            Collections.sort(_people, new Person.AgeComparator()); // Sort by "Age".
        else if (sort.getName().equals(_sorts[2].getName()))
            Collections.sort(_people, new Person.PopularityComparator()); // Sort by "Popularity".
        else if (sort.getName().equals(_sorts[3].getName()))
            Collections.sort(_people, new Person.DateAddedComparator()); // Sort by "Date Added".

        // Checks if sort order is descending:
        if (!isSortAsc)
            Collections.reverse(_people);
    }

    //region Selection Related

    /**
     * Method procedure for the selection action: "Share".
     *
     * @param selection The selected {@link Person} list to share.
     */
    private void actionSelectionShare(ArrayList<Person> selection) {
        if (selection == null)
            throw new NullPointerException("selection");

        StringBuilder sb = new StringBuilder();
        for (Person person : selection) {
            sb.append(person.getName());
            if (person.getIMDbId() != null)
                sb.append("\n").append(IMDbConsts.IMDB_PERSON_PAGE).append(person.getIMDbId());
            sb.append("\n\n");
        }

        if (sb.length() >= 2)
            Utils.Intents.share(sb.substring(0, sb.length() - 2), R.string.general_share_via, getActivity());
        else
            Toast.makeText(getContext(), R.string.general_msg_something_went_wrong, Toast.LENGTH_SHORT).show();

        _selectionHelper.exitSelectionMode();
    }

    /**
     * Method procedure for the selection action: "Delete".
     *
     * @param selection The selected {@link Person} list to delete.
     */
    private void actionSelectionDelete(ArrayList<Person> selection) {
        if (selection == null)
            throw new NullPointerException("selection");

        final ArrayList<Person> cache = new ArrayList<>();

        for (int i = 0; i < selection.size(); i++) {
            Person person = selection.get(i);
            cache.add(person);

            int position = _people.indexOf(person);
            _repository.people.delete(person.getId());
            _people.remove(position);
            _peopleRaw.remove(person);
            _adapter.notifyItemRemoved(position);
        }

        // Shows the user the number of deleted people, and an option to restore:
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinator), selection.size() + " " + getActivity().getString(R.string.general_msg_deleted), 10000)
                .setAction(getActivity().getString(R.string.general_btn_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (Person person : cache) {
                            person.setId(_repository.people.create(person));
                            _people.add(person);
                            _peopleRaw.add(person);
                        }
                        filter(SortAndFilterDialog.getFiltersFromPreferences(_preferences, UIConsts.Keys.SORT_MY_DIRECTORS_KEY));
                        sort(SortAndFilterDialog.getSortFromPreferences(_preferences, UIConsts.Keys.SORT_MY_DIRECTORS_KEY, _sorts, DEFAULT_SORT_INDEX),
                                SortAndFilterDialog.getSortOrderFromPreferences(_preferences, UIConsts.Keys.SORT_MY_DIRECTORS_KEY, DEFAULT_SORT_ORDER_IS_ASC));
                        _adapter.notifyDataSetChanged();
                        displayListState();
                    }
                }).setActionTextColor(Utils.Colors.ACCENT);

        this.displayListState();
        _selectionHelper.exitSelectionModeFromAction();

        // Shows the snackbar:
        SnackbarManager.with(getActivity()).set(snackbar).show();
    }

    //endregion

    //endregion

}
