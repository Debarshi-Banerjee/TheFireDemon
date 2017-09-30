package com.example.debarshibanerjee.projectfiredemon.events;

import android.util.Log;

import com.example.debarshibanerjee.projectfiredemon.helpers.Utility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.json.JSONObject;

import java.net.HttpURLConnection;

import retrofit2.Response;


/**
 * Created by debarshibanerjee on 29/09/17.
 */

public class BaseEvent {

    public static final int ERROR_UNKNOWN = -1;
    public static final String ERROR_CODE = "errorcode";
    public static final String ERROR_MESSAGES = "errormessages";
    private static final String LOG_TAG = "BaseEvent";
    private final Response mResponse;
    private ErrorType errorType;
    private int appErrorCode;
    private JsonObject mErrorMessages;

    public BaseEvent(Response response) {
        this.mResponse = response;
        this.errorType = ErrorType.NO_ERROR;
        this.appErrorCode = ERROR_UNKNOWN;
        parseErrorBody();
    }

    public BaseEvent(ErrorType errorType) {
        this.mResponse = null;
        this.errorType = errorType;
    }

    public BaseEvent(ErrorType errorType, int appErrorCode) {
        this.mResponse = null;
        this.errorType = errorType;
        this.appErrorCode = appErrorCode;
    }


    public boolean isSuccess() {
        return errorType == ErrorType.NO_ERROR;
    }

    public boolean isNetworkError() {
        return errorType.equals(ErrorType.NETWORK_ERROR);
    }

    public boolean isHttpError() {
        return (errorType.equals(ErrorType.SERVER_ERROR_400)
                ||errorType.equals(ErrorType.SERVER_ERROR_401)
                ||errorType.equals(ErrorType.SERVER_ERROR_403)
                ||errorType.equals(ErrorType.SERVER_ERROR_404)
                ||errorType.equals(ErrorType.SERVER_ERROR_500)
                ||errorType.equals(ErrorType.SERVER_ERROR_502)
                ||errorType.equals(ErrorType.SERVER_ERROR_503)
                ||errorType.equals(ErrorType.SERVER_ERROR_504)
                ||errorType.equals(ErrorType.SERVER_ERROR_UNKNOWN)
        );
    }

    /**
     * If the request hit an HTTP error, this will return the corresponding error code
     *
     * @return An HTTP error code if it is an HTTP error,
     * {ERROR_UNKNOWN} on error
     */
    public int getHttpStatus() {
        if (mResponse != null) {
            return mResponse.code();
        }
        return ERROR_UNKNOWN;
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

    private void parseErrorBody() {
        switch (getHttpStatus()) {
            case HttpURLConnection.HTTP_BAD_REQUEST:
                this.errorType=ErrorType.SERVER_ERROR_400;
                parseAppErrorCode();
                break;
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                this.errorType=ErrorType.SERVER_ERROR_401;
                parseAppErrorCode();
                break;
            case HttpURLConnection.HTTP_FORBIDDEN:
                this.errorType=ErrorType.SERVER_ERROR_403;
                break;
            case HttpURLConnection.HTTP_NOT_FOUND:
                this.errorType=ErrorType.SERVER_ERROR_404;
                parseAppErrorCode();
                break;
            case HttpURLConnection.HTTP_INTERNAL_ERROR:
                this.errorType=ErrorType.SERVER_ERROR_500;
                break;
            case HttpURLConnection.HTTP_BAD_GATEWAY:
                this.errorType=ErrorType.SERVER_ERROR_502;
                break;
            case HttpURLConnection.HTTP_UNAVAILABLE:
                this.errorType=ErrorType.SERVER_ERROR_503;
                break;
            case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                this.errorType=ErrorType.SERVER_ERROR_504;
                break;
            default:
                this.errorType=ErrorType.SERVER_ERROR_UNKNOWN;
                break;
        }
    }

    private void parseAppErrorCode() {
        try {
            String jsonData = mResponse.errorBody().string();
            JSONObject errorBody = new JSONObject(jsonData);

            if (errorBody != null) {
                JsonPrimitive jp = (JsonPrimitive) errorBody.get(ERROR_CODE);
                if (jp != null) {
                    appErrorCode = jp.getAsInt();
                }
                mErrorMessages = (JsonObject) errorBody.get(ERROR_MESSAGES);
            }
        } catch (Exception ex) {
            // do nothing. This can happen if conversion to JSON fails
            Log.e(LOG_TAG, ex.toString());
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
        PARSE,
        SERVER_ERROR_400,
        SERVER_ERROR_401,
        SERVER_ERROR_403,
        SERVER_ERROR_404,
        SERVER_ERROR_500,
        SERVER_ERROR_502,
        SERVER_ERROR_503,
        SERVER_ERROR_504,
        SERVER_ERROR_UNKNOWN,
        NETWORK_ERROR
    }
}
