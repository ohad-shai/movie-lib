package com.ohadshai.movielib.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ohadshai.movielib.R;
import com.ohadshai.movielib.adapters.SearchPeopleAdapter;
import com.ohadshai.movielib.entities.Person;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.utils.NoNetworkException;
import com.ohadshai.movielib.utils.Utils;
import com.ohadshai.movielib.utils.web_services.tmdb_api.TMDBService;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass for "Search People".
 */
public class SearchPeopleFragment extends Fragment {

    //region Private Members

    /**
     * Holds the view of this fragment.
     */
    private View _view;

    /**
     * Holds the list of persons.
     */
    private ArrayList<Person> _persons = new ArrayList<>();

    /**
     * Holds the adapter of the persons list for the {@link RecyclerView}.
     */
    private SearchPeopleAdapter _adapter;

    /**
     * Holds the {@link RecyclerView} control of the people list.
     */
    private RecyclerView _rvPeople;

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
     * Holds the task for searching people.
     */
    private SearchPeopleTask _searchPeopleTask;

    //endregion

    /**
     * Initializes a new instance of simple {@link Fragment} subclass for "Search People".
     */
    public SearchPeopleFragment() {
        // Required empty public constructor.
    }

    //region Fragment Events

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.fragment_search_people, container, false);

        _progressBar = (ProgressBar) _view.findViewById(R.id.progressBar);
        _progressBar.setVisibility(View.VISIBLE);

        _rvPeople = (RecyclerView) _view.findViewById(R.id.rvPeople);
        _rvPeople.setLayoutManager(new LinearLayoutManager(getActivity()));
        _adapter = new SearchPeopleAdapter(_persons, getActivity());
        _rvPeople.setAdapter(_adapter);

        _llNoResults = (LinearLayout) _view.findViewById(R.id.llNoResults);

        _txtSearchMessage = (TextView) _llNoResults.findViewById(R.id.txtSearchMessage);

        this.displaySearchInitial();

        return _view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Saves the people list:
        outState.putParcelableArrayList(UIConsts.Bundles.PEOPLE_LIST_KEY, _persons);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState == null)
            return;

        // Restores the people list:
        ArrayList<Person> people = savedInstanceState.getParcelableArrayList(UIConsts.Bundles.PEOPLE_LIST_KEY);
        if (people != null) {
            _persons.clear();
            for (Person person : people)
                _persons.add(person);
        }
        this.displayListState();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (_searchPeopleTask != null)
            _searchPeopleTask.cancel(true);
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

        if (this._persons.size() < 1) {
            _rvPeople.setVisibility(View.GONE);
            _llNoResults.setVisibility(View.VISIBLE);
        } else {
            _llNoResults.setVisibility(View.GONE);
            _rvPeople.setVisibility(View.VISIBLE);
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
            _persons.clear();
            _adapter.notifyDataSetChanged();
            this.displaySearchInitial();
            return;
        }

        if (_searchPeopleTask != null)
            _searchPeopleTask.cancel(true);

        _searchPeopleTask = new SearchPeopleTask(query);
        _searchPeopleTask.execute();
    }

    //endregion

    /**
     * Display the initial state of the search.
     */
    private void displaySearchInitial() {
        if (_searchPeopleTask != null)
            _searchPeopleTask.cancel(true);

        _txtSearchMessage.setText(R.string.general_search_people_hint);
        this.displayListState();
    }

    //region Inner Classes

    /**
     * Represents a task for searching people by the name.
     */
    private class SearchPeopleTask extends AsyncTask<Void, Void, ArrayList<Person>> {

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
         * Initializes a new instance of a task for searching people by the name.
         *
         * @param query The search query to search.
         */
        public SearchPeopleTask(String query) {
            this._query = query;
        }

        //region Task Events

        @Override
        protected void onPreExecute() {
            if (_query == null || _query.trim().equals(""))
                throw new NullPointerException("_query in null or empty.");

            _rvPeople.setVisibility(View.GONE);
            _llNoResults.setVisibility(View.GONE);
            _progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Person> doInBackground(Void... params) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String url = TMDBService.Urls.SEARCH_PEOPLE_BY_NAME + Uri.encode(_query) +
                    TMDBService.Params.APPEND_PAGE + "1" +
                    TMDBService.Params.APPEND_LANGUAGE + preferences.getString(UIConsts.Preferences.Keys.LANGUAGE, "") +
                    TMDBService.Params.APPEND_REGION + preferences.getString(UIConsts.Preferences.Keys.REGION, "");

            try {
                String json = Utils.Networking.sendHttpRequest(url, getContext()); // Sends an HTTP request, and gets the JSON.
                return TMDBService.Parse.personList(json); // Parses the persons list from the JSON.
            } catch (Exception e) {
                this._exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Person> persons) {
            if (_exception != null) {
                if (_exception instanceof NoNetworkException)
                    Utils.UI.showNoNetworkConnectionDialog(getContext());
                else
                    throw new IllegalStateException(_exception.getMessage(), _exception.getCause());
                return;
            }

            _persons.clear();
            for (Person person : persons)
                _persons.add(person);
            _adapter.notifyDataSetChanged();
            _txtSearchMessage.setText(R.string.general_msg_no_results);
            _rvPeople.scrollToPosition(0);
            displayListState();
        }

        //endregion

    }

    //endregion

}
