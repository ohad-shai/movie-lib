package com.ohadshai.movielib.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ohadshai.movielib.R;
import com.ohadshai.movielib.adapters.MovieCastAdapter;
import com.ohadshai.movielib.adapters.MovieCrewAdapter;
import com.ohadshai.movielib.db.DBHandler;
import com.ohadshai.movielib.entities.Movie;
import com.ohadshai.movielib.entities.PersonInMovieCast;
import com.ohadshai.movielib.entities.PersonInMovieCrew;
import com.ohadshai.movielib.loaders.FullMovieInformationLoader;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.utils.NoNetworkException;
import com.ohadshai.movielib.utils.RevealHelper;
import com.ohadshai.movielib.utils.Utils;
import com.ohadshai.movielib.utils.web_services.IMDbConsts;
import com.ohadshai.movielib.utils.web_services.tmdb_api.TMDBService;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import static com.ohadshai.movielib.R.id.lblMoviePlot;
import static com.ohadshai.movielib.R.id.lblVoteAverage;

public class MovieDisplayActivity extends AppCompatActivity {

    //region Constants

    /**
     * Holds a constant for the {@link FullMovieInformationLoader} loader id.
     */
    private static final int MOVIE_INFORMATION_LOADER_ID = 173281;

    /**
     * Holds a constant for the maximum people in a preview section like: Cast, or Crew.
     */
    private static final int MAX_PEOPLE_IN_PREVIEW_SECTION = 4;

    /**
     * Holds a constant for the save instance state key: Trailer Url.
     */
    private static final String TRAILER_URL_KEY = "trailer_url_key";

    /**
     * Holds a constant for the save instance state key: Trailer Seek.
     */
    private static final String TRAILER_SEEK_KEY = "trailer_seek_key";

    //endregion

    //region Private Members

    /**
     * Holds the database interactions object.
     */
    private DBHandler _repository;

    /**
     * Holds the _movie object.
     */
    private Movie _movie;

    /**
     * Holds the {@link ImageView} for the movie poster.
     */
    private ImageView _imgMovie;

    //region [Trailer Members]

    /**
     * Holds the URL of the movie trailer.
     */
    private String _trailerUrl;

    /**
     * Holds the VideoView control for the _movie trailer.
     */
    private VideoView _videoTrailer;

    /**
     * Holds the MediaController control for the _videoTrailer.
     */
    private MediaController _videoController;

    /**
     * Holds the view control for the trailer layout.
     */
    private RelativeLayout _layoutTrailer;

    /**
     * Holds the view control for the trailer progress bar.
     */
    private ProgressBar _progressTrailer;

    /**
     * Holds the view control for the trailer play layout.
     */
    private RelativeLayout _rlTrailerPlay;

    //endregion

    /**
     * Holds an indicator indicating whether if returned from a section like: Cast / Crew, or not.
     */
    private boolean _isReturnedFromSection = false;

    /**
     * Holds the {@link TextView} control for the movie genres.
     */
    private TextView _lblMovieGenres;

    /**
     * Holds the {@link TextView} control for the movie plot.
     */
    private TextView _lblMoviePlot;

    /**
     * Holds the {@link TextView} control for the movie vote average.
     */
    private TextView _lblVoteAverage;

    //region [Cast Members]

    /**
     * Holds the list of persons in the movie cast.
     */
    private ArrayList<PersonInMovieCast> _personsInMovieCast = new ArrayList<>();

    /**
     * Holds the {@link RecyclerView} control for the cast list.
     */
    private RecyclerView _rvCast;

    /**
     * Holds the movie cast adapter for the {@link RecyclerView}.
     */
    private MovieCastAdapter _adapterCast;

    /**
     * Holds the {@link ProgressBar} control for the cast loading.
     */
    private ProgressBar _progressCast;

    /**
     * Holds the {@link LinearLayout} control to show all the movie cast.
     */
    private LinearLayout _llShowAllCast;

    /**
     * Holds the {@link TextView} control for the movie cast state message.
     */
    private TextView _lblCastMessage;

    //endregion

    //region [Crew Members]

    /**
     * Holds the list of persons in the movie crew.
     */
    private ArrayList<PersonInMovieCrew> _personsInMovieCrew = new ArrayList<>();

    /**
     * Holds the {@link RecyclerView} control for the crew list.
     */
    private RecyclerView _rvCrew;

    /**
     * Holds the movie crew adapter for the {@link RecyclerView}.
     */
    private MovieCrewAdapter _adapterCrew;

    /**
     * Holds the {@link ProgressBar} control for the crew loading.
     */
    private ProgressBar _progressCrew;

    /**
     * Holds the {@link LinearLayout} control to show all the movie crew.
     */
    private LinearLayout _llShowAllCrew;

    /**
     * Holds the {@link TextView} control for the movie crew state message.
     */
    private TextView _lblCrewMessage;

    //endregion

    /**
     * Holds the {@link RevealHelper} for the "_movie add".
     */
    private RevealHelper _revealMovieAdd;

    /**
     * Holds an indicator indicating whether the auto-play trailers is enabled or not.
     */
    private boolean _isAutoplayTrailerEnabled;

    //endregion

    //region Activity Events

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_display);
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
                _revealMovieAdd.setRevealFrom(findViewById(R.id.flAddMovieContainer).getWidth() - (findViewById(R.id.actionAdd).getWidth() / 2), 0).toggle();
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

        if (requestCode == UIConsts.RequestCodes.FULL_MOVIE_CAST_REQUEST_CODE || requestCode == UIConsts.RequestCodes.FULL_MOVIE_CREW_REQUEST_CODE) {
            _isReturnedFromSection = true;
            if (_trailerUrl != null && _isAutoplayTrailerEnabled)
                this.loadTrailer(_trailerUrl);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        _videoTrailer.stopPlayback();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Saves the movie object:
        outState.putParcelable(UIConsts.Bundles.MOVIE_KEY, _movie);
        outState.putString(TRAILER_URL_KEY, _trailerUrl);
        outState.putInt(TRAILER_SEEK_KEY, (_videoTrailer != null ? _videoTrailer.getCurrentPosition() : 0));

        _revealMovieAdd.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState == null)
            return;

        // Restores the movie object:
        _trailerUrl = savedInstanceState.getString(TRAILER_URL_KEY, null);
        int trailerSeek = savedInstanceState.getInt(TRAILER_SEEK_KEY, 0);
        Movie movie = savedInstanceState.getParcelable(UIConsts.Bundles.MOVIE_KEY);
        if (movie != null) {
            _movie = movie;

            if (_movie.isExtraInfoLoaded() && _movie.getIMDbId() != null) {

                //region Builds the preview sections: Cast & Crew...

                _personsInMovieCast.clear();
                for (int i = 0; i < movie.getCast().size(); i++) {
                    if (i == MAX_PEOPLE_IN_PREVIEW_SECTION)
                        break;
                    _personsInMovieCast.add(movie.getCast().get(i));
                }

                _personsInMovieCrew.clear();
                for (int i = 0; i < movie.getCrew().size(); i++) {
                    if (i == MAX_PEOPLE_IN_PREVIEW_SECTION)
                        break;
                    _personsInMovieCrew.add(movie.getCrew().get(i));
                }

                _adapterCast.notifyDataSetChanged();
                this.displayListStateCast();
                _adapterCrew.notifyDataSetChanged();
                this.displayListStateCrew();

                //endregion

                if (_isAutoplayTrailerEnabled) {
                    // Checks if there's a trailer url:
                    if (_trailerUrl != null) {
                        _layoutTrailer.setVisibility(View.VISIBLE);
                        _progressTrailer.setVisibility(View.VISIBLE);
                        loadTrailer(_trailerUrl);
                        _videoTrailer.seekTo(trailerSeek);
                    } else {
                        // Gets the trailer url and load the trailer:
                        new Task_DisplayTrailer().execute(_movie.getIMDbId());
                    }
                } else {
                    _layoutTrailer.setVisibility(View.VISIBLE);
                    _rlTrailerPlay.setVisibility(View.VISIBLE);
                }
            }
            // If the full movie information not loaded:
            else {
                // First, checks if there's IMDb Id, means only cast & crew needs to be loaded:
                if (_movie.getIMDbId() != null) {
                    if (!_movie.isExtraInfoLoaded())
                        this.loadFullMovieInformation();
                }
                // Second, checks if the movie has a TMDB Id to get the full movie information:
                else if (_movie.getTMDBId() != null) {
                    if (!_movie.isExtraInfoLoaded())
                        this.loadFullMovieInformation();
                }
                // If the movie is added manually:
                else {
                    _layoutTrailer.setVisibility(View.GONE);
                    _progressTrailer.setVisibility(View.GONE);

                    _progressCast.setVisibility(View.GONE);
                    _llShowAllCast.setVisibility(View.GONE);
                    _lblCastMessage.setText(R.string.movie_no_cast);
                    _lblCastMessage.setVisibility(View.VISIBLE);

                    _progressCrew.setVisibility(View.GONE);
                    _llShowAllCrew.setVisibility(View.GONE);
                    _lblCrewMessage.setText(R.string.movie_no_crew);
                    _lblCrewMessage.setVisibility(View.VISIBLE);
                }
            }
        }

        _revealMovieAdd.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (_revealMovieAdd.onBackPress())
            return;

        super.onBackPressed();
    }

    //endregion

    //region Private Static Methods

    /**
     * Gets the IMDb video id from the IMDb movie page html.
     *
     * @param html The html of the IMDb movie page.
     * @return Returns the video id if found, otherwise null.
     */
    private static String getVideoIdFromIMDbHtml(String html) {
        StringBuilder sb = new StringBuilder();
        int keyIdx = -1; // Holds the index of the key to detect a video in the "IMDb" site.

        keyIdx = html.indexOf(IMDbConsts.IMDB_MOVIE_PAGE_VIDEO_ID_KEY); // Gets the index of the video id key.
        if (keyIdx != -1) {
            keyIdx += IMDbConsts.IMDB_MOVIE_PAGE_VIDEO_ID_KEY.length();

            // Runs for a loop on each character of the video player url (from the index of the key - to the right):
            for (int i = keyIdx; i < html.length(); i++) {
                char current = html.charAt(i); // Gets the current char.

                // Checks for closing video id:
                if (current == '\"' || current == '?')
                    break;

                sb.append(current); // Appends the current char to build the trailer url.
            }
            return sb.toString();
        }

        return null;
    }

    /**
     * Gets the IMDb trailer url from the IMDb video player page html.
     *
     * @param html      The html of the IMDb video player page.
     * @return Returns the trailer url if found, otherwise null.
     */
    private static String getTrailerUrlFromVideoPlayerHtml(String html) {
        StringBuilder sb = new StringBuilder();

        // Gets the index of the key to detect the video URL in the "IMDb" video player:
        int keyIdx = html.indexOf("videoUrl");
        if (keyIdx != -1) {
            keyIdx += 8; // Skips the length of "videoUrl" (8 chars).
            int quotationMarkCount = 0; // Needs to count 2 quotation mark, in order to get to the value.

            // Runs for a loop on each character of the trailer url (from the index of the key - to the right):
            for (int i = keyIdx; i < html.length(); i++) {
                char current = html.charAt(i); // Gets the current char.

                // Checks if currently in the value:
                // NOTE: 2 quotation mark is needed to get to the value in the JSON:
                if (quotationMarkCount >= 2) {
                    // Checks for closing trailer url:
                    if (current == '\"')
                        break;

                    sb.append(current); // Appends the current char to build the trailer url.
                }

                // Checks for the value start:
                if (current == '\"') {
                    quotationMarkCount++;
                }
            }
            return sb.toString();
        }

        return null;
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

        //region Movie object...

        // First, checks if to display the movie from the DB:
        final int movieId = getIntent().getIntExtra(UIConsts.Bundles.MOVIE_ID_KEY, -1);

        // Second, displays the provided movie from memory:
        Movie providedMovie = getIntent().getParcelableExtra(UIConsts.Bundles.MOVIE_KEY);

        if (movieId > 0) {
            _movie = _repository.movies.getById(movieId); // Gets the _movie object from the database.
        } else if (providedMovie != null) {
            _movie = providedMovie;

            // Checks if the movie is already in the database:
            Movie checkMovieInDB = _repository.movies.getByTMDBId(_movie.getTMDBId());
            if (checkMovieInDB != null)
                _movie = checkMovieInDB;
        } else {
            throw new IllegalStateException("MOVIE_ID_KEY or MOVIE_KEY not provided.");
        }

        //endregion

        setTitle(_movie.getTitle() + " " + _movie.displayYear());

        //region Movie Image Load

        _imgMovie = (ImageView) findViewById(R.id.imgMovie);
        if (_movie.getPosterPath() != null) {
            File imageFile = new File(Utils.Storage.getInternalStoragePath(getApplicationContext()), _movie.getPosterPath());

            if (imageFile.exists()) {
                Glide.with(getApplicationContext()).load(imageFile)
                        .placeholder(R.drawable.no_movie_image)
                        .override((int) getResources().getDimension(R.dimen.image_large_width), (int) getResources().getDimension(R.dimen.image_large_height))
                        .centerCrop()
                        .into(_imgMovie);
            } else {
                Glide.with(getApplicationContext()).load(TMDBService.Urls.GET_PHOTO_BY_PATH + "/" + _movie.getPosterPath())
                        .placeholder(R.drawable.no_movie_image)
                        .override((int) getResources().getDimension(R.dimen.image_large_width), (int) getResources().getDimension(R.dimen.image_large_height))
                        .centerCrop()
                        .into(_imgMovie);
            }
        } else {
            Glide.with(getApplicationContext()).load(R.drawable.no_movie_image)
                    .override((int) getResources().getDimension(R.dimen.image_large_width), (int) getResources().getDimension(R.dimen.image_large_height))
                    .centerCrop()
                    .into(_imgMovie);
        }

        //endregion

        TextView lblMovieTitle = (TextView) findViewById(R.id.lblMovieTitle);
        lblMovieTitle.setText(_movie.getTitle());

        TextView lblMovieYear = (TextView) findViewById(R.id.lblMovieYear);
        lblMovieYear.setText(_movie.displayYear());

        _lblMovieGenres = (TextView) findViewById(R.id.lblMovieGenres);
        _lblMovieGenres.setText(_movie.displayGenres());

        final NestedScrollView scrollViewMoviePlot = (NestedScrollView) findViewById(R.id.scrollViewMoviePlot);
        scrollViewMoviePlot.setNestedScrollingEnabled(false); // Disables the ScrollView of the _movie plot at the beginning.

        _lblMoviePlot = (TextView) findViewById(lblMoviePlot);
        _lblMoviePlot.setText(_movie.getPlot());
        _lblMoviePlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggles the ScrollView enabled or disable to read the full plot of the _movie:
                if (scrollViewMoviePlot.isNestedScrollingEnabled()) {
                    _lblMoviePlot.setBackgroundResource(0);
                    scrollViewMoviePlot.setNestedScrollingEnabled(false);
                } else {
                    _lblMoviePlot.setBackgroundColor(Color.rgb(50, 50, 50));
                    scrollViewMoviePlot.setNestedScrollingEnabled(true);
                }
            }
        });

        _lblVoteAverage = (TextView) findViewById(lblVoteAverage);
        if (_movie.getVoteAverage() == null || _movie.getVoteAverage() <= 0)
            _lblVoteAverage.setText(R.string.general_msg_unavailable);
        else
            _lblVoteAverage.setText(String.valueOf(_movie.getVoteAverage()));

        final RatingBar ratingUser = (RatingBar) findViewById(R.id.ratingUser);
        ratingUser.setRating(_movie.displayRating());
        ratingUser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (!fromUser)
                    return;

                if (_movie.getFolder() == null || _movie.getFolder() != Movie.Folders.MY_MOVIES) {
                    Toast.makeText(MovieDisplayActivity.this, R.string.general_msg_rate_movie_in_my_movies, Toast.LENGTH_LONG).show();
                    ratingUser.setRating(0);
                } else {
                    // Movie is in "My Movies" folder, and can be rated:
                    _movie.setRating((int) (rating + rating));
                    _repository.movies.updateFolderRelated(_movie);
                }
            }
        });

        ScrollView scrollViewMain = (ScrollView) findViewById(R.id.scrollViewMain);
        scrollViewMain.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (_videoController != null)
                    _videoController.hide();
            }
        });

        //region Favorite Button

        final ImageButton imgbtnFavoriteMovie = (ImageButton) findViewById(R.id.imgbtnFavoriteMovie);
        imgbtnFavoriteMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checks if the movie is in the DB:
                if (_movie.getFolder() == null) {
                    Toast.makeText(MovieDisplayActivity.this, R.string.general_msg_add_movie_to_folder, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (_movie.isFavorite()) {
                    imgbtnFavoriteMovie.setColorFilter(Color.rgb(80, 80, 80));
                    _movie.setFavorite(false);
                    _repository.movies.updateFolderRelated(_movie);
                } else {
                    imgbtnFavoriteMovie.setColorFilter(Utils.Colors.FAVORITE);
                    _movie.setFavorite(true);
                    _repository.movies.updateFolderRelated(_movie);
                }
            }
        });
        imgbtnFavoriteMovie.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utils.UI.showInformationToast(imgbtnFavoriteMovie, R.string.movie_favorite);
                return true;
            }
        });
        if (_movie.isFavorite())
            imgbtnFavoriteMovie.setColorFilter(Utils.Colors.FAVORITE);
        else
            imgbtnFavoriteMovie.setColorFilter(Color.rgb(80, 80, 80));

        //endregion

        //region Watched Button

        final ImageButton imgbtnIsWatched = (ImageButton) findViewById(R.id.imgbtnIsWatched);
        imgbtnIsWatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checks if the movie is in the DB:
                if (_movie.getFolder() == null) {
                    Toast.makeText(MovieDisplayActivity.this, R.string.general_msg_add_movie_to_folder, Toast.LENGTH_LONG).show();
                    return;
                } else if (_movie.getFolder() == Movie.Folders.WISHLIST) {
                    Toast.makeText(MovieDisplayActivity.this, R.string.general_msg_watched_and_wishlist, Toast.LENGTH_LONG).show();
                    return;
                }

                if (_movie.isWatched()) {
                    imgbtnIsWatched.setColorFilter(Color.rgb(80, 80, 80));
                    _movie.setWatched(false);
                    _repository.movies.updateFolderRelated(_movie);
                } else {
                    imgbtnIsWatched.setColorFilter(Utils.Colors.WATCHED);
                    _movie.setWatched(true);
                    _repository.movies.updateFolderRelated(_movie);
                }
            }
        });
        imgbtnIsWatched.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utils.UI.showInformationToast(imgbtnIsWatched, R.string.movie_watched);
                return true;
            }
        });
        if (_movie.isWatched())
            imgbtnIsWatched.setColorFilter(Utils.Colors.WATCHED);
        else
            imgbtnIsWatched.setColorFilter(Color.rgb(80, 80, 80));

        //endregion

        final View flAddMovieContainer = findViewById(R.id.flAddMovieContainer);
        _revealMovieAdd = RevealHelper.create(this, flAddMovieContainer, "_revealMovieAdd", new RevealHelper.RevealHelperCallback() {
            @Override
            public void initLayoutControls() {

                //region "My Movies" Button

                flAddMovieContainer.findViewById(R.id.llAddToMyMovies).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (_movie.getFolder() == null) {
                            // Selects the button "My Movies":
                            ((ImageView) flAddMovieContainer.findViewById(R.id.imgMyMovies)).setImageResource(R.mipmap.ic_my_movies);
                            ((TextView) flAddMovieContainer.findViewById(R.id.lblMyMovies)).setTextColor(Color.parseColor("#eeeeee"));
                            // Unselects the button "Wishlist":
                            ((ImageView) flAddMovieContainer.findViewById(R.id.imgWishlist)).setImageResource(R.mipmap.ic_wishlist_disabled);
                            ((TextView) flAddMovieContainer.findViewById(R.id.lblWishlist)).setTextColor(Color.parseColor("#666666"));

                            // Creates the movie in the DB:
                            _movie.setFolder(Movie.Folders.MY_MOVIES);
                            _movie.setTimeAdded(Calendar.getInstance());
                            _movie.setId(_repository.movies.create(_movie));
                            saveMovieImageToStorage();
                        } else if (_movie.getFolder() == Movie.Folders.WISHLIST) {
                            // Selects the button "My Movies":
                            ((ImageView) flAddMovieContainer.findViewById(R.id.imgMyMovies)).setImageResource(R.mipmap.ic_my_movies);
                            ((TextView) flAddMovieContainer.findViewById(R.id.lblMyMovies)).setTextColor(Color.parseColor("#eeeeee"));
                            // Unselects the button "Wishlist":
                            ((ImageView) flAddMovieContainer.findViewById(R.id.imgWishlist)).setImageResource(R.mipmap.ic_wishlist_disabled);
                            ((TextView) flAddMovieContainer.findViewById(R.id.lblWishlist)).setTextColor(Color.parseColor("#666666"));

                            // Updates the movie in the DB:
                            _movie.setFolder(Movie.Folders.MY_MOVIES);
                            _movie.setTimeAdded(Calendar.getInstance());
                            _repository.movies.updateFolderRelated(_movie);
                        } else if (_movie.getFolder() == Movie.Folders.MY_MOVIES) {
                            // Unselects the button "My Movies":
                            ((ImageView) flAddMovieContainer.findViewById(R.id.imgMyMovies)).setImageResource(R.mipmap.ic_my_movies_disabled);
                            ((TextView) flAddMovieContainer.findViewById(R.id.lblMyMovies)).setTextColor(Color.parseColor("#666666"));
                            // Unselects the button "Wishlist":
                            ((ImageView) flAddMovieContainer.findViewById(R.id.imgWishlist)).setImageResource(R.mipmap.ic_wishlist_disabled);
                            ((TextView) flAddMovieContainer.findViewById(R.id.lblWishlist)).setTextColor(Color.parseColor("#666666"));

                            // Clears any related to folder actions:
                            _movie.setFavorite(false);
                            imgbtnFavoriteMovie.setColorFilter(Color.rgb(80, 80, 80));
                            _movie.setWatched(false);
                            imgbtnIsWatched.setColorFilter(Color.rgb(80, 80, 80));
                            _movie.setRating(null);
                            ratingUser.setRating(0);

                            // Deletes the movie from the DB:
                            _movie.setFolder(null);
                            _repository.movies.delete(_movie.getId());
                            if (_movie.getPosterPath() != null)
                                Utils.Storage.deleteFromInternalStorage(_movie.getPosterPath(), getApplicationContext());
                        }
                    }
                });

                //endregion

                //region "Wishlist" Button

                flAddMovieContainer.findViewById(R.id.llAddToWishlist).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (_movie.getFolder() == null) {
                            // Unselects the button "My Movies":
                            ((ImageView) flAddMovieContainer.findViewById(R.id.imgMyMovies)).setImageResource(R.mipmap.ic_my_movies_disabled);
                            ((TextView) flAddMovieContainer.findViewById(R.id.lblMyMovies)).setTextColor(Color.parseColor("#666666"));
                            // Selects the button "Wishlist":
                            ((ImageView) flAddMovieContainer.findViewById(R.id.imgWishlist)).setImageResource(R.mipmap.ic_wishlist);
                            ((TextView) flAddMovieContainer.findViewById(R.id.lblWishlist)).setTextColor(Color.parseColor("#eeeeee"));

                            // Creates the movie in the DB:
                            _movie.setFolder(Movie.Folders.WISHLIST);
                            _movie.setWatched(false);
                            imgbtnIsWatched.setColorFilter(Color.rgb(80, 80, 80));
                            _movie.setRating(null);
                            ratingUser.setRating(0);
                            _movie.setTimeAdded(Calendar.getInstance());
                            _movie.setId(_repository.movies.create(_movie));
                            saveMovieImageToStorage();
                        } else if (_movie.getFolder() == Movie.Folders.MY_MOVIES) {
                            // Unselects the button "My Movies":
                            ((ImageView) flAddMovieContainer.findViewById(R.id.imgMyMovies)).setImageResource(R.mipmap.ic_my_movies_disabled);
                            ((TextView) flAddMovieContainer.findViewById(R.id.lblMyMovies)).setTextColor(Color.parseColor("#666666"));
                            // Selects the button "Wishlist":
                            ((ImageView) flAddMovieContainer.findViewById(R.id.imgWishlist)).setImageResource(R.mipmap.ic_wishlist);
                            ((TextView) flAddMovieContainer.findViewById(R.id.lblWishlist)).setTextColor(Color.parseColor("#eeeeee"));

                            // Updates the movie in the DB:
                            _movie.setFolder(Movie.Folders.WISHLIST);
                            _movie.setWatched(false);
                            imgbtnIsWatched.setColorFilter(Color.rgb(80, 80, 80));
                            _movie.setRating(null);
                            ratingUser.setRating(0);
                            _movie.setTimeAdded(Calendar.getInstance());
                            _repository.movies.updateFolderRelated(_movie);
                        } else if (_movie.getFolder() == Movie.Folders.WISHLIST) {
                            // Unselects the button "My Movies":
                            ((ImageView) flAddMovieContainer.findViewById(R.id.imgMyMovies)).setImageResource(R.mipmap.ic_my_movies_disabled);
                            ((TextView) flAddMovieContainer.findViewById(R.id.lblMyMovies)).setTextColor(Color.parseColor("#666666"));
                            // Unselects the button "Wishlist":
                            ((ImageView) flAddMovieContainer.findViewById(R.id.imgWishlist)).setImageResource(R.mipmap.ic_wishlist_disabled);
                            ((TextView) flAddMovieContainer.findViewById(R.id.lblWishlist)).setTextColor(Color.parseColor("#666666"));

                            // Clears any related to folder actions:
                            _movie.setFavorite(false);
                            imgbtnFavoriteMovie.setColorFilter(Color.rgb(80, 80, 80));
                            _movie.setWatched(false);
                            imgbtnIsWatched.setColorFilter(Color.rgb(80, 80, 80));
                            _movie.setRating(null);
                            ratingUser.setRating(0);

                            // Deletes the movie from the DB:
                            _movie.setFolder(null);
                            _repository.movies.delete(_movie.getId());
                            if (_movie.getPosterPath() != null)
                                Utils.Storage.deleteFromInternalStorage(_movie.getPosterPath(), getApplicationContext());
                        }
                    }
                });

                //endregion

                if (_movie.getFolder() != null) {
                    if (_movie.getFolder() == Movie.Folders.MY_MOVIES) {
                        // Selects the button "My Movies":
                        ((ImageView) flAddMovieContainer.findViewById(R.id.imgMyMovies)).setImageResource(R.mipmap.ic_my_movies);
                        ((TextView) flAddMovieContainer.findViewById(R.id.lblMyMovies)).setTextColor(Color.parseColor("#eeeeee"));
                        // Unselects the button "Wishlist":
                        ((ImageView) flAddMovieContainer.findViewById(R.id.imgWishlist)).setImageResource(R.mipmap.ic_wishlist_disabled);
                        ((TextView) flAddMovieContainer.findViewById(R.id.lblWishlist)).setTextColor(Color.parseColor("#666666"));
                    } else {
                        // Unselects the button "My Movies":
                        ((ImageView) flAddMovieContainer.findViewById(R.id.imgMyMovies)).setImageResource(R.mipmap.ic_my_movies_disabled);
                        ((TextView) flAddMovieContainer.findViewById(R.id.lblMyMovies)).setTextColor(Color.parseColor("#666666"));
                        // Selects the button "Wishlist":
                        ((ImageView) flAddMovieContainer.findViewById(R.id.imgWishlist)).setImageResource(R.mipmap.ic_wishlist);
                        ((TextView) flAddMovieContainer.findViewById(R.id.lblWishlist)).setTextColor(Color.parseColor("#eeeeee"));
                    }
                }

            }
        });

        //region Cast Controls

        _progressCast = (ProgressBar) findViewById(R.id.progressCast);

        _llShowAllCast = (LinearLayout) findViewById(R.id.llShowAllCast);
        _llShowAllCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDisplayActivity.this, FullMovieCastActivity.class);
                intent.putExtra(UIConsts.Bundles.MOVIE_CAST_LIST_KEY, _movie.getCast());
                intent.putExtra(UIConsts.Bundles.MOVIE_NAME_KEY, (_movie.getOriginalTitle() != null ? _movie.getOriginalTitle() : _movie.getTitle()));
                startActivityForResult(intent, UIConsts.RequestCodes.FULL_MOVIE_CAST_REQUEST_CODE);
                MovieDisplayActivity.this.overridePendingTransition(R.anim.go_right_enter, R.anim.go_right_exit);
            }
        });

        _lblCastMessage = (TextView) findViewById(R.id.lblCastMessage);

        _rvCast = (RecyclerView) findViewById(R.id.rvCast);
        _rvCast.setHasFixedSize(true);
        _rvCast.setNestedScrollingEnabled(false);
        _rvCast.setLayoutManager(new LinearLayoutManager(this));
        _adapterCast = new MovieCastAdapter(_personsInMovieCast, this);
        _rvCast.setAdapter(_adapterCast);

        //endregion

        //region Crew Controls

        _progressCrew = (ProgressBar) findViewById(R.id.progressCrew);

        _llShowAllCrew = (LinearLayout) findViewById(R.id.llShowAllCrew);
        _llShowAllCrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDisplayActivity.this, FullMovieCrewActivity.class);
                intent.putExtra(UIConsts.Bundles.MOVIE_CREW_LIST_KEY, _movie.getCrew());
                intent.putExtra(UIConsts.Bundles.MOVIE_NAME_KEY, (_movie.getOriginalTitle() != null ? _movie.getOriginalTitle() : _movie.getTitle()));
                startActivityForResult(intent, UIConsts.RequestCodes.FULL_MOVIE_CREW_REQUEST_CODE);
                MovieDisplayActivity.this.overridePendingTransition(R.anim.go_right_enter, R.anim.go_right_exit);
            }
        });

        _lblCrewMessage = (TextView) findViewById(R.id.lblCrewMessage);

        _rvCrew = (RecyclerView) findViewById(R.id.rvCrew);
        _rvCrew.setHasFixedSize(true);
        _rvCrew.setNestedScrollingEnabled(false);
        _rvCrew.setLayoutManager(new LinearLayoutManager(this));
        _adapterCrew = new MovieCrewAdapter(_personsInMovieCrew, this);
        _rvCrew.setAdapter(_adapterCrew);

        //endregion

        //region Movie Trailer

        // Checks if auto-play trailers is disabled:
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        _isAutoplayTrailerEnabled = preferences.getBoolean(UIConsts.Preferences.Keys.AUTOPLAY_TRAILERS, true);

        _rlTrailerPlay = (RelativeLayout) findViewById(R.id.rlTrailerPlay);

        _layoutTrailer = (RelativeLayout) findViewById(R.id.layoutTrailer);
        _layoutTrailer.setVisibility(View.GONE);

        _videoTrailer = (VideoView) findViewById(R.id.videoTrailer);
        _videoTrailer.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1));
        _videoTrailer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                _layoutTrailer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                _videoTrailer.requestFocus();

                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int arg1, int arg2) {
                        _progressTrailer.setVisibility(View.GONE);

                        // Checks if returned from a section like: Cast / Crew, then the trailer will not start:
                        if (_isReturnedFromSection)
                            _videoTrailer.pause();
                        else
                            mp.start();
                    }
                });
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            _videoTrailer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                        _progressTrailer.setVisibility(View.GONE);
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                        _progressTrailer.setVisibility(View.VISIBLE);
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                        _progressTrailer.setVisibility(View.GONE);
                    }
                    return false;
                }
            });
        }
        _videoTrailer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                _layoutTrailer.setVisibility(View.GONE);
                Snackbar.make(findViewById(R.id.coordinator), getString(R.string.movie_display_trailer_error), Snackbar.LENGTH_LONG).show();
                return true;
            }
        });

        _progressTrailer = (ProgressBar) findViewById(R.id.progressTrailer);

        // Checks if auto-play trailers is enabled or not:
        if (_isAutoplayTrailerEnabled) {
            _rlTrailerPlay.setVisibility(View.GONE);
        } else {
            _progressTrailer.setVisibility(View.GONE);
            _rlTrailerPlay.setVisibility(View.VISIBLE);

            // Adds a listener to trailer play img-btn to stream trailer:
            ImageButton imgbtnTrailerPlay = (ImageButton) findViewById(R.id.imgbtnTrailerPlay);
            imgbtnTrailerPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _progressTrailer.setVisibility(View.VISIBLE);
                    _rlTrailerPlay.setVisibility(View.GONE);

                    // Checks if the IMDb id is not null, which indicates a trailer can be displayed:
                    if (_movie.getIMDbId() != null)
                        new Task_DisplayTrailer().execute(_movie.getIMDbId());
                }
            });
        }

        //endregion

        if (savedInstanceState == null) {
            // First, checks if there's IMDb Id, means only cast & crew needs to be loaded:
            if (_movie.getIMDbId() != null) {
                if (!_movie.isExtraInfoLoaded())
                    this.loadFullMovieInformation();
            }
            // Second, checks if the movie has a TMDB Id to get the full movie information:
            else if (_movie.getTMDBId() != null) {
                if (!_movie.isExtraInfoLoaded())
                    this.loadFullMovieInformation();
            }
            // If the movie is added manually:
            else {
                _layoutTrailer.setVisibility(View.GONE);
                _rlTrailerPlay.setVisibility(View.GONE);
                _progressTrailer.setVisibility(View.GONE);

                _progressCast.setVisibility(View.GONE);
                _llShowAllCast.setVisibility(View.GONE);
                _lblCastMessage.setText(R.string.movie_no_cast);
                _lblCastMessage.setVisibility(View.VISIBLE);

                _progressCrew.setVisibility(View.GONE);
                _llShowAllCrew.setVisibility(View.GONE);
                _lblCrewMessage.setText(R.string.movie_no_crew);
                _lblCrewMessage.setVisibility(View.VISIBLE);
            }
        }

    }

    /**
     * Displays the state of the adapter (list / empty message), according to the items in the list.
     */
    private void displayListStateCast() {
        _progressCast.setVisibility(View.GONE);

        if (this._personsInMovieCast.size() < 1) {
            _rvCast.setVisibility(View.GONE);
            _llShowAllCast.setVisibility(View.GONE);
            _lblCastMessage.setText(R.string.movie_no_cast);
            _lblCastMessage.setVisibility(View.VISIBLE);
        } else {
            _lblCastMessage.setVisibility(View.GONE);
            _llShowAllCast.setVisibility(View.VISIBLE);
            _rvCast.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Displays the state of the adapter (list / empty message), according to the items in the list.
     */
    private void displayListStateCrew() {
        _progressCrew.setVisibility(View.GONE);

        if (this._personsInMovieCrew.size() < 1) {
            _rvCrew.setVisibility(View.GONE);
            _llShowAllCrew.setVisibility(View.GONE);
            _lblCrewMessage.setText(R.string.movie_no_crew);
            _lblCrewMessage.setVisibility(View.VISIBLE);
        } else {
            _lblCrewMessage.setVisibility(View.GONE);
            _llShowAllCrew.setVisibility(View.VISIBLE);
            _rvCrew.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Loads the "Full Movie Information" from the TMDB service.
     */
    public void loadFullMovieInformation() {
        _layoutTrailer.setVisibility(View.VISIBLE);
        _progressTrailer.setVisibility(View.VISIBLE);

        getSupportLoaderManager().initLoader(MOVIE_INFORMATION_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Movie>() {
            @Override
            public Loader<Movie> onCreateLoader(int id, Bundle args) {
                return new FullMovieInformationLoader(getApplicationContext(), _movie.getTMDBId());
            }

            @Override
            public void onLoadFinished(Loader<Movie> loader, Movie data) {
                if (data == null) {
                    Exception exception = ((FullMovieInformationLoader) loader).getException();
                    if (exception != null) {
                        if (exception instanceof NoNetworkException) {
                            _layoutTrailer.setVisibility(View.GONE);
                            _progressTrailer.setVisibility(View.GONE);

                            _progressCast.setVisibility(View.GONE);
                            _llShowAllCast.setVisibility(View.GONE);
                            _lblCastMessage.setText(R.string.general_msg_no_network_connection);
                            _lblCastMessage.setVisibility(View.VISIBLE);

                            _progressCrew.setVisibility(View.GONE);
                            _llShowAllCrew.setVisibility(View.GONE);
                            _lblCrewMessage.setText(R.string.general_msg_no_network_connection);
                            _lblCrewMessage.setVisibility(View.VISIBLE);

                            Toast.makeText(MovieDisplayActivity.this, R.string.general_msg_no_network_connection, Toast.LENGTH_SHORT).show();
                        } else {
                            _layoutTrailer.setVisibility(View.GONE);
                            _progressTrailer.setVisibility(View.GONE);

                            _progressCast.setVisibility(View.GONE);
                            _llShowAllCast.setVisibility(View.GONE);
                            _lblCastMessage.setText(R.string.general_msg_something_went_wrong);
                            _lblCastMessage.setVisibility(View.VISIBLE);

                            _progressCrew.setVisibility(View.GONE);
                            _llShowAllCrew.setVisibility(View.GONE);
                            _lblCrewMessage.setText(R.string.general_msg_something_went_wrong);
                            _lblCrewMessage.setVisibility(View.VISIBLE);

                            Toast.makeText(MovieDisplayActivity.this, R.string.general_msg_something_went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                    return;
                }

                //region Builds the preview sections: Cast & Crew...

                _personsInMovieCast.clear();
                for (int i = 0; i < data.getCast().size(); i++) {
                    if (i == MAX_PEOPLE_IN_PREVIEW_SECTION)
                        break;
                    _personsInMovieCast.add(data.getCast().get(i));
                }

                _personsInMovieCrew.clear();
                for (int i = 0; i < data.getCrew().size(); i++) {
                    if (i == MAX_PEOPLE_IN_PREVIEW_SECTION)
                        break;
                    _personsInMovieCrew.add(data.getCrew().get(i));
                }

                //endregion

                //region Sets dynamic properties of the movie...

                data.setId(_movie.getId());
                data.setFolder(_movie.getFolder());
                data.setRating(_movie.getRating());
                data.setWatched(_movie.isWatched());
                data.setFavorite(_movie.isFavorite());
                data.setTimeAdded(_movie.getTimeAdded());

                //endregion

                data.setExtraInfoLoaded(true);
                _movie = data;

                //region Update UI Controls...

                _lblMovieGenres.setText(data.displayGenres());
                _lblMoviePlot.setText(_movie.getPlot());
                if (_movie.getVoteAverage() == null || _movie.getVoteAverage() <= 0)
                    _lblVoteAverage.setText(R.string.general_msg_unavailable);
                else
                    _lblVoteAverage.setText(String.valueOf(_movie.getVoteAverage()));

                _adapterCast.notifyDataSetChanged();
                displayListStateCast();
                _adapterCrew.notifyDataSetChanged();
                displayListStateCrew();

                //endregion

                if (_isAutoplayTrailerEnabled) {
                    // Checks if the IMDb id is not null, which indicates a trailer can be displayed:
                    if (_movie.getIMDbId() != null)
                        new Task_DisplayTrailer().execute(_movie.getIMDbId());
                }
            }

            @Override
            public void onLoaderReset(Loader<Movie> loader) {
            }
        }).forceLoad();
    }

    /**
     * Loads a trailer by the trailer url.
     *
     * @param trailerUrl The url to load the trailer from.
     */
    private void loadTrailer(String trailerUrl) {
        try {
            _videoController = new MediaController(MovieDisplayActivity.this);
            _videoController.setAnchorView(_videoTrailer);
            _videoController.setMediaPlayer(_videoTrailer);
            _videoTrailer.setMediaController(_videoController);
            Uri link = Uri.parse(trailerUrl);
            _videoTrailer.setVideoURI(link);
            _videoTrailer.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            _videoTrailer.requestFocus();
            _videoTrailer.start();
        } catch (Exception e) {
            e.printStackTrace();
            _layoutTrailer.setVisibility(View.GONE);
        }
    }

    /**
     * Saves the movie image to the internal storage.
     */
    private void saveMovieImageToStorage() {
        if (_movie.getPosterPath() == null)
            return;

        Glide.with(this)
                .load(TMDBService.Urls.GET_PHOTO_BY_PATH + _movie.getPosterPath())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        // Here the movie image can be saved, so saves it to the internal storage:
                        Utils.Storage.saveToInternalStorage(bitmap, _movie.getPosterPath(), getApplicationContext());
                    }
                });
    }

    //endregion

    //region Tasks

    /**
     * Represents a task for getting the _movie trailer url from the "IMDb" site, and displaying the trailer to the {@link VideoView} control.
     */
    private class Task_DisplayTrailer extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            _layoutTrailer.setVisibility(View.VISIBLE);
            _progressTrailer.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String imdbPageURL = IMDbConsts.IMDB_MOVIE_PAGE_MOBILE + Uri.encode(strings[0]); // Builds the url to get the "IMDb" movie page.
                String imdbPageHTML = Utils.Networking.sendHttpRequest(imdbPageURL, getApplicationContext()); // Sends the request to the service, and returns the response.
                String videoId = getVideoIdFromIMDbHtml(imdbPageHTML);
                String videoPlayerURL = IMDbConsts.IMDB_VIDEO_PLAYER_START + videoId + IMDbConsts.IMDB_VIDEO_PLAYER_END;
                String videoPlayerHTML = Utils.Networking.sendHttpRequest(videoPlayerURL, getApplicationContext());
                return getTrailerUrlFromVideoPlayerHtml(videoPlayerHTML);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String videoUrl) {
            if (videoUrl != null) {
                _trailerUrl = videoUrl;
                loadTrailer(_trailerUrl);
            } else {
                _layoutTrailer.setVisibility(View.GONE);
            }
        }

    }

    //endregion

}
