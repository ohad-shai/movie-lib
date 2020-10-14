package com.ohadshai.movielib.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ohadshai.movielib.R;
import com.ohadshai.movielib.adapters.MovieGenresAdapter;
import com.ohadshai.movielib.db.DBHandler;
import com.ohadshai.movielib.entities.Genre;
import com.ohadshai.movielib.entities.Movie;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.utils.Cache;
import com.ohadshai.movielib.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class MovieCreateActivity extends AppCompatActivity {

    //region Private Members

    /**
     * Holds the database interactions object.
     */
    private DBHandler _repository;

    /**
     * Holds the _mode of the create _movie activity (whether it's create, edit, etc...).
     */
    private int _mode;

    /**
     * Holds the _movie object.
     */
    private Movie _movie;

    /**
     * Holds the View control for clearing any focus of control in the activity.
     */
    private View _clearFocus;

    /**
     * Holds the Button control for the _movie add image button.
     */
    private Button _btnAddImage;

    /**
     * Holds the ImageView control for the _movie image.
     */
    private ImageView _imgMovie;

    /**
     * Holds the {@link Bitmap} for the loaded image.
     */
    private Bitmap _selectedImage;

    /**
     * Holds an indicator indicating whether an image is loaded or not.
     */
    private boolean _isImageLoaded = false;

    /**
     * Holds the EditText control for the _movie title.
     */
    private EditText _txtMovieTitle;

    /**
     * Holds the EditText control for the _movie plot.
     */
    private EditText _txtMoviePlot;

    /**
     * Holds the EditText control for the _movie year.
     */
    private EditText _txtMovieYear;

    /**
     * Holds the RatingBar control for the _movie user rating.
     */
    private RatingBar _ratingUser;

    /**
     * Holds the TextView control for the _movie IMDb rating.
     */
    private TextView _lblIMDbRating;

    /**
     * Holds the ImageButton control for the _movie favorite button.
     */
    private ImageButton _imgbtnFavoriteMovie;

    /**
     * Holds the ImageButton control for the _movie watched button.
     */
    private ImageButton _imgbtnIsWatched;

    //region (Movie Genres) Members

    /**
     * Holds the list of all the genres available.
     */
    private ArrayList<Genre> _genres = new ArrayList<>();

    /**
     * Holds the Button control for the no genres button (add genre).
     */
    private Button _btnNoGenres;

    /**
     * Holds the RecyclerView control for the list of _movie genres.
     */
    private RecyclerView _rvGenres;

    /**
     * Holds the adapter of the _movie genres.
     */
    private MovieGenresAdapter _adapterGenres;

    //endregion

    //endregion

    //region Activity Events

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Adds the "Navigate Up to Parent Activity" icon.
        this.initControls();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_general_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionDone:
                this.actionSaveMovie();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.context_menu_general_add_image, menu);

        if (v == _btnAddImage)
            menu.setHeaderTitle(getString(R.string.general_add_image));
        else if (v == _imgMovie)
            menu.setHeaderTitle(getString(R.string.general_change_image));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionDefault:
                this.addImageActionDefault();
                return true;
            case R.id.actionFromGallery:
                Utils.Intents.gallery(this, UIConsts.RequestCodes.IMAGE_GALLERY_REQUEST_CODE);
                return true;
            case R.id.actionCamera:
                Utils.Intents.camera(this, UIConsts.RequestCodes.IMAGE_CAMERA_REQUEST_CODE);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == UIConsts.RequestCodes.IMAGE_GALLERY_REQUEST_CODE || requestCode == UIConsts.RequestCodes.IMAGE_CAMERA_REQUEST_CODE) && resultCode == RESULT_OK && data != null) {
            if (_movie.getPosterPath() != null)
                Utils.Storage.deleteFromInternalStorage(_movie.getPosterPath(), getApplicationContext()); // Deletes the existed image if found.

            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                _selectedImage = BitmapFactory.decodeStream(imageStream);
                _imgMovie.setImageBitmap(Utils.Bitmaps.scaleCenterCrop(_selectedImage, (int) getResources().getDimension(R.dimen.image_xlarge_height), (int) getResources().getDimension(R.dimen.image_xlarge_width)));
                _btnAddImage.setVisibility(View.GONE);
                _isImageLoaded = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MovieCreateActivity.this, getString(R.string.general_msg_something_went_wrong), Toast.LENGTH_LONG).show();
            }
        }
    }

    //endregion

    //region Public API

    /**
     * Displays the state of the adapter (list / empty message), according to the items in the list.
     */
    public void displayMovieGenresListState() {
        if (_movie.getGenres().size() < 1) {
            _rvGenres.setVisibility(View.GONE);
            _btnNoGenres.setVisibility(View.VISIBLE);
        } else {
            _btnNoGenres.setVisibility(View.GONE);
            _rvGenres.setVisibility(View.VISIBLE);
        }
    }

    //endregion

    //region Private Methods

    /**
     * Initializes all view controls.
     */
    private void initControls() {

        _repository = DBHandler.getInstance(this);

        _movie = new Movie();
        _movie.setGenres(new ArrayList<Genre>());
        _mode = UIConsts.Bundles.MODE.CREATE;

        int folder = getIntent().getIntExtra(UIConsts.Bundles.MOVIE_FOLDER_KEY, -1);
        if (folder == Movie.Folders.WISHLIST || folder == Movie.Folders.MY_MOVIES)
            _movie.setFolder(folder);
        else if (folder != -1)
            throw new IllegalStateException("Invalid value on the provided key MOVIE_FOLDER_KEY.");

        _genres = Cache.getOtherwisePut(Cache.Keys.GENRES_LIST_FROM_DB, _repository.genres.getAll()); // Gets all the genres in the database.

        _clearFocus = findViewById(R.id.clearFocus);

        _imgbtnFavoriteMovie = (ImageButton) findViewById(R.id.imgbtnFavoriteMovie);
        _imgbtnFavoriteMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAnyFocus();
                if (_movie.isFavorite()) {
                    _movie.setFavorite(false);
                    _imgbtnFavoriteMovie.setColorFilter(Color.BLACK);
                } else {
                    _movie.setFavorite(true);
                    _imgbtnFavoriteMovie.setColorFilter(Utils.Colors.FAVORITE);
                }
            }
        });
        _imgbtnFavoriteMovie.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utils.UI.showInformationToast(_imgbtnFavoriteMovie, R.string.movie_favorite);
                return true;
            }
        });

        _imgbtnIsWatched = (ImageButton) findViewById(R.id.imgbtnIsWatched);
        _imgbtnIsWatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAnyFocus();
                if (_movie.isWatched()) {
                    _movie.setWatched(false);
                    _imgbtnIsWatched.setColorFilter(Color.BLACK);
                } else {
                    _movie.setWatched(true);
                    _imgbtnIsWatched.setColorFilter(Utils.Colors.WATCHED);
                }
            }
        });
        _imgbtnIsWatched.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utils.UI.showInformationToast(_imgbtnFavoriteMovie, R.string.movie_watched);
                return true;
            }
        });
        if (folder == Movie.Folders.WISHLIST)
            _imgbtnIsWatched.setVisibility(View.GONE);

        _txtMovieTitle = (EditText) findViewById(R.id.txtMovieTitle);

        _txtMoviePlot = (EditText) findViewById(R.id.txtMoviePlot);

        _txtMovieYear = (EditText) findViewById(R.id.txtMovieYear);

        _lblIMDbRating = (TextView) findViewById(R.id.lblVoteAverage);

        _ratingUser = (RatingBar) findViewById(R.id.ratingUser);
        if (folder == Movie.Folders.WISHLIST) {
            _ratingUser.setVisibility(View.GONE);
            findViewById(R.id.rlMovieRating).setVisibility(View.INVISIBLE);
        }

        _imgMovie = (ImageView) findViewById(R.id.imgMovie);
        _imgMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAnyFocus();
                _imgMovie.performLongClick(); // Opens the ContextMenu.
            }
        });
        registerForContextMenu(_imgMovie);

        _btnAddImage = (Button) findViewById(R.id.btnAddImage);
        _btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAnyFocus();
                _btnAddImage.performLongClick(); // Opens the ContextMenu.
            }
        });
        registerForContextMenu(_btnAddImage);
        _btnAddImage.setVisibility(View.VISIBLE);

        // For edit mode --> gets the movie from the DB and sets it to the views:
        int movieEdit = getIntent().getIntExtra(UIConsts.Bundles.MOVIE_ID_KEY, -1);
        if (movieEdit != -1) {
            _movie = _repository.movies.getById(movieEdit);
            _mode = UIConsts.Bundles.MODE.UPDATE;
            setTitle(getString(R.string.movie_edit_activity_title)); // If in edit _mode, then changes the title to "Edit Movie".
            this.setMovieToViews(_movie);
        }

        //region (Movie Genres) Controls

        Button btnAddGenre = (Button) findViewById(R.id.btnAddGenre);
        btnAddGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionAddGenre();
            }
        });

        _btnNoGenres = (Button) findViewById(R.id.btnNoGenres);
        _btnNoGenres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionAddGenre();
            }
        });

        _rvGenres = (RecyclerView) findViewById(R.id.rvGenres);
        _adapterGenres = new MovieGenresAdapter(_movie.getGenres(), this); // Initializes the adapter.
        _rvGenres.setAdapter(_adapterGenres); // Attaches the adapter to the RecyclerView to populate items.
        _rvGenres.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); // Sets a layout manager to position the items.
        this.displayMovieGenresListState(); // Checks if to display "No Genres" when the list is empty.

        //endregion

    }

    /**
     * Sets a _movie object to the activity views.
     *
     * @param movie The _movie object to set.
     */
    private void setMovieToViews(Movie movie) {
        if (movie.isFavorite()) {
            _imgbtnFavoriteMovie.setColorFilter(Utils.Colors.FAVORITE);
        } else {
            _imgbtnFavoriteMovie.setColorFilter(Color.BLACK);
        }
        if (movie.isWatched()) {
            _imgbtnIsWatched.setColorFilter(Utils.Colors.WATCHED);
        } else {
            _imgbtnIsWatched.setColorFilter(Color.BLACK);
        }

        //region Movie Storage Load

        if (_mode == UIConsts.Bundles.MODE.UPDATE) {
            File imageFile = null;
            if (movie.getPosterPath() != null) {
                imageFile = new File(Utils.Storage.getInternalStoragePath(getApplicationContext()), movie.getPosterPath());
                _isImageLoaded = true;
            }

            Glide.with(getApplicationContext()).load(imageFile).placeholder(R.drawable.no_movie_image)
                    .override((int) getResources().getDimension(R.dimen.image_xlarge_width), (int) getResources().getDimension(R.dimen.image_xlarge_height))
                    .centerCrop()
                    .into(_imgMovie);

            _btnAddImage.setVisibility(View.GONE);
        }

        //endregion

        _txtMovieTitle.setText(movie.getTitle());
        _txtMoviePlot.setText(movie.getPlot());
        if (movie.getReleaseDate() != null)
            _txtMovieYear.setText(String.valueOf(movie.getReleaseDate().get(Calendar.YEAR)));
        _ratingUser.setRating(movie.displayRating());
        if (movie.getVoteAverage() == null)
            _lblIMDbRating.setText(getString(R.string.general_msg_unavailable));
        else
            _lblIMDbRating.setText(String.valueOf(movie.getVoteAverage()));

        // NOTE: No need to set "movie genres" and "movie actors" lists, because in the "initControls()" method, they're going to be initialized anyway.
    }

    /**
     * Method procedure for the "Add Genre" action.
     */
    private void actionAddGenre() {
        this.clearAnyFocus();

        if (_movie.getGenres() == null)
            _movie.setGenres(new ArrayList<Genre>());

        // Displays an AlertDialog with a checkbox list, to let the user choose the movie genres:

        // Creates the array to populate the dialog checkbox list:
        final CharSequence[] items = new CharSequence[_genres.size()];
        for (int i = 0; i < _genres.size(); i++)
            items[i] = _genres.get(i).getName();
        final boolean[] checked = new boolean[_genres.size()];
        final ArrayList<Integer> selection = new ArrayList<>(); // Holds the selection of items (checkbox) in the list.
        for (int i = 0; i < _genres.size(); i++) {
            if (Genre.isListContainsGenreName(_genres.get(i).getName(), _movie.getGenres())) {
                checked[i] = true;
                selection.add(i);
            } else {
                checked[i] = false;
            }
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.movie_create_select_movie_genres)
                .setMultiChoiceItems(items, checked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked)
                            selection.add(indexSelected); // If the user checked the item, adds it to the selection.
                        else if (selection.contains(indexSelected))
                            selection.remove(Integer.valueOf(indexSelected)); // If the item is already in the array, removes it from the selection.
                    }
                }).setPositiveButton(getString(R.string.general_btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        _movie.getGenres().clear();

                        for (int position : selection)
                            _movie.getGenres().add(_genres.get(position));

                        _adapterGenres.notifyDataSetChanged();
                        displayMovieGenresListState();
                    }
                }).setNegativeButton(getString(R.string.general_btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * Method procedure for the "Add Storage" ContextMenu action: "Default".
     */
    private void addImageActionDefault() {
        if (_movie.getPosterPath() != null)
            Utils.Storage.deleteFromInternalStorage(_movie.getPosterPath(), getApplicationContext()); // Deletes the existed image if found.

        _isImageLoaded = false;
        _movie.setPosterPath(null);
        Glide.with(getApplicationContext()).load(R.drawable.no_movie_image).into(_imgMovie);
        _btnAddImage.setVisibility(View.GONE);
    }

    /**
     * Clears any control's focus in the activity.
     */
    private void clearAnyFocus() {
        _clearFocus.requestFocus();
        Utils.UI.hideSoftKeyboard(this, _clearFocus);
    }

    /**
     * Method procedure for menu action: "Done" (Save Movie).
     */
    private void actionSaveMovie() {
        this.clearAnyFocus();

        String movieTitle = _txtMovieTitle.getText().toString().trim();
        String moviePlot = _txtMoviePlot.getText().toString();
        String movieYear = _txtMovieYear.getText().toString().trim();

        //region Validations

        if (movieTitle.equals("")) {
            Toast.makeText(MovieCreateActivity.this, getString(R.string.movie_validation_title_empty), Toast.LENGTH_SHORT).show();
            return;
        } else if (movieYear.equals("")) {
            Toast.makeText(MovieCreateActivity.this, getString(R.string.movie_validation_year_empty), Toast.LENGTH_SHORT).show();
            return;
        } else if (!Utils.Strings.isNumeric(movieYear)) {
            Toast.makeText(MovieCreateActivity.this, getString(R.string.movie_validation_year_numeric), Toast.LENGTH_SHORT).show();
            return;
        }

        //endregion

        // Saves the _movie:
        _movie.setTitle(movieTitle);
        _movie.setPlot(moviePlot);

        _movie.setReleaseDate(Calendar.getInstance());
        _movie.getReleaseDate().setTimeInMillis(0);
        _movie.getReleaseDate().set(Calendar.YEAR, Integer.valueOf(movieYear));
        _movie.setRating((int) (_ratingUser.getRating() + _ratingUser.getRating()));

        // Checks if an image is loaded, in order to save the image to the movie poster path:
        if (_isImageLoaded) {
            // Checks if in Edit _mode, and there's an image to replace:
            if (_mode == UIConsts.Bundles.MODE.UPDATE && _movie.getPosterPath() != null)
                Utils.Storage.deleteFromInternalStorage(_movie.getPosterPath(), getApplicationContext()); // Deletes the existed image.

            if (_selectedImage != null) {
                String fileName = Calendar.getInstance().getTimeInMillis() + ".jpg";
                Utils.Storage.saveToInternalStorage(_selectedImage, fileName, getApplicationContext()); // Saves the movie image to internal storage, and saves the path in the movie object.
                _movie.setPosterPath(fileName);
            }
        }

        // Checks if it's an update mode:
        if (_mode == UIConsts.Bundles.MODE.UPDATE) {
            _repository.movies.update(_movie);
        } else {
            _movie.setTimeAdded(Calendar.getInstance());
            _repository.movies.create(_movie);
        }

        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    //endregion

}
