package com.example.debarshibanerjee.projectfiredemon.events;

import android.util.Log;

import com.example.debarshibanerjee.projectfiredemon.helpers.Utility;
import com.example.debarshibanerjee.projectfiredemon.rest.RestClientV1;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.net.HttpURLConnection;

import okhttp3.Response;


/**
 * Created by debarshibanerjee on 29/09/17.
 */

public class BaseEvent {

    private static final String LOG_TAG = "BaseEvent";
    private final ErrorType errorType;
    private int appErrorCode;
    private JsonObject mErrorMessages;
    private RetrofitError error;
    private Exception ex;

    public BaseEvent(ErrorType errorType) {
        this(errorType, null);
    }

    public BaseEvent(ErrorType errorType,
                     RetrofitError error) {
        this.errorType = errorType;
        this.error = error;
        this.appErrorCode = RestClientV1.ERROR_UNKNOWN;
        parseErrorBody();
    }

    public BaseEvent(ErrorType errorType,
                     Exception ex) {
        this.errorType = errorType;
        this.ex = ex;
        this.appErrorCode = RestClientV1.ERROR_UNKNOWN;
    }

    /**
     * Returns the exception hit by the request. This should not be called for for {@link ErrorType#PARSE}
     * or {@link ErrorType#STREAM_ERROR}
     *
     * @return The exception hit by the request.
     * @throws IllegalStateException if the function is called for {@link ErrorType#PARSE}
     *                               or {@link ErrorType#STREAM_ERROR}
     */
    public Exception getException() {
        if (errorType != ErrorType.PARSE
                && errorType != ErrorType.STREAM_ERROR) {
            throw new IllegalStateException(String.format("GetException should not be called for %s", errorType));
        } else {
            return ex;
        }
    }

    public boolean isSuccess() {
        return errorType == ErrorType.NO_ERROR;
    }

    public boolean isNetworkError() {
        return error != null && error.getKind() == RetrofitError.Kind.NETWORK;
    }

    public boolean isHttpError() {
        return (errorType == ErrorType.RETROFIT_ERROR
                && error != null
                && error.getKind() == RetrofitError.Kind.HTTP);
    }

    /**
     * If the request hit an HTTP error, this will return the corresponding error code
     *
     * @return An HTTP error code if it is an HTTP error,
     * {@link RestClientV1#ERROR_UNKNOWN} on error
     */
    public int getHttpStatus() {
        if (isHttpError()) {
            Response response = error.getResponse();
            if (response != null) {
                return response.getStatus();
            }
        }

        return RestClientV1.ERROR_UNKNOWN;
    }

    /**
     * Returns the app-specific error code hit by the request. This error code will be
     * related to the business logic of the application and needs to be known by backend
     * and app both
     *
     * @return The app/backend-specific error code
     */
    public int getAppErrorCode() {
        return appErrorCode;
    }

    /**
     * Whether the event posted should be a sticky post
     *
     * @return whether the event posted should be a sticky post
     */
    public boolean getPostSticky() {
        return false;
    }

    /**
     * Returns the error message for the passed in attribute name
     *
     * @param attribute The key name for the attribute
     * @return Corresponding error message if found, null otherwise
     */
    public String getErrorMessages(String attribute) {
        if (mErrorMessages == null) {
            return null;
        }
        JsonArray errorMessages = mErrorMessages.getAsJsonArray(attribute);
        if (errorMessages != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < errorMessages.size(); i++) {
                if (builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(errorMessages.get(i).getAsString());
            }

            return builder.toString();
        }

        return null;
    }

    /**
     * Get the error details for the passed in error attribute
     *
     * @param attribute Name of the attribute key
     * @return The value for the passed in attribute key
     */
    public String getErrorDetail(String attribute) {
        if (mErrorMessages == null) {
            return null;
        }

        JsonPrimitive error = mErrorMessages.getAsJsonPrimitive(attribute);
        if (error != null) {
            return error.getAsString();
        }

        return null;
    }

    private void parseErrorBody() {
        if (getHttpStatus() == HttpURLConnection.HTTP_BAD_REQUEST
                || getHttpStatus() == HttpURLConnection.HTTP_NOT_FOUND
                || getHttpStatus() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            try {
                JsonObject errorBody = (JsonObject) error.getBodyAs(JsonObject.class);
                if (errorBody != null) {
                    JsonPrimitive jp = errorBody.getAsJsonPrimitive(RestClientV1.ERROR_CODE);
                    if (jp != null) {
                        appErrorCode = jp.getAsInt();
                    }

                    mErrorMessages = errorBody.getAsJsonObject(RestClientV1.ERROR_MESSAGES);
                }
            } catch (Exception ex) {
                // do nothing. This can happen if conversion to JSON fails
                Log.e(LOG_TAG, ex.toString());
            }
        }
    }

    /**
     * Should be called in case of failure. Checks whether the error
     * was due to Internet unavailability
     *
     * @return True if internet is not available
     */
    public boolean isInternetUnavailable() {
        return isNetworkError() && Utility.isNetworkUnavailable();
    }

    /**
     * Error types that can be hit by a Retrofit request
     */
    public enum ErrorType {
        NO_ERROR,
        RETROFIT_ERROR,
        PARSE,
        STREAM_ERROR
    }
}
