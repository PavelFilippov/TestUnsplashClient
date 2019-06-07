package ru.com.testunsplashclient.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.ReceiverAction;
import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ru.com.testunsplashclient.core.data.events.NetworkData;
import ru.com.testunsplashclient.core.utils.NetworkUtils;

@EReceiver
public class NetworkConnectivityAware extends BroadcastReceiver {

    @Inject
    NetworkUtils utils;
    @App
    TheApplication app;

    public NetworkConnectivityAware() {
        super();
        TheApplication.INSTANCE.getAppComponent().inject(this);
    }

    @ReceiverAction(actions = {ConnectivityManager.CONNECTIVITY_ACTION, WifiManager.WIFI_STATE_CHANGED_ACTION})
    void onConnectivityChanged(Intent intent) {
        boolean connected = utils.hasNetworkConnection();
        boolean hasNetworkConnection = app.isHasNetworkConnection();
        if ((connected && hasNetworkConnection) || (!connected && !hasNetworkConnection)) return;
        app.setHasNetworkConnection(connected);
        sendEvent(connected);
    }

    private void sendEvent(boolean hasNetworkConnection) {
        EventBus.getDefault().post(new NetworkData(hasNetworkConnection));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    }

}
