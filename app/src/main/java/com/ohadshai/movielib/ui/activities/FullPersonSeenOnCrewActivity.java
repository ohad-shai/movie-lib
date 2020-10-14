package com.ohadshai.movielib.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.ohadshai.movielib.R;
import com.ohadshai.movielib.adapters.PersonSeenOnCrewAdapter;
import com.ohadshai.movielib.entities.MovieInPersonCrew;
import com.ohadshai.movielib.ui.UIConsts;

import java.util.ArrayList;

public class FullPersonSeenOnCrewActivity extends AppCompatActivity {

    //region Private Members

    /**
     * Holds the list of {@link MovieInPersonCrew}.
     */
    private ArrayList<MovieInPersonCrew> _crew = new ArrayList<>();

    /**
     * Holds the adapter of the cast list to the {@link RecyclerView}.
     */
    private PersonSeenOnCrewAdapter _adapter;

    /**
     * Holds the {@link RecyclerView} control for the cast list.
     */
    private RecyclerView _rvList;

    //endregion

    //region Activity Events

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_section_displayer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String personName = getIntent().getStringExtra(UIConsts.Bundles.PERSON_NAME_KEY);
        if (personName == null)
            throw new NullPointerException("PERSON_NAME_KEY not provided in the intent.");

        _crew = getIntent().getParcelableArrayListExtra(UIConsts.Bundles.PERSON_SEEN_ON_CREW_LIST_KEY);
        if (_crew == null)
            throw new NullPointerException("PERSON_SEEN_ON_CREW_LIST_KEY not provided in the intent.");

        setTitle(getString(R.string.general_msg_seen_on_crew) + " " + personName);

        _rvList = (RecyclerView) findViewById(R.id.rvList);
        _rvList.setLayoutManager(new LinearLayoutManager(this));
        _adapter = new PersonSeenOnCrewAdapter(_crew, this);
        _rvList.setAdapter(_adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                FullPersonSeenOnCrewActivity.this.overridePendingTransition(R.anim.go_left_enter, R.anim.go_left_exit);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        FullPersonSeenOnCrewActivity.this.overridePendingTransition(R.anim.go_left_enter, R.anim.go_left_exit);

        super.onBackPressed();
    }

    //endregion

}
