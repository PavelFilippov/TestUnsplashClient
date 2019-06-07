package ru.com.testunsplashclient.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ru.com.testunsplashclient.app.TheApplication;

public class NetworkUtils {

    public boolean hasNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) TheApplication.INSTANCE.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
