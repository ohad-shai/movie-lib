package com.ohadshai.movielib.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ohadshai.movielib.R;
import com.ohadshai.movielib.utils.Utils;

/**
 * Represents a dialog for choosing an action (manual / select).
 * Created by Ohad on 03/20/2017.
 */
public class AddManualOrSelectDialog extends DialogFragment {

    //region Constants

    /**
     * Holds a constant for the dialog fragment tag.
     */
    public static final String DIALOG_TAG = "add_manual_or_select_dialog_tag";

    /**
     * Holds a constant for a choice result: "Dismiss" (the user dismissed the dialog).
     */
    public static final int CHOICE_DISMISS = 0;

    /**
     * Holds a constant for a choice result: "Manual".
     */
    public static final int CHOICE_MANUAL = 1;

    /**
     * Holds a constant for a choice result: "Select".
     */
    public static final int CHOICE_SELECT = 2;

    //endregion

    //region Private Members

    /**
     * Holds an indicator indicating whether the result from the dialog is positive (result is Manual or Select, and not Dismiss).
     */
    private boolean _isPositiveResult = false;

    /**
     * Holds the choice result listener.
     */
    private ChoiceResultListener _choiceResult;

    //endregion

    //region Dialog Events

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_manual_or_select, null);

        this.initControls(dialogView);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (!_isPositiveResult && _choiceResult != null)
            _choiceResult.onResult(CHOICE_DISMISS);
    }

    //endregion

    //region Public Static API

    /**
     * Initializes a new instance of the dialog, and shows it.
     *
     * @param manager  The fragment manager to show the dialog in.
     * @param listener The listener for the choice result to set.
     * @return Returns the initialized dialog.
     */
    public static AddManualOrSelectDialog show(@NonNull FragmentManager manager, ChoiceResultListener listener) {
        AddManualOrSelectDialog dialog = new AddManualOrSelectDialog();
        dialog.setChoiceResultListener(listener);
        dialog.show(manager, AddManualOrSelectDialog.DIALOG_TAG);
        return dialog;
    }

    //endregion

    //region Public API

    /**
     * Sets an event listener for a choice result from the dialog.
     *
     * @param listener The listener for the choice result to set.
     */
    public void setChoiceResultListener(ChoiceResultListener listener) {
        this._choiceResult = listener;
    }

    //endregion

    //region Private Methods

    /**
     * Initializes all dialog view controls.
     *
     * @param view The view of the dialog.
     */
    private void initControls(final View view) {

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

        LinearLayout llManual = (LinearLayout) view.findViewById(R.id.llManual);
        llManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_choiceResult != null)
                    _choiceResult.onResult(CHOICE_MANUAL);

                _isPositiveResult = true;
                dismiss();
            }
        });
        llManual.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), R.string.dialog_add_movie_action_create_desc, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        LinearLayout llSelect = (LinearLayout) view.findViewById(R.id.llSelect);
        llSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_choiceResult != null)
                    _choiceResult.onResult(CHOICE_SELECT);

                _isPositiveResult = true;
                dismiss();
            }
        });
        llSelect.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), R.string.dialog_add_movie_action_select_desc, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    //endregion

    //region Inner Classes

    /**
     * Represents a listener for a choice result from the dialog.
     */
    public interface ChoiceResultListener {

        /**
         * Event occurs when there's a choice result from the dialog.
         *
         * @param choiceResult The choice result from the dialog (Manual / Select / Dismiss).
         */
        void onResult(int choiceResult);

    }

    //endregion

}
