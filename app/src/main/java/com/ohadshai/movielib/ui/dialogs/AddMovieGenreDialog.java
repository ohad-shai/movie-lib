package com.ohadshai.movielib.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ohadshai.movielib.R;
import com.ohadshai.movielib.entities.Genre;
import com.ohadshai.movielib.utils.Utils;

import java.util.ArrayList;

/**
 * Represents a dialog for entering a genre name when adding a movie genre.
 * Created by Ohad on 10/3/2016.
 */
public class AddMovieGenreDialog extends DialogFragment {

    //region Constants

    /**
     * Holds a constant for the dialog fragment tag.
     */
    public static final String DIALOG_TAG = "add_movie_genre_dialog_tag";

    //endregion

    //region Private Members

    /**
     * Holds the genre name EditText control.
     */
    private EditText _txtGenreName;

    /**
     * Holds the current movie genres list.
     */
    private ArrayList<Genre> _currentGenres;

    /**
     * Holds the positive result listener.
     */
    private PositiveResultListener _positiveResult;

    //endregion

    //region Dialog Events

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_movie_genre, null);

        this.initControls(dialogView);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        return builder.create();
    }

    //endregion

    //region Public Static API

    /**
     * Initializes a new instance of the dialog, and shows it.
     *
     * @param manager  The fragment manager to show the dialog in.
     * @param genres   The current movie genres list.
     * @param listener The listener of positive result to set.
     * @return Returns the initialized dialog.
     */
    public static AddMovieGenreDialog show(@NonNull FragmentManager manager, ArrayList<Genre> genres, PositiveResultListener listener) {
        AddMovieGenreDialog dialog = new AddMovieGenreDialog();
        dialog.setCurrentGenres(genres);
        dialog.setPositiveResultListener(listener);
        dialog.show(manager, AddMovieGenreDialog.DIALOG_TAG);
        return dialog;
    }

    //endregion

    //region Public API

    /**
     * Sets the current movie genres list.
     *
     * @param genres The current movie genres list.
     */
    public void setCurrentGenres(ArrayList<Genre> genres) {
        this._currentGenres = genres;
    }

    /**
     * Sets an event listener for a positive result from the dialog.
     *
     * @param listener The listener of positive result to set.
     */
    public void setPositiveResultListener(PositiveResultListener listener) {
        _positiveResult = listener;
    }

    //endregion

    //region Private Methods

    /**
     * Initializes all dialog view controls.
     *
     * @param view The view of the dialog.
     */
    private void initControls(final View view) {

        _txtGenreName = (EditText) view.findViewById(R.id.txtGenreName);

        final ImageButton imgbtnClose = (ImageButton) view.findViewById(R.id.imgbtnClose);
        imgbtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        Button btnDone = (Button) view.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String genreName = _txtGenreName.getText().toString().trim();

                //region Validations
                if (genreName.equals("")) {
                    Toast.makeText(getActivity(), getString(R.string.dialog_add_movie_genre_validation_empty), Toast.LENGTH_SHORT).show();
                    return;
                } else if (checkIfGenreExists(genreName)) {
                    Toast.makeText(getActivity(), getString(R.string.dialog_add_movie_genre_validation_exists), Toast.LENGTH_SHORT).show();
                    return;
                }
                //endregion

                if (_positiveResult != null)
                    _positiveResult.onPositiveResult(genreName);
                dismiss();
            }
        });

    }

    /**
     * Checks if a movie genre exists in the genres list.
     *
     * @param genreName The genre name to check if exists in the genres list.
     * @return Returns true if the movie genre exists, otherwise false.
     */
    private boolean checkIfGenreExists(String genreName) {
        if (_currentGenres == null)
            throw new NullPointerException("_currentGenres");

        for (Genre genre : _currentGenres) {
            if (genre.getName().equals(genreName))
                return true;
        }
        return false;
    }

    //endregion

    //region Inner Classes

    /**
     * Represents a callback for a positive result from the AddMovieGenreDialog.
     */
    public interface PositiveResultListener {

        /**
         * Event occurs when a positive button in the dialog pressed.
         *
         * @param genreName The genre name created from the dialog.
         */
        void onPositiveResult(String genreName);

    }

    //endregion

}
