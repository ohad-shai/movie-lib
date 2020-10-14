package com.ohadshai.movielib.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import com.ohadshai.movielib.R;
import com.ohadshai.movielib.adapters.MoviesDiscoverAdapter;
import com.ohadshai.movielib.entities.Movie;
import com.ohadshai.movielib.loaders.InTheatersLoader;
import com.ohadshai.movielib.tasks.LoadIMDbIdsForShareTask;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.ui.dialogs.SortAndFilterDialog;
import com.ohadshai.movielib.utils.NoNetworkException;
import com.ohadshai.movielib.utils.RevealHelper;
import com.ohadshai.movielib.utils.SelectionHelper;
import com.ohadshai.movielib.utils.SnackbarManager;
import com.ohadshai.movielib.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;

import static com.ohadshai.movielib.R.id.actionSearch;
import static com.ohadshai.movielib.R.id.fabAdd;
import static com.ohadshai.movielib.R.id.progressBar;

/**
 * A simple {@link Fragment} subclass for "In Theaters".
 */
public class InTheatersFragment extends Fragment implements SelectionHelper.SelectionController<Movie>, MenuItem.OnMenuItemClickListener {

    //region Constants

    /**
     * Holds a constant for the {@link com.ohadshai.movielib.loaders.InTheatersLoader} loader id.
     */
    private static final int IN_THEATERS_LOADER_ID = 173211;

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
     * Holds the {@link SharedPreferences} control.
     */
    private SharedPreferences _preferences;

    /**
     * Holds the list of movies.
     */
    private ArrayList<Movie> _movies = new ArrayList<>();

    /**
     * Holds the adapter of the movies list for the {@link RecyclerView}.
     */
    private MoviesDiscoverAdapter _adapter;

    /**
     * Holds the {@link RecyclerView} control of the movies list.
     */
    private RecyclerView _rvMovies;

    /**
     * Holds the main {@link ProgressBar} control of the fragment.
     */
    private ProgressBar _progressBar;

    /**
     * Holds the {@link Button} control to show a message on error.
     */
    private Button _btnMessage;

    /**
     * Holds the {@link RevealHelper} for the search.
     */
    private RevealHelper _revealSearch;

    /**
     * Holds an array of {@link com.ohadshai.movielib.ui.dialogs.SortAndFilterDialog.Sort}, which represents the sort options.
     */
    private SortAndFilterDialog.Sort[] _sorts;

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
    private SelectionHelper<Movie> _selectionHelper = new SelectionHelper<>(_movies, new SelectionHelper.SelectionCallback<Movie>() {

        @Override
        public void onShowHeader() {
            _revealSelection.show();
        }

        @Override
        public void onAnimateShowHeader() {
            _revealSelection.reveal();
        }

        @Override
        public void onSelectionChanged(ArrayList<Movie> selection) {
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

    /**
     * Holds the {@link LoadIMDbIdsForShareTask} task for loading the IMDb Ids of movies in a "Share" action.
     */
    private LoadIMDbIdsForShareTask _loadIMDbIdsForShareTask;

    //endregion

    //endregion

    /**
     * Initializes a new instance of simple {@link Fragment} subclass for "In Theaters".
     */
    public InTheatersFragment() {
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
        _view = inflater.inflate(R.layout.fragment_discover, container, false);

        getActivity().setTitle(R.string.in_theaters_fragment_title);

        _preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        _sorts = new SortAndFilterDialog.Sort[]{
                new SortAndFilterDialog.Sort(getString(R.string.general_title), getString(R.string.dialog_sort_a_to_z), getString(R.string.dialog_sort_z_to_a)),
                new SortAndFilterDialog.Sort(getString(R.string.general_year), getString(R.string.dialog_sort_low_to_high), getString(R.string.dialog_sort_high_to_low)),
                new SortAndFilterDialog.Sort(getString(R.string.general_rating), getString(R.string.dialog_sort_low_to_high), getString(R.string.dialog_sort_high_to_low)),
                new SortAndFilterDialog.Sort(getString(R.string.general_popularity), getString(R.string.dialog_sort_most_unpopular), getString(R.string.dialog_sort_most_popular))
        };

        ((FloatingActionButton) getActivity().findViewById(fabAdd)).hide();

        _progressBar = (ProgressBar) _view.findViewById(progressBar);
        _progressBar.setVisibility(View.VISIBLE);

        _rvMovies = (RecyclerView) _view.findViewById(R.id.rvMovies);
        _rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        _adapter = new MoviesDiscoverAdapter(_movies, this);
        _rvMovies.setAdapter(_adapter);

        _btnMessage = (Button) _view.findViewById(R.id.btnMessage);

        //region Search Related...

        final View flSearchContainer = getActivity().findViewById(R.id.flSearchContainer);
        _revealSearch = RevealHelper.create(getActivity(), flSearchContainer, "_revealSearch_InTheaters", new RevealHelper.RevealHelperCallback() {
            @Override
            public void onReveal() {
                // Checks if any snackbar is showing in this area, then closes it:
                if (SnackbarManager.with(getActivity()).isShowing())
                    SnackbarManager.with(getActivity()).dismiss();

                // Locks the navigation drawer:
                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
                searchMain.setQueryHint(getText(R.string.general_search_movies_hint));
                searchMain.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searchMain.clearFocus();
                        flSearchContainer.findViewById(R.id.toolbarSearch).requestFocus();

                        SearchMoviesFragment fragment = (SearchMoviesFragment) getActivity().getSupportFragmentManager().findFragmentByTag(UIConsts.Fragments.SEARCH_MOVIES_FRAGMENT_TAG);
                        if (fragment != null)
                            fragment.search(query);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        SearchMoviesFragment fragment = (SearchMoviesFragment) getActivity().getSupportFragmentManager().findFragmentByTag(UIConsts.Fragments.SEARCH_MOVIES_FRAGMENT_TAG);
                        if (fragment != null)
                            fragment.search(newText);
                        return true;
                    }
                });
                searchMain.setQuery("", false);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                SearchMoviesFragment searchMoviesFragment = (SearchMoviesFragment) fragmentManager.findFragmentByTag(UIConsts.Fragments.SEARCH_MOVIES_FRAGMENT_TAG);
                if (searchMoviesFragment != null)
                    fragmentManager.beginTransaction().replace(R.id.flSearchContent, searchMoviesFragment, UIConsts.Fragments.SEARCH_MOVIES_FRAGMENT_TAG).commit();
                else
                    fragmentManager.beginTransaction().replace(R.id.flSearchContent, new SearchMoviesFragment(), UIConsts.Fragments.SEARCH_MOVIES_FRAGMENT_TAG).commit();

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
            }

            @Override
            public void onConcealed() {
                // Removes the SearchMoviesFragment:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                SearchMoviesFragment fragment = (SearchMoviesFragment) fragmentManager.findFragmentByTag(UIConsts.Fragments.SEARCH_MOVIES_FRAGMENT_TAG);
                if (fragment != null)
                    fragmentManager.beginTransaction().remove(fragment).commit();
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

        _revealSelection = RevealHelper.create(getActivity(), _toolbarSelection, "_revealSelection_InTheaters", new RevealHelper.RevealHelperCallback() {
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
            }

            @Override
            public void initLayoutControls() {

                _toolbarSelection.getMenu().clear();
                _toolbarSelection.inflateMenu(R.menu.menu_selection);

                _toolbarSelection.getMenu().findItem(R.id.actionEdit).setVisible(false);

                MenuItem actionShare = _toolbarSelection.getMenu().findItem(R.id.actionShare);
                actionShare.setOnMenuItemClickListener(InTheatersFragment.this);

                _toolbarSelection.getMenu().findItem(R.id.actionDelete).setVisible(false);

                MenuItem actionSelectAll = _toolbarSelection.getMenu().findItem(R.id.actionSelectAll);
                actionSelectAll.setOnMenuItemClickListener(InTheatersFragment.this);

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
        }).setStatusBarTransition(Utils.Colors.PRIMARY_DARK, Utils.Colors.SELECTION_STATUS, 500, 0, 500, 200);

        //endregion

        // Checks if network connection is available:
        if (Utils.Networking.isNetworkAvailable(getContext())) {
            if (savedInstanceState == null)
                this.loadInTheatersList();
        } else {
            // Network is not available:
            _progressBar.setVisibility(View.GONE);
            _btnMessage.setText(R.string.general_msg_no_network_connection_turn_on);
            _btnMessage.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_big_error_network, 0, 0);
            _btnMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.Intents.networkSettings(getContext());
                }
            });
            _btnMessage.setVisibility(View.VISIBLE);
        }

        return _view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case actionSearch:
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
        } else if (item.getItemId() == R.id.actionSelectAll) {
            _selectionHelper.actionSelectAll();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SelectionHelper<Movie> getSelectionHelper() {
        return _selectionHelper;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Saves the movies list:
        outState.putParcelableArrayList(UIConsts.Bundles.MOVIES_LIST_KEY, _movies);

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

        // Restores the movies list:
        ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(UIConsts.Bundles.MOVIES_LIST_KEY);
        if (movies != null) {
            _movies.clear();
            for (Movie movie : movies)
                _movies.add(movie);
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
        SearchMoviesFragment searchMoviesFragment = (SearchMoviesFragment) getFragmentManager().findFragmentByTag(UIConsts.Fragments.SEARCH_MOVIES_FRAGMENT_TAG);
        if (searchMoviesFragment != null && searchMoviesFragment.onBackPressed())
            return true;
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

        if (_loadIMDbIdsForShareTask != null)
            _loadIMDbIdsForShareTask.cancel(true);
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

        if (this._movies.size() < 1) {
            _rvMovies.setVisibility(View.GONE);
            _btnMessage.setVisibility(View.VISIBLE);
        } else {
            _btnMessage.setVisibility(View.GONE);
            _rvMovies.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Loads the "In Theaters" list from the TMDB service.
     */
    public void loadInTheatersList() {
        getLoaderManager().initLoader(IN_THEATERS_LOADER_ID, null, new LoaderManager.LoaderCallbacks<ArrayList<Movie>>() {
            @Override
            public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
                return new InTheatersLoader(getContext());
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
                if (data == null) {
                    Exception exception = ((InTheatersLoader) loader).getException();
                    if (exception != null) {
                        if (exception instanceof NoNetworkException) {
                            _btnMessage.setText(R.string.general_msg_no_network_connection_turn_on);
                            _btnMessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Utils.Intents.networkSettings(getContext());
                                }
                            });
                            _progressBar.setVisibility(View.GONE);
                            _btnMessage.setVisibility(View.VISIBLE);
                        } else {
                            _btnMessage.setText(R.string.general_msg_something_went_wrong);
                            _btnMessage.setOnClickListener(null);
                            _progressBar.setVisibility(View.GONE);
                            _btnMessage.setVisibility(View.VISIBLE);
                        }
                    }
                    return;
                }

                _movies.clear();
                for (Movie movie : data)
                    _movies.add(movie);
                sort(SortAndFilterDialog.getSortFromPreferences(_preferences, UIConsts.Keys.SORT_IN_THEATERS_KEY, _sorts, DEFAULT_SORT_INDEX),
                        SortAndFilterDialog.getSortOrderFromPreferences(_preferences, UIConsts.Keys.SORT_IN_THEATERS_KEY, DEFAULT_SORT_ORDER_IS_ASC));
                _adapter.notifyDataSetChanged();
                displayListState();
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
            }
        }).forceLoad();
    }

    //endregion

    //region Private Methods

    /**
     * Method procedure for menu action: "Sort & Filter".
     */
    private void actionSortAndFilter() {
        SortAndFilterDialog.show(getActivity().getFragmentManager(), UIConsts.Keys.SORT_IN_THEATERS_KEY, _sorts, DEFAULT_SORT_INDEX, DEFAULT_SORT_ORDER_IS_ASC, new SortAndFilterDialog.ResultListener() {
            @Override
            public void onPositiveResult(@Nullable SortAndFilterDialog.Sort sort, boolean isSortAsc, @Nullable ArrayList<String> selectedFilters) {
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
     * Sorts the list of {@link Movie}.
     *
     * @param sort      The sort option to apply.
     * @param isSortAsc An indicator indicating whether the sort order is ascending or not (descending).
     */
    private void sort(@NonNull SortAndFilterDialog.Sort sort, boolean isSortAsc) {
        if (sort.getName().equals(_sorts[0].getName()))
            Collections.sort(_movies, new Movie.TitleComparator()); // Sort by "Title".
        else if (sort.getName().equals(_sorts[1].getName()))
            Collections.sort(_movies, new Movie.YearComparator()); // Sort by "Year".
        else if (sort.getName().equals(_sorts[2].getName()))
            Collections.sort(_movies, new Movie.RatingComparator()); // Sort by "Rating".
        else if (sort.getName().equals(_sorts[3].getName()))
            Collections.sort(_movies, new Movie.PopularityComparator()); // Sort by "Popularity".

        // Checks if sort order is descending:
        if (!isSortAsc)
            Collections.reverse(_movies);
    }

    //region Selection Related

    /**
     * Method procedure for the selection action: "Share".
     *
     * @param selection The selected {@link Movie} list to share.
     */
    private void actionSelectionShare(ArrayList<Movie> selection) {
        if (selection == null)
            throw new NullPointerException("selection");

        if (_loadIMDbIdsForShareTask != null)
            _loadIMDbIdsForShareTask.cancel(true);

        _loadIMDbIdsForShareTask = new LoadIMDbIdsForShareTask(getActivity(), selection);
        _loadIMDbIdsForShareTask.execute();
        _selectionHelper.exitSelectionMode();
    }

    //endregion

    //endregion

}
