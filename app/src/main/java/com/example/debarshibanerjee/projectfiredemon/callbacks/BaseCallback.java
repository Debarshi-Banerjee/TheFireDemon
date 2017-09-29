package com.example.debarshibanerjee.projectfiredemon.callbacks;


import android.util.Log;

import com.example.debarshibanerjee.projectfiredemon.events.BaseEvent;
import com.example.debarshibanerjee.projectfiredemon.helpers.Utility;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.InvocationTargetException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by debarshibanerjee on 29/09/17.
 */

public abstract class BaseCallback<T> implements Callback<T> {

    private static String LOG_TAG = "BaseCallback";
    private final Class<? extends BaseEvent> mEvent;

    BaseCallback(Class<? extends BaseEvent> event) {
        LOG_TAG = event.getSimpleName();
        mEvent = event;
    }

    public Class<? extends BaseEvent> getEvent() {
        return mEvent;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.isSuccessful()){

        }else{
         switch (response.code()){

         }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }
//    @Override
//    public void failure(RetrofitError error) {
//        if (error.getKind() == RetrofitError.Kind.UNEXPECTED) {
//            Log.e(LOG_TAG, error.getMessage());
//            throw error;
//        }
//
//        try {
//            BaseEvent event = mEvent.getDeclaredConstructor(BaseEvent.ErrorType.class, RetrofitError.class)
//                    .newInstance(BaseEvent.ErrorType.RETROFIT_ERROR, error);
//
//            // We will handle all common errors here that are relevant to all retrofit calls
//
//            // If the error was 404 send the user to login page
//            // Clear previous auth token
//            if (event.isHttpError()) {
//            } else if (event.isNetworkError() && Utility.isNetworkUnavailable()) {
//                // retry maybe??
//            }
//
//            postEvent(event);
//        } catch (InstantiationException e) {
//            Log.e(LOG_TAG, e.toString());
//        } catch (IllegalAccessException e) {
//            Log.e(LOG_TAG, e.toString());
//        } catch (InvocationTargetException e) {
//            Log.e(LOG_TAG, e.toString());
//        } catch (NoSuchMethodException e) {
//            Log.e(LOG_TAG, e.toString());
//        }
//    }

    /**
     * Posts an event with default sticky value
     *
     * @param event Event to be posted
     */
    public void postEvent(BaseEvent event) {
        postEvent(null, event);
    }

    /**
     * Posts an event with optional override of sticky flag
     *
     * @param stickyOverride Whether to override the base class sticky characteristics
     * @param event          Event to be posted
     */
    public void postEvent(Boolean stickyOverride, BaseEvent event) {
        boolean isStickyEvent = event.getPostSticky();
        if (stickyOverride != null) {
            isStickyEvent = stickyOverride;
        }

        if (isStickyEvent) {
            EventBus.getDefault().postSticky(event);
        } else {
            EventBus.getDefault().post(event);
        }
    }
}
