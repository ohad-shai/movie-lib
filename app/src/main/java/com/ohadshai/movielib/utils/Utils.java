package com.ohadshai.movielib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ohadshai.movielib.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Represents a class utilities and helpers for general classes.
 * Created by Ohad on 10/6/2016.
 */
public class Utils {

    /**
     * Represents utilities and helpers for the UI.
     */
    public static final class UI {

        /**
         * Hides the keyboard from the UI.
         *
         * @param activity The current activity.
         * @param view     The view that hides the keyboard from the UI (like Button).
         */
        public static void hideSoftKeyboard(Activity activity, View view) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }

        /**
         * Displays a dialog that user has no network connection.
         *
         * @param context The context owner of the dialog.
         */
        public static void showNoNetworkConnectionDialog(final Context context) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setTitle(R.string.dialog_network_error_title);
            builder.setMessage(R.string.dialog_network_error_message);
            builder.setPositiveButton(R.string.general_settings, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intents.networkSettings(context);
                }
            });
            builder.setNegativeButton(R.string.general_btn_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    return;
                }
            });
            builder.show();
        }

        /**
         * Shows an information {@link Toast} with the text provided, to the {@link View} provided.
         *
         * @param view  The view to show the information toast.
         * @param resId The string resource id that holds the text to show in the information toast.
         */
        @SuppressLint("RtlHardcoded")
        public static void showInformationToast(@NonNull View view, @StringRes int resId) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);

            Toast toast = Toast.makeText(view.getContext(), resId, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.LEFT, (location[0] - (view.getWidth()) / 2), (location[1] + (view.getHeight() / 2)));
            toast.show();
        }

        /**
         * Shows a {@link FloatingActionButton} with an animation, after the delay provided.
         *
         * @param fab   The {@link FloatingActionButton} to show.
         * @param delay The amount of delay till the fab will be shown (in millis).
         */
        public static void showFabWithAnimation(final FloatingActionButton fab, final int delay) {
            fab.setVisibility(View.INVISIBLE);
            fab.setScaleX(0.0F);
            fab.setScaleY(0.0F);
            fab.setAlpha(0.0F);
            fab.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    fab.getViewTreeObserver().removeOnPreDrawListener(this);
                    fab.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fab.show();
                        }
                    }, delay);
                    return true;
                }
            });
        }

        /**
         * Hides a {@link FloatingActionButton} with an animation, after the delay provided.
         *
         * @param fab   The {@link FloatingActionButton} to hide.
         * @param delay The amount of delay till the fab will be hidden (in millis).
         */
        public static void hideFabWithAnimation(final FloatingActionButton fab, final int delay) {
            fab.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    fab.getViewTreeObserver().removeOnPreDrawListener(this);
                    fab.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fab.hide();
                        }
                    }, delay);
                    return true;
                }
            });
        }

        /**
         * Sets margins to a view.
         *
         * @param view   The view to set the margins.
         * @param left   The left margin value.
         * @param top    The top margin value.
         * @param right  The right margin value.
         * @param bottom The bottom margin value.
         */
        public static void setMargins(View view, int left, int top, int right, int bottom) {
            if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                p.setMargins(left, top, right, bottom);
                view.requestLayout();
            }
        }

    }

    /**
     * Represents utilities and helpers for colors.
     */
    public static final class Colors {

        /**
         * Holds a constant for the "Primary Color" integer value.
         */
        public static final int PRIMARY = Color.rgb(37, 37, 37);

        /**
         * Holds a constant for the "Primary Dark Color" integer value.
         */
        public static final int PRIMARY_DARK = Color.rgb(24, 24, 24);

        /**
         * Holds a constant for the "Accent Color" integer value.
         */
        public static final int ACCENT = Color.rgb(255, 87, 51);

        /**
         * Holds a constant for the "Search Color" integer value.
         */
        public static final int SEARCH = Color.rgb(187, 187, 187);

        /**
         * Holds a constant for the "Favorite Color" integer value.
         */
        public static final int FAVORITE = Color.rgb(255, 80, 80);

        /**
         * Holds a constant for the "Watched Color" integer value.
         */
        public static final int WATCHED = Color.rgb(0, 204, 102);

        /**
         * Holds a constant for the "Facebook Color" integer value.
         */
        public static final int FACEBOOK = Color.rgb(59, 89, 152);

        /**
         * Holds a constant for the "Twitter Color" integer value.
         */
        public static final int TWITTER = Color.rgb(85, 172, 238);

        /**
         * Holds a constant for the "Selection Status Bar Color" integer value.
         */
        public static final int SELECTION_STATUS = Color.rgb(60, 60, 60);

    }

    /**
     * Represents utilities and helpers for intents.
     */
    public static final class Intents {

        /**
         * Shares a simple text.
         *
         * @param text     The simple text to share.
         * @param resId    The string resource id holds the title of the share intent.
         * @param activity The activity owner.
         */
        public static void share(@NonNull String text, @StringRes int resId, @NonNull Activity activity) {
            if (text.trim().equals(""))
                throw new IllegalArgumentException("text is empty.");

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");
            activity.startActivity(Intent.createChooser(sendIntent, activity.getResources().getText(resId)));
        }

        /**
         * Opens the network settings activity.
         *
         * @param context The context owner.
         */
        public static void networkSettings(@NonNull Context context) {
            context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        }

        /**
         * Starts the "Gallery" activity, to let the user choose an image.
         *
         * @param activity    The activity owner.
         * @param requestCode The request code for the activity result.
         */
        public static void gallery(@NonNull Activity activity, int requestCode) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");
            activity.startActivityForResult(galleryIntent, requestCode);
        }

        /**
         * Starts the "Camera" activity, to let the user picture an image.
         *
         * @param activity    The activity owner.
         * @param requestCode The request code for the activity result.
         */
        public static void camera(@NonNull Activity activity, int requestCode) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            activity.startActivityForResult(cameraIntent, requestCode);
        }

        /**
         * Opens the web browser on the provided URL.
         *
         * @param activity The activity owner.
         * @param url      The URL to open the web browser on.
         */
        public static void webBrowser(@NonNull Activity activity, @NonNull String url) {
            if (!url.startsWith("http://"))
                url = "http://" + url;

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
        }

        /**
         * Opens the Facebook app if found, otherwise the web browser on the Facebook site.
         *
         * @param activity   The activity owner.
         * @param facebookId The Facebook id to open the profile on.
         */
        public static void facebook(@NonNull Activity activity, @NonNull String facebookId) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + facebookId));
                activity.startActivity(intent);
            } catch (Exception e) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + facebookId)));
            }
        }

        /**
         * Opens the Twitter app if found, otherwise the web browser on the Twitter site.
         *
         * @param activity  The activity owner.
         * @param twitterId The Twitter id to open the profile on.
         */
        public static void twitter(@NonNull Activity activity, @NonNull String twitterId) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitterId));
                activity.startActivity(intent);
            } catch (Exception e) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + twitterId)));
            }
        }

        /**
         * Opens the Instagram app if found, otherwise the web browser on the Instagram site.
         *
         * @param activity    The activity owner.
         * @param instagramId The Instagram id to open the profile on.
         */
        public static void instagram(@NonNull Activity activity, @NonNull String instagramId) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/" + instagramId));
                intent.setPackage("com.instagram.android");
                activity.startActivity(intent);
            } catch (Exception e) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/" + instagramId)));
            }
        }

    }

    /**
     * Represents utilities and helpers for class: "Strings".
     */
    public static final class Strings {

        /**
         * Checks if a string is a numeric value.
         *
         * @param str The string to check.
         * @return Returns true if the string is numeric, otherwise false.
         */
        public static boolean isNumeric(String str) {
            for (char c : str.toCharArray()) {
                if (!Character.isDigit(c)) return false;
            }
            return true;
        }

        /**
         * Makes a string in a title representation (changes to camel case).
         *
         * @param str The string to change.
         * @return Returns the title representation (camel case).
         */
        public static String toTitle(String str) {
            StringBuilder sb = new StringBuilder();
            boolean isNextWord = true;

            for (char c : str.toCharArray()) {
                if (Character.isSpaceChar(c)) {
                    isNextWord = true;
                } else if (isNextWord) {
                    c = Character.toTitleCase(c);
                    isNextWord = false;
                }
                sb.append(c);
            }

            return sb.toString();
        }

        /**
         * Converts a string contains a list separated cy commas, to a list of strings.
         *
         * @param str The string to convert from.
         * @return Returns the list of strings converted from the string.
         */
        public static ArrayList<String> toList(String str) {
            if (str == null)
                return null;
            else
                return new ArrayList<String>(java.util.Arrays.asList(str.split("\\s*,\\s*")));
        }

        /**
         * Converts a list of strings to a string contains the list separated by commas.
         *
         * @param strings The list of strings to convert.
         * @return Returns the converted string.
         */
        public static String fromList(ArrayList<String> strings) {
            if (strings == null)
                return null;

            StringBuilder sb = new StringBuilder();

            for (String str : strings)
                sb.append(str).append(",");

            if (sb.length() > 0)
                return sb.substring(0, sb.length() - 1);
            else
                return null;
        }

    }

    /**
     * Represents utilities and helpers for arrays.
     */
    public static final class Arrays {

        /**
         * Gets the index of an object in an array of objects.
         *
         * @param obj   The object to get it's index in the array.
         * @param array The array to get the index from.
         * @return Returns the index of the object in the array if found, otherwise -1.
         */
        public static int indexOf(Object obj, Object[] array) {
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(obj))
                    return i;
            }
            return -1;
        }

    }

    /**
     * Represents utilities and helpers for networking.
     */
    public static final class Networking {

        /**
         * Sends an HTTP request to the url provided.
         *
         * @param urlString The url to send the HTTP request.
         * @param context   The context.
         * @return Returns the response from the HTTP request.
         * @throws NoNetworkException Throws a NoNetworkException when there's no internet connection.
         */
        public static String sendHttpRequest(String urlString, Context context) throws NoNetworkException {
            if (urlString == null || urlString.trim().equals(""))
                throw new NullPointerException("urlString");

            HttpURLConnection httpCon = null;
            InputStream input_stream = null;
            InputStreamReader input_stream_reader = null;
            BufferedReader input = null;
            StringBuilder response = new StringBuilder();
            try {
                // Checks if there's no network connection:
                if (!isNetworkAvailable(context))
                    throw new NoNetworkException();

                URL url = new URL(urlString);
                httpCon = (HttpURLConnection) url.openConnection();

                if (httpCon.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e("TAG", "Cannot Connect to : " + urlString);
                    return null;
                }

                input_stream = httpCon.getInputStream();
                input_stream_reader = new InputStreamReader(input_stream);
                input = new BufferedReader(input_stream_reader);
                String line;
                while ((line = input.readLine()) != null) {
                    response.append(line).append("\n");
                }
            } catch (NoNetworkException e) {
                throw e;
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input_stream_reader.close();
                        input_stream.close();
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (httpCon != null) {
                        httpCon.disconnect();
                    }
                }
            }
            return response.toString();
        }

        /**
         * Detects if a network connection is available in the device.
         *
         * @param context The context.
         * @return Returns true if a network connection is available, otherwise false.
         */
        public static boolean isNetworkAvailable(@NonNull Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

    }

    /**
     * Represents utilities and helpers for storage.
     */
    public static final class Storage {

        //region Private Members

        /**
         * Holds the internal storage path.
         */
        private static String _internalStoragePath = null;

        //endregion

        /**
         * Gets the internal images storage path.
         *
         * @param context The context owner.
         * @return Returns the internal images storage path.
         */
        public static String getInternalStoragePath(@NonNull Context context) {
            if (_internalStoragePath != null)
                return _internalStoragePath;

            _internalStoragePath = new ContextWrapper(context).getDir("images", Context.MODE_APPEND).getAbsolutePath();
            return _internalStoragePath;
        }

        /**
         * Saves an image to the internal storage.
         *
         * @param imageBitmap The image bitmap to save.
         * @param fileName    The name of the image file, including the format ending.
         * @param context     The context owner.
         * @return Returns the path file of the saved image.
         */
        public static String saveToInternalStorage(@NonNull Bitmap imageBitmap, @NonNull String fileName, @NonNull Context context) {
            String savePath = null;
            File path = new File(getInternalStoragePath(context), fileName.substring(fileName.lastIndexOf("/") + 1)); // Sets the image file path.

            if (path.exists())
                return path.getAbsolutePath();

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(path);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); // Writes the image to the OutputStream.
                savePath = path.getAbsolutePath(); // Sets the save path, to be returned.
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return savePath;
        }

        /**
         * Deletes an image from the internal storage.
         *
         * @param fileName The path of the image to delete.
         * @param context  The context owner.
         * @return Returns true if the image deleted, otherwise false.
         */
        public static boolean deleteFromInternalStorage(@NonNull String fileName, @NonNull Context context) {
            File file = new File(getInternalStoragePath(context), fileName.substring(fileName.lastIndexOf("/") + 1));
            return file.delete();
        }

    }

    /**
     * Represents utilities and helpers for class: "Bitmaps".
     */
    public static final class Bitmaps {

        /**
         * Scales a bitmap via method "Center Crop" to the provided height and width.
         *
         * @param source    The bitmap to scale.
         * @param newHeight The new height of the bitmap to scale.
         * @param newWidth  The new width of the bitmap to scale.
         * @return Returns the new scaled bitmap.
         */
        public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
            int sourceWidth = source.getWidth();
            int sourceHeight = source.getHeight();

            // Computes the scaling factors to fit the new height and width, respectively. To cover the final image, the final scaling will be the bigger of these two:
            float xScale = (float) newWidth / sourceWidth;
            float yScale = (float) newHeight / sourceHeight;
            float scale = Math.max(xScale, yScale);

            // Gets the size of the source bitmap when scaled:
            float scaledWidth = scale * sourceWidth;
            float scaledHeight = scale * sourceHeight;

            // Let's find out the upper left coordinates if the scaled bitmap should be centered in the new size given by the parameters:
            float left = (newWidth - scaledWidth) / 2;
            float top = (newHeight - scaledHeight) / 2;

            // The target rectangle for the new scaled version of the source bitmap:
            RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

            // Creates a new bitmap of the specified size, and draw the new scaled bitmap onto it:
            Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
            Canvas canvas = new Canvas(dest);
            canvas.drawBitmap(source, null, targetRect, null);

            return dest;
        }

    }

    /**
     * Represents utilities and helpers for class: {@link Cursor}.
     */
    public static final class Cursors {

        /**
         * Gets a column name index in a {@link Cursor} object.
         *
         * @param cursor     The {@link Cursor} object to find the index from.
         * @param columnName The column name to get the the index in the {@link Cursor}.
         * @return Returns the index of the column name in the {@link Cursor}.
         */
        public static int getColumnIndex(@NonNull Cursor cursor, @NonNull String columnName) {
            int index = cursor.getColumnIndex(columnName);
            if (index > -1)
                return index;

            index = cursor.getColumnIndex(removeColumnClosures(columnName));
            if (index > -1)
                return index;

            throw new IllegalArgumentException("Column name: \"" + columnName + "\" was not found in the cursor.");
        }

        //region Private Methods

        /**
         * Removes a column closures from a column name, like from: "[Column]" to "Column".
         *
         * @param columnName The column name to remove closures.
         * @return Returns the column name with closures removed.
         */
        private static String removeColumnClosures(@NonNull String columnName) {
            return columnName.substring(1, columnName.length() - 1);
        }

        //endregion

    }

}
