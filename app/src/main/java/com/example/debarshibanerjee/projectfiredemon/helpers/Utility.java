package com.example.debarshibanerjee.projectfiredemon.helpers;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.example.debarshibanerjee.projectfiredemon.App;
import com.example.debarshibanerjee.projectfiredemon.R;

/**
 * Created by debarshibanerjee on 28/09/17.
 */

public class Utility {
    private static final String GOOGLE_MAPS_DIRECTIONS_BASE_URL = "google.navigation:";
    private static final String GOOGLE_MAPS_DESTINATION_QUERY = "q=";
    private static final String GOOGLE_MAPS_QUERY_MODE = "&mode=d";
    private static final String PREFIX_MAIL = "mailto:";
    private static final String PREFIX_SMS = "smsto:";
    private static final String PREFIX_SMS_BODY = "sms_body";

    /**
     * Hides keyboard from view
     *
     * @param context Context
     * @param view    View
     */
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Shows the alert dialog with a single positive button only
     *
     * @param titleResId    Resource ID for title
     * @param messageResId  Resource ID for Message
     * @param context       Context
     * @param positiveResId Resource ID for positive Text
     * @param positive      OnClickListener for the positive button
     * @return The AlertDialog
     */
    public static AlertDialog showAlertDialog(int titleResId,
                                              int messageResId,
                                              Context context,
                                              int positiveResId,
                                              DialogInterface.OnClickListener positive) {
        String message = null;
        if (messageResId != -1) {
            message = context.getString(messageResId);
        }
        return showAlertDialog(titleResId, message, context, positiveResId, positive);
    }

    /**
     * Shows the alert dialog with a single positive button only
     *
     * @param titleResId    Resource ID for title
     * @param message       String message
     * @param context       Context
     * @param positiveResId Resource ID for positive Text
     * @param positive      OnClickListener for the positive button
     * @return The AlertDialog
     */
    public static AlertDialog showAlertDialog(int titleResId,
                                              String message,
                                              Context context,
                                              int positiveResId,
                                              DialogInterface.OnClickListener positive) {

        return showAlertDialog(titleResId, message, context, positiveResId, positive,
                -1, null, false);
    }

    /**
     * Shows the alert dialog with a positive and negative buttons
     *
     * @param titleResId    Resource ID for the title
     * @param messageResId  Resource ID for the message
     * @param context       Context
     * @param positiveResId Resource ID for positive button text
     * @param positive      OnClickHandler for positive button
     * @param negativeResId Resource ID for negative button text
     * @param negative      OnClickHandler for negative button
     * @return The alert dialog
     */
    public static AlertDialog showAlertDialog(int titleResId,
                                              int messageResId,
                                              Context context,
                                              int positiveResId,
                                              DialogInterface.OnClickListener positive,
                                              int negativeResId,
                                              DialogInterface.OnClickListener negative) {
        String message = null;
        if (messageResId != -1) {
            message = context.getString(messageResId);
        }
        return showAlertDialog(titleResId, message, context, positiveResId, positive, negativeResId, negative);
    }

    /**
     * Shows the alert dialog with a positive and negative buttons
     *
     * @param titleResId    Resource ID for the title
     * @param message       String message
     * @param context       context
     * @param positiveResId Resource ID for positive button text
     * @param positive      OnClickHandler for positive button
     * @param negativeResId Resource ID for negative button text
     * @param negative      OnClickHandler for negative button
     * @return The alert dialog
     */
    public static AlertDialog showAlertDialog(int titleResId,
                                              String message,
                                              Context context,
                                              int positiveResId,
                                              DialogInterface.OnClickListener positive,
                                              int negativeResId,
                                              DialogInterface.OnClickListener negative) {

        return showAlertDialog(titleResId, message, context, positiveResId,
                positive, negativeResId, negative, false /* not cancelable */);
    }

    /**
     * Shows the alert dialog with a positive and negative buttons
     *
     * @param titleResId    Resource ID for the title
     * @param messageResId  Resource ID for the message
     * @param context       Context
     * @param positiveResId Resource ID for positive button text
     * @param positive      OnClickHandler for positive button
     * @param negativeResId Resource ID for negative button text
     * @param negative      OnClickHandler for negative button
     * @param isCancelable  Makes Dialog is cancelable
     * @return The alert dialog
     */
    public static AlertDialog showAlertDialog(int titleResId,
                                              int messageResId,
                                              Context context,
                                              int positiveResId,
                                              DialogInterface.OnClickListener positive,
                                              int negativeResId,
                                              DialogInterface.OnClickListener negative,
                                              boolean isCancelable) {
        String message = null;
        if (messageResId != -1) {
            message = context.getString(messageResId);
        }
        return showAlertDialog(titleResId, message, context, positiveResId, positive, negativeResId, negative, isCancelable);
    }

    /**
     * Shows the alert dialog with a positive and negative buttons
     *
     * @param titleResId    Resource ID for the title
     * @param message       String message
     * @param context       context
     * @param positiveResId Resource ID for positive button text
     * @param positive      OnClickHandler for positive button
     * @param negativeResId Resource ID for negative button text
     * @param negative      OnClickHandler for negative button
     * @param isCancelable  Dialog is Cancelable
     * @return The alert dialog
     */
    public static AlertDialog showAlertDialog(int titleResId,
                                              String message,
                                              Context context,
                                              int positiveResId,
                                              DialogInterface.OnClickListener positive,
                                              int negativeResId,
                                              DialogInterface.OnClickListener negative,
                                              boolean isCancelable) {
        String title = null;
        if (titleResId != -1) {
            title = context.getString(titleResId);
        }
        return showAlertDialog(title, message, context, positiveResId, positive, negativeResId, negative, isCancelable);
    }


    /**
     * Shows the alert dialog with a positive and negative buttons
     *
     * @param title         String title
     * @param message       String message
     * @param context       context
     * @param positiveResId Resource ID for positive button text
     * @param positive      OnClickHandler for positive button
     * @param negativeResId Resource ID for negative button text
     * @param negative      OnClickHandler for negative button
     * @param isCancelable  Dialog is Cancelable
     * @return The alert dialog
     */
    public static AlertDialog showAlertDialog(String title,
                                              String message,
                                              Context context,
                                              int positiveResId,
                                              DialogInterface.OnClickListener positive,
                                              int negativeResId,
                                              DialogInterface.OnClickListener negative,
                                              boolean isCancelable) {
        // Fullscreen activities use different theme compared to other activities
        // To make dialogs consistent through the app lets apply this theme.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (message != null) {
            builder.setMessage(message);
        }

        if (title != null) {
            builder.setTitle(title);
        }

        builder.setCancelable(isCancelable);

        builder.setPositiveButton(positiveResId, positive);

        if (negativeResId != -1) {
            builder.setNegativeButton(negativeResId, negative);
        }

        AlertDialog dialog = builder.create();

        dialog.show();

        return dialog;
    }

    /**
     * Shows custom snackbar alert on failure to make calls
     *
     * @param view     view to stick to
     * @param msgResId the resource id for the shown message
     */
    public static void showSnackBar(View view, int msgResId) {
        final Snackbar snackbar = Snackbar.make(view,
                msgResId,
                Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction(R.string.close_snack_bar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    /**
     * Calls the system service to check if we have Internet access
     *
     * @return True if internet is connected, false otherwise
     */
    public static boolean isNetworkUnavailable() {
        ConnectivityManager cm =
                (ConnectivityManager) App.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork == null || !activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Are we running the app in debug mode
     *
     * @return True if in debug mode, false otherwise
     */
    public static boolean isDebug() {
        return (0 != (App.getAppContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
    }

    /**
     * Gets the pref from Shared Preferences
     *
     * @param stringResId  Key's string resource ID
     * @param defaultValue default value to be returned if the key is not found
     * @return Value from SharedPref
     */
    public static String getStringPreference(int stringResId, String defaultValue) {
        Context context = App.getAppContext();
        SharedPreferences prefs = App.getSharedPreferences();
        return prefs.getString(context.getString(stringResId), defaultValue);
    }

    /**
     * Gets the pref from Shared Preferences
     *
     * @param stringKey    Key's string resource
     * @param defaultValue default value to be returned if the key is not found
     * @return Value from SharedPref
     */
    public static boolean getBooleanPreference(String stringKey, boolean defaultValue) {
        SharedPreferences prefs = App.getSharedPreferences();
        return prefs.getBoolean(stringKey, defaultValue);
    }

    /**
     * Gets the pref from Shared Preferences
     *
     * @param stringResId  Key's string resource ID
     * @param defaultValue default value to be returned if the key is not found
     * @return Value from SharedPref
     */
    public static boolean getBooleanPreference(int stringResId, boolean defaultValue) {
        Context context = App.getAppContext();
        SharedPreferences prefs = App.getSharedPreferences();
        return prefs.getBoolean(context.getString(stringResId), defaultValue);
    }

    /**
     * Sets the key and String value in Shared Preferences
     *
     * @param stringResId Key's string resource ID
     * @param value       Value to be stored
     */
    public static void storeStringPreference(int stringResId, String value) {
        Context context = App.getAppContext();
        SharedPreferences prefs = App.getSharedPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(stringResId), value);
        editor.apply();
    }

    /**
     * Sets the key and boolean value in Shared Preferences
     *
     * @param stringKey Key's string resource
     * @param value     Value to be stored
     */
    public static void storeBooleanPreference(String stringKey, boolean value) {
        SharedPreferences prefs = App.getSharedPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(stringKey, value);
        editor.apply();
    }

    /**
     * Sets the key and boolean value in Shared Preferences
     *
     * @param stringResId Key's string resource ID
     * @param value       Value to be stored
     */
    public static void storeBooleanPreference(int stringResId, boolean value) {
        Context context = App.getAppContext();
        SharedPreferences prefs = App.getSharedPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(context.getString(stringResId), value);
        editor.apply();
    }

    /**
     * Gets the tinted drawable in backwards compatible way
     *
     * @param drawableResource the drawable
     * @param tintColor        Color ID for the tint
     * @return Tinted drawable
     */
    public static Drawable getTintedDrawableCompat(Drawable drawableResource, int tintColor) {
        Resources r = App.getAppContext().getResources();
        Drawable drawable = DrawableCompat.wrap(drawableResource);
        DrawableCompat.setTint(drawable, r.getColor(tintColor));
        return drawable;
    }

    /**
     * Used to get a share intent with the passed-in string
     *
     * @param shareText Text to be shared
     * @return An intent to share the share string.
     */
    public static Intent buildShareIntent(String shareText) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        sendIntent.setType("text/plain");
        return sendIntent;
    }

    /**
     * Opens the email intent
     *
     * @param context Context
     * @param to      To address
     */
    public static void sendEmail(Context context, String to) {
        Intent emailIntent = getEmailIntentInternal(to, "", "");

        context.startActivity(Intent.createChooser(emailIntent, "Select Email client"));
    }

    /**
     * Sends the email intent
     *
     * @param to      To address
     * @param subject Subject
     * @param text    Body
     */
    public static void sendEmail(Context context, String to, String subject, String text) {
        Intent emailIntent = getEmailIntentInternal(to, subject, text);

        context.startActivity(Intent.createChooser(emailIntent, "Select Email Client"));
    }

    private static Intent getEmailIntentInternal(String to, String subject, String text) {
        Intent emailIntent = new Intent();
        emailIntent.setType("text/html");
        emailIntent.setAction(Intent.ACTION_SENDTO);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(text));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        Uri uri = Uri.parse(PREFIX_MAIL);
        emailIntent.setData(uri);

        return emailIntent;
    }

    /**
     * Opens a share intent for whatsapp
     *
     * @param context Context
     * @param text    Text to share
     */
    public static void shareViaWhatsApp(Context context, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setPackage("com.whatsapp");
        sendIntent.setType("text/plain");
        //User may not have whatsApp installed
        try {
            context.startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {

            //no whats app found add a dialog maybe to show the user that the user has no life
            e.printStackTrace();
        }
    }

    /**
     * Returns a SMS intent
     *
     * @param context Context
     * @param text    the sms body
     * @param to      the number to whom it may concern
     */
    public static Intent sendSmsIntent(Context context, String text, String to) {
        Uri uri = Uri.parse(PREFIX_SMS + to);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        if (text != null) {
            intent.putExtra(PREFIX_SMS_BODY, text);
        }

        context.startActivity(Intent.createChooser(intent, "Select SMS client"));
        return intent;
    }

    /**
     * Opens a call phone number intent
     *
     * @param context Context
     * @param phone   Phone
     */
    public static void sendCallIntent(Context context, String phone) {
        String uri = "tel:" + phone;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));

        context.startActivity(intent);
    }

    /**
     * It generates an intent which is used to open maps activity
     * and get driving navigation from users's current location to clinic....
     *
     * @param fullAddress the full address string found as property in a clinic object
     * @return an intent to open maps activity and start navigation
     */
    public static Intent getDirections(String fullAddress) {
        String mapsIntentUrl = GOOGLE_MAPS_DIRECTIONS_BASE_URL +
                GOOGLE_MAPS_DESTINATION_QUERY + fullAddress +
                GOOGLE_MAPS_QUERY_MODE;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(mapsIntentUrl));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * Shows an internet unavailability dialog
     *
     * @param event   BaseEvent
     * @param context Context
     * @return boolean indicating whether the event should be propagated. True means consumed, false means propagate
     */
    public static boolean handleInternetUnavailabilityError(BaseEvent event, Context context) {
        if (event.isInternetUnavailable()) {
            Utility.showAlertDialog(R.string.network_error_title,
                    R.string.network_error_details,
                    context, R.string.dialog_ok, null);

            return true;
        }
        return false;
    }

    /**
     * Gets the distance in metres between that two passed in latlngs
     *
     * @param point1 First point
     * @param point2 Second point
     * @return Distance in Metres
     */
    public static float getDistanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return Float.MAX_VALUE;
        }

        float[] results = new float[3];
        Location.distanceBetween(point1.latitude, point1.longitude, point2.latitude, point2.longitude, results);

        return results[0];
    }


    /**
     * Converts a view to a bitmap
     *
     * @param context Context
     * @param view    View
     * @return Bitmap containing the view
     */
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * Returns a share invite
     *
     * @return Intent for sharing
     */
    public static Intent shareInvite() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.setType("text/plain");
        return shareIntent;
    }

    /**
     * Checks whether the error is a retriable error
     *
     * @param error The retrofit error
     * @return True if error can be retried
     */
    public static boolean isRetriableError(RetrofitError error) {
        // TODO: Check for HTTP error as well
        return error.getKind() == RetrofitError.Kind.NETWORK
                || error.getKind() == RetrofitError.Kind.CONVERSION
                || error.getKind() == RetrofitError.Kind.UNEXPECTED;

    }


    private static int getIntegerPreference(int stringResId) {
        Context context = App.getAppContext();
        return getIntegerPreference(context.getString(stringResId), -1);
    }

    private static int getIntegerPreference(String key, int defaultValue) {
        SharedPreferences prefs = App.getSharedPreferences();
        return prefs.getInt(key, defaultValue);
    }

    private static void storeIntegerPreference(int stringResId, int value) {
        Context context = App.getAppContext();
        storeIntegerPreference(context.getString(stringResId), value);
    }

    private static void storeIntegerPreference(String key, int value) {
        SharedPreferences prefs = App.getSharedPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * Hide a notification which is currently being shown
     *
     * @param ordinal the int value of the notification type, passed when the notification was created
     */
    public static void hideNotification(int ordinal) {
        NotificationManager notificationManager =
                (NotificationManager) App.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(ordinal);
    }


    /**
     * Generates random number between [min, max)
     *
     * @param min Min number
     * @param max Max number
     * @return random number
     */
    public static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }
}
