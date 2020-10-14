package com.ohadshai.movielib.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ohadshai.movielib.R;
import com.ohadshai.movielib.adapters.SearchMoviesAdapter;
import com.ohadshai.movielib.db.DBHandler;
import com.ohadshai.movielib.entities.Genre;
import com.ohadshai.movielib.entities.Movie;
import com.ohadshai.movielib.tasks.LoadIMDbIdsForShareTask;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.utils.Cache;
import com.ohadshai.movielib.utils.NoNetworkException;
import com.ohadshai.movielib.utils.RevealHelper;
import com.ohadshai.movielib.utils.SelectionHelper;
import com.ohadshai.movielib.utils.SnackbarManager;
import com.ohadshai.movielib.utils.Utils;
import com.ohadshai.movielib.utils.web_services.tmdb_api.TMDBService;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass for "Search Movies".
 */
public class SearchMoviesFragment extends Fragment implements SelectionHelper.SelectionController<Movie>, MenuItem.OnMenuItemClickListener {

    //region Constants

    /**
     * Holds a constant for a {@link Bundle} key: "Last Search Query".
     */
    private static final String LAST_SEARCH_QUERY_KEY = "last_search_query_key";

    //endregion

    //region Private Members

    /**
     * Holds the view of this fragment.
     */
    private View _view;

    /**
     * Holds the list of movies.
     */
    private ArrayList<Movie> _movies = new ArrayList<>();

    /**
     * Holds the adapter of the movies list for the {@link RecyclerView}.
     */
    private SearchMoviesAdapter _adapter;

    /**
     * Holds the {@link RecyclerView} control of the movies list.
     */
    private RecyclerView _rvMovies;

    /**
     * Holds the main {@link ProgressBar} control of the fragment.
     */
    private ProgressBar _progressBar;

    /**
     * Holds the {@link LinearLayout} control to show the layout when there are no results.
     */
    private LinearLayout _llNoResults;

    /**
     * Holds the {@link TextView} control for the search state message.
     */
    private TextView _txtSearchMessage;

    /**
     * Holds the task for searching movies.
     */
    private SearchMoviesTask _searchMoviesTask;

    //region [ Selection ]

    /**
     * Holds the {@link FrameLayout} control for the selection header.
     */
    private FrameLayout _flSearchSelectionHeader;

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

    /**
     * Holds the last search query.
     */
    private String _lastQuery;

    /**
     * Holds the {@link Bundle} for the saved instance state.
     */
    private Bundle _savedInstanceState;

    //endregion

    /**
     * Initializes a new instance of simple {@link Fragment} subclass for "Search Movies".
     */
    public SearchMoviesFragment() {
        // Required empty public constructor.
    }

    //region Fragment Events

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this._savedInstanceState = savedInstanceState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.fragment_search_movies, container, false);

        _progressBar = (ProgressBar) _view.findViewById(R.id.progressBar);
        _progressBar.setVisibility(View.VISIBLE);

        _rvMovies = (RecyclerView) _view.findViewById(R.id.rvMovies);
        _rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        _adapter = new SearchMoviesAdapter(_movies, this);
        _rvMovies.setAdapter(_adapter);

        _llNoResults = (LinearLayout) _view.findViewById(R.id.llNoResults);

        _txtSearchMessage = (TextView) _llNoResults.findViewById(R.id.txtSearchMessage);

        //region Selection Related...

        _flSearchSelectionHeader = (FrameLayout) getActivity().findViewById(R.id.flSearchSelectionHeader);

        _txtSelectionTitle = (TextView) _flSearchSelectionHeader.findViewById(R.id.txtSelectionTitle);

        _revealSelection = RevealHelper.create(getActivity(), _flSearchSelectionHeader, "_revealSelection_SearchMovies", new RevealHelper.RevealHelperCallback() {
            @Override
            public void onReveal() {
                int revealX = _flSearchSelectionHeader.getWidth() / 2;
                int revealY = _flSearchSelectionHeader.getHeight() + 500;
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

                Toolbar toolbarSelection = (Toolbar) _flSearchSelectionHeader.findViewById(R.id.toolbarSelection);

                toolbarSelection.getMenu().clear();
                toolbarSelection.inflateMenu(R.menu.menu_selection);

                toolbarSelection.getMenu().findItem(R.id.actionEdit).setVisible(false);

                MenuItem actionShare = toolbarSelection.getMenu().findItem(R.id.actionShare);
                actionShare.setOnMenuItemClickListener(SearchMoviesFragment.this);

                toolbarSelection.getMenu().findItem(R.id.actionDelete).setVisible(false);

                MenuItem actionSelectAll = toolbarSelection.getMenu().findItem(R.id.actionSelectAll);
                actionSelectAll.setOnMenuItemClickListener(SearchMoviesFragment.this);

                final ImageButton imgbtnClose = (ImageButton) _flSearchSelectionHeader.findViewById(R.id.imgbtnClose);
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
                int revealX = _flSearchSelectionHeader.getWidth() / 2;
                int revealY = _flSearchSelectionHeader.getHeight() + 500;
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
        }).setStatusBarTransition(Utils.Colors.SEARCH, Utils.Colors.SELECTION_STATUS, 500, 0, 500, 200);

        //endregion

        this.displaySearchInitial();

        return _view;
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

        // Saves the last search query:
        outState.putString(LAST_SEARCH_QUERY_KEY, _lastQuery);

        // Saves the movies list:
        outState.putParcelableArrayList(UIConsts.Bundles.MOVIES_LIST_KEY, _movies);

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
        if (_selectionHelper.isInSelectionMode()) {
            _selectionHelper.exitSelectionMode();
            return true;
        }

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (_searchMoviesTask != null)
            _searchMoviesTask.cancel(true);

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
            _llNoResults.setVisibility(View.VISIBLE);
        } else {
            _llNoResults.setVisibility(View.GONE);
            _rvMovies.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Searches a query.
     *
     * @param query The search query to search.
     */
    public void search(String query) {
        if (_view == null)
            return;

        if (query == null) {
            return;
        } else if (query.trim().equals("")) {
            _movies.clear();
            _adapter.notifyDataSetChanged();
            this.displaySearchInitial();
            return;
        }

        if (_searchMoviesTask != null)
            _searchMoviesTask.cancel(true);

        // Executes the search task in the following scenarios:
        // 1. It's the first instance state.
        // 2. It's not the first instance state, but the query is same as the last query member (in local).
        // 3. It's not the first instance state, but the query is different than the last query (in the last saved instance state).
        if (_savedInstanceState == null || query.equals(_lastQuery) || !query.equals(_savedInstanceState.getString(LAST_SEARCH_QUERY_KEY, ""))) {
            _searchMoviesTask = new SearchMoviesTask(query);
            _searchMoviesTask.execute();
        }

        this._lastQuery = query;
    }

    //endregion

    //region Private Methods

    /**
     * Display the initial state of the search.
     */
    private void displaySearchInitial() {
        if (_searchMoviesTask != null)
            _searchMoviesTask.cancel(true);

        _txtSearchMessage.setText(R.string.general_search_movies_hint);
        this.displayListState();
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

    //region Inner Classes

    /**
     * Represents a task for searching movies by the title.
     */
    private class SearchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

        //region Private Members

        /**
         * Holds the search query to search.
         */
        private String _query;

        /**
         * Holds an exception object if thrown.
         */
        private Exception _exception;

        //endregion

        /**
         * Initializes a new instance of a task for searching movies by the title.
         *
         * @param query The search query to search.
         */
        public SearchMoviesTask(String query) {
            this._query = query;
        }

        //region Task Events

        @Override
        protected void onPreExecute() {
            if (_query == null || _query.trim().equals(""))
                throw new NullPointerException("_query in null or empty.");

            _rvMovies.setVisibility(View.GONE);
            _llNoResults.setVisibility(View.GONE);
            _progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String url = TMDBService.Urls.SEARCH_MOVIES_BY_TITLE + Uri.encode(_query) +
                    TMDBService.Params.APPEND_PAGE + "1" +
                    TMDBService.Params.APPEND_LANGUAGE + preferences.getString(UIConsts.Preferences.Keys.LANGUAGE, "") +
                    TMDBService.Params.APPEND_REGION + preferences.getString(UIConsts.Preferences.Keys.REGION, "");

            try {
                String json = Utils.Networking.sendHttpRequest(url, getContext()); // Sends an HTTP request, and gets the JSON.
                ArrayList<Movie> movies = TMDBService.Parse.movieList(json); // Parses the movies list from the JSON.
                ArrayList<Genre> genres = Cache.getOtherwisePut(Cache.Keys.GENRES_LIST_FROM_DB, DBHandler.getInstance(getContext()).genres.getAll()); // Gets all the genres in the database.
                for (Movie movie : movies) {
                    // Gets the name of each genre by it's id:
                    for (Genre genre : movie.getGenres())
                        genre.setName(Genre.getGenreNameInListById(genre.getId(), genres));
                }
                return movies;
            } catch (Exception e) {
                this._exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if (_exception != null) {
                if (_exception instanceof NoNetworkException)
                    Utils.UI.showNoNetworkConnectionDialog(getContext());
                else
                    throw new IllegalStateException(_exception.getMessage(), _exception.getCause());
                return;
            }

            _movies.clear();
            for (Movie movie : movies)
                _movies.add(movie);
            _adapter.notifyDataSetChanged();
            _txtSearchMessage.setText(R.string.general_msg_no_results);
            _rvMovies.scrollToPosition(0);
            displayListState();
        }

        //endregion

    }

    //endregion

}
