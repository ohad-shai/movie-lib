package com.ohadshai.movielib.ui.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import com.ohadshai.movielib.R;
import com.ohadshai.movielib.db.DBHandler;
import com.ohadshai.movielib.services.GetGenresListService;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.ui.dialogs.AboutDialog;
import com.ohadshai.movielib.utils.AppCompatPreferenceActivity;
import com.ohadshai.movielib.utils.Utils;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatPreferenceActivity {

    //region Private Members

    /**
     * Holds the database interactions object.
     */
    private DBHandler _repository;

    //endregion

    //region Activity Events

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Adds the "Navigate Up to Parent Activity".

        _repository = DBHandler.getInstance(this); // Initializes the database object.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region Private Methods

    /**
     * Notifies a preference change by raising a SharedPreferenceChanged event.
     *
     * @param key The key of the preference changed.
     */
    private void notifyPreferenceChanged(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences((getApplicationContext()));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, true);
        editor.apply();
        editor.putBoolean(key, false);
        editor.apply();
    }

    //endregion

    //region Inner Classes

    /**
     * Holds the main PreferenceFragment that display all the preferences.
     */
    public static class MainPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        //region Private Members

        /**
         * Holds the {@link SharedPreferences} control.
         */
        private SharedPreferences _preferences;

        //endregion

        //region Events

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            _preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            _preferences.registerOnSharedPreferenceChangeListener(this);

            //region Category - "General"

            //region Preference - "Language"

            // Displays the current "Language" to the summary of the preference:
            ListPreference language = (ListPreference) findPreference(UIConsts.Preferences.Keys.LANGUAGE);
            language.setSummary(language.getEntry());
            language.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(getActivity(), R.string.settings_languages_notice, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    // Displays the current "Language" to the summary of the preference:
                    ListPreference language = (ListPreference) preference;
                    language.setSummary(language.getEntries()[Utils.Arrays.indexOf(newValue, language.getEntryValues())]);
                    return true;
                }
            });

            //endregion

            //region Preference - "Region"

            // Displays the current "Region" to the summary of the preference:
            ListPreference region = (ListPreference) findPreference(UIConsts.Preferences.Keys.REGION);
            region.setSummary(region.getEntry());
            region.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(getActivity(), R.string.settings_regions_notice, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            region.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    // Displays the current "Region" to the summary of the preference:
                    ListPreference region = (ListPreference) preference;
                    region.setSummary(region.getEntries()[Utils.Arrays.indexOf(newValue, region.getEntryValues())]);
                    return true;
                }
            });

            //endregion

            //endregion

            //region Category - "Actions"

            //region Autoplay Trailers

            SwitchPreference autoplayTrailers = (SwitchPreference) findPreference(UIConsts.Preferences.Keys.AUTOPLAY_TRAILERS);
            autoplayTrailers.setChecked(_preferences.getBoolean(UIConsts.Preferences.Keys.AUTOPLAY_TRAILERS, true));

            //endregion

            //region Preference - Delete all data
            findPreference(UIConsts.Preferences.Keys.DELETE_ALL_DATA).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // Displays an AlertDialog with a checkbox list, to let the user choose what will be erased:

                    // Creates the array to populate the dialog checkbox list:
                    final CharSequence[] items = {UIConsts.Preferences.Values.DELETE_ALL_DATA_WISHLIST,
                            UIConsts.Preferences.Values.DELETE_ALL_DATA_MY_MOVIES,
                            UIConsts.Preferences.Values.DELETE_ALL_DATA_MY_ACTORS,
                            UIConsts.Preferences.Values.DELETE_ALL_DATA_MY_DIRECTORS};
                    final ArrayList<Integer> selection = new ArrayList<Integer>(); // Holds the selection of items (checkbox) in the list.

                    AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Settings_Dialog)
                            .setTitle(getString(R.string.settings_delete_all_data_dialog_title))
                            .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                    if (isChecked)
                                        selection.add(indexSelected); // If the user checked the item, adds it to the selection.
                                    else if (selection.contains(indexSelected))
                                        selection.remove(Integer.valueOf(indexSelected)); // If the item is already in the array, removes it from the selection.
                                }
                            }).setPositiveButton(getString(R.string.general_btn_ok), new DialogInterface.OnClickListener() {
                                @SuppressLint("CommitPrefEdits")
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    if (selection.size() > 0) {
                                        StringBuilder msgDeleted = new StringBuilder(); // Holds the deleted message.

                                        // Checks if "Wishlist" checked:
                                        if (selection.contains(0)) {
                                            msgDeleted.append(UIConsts.Preferences.Values.DELETE_ALL_DATA_WISHLIST).append(", ");
                                            ((SettingsActivity) getActivity())._repository.movies.deleteAllWishlist();
                                            ((SettingsActivity) getActivity()).notifyPreferenceChanged(UIConsts.Preferences.Keys.WISHLIST_DELETED);
                                        }
                                        // Checks if "My Movies" checked:
                                        if (selection.contains(1)) {
                                            msgDeleted.append(UIConsts.Preferences.Values.DELETE_ALL_DATA_MY_MOVIES).append(", ");
                                            ((SettingsActivity) getActivity())._repository.movies.deleteAllMyMovies();
                                            ((SettingsActivity) getActivity()).notifyPreferenceChanged(UIConsts.Preferences.Keys.MY_MOVIES_DELETED);
                                        }
                                        // Checks if "My Actors" checked:
                                        if (selection.contains(2)) {
                                            msgDeleted.append(UIConsts.Preferences.Values.DELETE_ALL_DATA_MY_ACTORS).append(", ");
                                            ((SettingsActivity) getActivity())._repository.people.deleteAllMyActors();
                                            ((SettingsActivity) getActivity()).notifyPreferenceChanged(UIConsts.Preferences.Keys.MY_ACTORS_DELETED);
                                        }
                                        // Checks if "My Directors" checked:
                                        if (selection.contains(3)) {
                                            msgDeleted.append(UIConsts.Preferences.Values.DELETE_ALL_DATA_MY_DIRECTORS).append(", ");
                                            ((SettingsActivity) getActivity())._repository.people.deleteAllMyDirectors();
                                            ((SettingsActivity) getActivity()).notifyPreferenceChanged(UIConsts.Preferences.Keys.MY_DIRECTORS_DELETED);
                                        }
                                        msgDeleted.setLength(msgDeleted.length() - 2); // Removes the last ", " from the message.
                                        Snackbar.make(getView(), getString(R.string.settings_delete_all_data_msg_deleted) + " " + msgDeleted.toString() + ".", Snackbar.LENGTH_LONG).show();
                                    } else {
                                        Snackbar.make(getView(), getString(R.string.settings_delete_all_data_msg_no_delete), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }).setNegativeButton(getString(R.string.general_btn_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();

                    return true;
                }
            });
            //endregion

            //endregion

            //region Category - "Support"

            //region Preference - "Tell a friend"

            findPreference(UIConsts.Preferences.Keys.TELL_A_FRIEND).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String text = "https://play.google.com/store/apps/details?id=" + UIConsts.APP_PACKAGE_NAME;
                    Utils.Intents.share(text, R.string.general_share_via, getActivity());
                    return true;
                }
            });

            //endregion

            //region Preference - "Rate this app"

            findPreference(UIConsts.Preferences.Keys.RATE_APP).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // Opens "Google Play" in the app profile to let the user rate this app:
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + UIConsts.APP_PACKAGE_NAME)));
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), R.string.general_msg_google_play_not_found, Toast.LENGTH_LONG).show(); // No "Google Play" was found.
                    }
                    return true;
                }
            });

            //endregion

            //region Preference - "About"

            findPreference(UIConsts.Preferences.Keys.ABOUT_APP).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AboutDialog.show(getFragmentManager());
                    return true;
                }
            });

            //endregion

            //endregion

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(UIConsts.Preferences.Keys.LANGUAGE)) {
                // If the language changed, updates the genres list in the database to the new language:
                Intent intent = new Intent(getActivity(), GetGenresListService.class);
                getActivity().startService(intent);
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            if (_preferences != null)
                _preferences.unregisterOnSharedPreferenceChangeListener(this);
        }

        //endregion

    }

    //endregion

}
