package com.example.debarshibanerjee.projectfiredemon;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by debarshibanerjee on 28/09/17.
 */

public class App extends Application {
    private static Context mContext;

    private static SharedPreferences mSharedPreferences;

    /**
     * Gets the application context
     *
     * @return The application context
     */
    public static Context getAppContext() {
        return mContext;
    }

    /**
     * Returns the Shared Preference object
     *
     * @return The Shared Preference object
     */
    public static SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        FlowManager.init(new FlowConfig.Builder(this).openDatabasesOnInit(true).build());

    }
}
