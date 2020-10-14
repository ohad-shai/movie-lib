package com.ohadshai.movielib.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ohadshai.movielib.R;
import com.ohadshai.movielib.adapters.PersonSeenOnCastAdapter;
import com.ohadshai.movielib.adapters.PersonSeenOnCrewAdapter;
import com.ohadshai.movielib.db.DBHandler;
import com.ohadshai.movielib.entities.MovieInPersonCast;
import com.ohadshai.movielib.entities.MovieInPersonCrew;
import com.ohadshai.movielib.entities.Person;
import com.ohadshai.movielib.loaders.FullPersonInformationLoader;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.utils.NoNetworkException;
import com.ohadshai.movielib.utils.RevealHelper;
import com.ohadshai.movielib.utils.Utils;
import com.ohadshai.movielib.utils.web_services.tmdb_api.TMDBService;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import static com.ohadshai.movielib.R.id.lblPersonAge;
import static com.ohadshai.movielib.R.id.lblPersonBio;
import static com.ohadshai.movielib.R.id.lblPersonPlaceOfBirth;

public class PersonDisplayActivity extends AppCompatActivity {

    //region Constants

    /**
     * Holds a constant for the {@link com.ohadshai.movielib.loaders.FullPersonInformationLoader} loader id.
     */
    private static final int PERSON_INFORMATION_LOADER_ID = 173311;

    /**
     * Holds a constant for the maximum items in the seen on sections.
     */
    private static final int MAX_ITEMS_IN_SEEN_ON_SECTIONS = 4;

    //endregion

    //region Private Members

    /**
     * Holds the database interactions object.
     */
    private DBHandler _repository;

    /**
     * Holds the {@link Person} object.
     */
    private Person _person;

    /**
     * Holds the {@link ImageView} for the person image.
     */
    private ImageView _imgPerson;

    /**
     * Holds an indicator indicating whether if returned from a section like: Cast / Crew, or not.
     */
    private boolean _isReturnedFromSection = false;

    /**
     * Holds the {@link TextView} control for the person age.
     */
    private TextView _lblPersonAge;

    /**
     * Holds the {@link TextView} control for the person place of birth.
     */
    private TextView _lblPersonPlaceOfBirth;

    /**
     * Holds the {@link TextView} control for the person biography.
     */
    private TextView _lblPersonBio;

    /**
     * Holds the {@link ImageButton} control for the facebook button.
     */
    private ImageButton _imgbtnFacebook;

    /**
     * Holds the {@link ImageButton} control for the twitter button.
     */
    private ImageButton _imgbtnTwitter;

    /**
     * Holds the {@link ImageButton} control for the instagram button.
     */
    private ImageButton _imgbtnInstagram;

    /**
     * Holds the {@link Button} control for the website button.
     */
    private Button _btnWebsite;

    //region [Seen On Cast Members]

    /**
     * Holds the list of movies seen on cast.
     */
    private ArrayList<MovieInPersonCast> _moviesSeenOnCast = new ArrayList<>();

    /**
     * Holds the {@link RecyclerView} control for the cast list.
     */
    private RecyclerView _rvSeenOnCast;

    /**
     * Holds the movie cast adapter for the {@link RecyclerView}.
     */
    private PersonSeenOnCastAdapter _adapterCast;

    /**
     * Holds the {@link ProgressBar} control for the cast loading.
     */
    private ProgressBar _progressSeenOnCast;

    /**
     * Holds the {@link LinearLayout} control to show all the movie cast.
     */
    private LinearLayout _llShowAllSeenOnCast;

    /**
     * Holds the {@link TextView} control for the movie cast state message.
     */
    private TextView _lblSeenOnCastMessage;

    //endregion

    //region [Seen On Crew Members]

    /**
     * Holds the list of movies seen on crew.
     */
    private ArrayList<MovieInPersonCrew> _moviesSeenOnCrew = new ArrayList<>();

    /**
     * Holds the {@link RecyclerView} control for the crew list.
     */
    private RecyclerView _rvSeenOnCrew;

    /**
     * Holds the movie crew adapter for the {@link RecyclerView}.
     */
    private PersonSeenOnCrewAdapter _adapterCrew;

    /**
     * Holds the {@link ProgressBar} control for the crew loading.
     */
    private ProgressBar _progressSeenOnCrew;

    /**
     * Holds the {@link LinearLayout} control to show all the movie crew.
     */
    private LinearLayout _llShowAllSeenOnCrew;

    /**
     * Holds the {@link TextView} control for the movie crew state message.
     */
    private TextView _lblSeenOnCrewMessage;

    //endregion

    /**
     * Holds the {@link RevealHelper} for the "person add" action.
     */
    private RevealHelper _revealPersonAdd;

    //endregion

    //region Activity Events

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_display);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.initControls(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addable, menu);

        // Colors the add icon:
        menu.findItem(R.id.actionAdd).getIcon().mutate().setColorFilter(Utils.Colors.ACCENT, PorterDuff.Mode.SRC_ATOP);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionAdd:
                _revealPersonAdd.setRevealFrom(findViewById(R.id.flAddPersonContainer).getWidth() - (findViewById(R.id.actionAdd).getWidth() / 2), 0).toggle();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UIConsts.RequestCodes.FULL_PERSON_SEEN_ON_CAST_REQUEST_CODE || requestCode == UIConsts.RequestCodes.FULL_PERSON_SEEN_ON_CREW_REQUEST_CODE)
            _isReturnedFromSection = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Saves the person object:
        outState.putParcelable(UIConsts.Bundles.PERSON_KEY, _person);

        _revealPersonAdd.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState == null)
            return;

        // Restores the person object:
        Person person = savedInstanceState.getParcelable(UIConsts.Bundles.PERSON_KEY);
        if (person != null) {
            _person = person;

            if (_person.isExtraInfoLoaded()) {
                //region Builds the preview sections: Seen on Cast & Crew...

                _moviesSeenOnCast.clear();
                for (int i = 0; i < person.getSeenOnCast().size(); i++) {
                    if (i == MAX_ITEMS_IN_SEEN_ON_SECTIONS)
                        break;
                    _moviesSeenOnCast.add(person.getSeenOnCast().get(i));
                }

                _moviesSeenOnCrew.clear();
                for (int i = 0; i < person.getSeenOnCrew().size(); i++) {
                    if (i == MAX_ITEMS_IN_SEEN_ON_SECTIONS)
                        break;
                    _moviesSeenOnCrew.add(person.getSeenOnCrew().get(i));
                }

                _adapterCast.notifyDataSetChanged();
                this.displayListStateSeenOnCast();
                _adapterCrew.notifyDataSetChanged();
                this.displayListStateSeenOnCrew();

                //endregion
                //region Update UI Controls

                _lblPersonAge.setText(_person.displayAge());
                _lblPersonPlaceOfBirth.setText(_person.getPlaceOfBirth());
                _lblPersonBio.setText(_person.getBiography());

                if (_person.getFacebook() != null) {
                    _imgbtnFacebook.setEnabled(true);
                    _imgbtnFacebook.setColorFilter(Utils.Colors.FACEBOOK);
                } else {
                    _imgbtnFacebook.setColorFilter(Color.rgb(80, 80, 80));
                    _imgbtnFacebook.setEnabled(false);
                }
                if (_person.getTwitter() != null) {
                    _imgbtnTwitter.setEnabled(true);
                    _imgbtnTwitter.setColorFilter(Utils.Colors.TWITTER);
                } else {
                    _imgbtnTwitter.setColorFilter(Color.rgb(80, 80, 80));
                    _imgbtnTwitter.setEnabled(false);
                }
                if (_person.getInstagram() != null) {
                    _imgbtnInstagram.setEnabled(true);
                    _imgbtnInstagram.setColorFilter(Color.WHITE);
                } else {
                    _imgbtnInstagram.setColorFilter(Color.rgb(80, 80, 80));
                    _imgbtnInstagram.setEnabled(false);
                }

                if (_person.getHomepage() != null) {
                    _btnWebsite.setEnabled(true);
                    _btnWebsite.setTextColor(Color.parseColor("#cccccc"));
                } else {
                    _btnWebsite.setTextColor(Color.parseColor("#444444"));
                    _btnWebsite.setEnabled(false);
                }

                _adapterCast.notifyDataSetChanged();
                displayListStateSeenOnCast();
                _adapterCrew.notifyDataSetChanged();
                displayListStateSeenOnCrew();

                //endregion
            }
            // If the full person information not loaded:
            else {
                // Checks if the person has a TMDB Id to get the full person information:
                if (_person.getTMDBId() != null) {
                    if (!_person.isExtraInfoLoaded())
                        this.loadFullPersonInformation();
                } else {
                    _progressSeenOnCast.setVisibility(View.GONE);
                    _llShowAllSeenOnCast.setVisibility(View.GONE);
                    _lblSeenOnCastMessage.setText(R.string.general_msg_nothing_to_show_here);
                    _lblSeenOnCastMessage.setVisibility(View.VISIBLE);

                    _progressSeenOnCrew.setVisibility(View.GONE);
                    _llShowAllSeenOnCrew.setVisibility(View.GONE);
                    _lblSeenOnCrewMessage.setText(R.string.general_msg_nothing_to_show_here);
                    _lblSeenOnCrewMessage.setVisibility(View.VISIBLE);
                }
            }
        }

        _revealPersonAdd.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (_revealPersonAdd.onBackPress())
            return;

        super.onBackPressed();
    }

    //endregion

    //region Private Methods

    /**
     * Initializes all view controls.
     *
     * @param savedInstanceState The {@link Bundle} saved instance state of the activity.
     */
    private void initControls(Bundle savedInstanceState) {

        _repository = DBHandler.getInstance(this);

        //region Person object...

        // First, checks if to display the person from the DB:
        final int personId = getIntent().getIntExtra(UIConsts.Bundles.PERSON_ID_KEY, -1);

        // Second, displays the provided person from memory:
        Person providedPerson = getIntent().getParcelableExtra(UIConsts.Bundles.PERSON_KEY);

        if (personId > 0) {
            _person = _repository.people.getById(personId); // Gets the person object from the database.
        } else if (providedPerson != null) {
            _person = providedPerson;

            // Checks if the person is already in the database:
            Person checkPersonInDB = _repository.people.getByTMDBId(_person.getTMDBId());
            if (checkPersonInDB != null)
                _person = checkPersonInDB;
        } else {
            throw new IllegalStateException("PERSON_ID_KEY or PERSON_KEY not provided.");
        }

        //endregion

        setTitle(_person.getName());

        //region Person Image Load

        _imgPerson = (ImageView) findViewById(R.id.imgPerson);
        if (_person.getProfilePath() != null) {
            File imageFile = new File(Utils.Storage.getInternalStoragePath(getApplicationContext()), _person.getProfilePath());

            if (imageFile.exists()) {
                Glide.with(getApplicationContext()).load(imageFile)
                        .placeholder(R.drawable.no_person_image)
                        .override((int) getResources().getDimension(R.dimen.image_large_width), (int) getResources().getDimension(R.dimen.image_large_height))
                        .centerCrop()
                        .into(_imgPerson);
            } else {
                Glide.with(getApplicationContext()).load(TMDBService.Urls.GET_PHOTO_BY_PATH + "/" + _person.getProfilePath())
                        .placeholder(R.drawable.no_person_image)
                        .override((int) getResources().getDimension(R.dimen.image_large_width), (int) getResources().getDimension(R.dimen.image_large_height))
                        .centerCrop()
                        .into(_imgPerson);
            }
        } else {
            Glide.with(getApplicationContext()).load(R.drawable.no_person_image)
                    .override((int) getResources().getDimension(R.dimen.image_large_width), (int) getResources().getDimension(R.dimen.image_large_height))
                    .centerCrop()
                    .into(_imgPerson);
        }

        //endregion

        TextView lblPersonName = (TextView) findViewById(R.id.lblPersonName);
        lblPersonName.setText(_person.getName());

        _lblPersonAge = (TextView) findViewById(lblPersonAge);
        _lblPersonAge.setText(_person.displayAge());

        _lblPersonPlaceOfBirth = (TextView) findViewById(lblPersonPlaceOfBirth);
        _lblPersonPlaceOfBirth.setText(_person.getPlaceOfBirth());

        final NestedScrollView scrollViewPersonBio = (NestedScrollView) findViewById(R.id.scrollViewPersonBio);
        scrollViewPersonBio.setNestedScrollingEnabled(false); // Disables the ScrollView of the person bio at the beginning.

        _lblPersonBio = (TextView) findViewById(lblPersonBio);
        _lblPersonBio.setText(_person.getBiography());
        _lblPersonBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggles the ScrollView enabled or disable to read the full bio of the person:
                if (scrollViewPersonBio.isNestedScrollingEnabled()) {
                    _lblPersonBio.setBackgroundResource(0);
                    scrollViewPersonBio.setNestedScrollingEnabled(false);
                } else {
                    _lblPersonBio.setBackgroundColor(Color.rgb(50, 50, 50));
                    scrollViewPersonBio.setNestedScrollingEnabled(true);
                }
            }
        });

        //region External Ids

        //region Facebook
        _imgbtnFacebook = (ImageButton) findViewById(R.id.imgbtnFacebook);
        _imgbtnFacebook.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utils.UI.showInformationToast(_imgbtnFacebook, R.string.general_facebook);
                return true;
            }
        });
        _imgbtnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.Intents.facebook(PersonDisplayActivity.this, _person.getFacebook());
            }
        });
        if (_person.getFacebook() != null) {
            _imgbtnFacebook.setEnabled(true);
            _imgbtnFacebook.setColorFilter(Utils.Colors.FACEBOOK);
        } else {
            _imgbtnFacebook.setColorFilter(Color.rgb(80, 80, 80));
            _imgbtnFacebook.setEnabled(false);
        }
        //endregion

        //region Twitter
        _imgbtnTwitter = (ImageButton) findViewById(R.id.imgbtnTwitter);
        _imgbtnTwitter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utils.UI.showInformationToast(_imgbtnTwitter, R.string.general_twitter);
                return true;
            }
        });
        _imgbtnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.Intents.twitter(PersonDisplayActivity.this, _person.getTwitter());
            }
        });
        if (_person.getTwitter() != null) {
            _imgbtnTwitter.setEnabled(true);
            _imgbtnTwitter.setColorFilter(Utils.Colors.TWITTER);
        } else {
            _imgbtnTwitter.setColorFilter(Color.rgb(80, 80, 80));
            _imgbtnTwitter.setEnabled(false);
        }
        //endregion

        //region Instagram
        _imgbtnInstagram = (ImageButton) findViewById(R.id.imgbtnInstagram);
        _imgbtnInstagram.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utils.UI.showInformationToast(_imgbtnInstagram, R.string.general_instagram);
                return true;
            }
        });
        _imgbtnInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.Intents.instagram(PersonDisplayActivity.this, _person.getInstagram());
            }
        });
        if (_person.getInstagram() != null) {
            _imgbtnInstagram.setEnabled(true);
            _imgbtnInstagram.setColorFilter(Color.WHITE);
        } else {
            _imgbtnInstagram.setColorFilter(Color.rgb(80, 80, 80));
            _imgbtnInstagram.setEnabled(false);
        }
        //endregion

        //endregion

        _btnWebsite = (Button) findViewById(R.id.btnWebsite);
        _btnWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.Intents.webBrowser(PersonDisplayActivity.this, _person.getHomepage());
            }
        });
        if (_person.getHomepage() != null) {
            _btnWebsite.setEnabled(true);
            _btnWebsite.setTextColor(Color.parseColor("#cccccc"));
        } else {
            _btnWebsite.setTextColor(Color.parseColor("#444444"));
            _btnWebsite.setEnabled(false);
        }

        //region Favorite Button

        final ImageButton imgbtnFavoritePerson = (ImageButton) findViewById(R.id.imgbtnFavoritePerson);
        imgbtnFavoritePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checks if the person is in the DB:
                if (_person.getFolder() == null) {
                    Toast.makeText(PersonDisplayActivity.this, R.string.general_msg_add_person_to_folder, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (_person.isFavorite()) {
                    imgbtnFavoritePerson.setColorFilter(Color.rgb(80, 80, 80));
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
        if (_person.isFavorite())
            imgbtnFavoritePerson.setColorFilter(Utils.Colors.FAVORITE);
        else
            imgbtnFavoritePerson.setColorFilter(Color.rgb(80, 80, 80));

        //endregion

        final View flAddPersonContainer = findViewById(R.id.flAddPersonContainer);
        _revealPersonAdd = RevealHelper.create(this, flAddPersonContainer, "_revealPersonAdd", new RevealHelper.RevealHelperCallback() {
            @Override
            public void initLayoutControls() {

                //region "My Actors" Button

                flAddPersonContainer.findViewById(R.id.llAddToMyActors).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (_person.getFolder() == null) {
                            // Selects the button "My Actors":
                            ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyActors)).setImageResource(R.mipmap.ic_my_actors);
                            ((TextView) flAddPersonContainer.findViewById(R.id.lblMyActors)).setTextColor(Color.parseColor("#eeeeee"));
                            // Unselects the button "My Directors":
                            ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyDirectors)).setImageResource(R.mipmap.ic_my_directors_disabled);
                            ((TextView) flAddPersonContainer.findViewById(R.id.lblMyDirectors)).setTextColor(Color.parseColor("#666666"));

                            // Creates the person in the DB:
                            _person.setFolder(Person.Folders.MY_ACTORS);
                            _person.setTimeAdded(Calendar.getInstance());
                            _person.setId(_repository.people.create(_person));
                            savePersonImageToStorage();
                        } else if (_person.getFolder() == Person.Folders.MY_DIRECTORS) {
                            // Selects the button "My Actors":
                            ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyActors)).setImageResource(R.mipmap.ic_my_actors);
                            ((TextView) flAddPersonContainer.findViewById(R.id.lblMyActors)).setTextColor(Color.parseColor("#eeeeee"));
                            // Unselects the button "My Directors":
                            ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyDirectors)).setImageResource(R.mipmap.ic_my_directors_disabled);
                            ((TextView) flAddPersonContainer.findViewById(R.id.lblMyDirectors)).setTextColor(Color.parseColor("#666666"));

                            // Updates the person in the DB:
                            _person.setFolder(Person.Folders.MY_ACTORS);
                            _person.setTimeAdded(Calendar.getInstance());
                            _repository.people.updateFolderRelated(_person);
                        } else if (_person.getFolder() == Person.Folders.MY_ACTORS) {
                            // Unselects the button "My Actors":
                            ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyActors)).setImageResource(R.mipmap.ic_my_actors_disabled);
                            ((TextView) flAddPersonContainer.findViewById(R.id.lblMyActors)).setTextColor(Color.parseColor("#666666"));
                            // Unselects the button "My Directors":
                            ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyDirectors)).setImageResource(R.mipmap.ic_my_directors_disabled);
                            ((TextView) flAddPersonContainer.findViewById(R.id.lblMyDirectors)).setTextColor(Color.parseColor("#666666"));

                            // Clears any related to folder actions:
                            _person.setFavorite(false);
                            imgbtnFavoritePerson.setColorFilter(Color.rgb(80, 80, 80));

                            // Deletes the person from the DB:
                            _person.setFolder(null);
                            _repository.people.delete(_person.getId());
                            if (_person.getProfilePath() != null)
                                Utils.Storage.deleteFromInternalStorage(_person.getProfilePath(), getApplicationContext());
                        }
                    }
                });

                //endregion

                //region "My Directors" Button

                flAddPersonContainer.findViewById(R.id.llAddToMyDirectors).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (_person.getFolder() == null) {
                            // Unselects the button "My Actors":
                            ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyActors)).setImageResource(R.mipmap.ic_my_actors_disabled);
                            ((TextView) flAddPersonContainer.findViewById(R.id.lblMyActors)).setTextColor(Color.parseColor("#666666"));
                            // Selects the button "My Directors":
                            ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyDirectors)).setImageResource(R.mipmap.ic_my_directors);
                            ((TextView) flAddPersonContainer.findViewById(R.id.lblMyDirectors)).setTextColor(Color.parseColor("#eeeeee"));

                            // Creates the person in the DB:
                            _person.setFolder(Person.Folders.MY_DIRECTORS);
                            _person.setTimeAdded(Calendar.getInstance());
                            _person.setId(_repository.people.create(_person));
                            savePersonImageToStorage();
                        } else if (_person.getFolder() == Person.Folders.MY_ACTORS) {
                            // Unselects the button "My Actors":
                            ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyActors)).setImageResource(R.mipmap.ic_my_actors_disabled);
                            ((TextView) flAddPersonContainer.findViewById(R.id.lblMyActors)).setTextColor(Color.parseColor("#666666"));
                            // Selects the button "My Directors":
                            ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyDirectors)).setImageResource(R.mipmap.ic_my_directors);
                            ((TextView) flAddPersonContainer.findViewById(R.id.lblMyDirectors)).setTextColor(Color.parseColor("#eeeeee"));

                            // Updates the person in the DB:
                            _person.setFolder(Person.Folders.MY_DIRECTORS);
                            _person.setTimeAdded(Calendar.getInstance());
                            _repository.people.updateFolderRelated(_person);
                        } else if (_person.getFolder() == Person.Folders.MY_DIRECTORS) {
                            // Unselects the button "My Actors":
                            ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyActors)).setImageResource(R.mipmap.ic_my_actors_disabled);
                            ((TextView) flAddPersonContainer.findViewById(R.id.lblMyActors)).setTextColor(Color.parseColor("#666666"));
                            // Unselects the button "My Directors":
                            ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyDirectors)).setImageResource(R.mipmap.ic_my_directors_disabled);
                            ((TextView) flAddPersonContainer.findViewById(R.id.lblMyDirectors)).setTextColor(Color.parseColor("#666666"));

                            // Clears any related to folder actions:
                            _person.setFavorite(false);
                            imgbtnFavoritePerson.setColorFilter(Color.rgb(80, 80, 80));

                            // Deletes the person from the DB:
                            _person.setFolder(null);
                            _repository.people.delete(_person.getId());
                            if (_person.getProfilePath() != null)
                                Utils.Storage.deleteFromInternalStorage(_person.getProfilePath(), getApplicationContext());
                        }
                    }
                });

                //endregion

                if (_person.getFolder() != null) {
                    if (_person.getFolder() == Person.Folders.MY_ACTORS) {
                        // Selects the button "My Actors":
                        ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyActors)).setImageResource(R.mipmap.ic_my_actors);
                        ((TextView) flAddPersonContainer.findViewById(R.id.lblMyActors)).setTextColor(Color.parseColor("#eeeeee"));
                        // Unselects the button "My Directors":
                        ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyDirectors)).setImageResource(R.mipmap.ic_my_directors_disabled);
                        ((TextView) flAddPersonContainer.findViewById(R.id.lblMyDirectors)).setTextColor(Color.parseColor("#666666"));
                    } else {
                        // Unselects the button "My Actors":
                        ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyActors)).setImageResource(R.mipmap.ic_my_actors_disabled);
                        ((TextView) flAddPersonContainer.findViewById(R.id.lblMyActors)).setTextColor(Color.parseColor("#666666"));
                        // Selects the button "My Directors":
                        ((ImageView) flAddPersonContainer.findViewById(R.id.imgMyDirectors)).setImageResource(R.mipmap.ic_my_directors);
                        ((TextView) flAddPersonContainer.findViewById(R.id.lblMyDirectors)).setTextColor(Color.parseColor("#eeeeee"));
                    }
                }

            }
        });

        //region Seen On (Cast) Controls

        _progressSeenOnCast = (ProgressBar) findViewById(R.id.progressSeenOnCast);

        _llShowAllSeenOnCast = (LinearLayout) findViewById(R.id.llShowAllSeenOnCast);
        _llShowAllSeenOnCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonDisplayActivity.this, FullPersonSeenOnCastActivity.class);
                intent.putExtra(UIConsts.Bundles.PERSON_SEEN_ON_CAST_LIST_KEY, _person.getSeenOnCast());
                intent.putExtra(UIConsts.Bundles.PERSON_NAME_KEY, _person.getName());
                startActivityForResult(intent, UIConsts.RequestCodes.FULL_PERSON_SEEN_ON_CAST_REQUEST_CODE);
                PersonDisplayActivity.this.overridePendingTransition(R.anim.go_right_enter, R.anim.go_right_exit);
            }
        });

        _lblSeenOnCastMessage = (TextView) findViewById(R.id.lblSeenOnCastMessage);

        _rvSeenOnCast = (RecyclerView) findViewById(R.id.rvSeenOnCast);
        _rvSeenOnCast.setHasFixedSize(true);
        _rvSeenOnCast.setNestedScrollingEnabled(false);
        _rvSeenOnCast.setLayoutManager(new LinearLayoutManager(this));
        _adapterCast = new PersonSeenOnCastAdapter(_moviesSeenOnCast, this);
        _rvSeenOnCast.setAdapter(_adapterCast);

        //endregion

        //region Seen On (Crew) Controls

        _progressSeenOnCrew = (ProgressBar) findViewById(R.id.progressSeenOnCrew);

        _llShowAllSeenOnCrew = (LinearLayout) findViewById(R.id.llShowAllSeenOnCrew);
        _llShowAllSeenOnCrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonDisplayActivity.this, FullPersonSeenOnCrewActivity.class);
                intent.putExtra(UIConsts.Bundles.PERSON_SEEN_ON_CREW_LIST_KEY, _person.getSeenOnCrew());
                intent.putExtra(UIConsts.Bundles.PERSON_NAME_KEY, _person.getName());
                startActivityForResult(intent, UIConsts.RequestCodes.FULL_PERSON_SEEN_ON_CREW_REQUEST_CODE);
                PersonDisplayActivity.this.overridePendingTransition(R.anim.go_right_enter, R.anim.go_right_exit);
            }
        });

        _lblSeenOnCrewMessage = (TextView) findViewById(R.id.lblSeenOnCrewMessage);

        _rvSeenOnCrew = (RecyclerView) findViewById(R.id.rvSeenOnCrew);
        _rvSeenOnCrew.setHasFixedSize(true);
        _rvSeenOnCrew.setNestedScrollingEnabled(false);
        _rvSeenOnCrew.setLayoutManager(new LinearLayoutManager(this));
        _adapterCrew = new PersonSeenOnCrewAdapter(_moviesSeenOnCrew, this);
        _rvSeenOnCrew.setAdapter(_adapterCrew);

        //endregion

        if (savedInstanceState == null) {
            // Checks if the person has a TMDB Id to get the full person information:
            if (_person.getTMDBId() != null) {
                if (!_person.isExtraInfoLoaded())
                    this.loadFullPersonInformation();
            } else {
                _progressSeenOnCast.setVisibility(View.GONE);
                _llShowAllSeenOnCast.setVisibility(View.GONE);
                _lblSeenOnCastMessage.setText(R.string.general_msg_nothing_to_show_here);
                _lblSeenOnCastMessage.setVisibility(View.VISIBLE);

                _progressSeenOnCrew.setVisibility(View.GONE);
                _llShowAllSeenOnCrew.setVisibility(View.GONE);
                _lblSeenOnCrewMessage.setText(R.string.general_msg_nothing_to_show_here);
                _lblSeenOnCrewMessage.setVisibility(View.VISIBLE);
            }
        }

    }

    /**
     * Displays the state of the adapter (list / empty message), according to the items in the list.
     */
    private void displayListStateSeenOnCast() {
        _progressSeenOnCast.setVisibility(View.GONE);

        if (this._moviesSeenOnCast.size() < 1) {
            _rvSeenOnCast.setVisibility(View.GONE);
            _llShowAllSeenOnCast.setVisibility(View.GONE);
            _lblSeenOnCastMessage.setText(R.string.general_msg_nothing_to_show_here);
            _lblSeenOnCastMessage.setVisibility(View.VISIBLE);
        } else {
            _lblSeenOnCastMessage.setVisibility(View.GONE);
            _llShowAllSeenOnCast.setVisibility(View.VISIBLE);
            _rvSeenOnCast.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Displays the state of the adapter (list / empty message), according to the items in the list.
     */
    private void displayListStateSeenOnCrew() {
        _progressSeenOnCrew.setVisibility(View.GONE);

        if (this._moviesSeenOnCrew.size() < 1) {
            _rvSeenOnCrew.setVisibility(View.GONE);
            _llShowAllSeenOnCrew.setVisibility(View.GONE);
            _lblSeenOnCrewMessage.setText(R.string.general_msg_nothing_to_show_here);
            _lblSeenOnCrewMessage.setVisibility(View.VISIBLE);
        } else {
            _lblSeenOnCrewMessage.setVisibility(View.GONE);
            _llShowAllSeenOnCrew.setVisibility(View.VISIBLE);
            _rvSeenOnCrew.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Loads the "Full Person Information" from the TMDB service.
     */
    public void loadFullPersonInformation() {
        getSupportLoaderManager().initLoader(PERSON_INFORMATION_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Person>() {
            @Override
            public Loader<Person> onCreateLoader(int id, Bundle args) {
                return new FullPersonInformationLoader(getApplicationContext(), _person.getTMDBId());
            }

            @Override
            public void onLoadFinished(Loader<Person> loader, Person data) {
                if (data == null) {
                    Exception exception = ((FullPersonInformationLoader) loader).getException();
                    if (exception != null) {
                        if (exception instanceof NoNetworkException) {
                            _progressSeenOnCast.setVisibility(View.GONE);
                            _llShowAllSeenOnCast.setVisibility(View.GONE);
                            _lblSeenOnCastMessage.setText(R.string.general_msg_no_network_connection);
                            _lblSeenOnCastMessage.setVisibility(View.VISIBLE);

                            _progressSeenOnCrew.setVisibility(View.GONE);
                            _llShowAllSeenOnCrew.setVisibility(View.GONE);
                            _lblSeenOnCrewMessage.setText(R.string.general_msg_no_network_connection);
                            _lblSeenOnCrewMessage.setVisibility(View.VISIBLE);

                            Toast.makeText(PersonDisplayActivity.this, R.string.general_msg_no_network_connection, Toast.LENGTH_SHORT).show();
                        } else {
                            _progressSeenOnCast.setVisibility(View.GONE);
                            _llShowAllSeenOnCast.setVisibility(View.GONE);
                            _lblSeenOnCastMessage.setText(R.string.general_msg_something_went_wrong);
                            _lblSeenOnCastMessage.setVisibility(View.VISIBLE);

                            _progressSeenOnCrew.setVisibility(View.GONE);
                            _llShowAllSeenOnCrew.setVisibility(View.GONE);
                            _lblSeenOnCrewMessage.setText(R.string.general_msg_something_went_wrong);
                            _lblSeenOnCrewMessage.setVisibility(View.VISIBLE);

                            Toast.makeText(PersonDisplayActivity.this, R.string.general_msg_something_went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                    return;
                }

                //region Builds the preview sections: Seen on Cast & Crew...

                _moviesSeenOnCast.clear();
                for (int i = 0; i < data.getSeenOnCast().size(); i++) {
                    if (i == MAX_ITEMS_IN_SEEN_ON_SECTIONS)
                        break;
                    _moviesSeenOnCast.add(data.getSeenOnCast().get(i));
                }

                _moviesSeenOnCrew.clear();
                for (int i = 0; i < data.getSeenOnCrew().size(); i++) {
                    if (i == MAX_ITEMS_IN_SEEN_ON_SECTIONS)
                        break;
                    _moviesSeenOnCrew.add(data.getSeenOnCrew().get(i));
                }

                //endregion

                //region Sets dynamic properties of the person...

                data.setId(_person.getId());
                data.setFolder(_person.getFolder());
                data.setFavorite(_person.isFavorite());
                data.setTimeAdded(_person.getTimeAdded());

                //endregion

                data.setExtraInfoLoaded(true);
                _person = data;

                //region Update UI Controls

                _lblPersonAge.setText(_person.displayAge());
                _lblPersonPlaceOfBirth.setText(_person.getPlaceOfBirth());
                _lblPersonBio.setText(_person.getBiography());

                if (_person.getFacebook() != null) {
                    _imgbtnFacebook.setEnabled(true);
                    _imgbtnFacebook.setColorFilter(Utils.Colors.FACEBOOK);
                } else {
                    _imgbtnFacebook.setColorFilter(Color.rgb(80, 80, 80));
                    _imgbtnFacebook.setEnabled(false);
                }
                if (_person.getTwitter() != null) {
                    _imgbtnTwitter.setEnabled(true);
                    _imgbtnTwitter.setColorFilter(Utils.Colors.TWITTER);
                } else {
                    _imgbtnTwitter.setColorFilter(Color.rgb(80, 80, 80));
                    _imgbtnTwitter.setEnabled(false);
                }
                if (_person.getInstagram() != null) {
                    _imgbtnInstagram.setEnabled(true);
                    _imgbtnInstagram.setColorFilter(Color.WHITE);
                } else {
                    _imgbtnInstagram.setColorFilter(Color.rgb(80, 80, 80));
                    _imgbtnInstagram.setEnabled(false);
                }

                if (_person.getHomepage() != null) {
                    _btnWebsite.setEnabled(true);
                    _btnWebsite.setTextColor(Color.parseColor("#cccccc"));
                } else {
                    _btnWebsite.setTextColor(Color.parseColor("#444444"));
                    _btnWebsite.setEnabled(false);
                }

                _adapterCast.notifyDataSetChanged();
                displayListStateSeenOnCast();
                _adapterCrew.notifyDataSetChanged();
                displayListStateSeenOnCrew();

                //endregion
            }

            @Override
            public void onLoaderReset(Loader<Person> loader) {
            }
        }).forceLoad();
    }

    /**
     * Saves the person image to the internal storage.
     */
    private void savePersonImageToStorage() {
        if (_person.getProfilePath() == null)
            return;

        Glide.with(this)
                .load(TMDBService.Urls.GET_PHOTO_BY_PATH + _person.getProfilePath())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        // Here the person image can be saved, so saves it to the internal storage:
                        Utils.Storage.saveToInternalStorage(bitmap, _person.getProfilePath(), getApplicationContext());
                    }
                });
    }

    //endregion

}
