package com.ohadshai.movielib.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.ohadshai.movielib.R;
import com.ohadshai.movielib.utils.Utils;

/**
 * Represents a dialog for showing the "Application About".
 * Created by Ohad on 11/4/2016.
 */
public class AboutDialog extends DialogFragment {

    //region Constants

    /**
     * Holds a constant for the dialog fragment tag.
     */
    public static final String DIALOG_TAG = "app_about_dialog_tag";

    //endregion

    //region Dialog Events

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_about, null);

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
     * @param manager The fragment manager to show the dialog in.
     * @return Returns the initialized dialog.
     */
    public static AboutDialog show(@NonNull FragmentManager manager) {
        AboutDialog dialog = new AboutDialog();
        dialog.show(manager, AboutDialog.DIALOG_TAG);
        return dialog;
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
            public void onClick(View v) {
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

    }

    //endregion

}
