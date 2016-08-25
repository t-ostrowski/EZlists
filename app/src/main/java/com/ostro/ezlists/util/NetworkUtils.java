package com.ostro.ezlists.util;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ostro.ezlists.R;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 19/08/2016.
 */

public class NetworkUtils {

    public static final int TYPE_NOT_CONNECTED = 0;
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;

    public static final int ERROR_UNKNOWN = 0;
    public static final int ERROR_INTERRUPT = 1;
    public static final int ERROR_NOT_CONNECTED = 10;
    public static final int ERROR_NETWORK = 11;
    public static final int ERROR_TIME_OUT = 12;

    private static NetworkUtils INSTANCE;
    private Context mContext;
    private Resources mResources;

    public static void init(Context context) {
        INSTANCE = new NetworkUtils(context);
    }

    private NetworkUtils(Context context) {
        this.mContext = context;
        this.mResources = context.getResources();
    }

    public static NetworkUtils get() {
        return INSTANCE;
    }

    public int getConnectivityStatus() {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork && activeNetwork.isConnectedOrConnecting()) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public boolean isApplicationConnected() {
        return getConnectivityStatus() != 0;
    }

    public boolean isNetworkError(Throwable throwable) {
        return throwable instanceof IOException;
    }

    public boolean isNetworkErrorWithoutAppConnected(Throwable throwable) {
        return isNetworkError(throwable) && isApplicationConnected();
    }

    public boolean isTimeout(Throwable throwable) {
        return throwable.getCause() instanceof SocketTimeoutException || (throwable.getMessage() != null && throwable.getMessage().contains("ETIMEDOUT"));
    }

    public boolean isInterrupted(Throwable throwable) {
        return throwable instanceof InterruptedException;
    }

    public int getErrorType(Throwable throwable) {
        if (isApplicationConnected()) {
            if (isTimeout(throwable)) {
                return ERROR_TIME_OUT;
            } else if (isNetworkError(throwable)) {
                return ERROR_NETWORK;
            }
        } else if (isNetworkError(throwable)) {
            return ERROR_NOT_CONNECTED;
        }
        return isInterrupted(throwable) ? ERROR_INTERRUPT : ERROR_UNKNOWN;
    }

    public String getErrorMessage(Throwable throwable) {
        return getErrorMessage(getErrorType(throwable));
    }

    public String getErrorMessage(int type) {
        String errorMessage;
        switch (type) {
            case NetworkUtils.ERROR_NOT_CONNECTED:
                errorMessage = mResources.getString(R.string.error_network_connection);
                break;
            case NetworkUtils.ERROR_NETWORK:
                errorMessage = mResources.getString(R.string.error_network_problem);
                break;
            case NetworkUtils.ERROR_TIME_OUT:
                errorMessage = mResources.getString(R.string.error_network_time_out);
                break;
            default:
                errorMessage = mResources.getString(R.string.error_unknown);
        }
        return errorMessage;
    }
}
