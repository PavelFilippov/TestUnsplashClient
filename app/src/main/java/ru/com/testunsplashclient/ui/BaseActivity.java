package ru.com.testunsplashclient.ui;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import ru.com.testunsplashclient.R;
import ru.com.testunsplashclient.app.NetworkConnectivityAware_;
import ru.com.testunsplashclient.app.NoNetworkException;
import ru.com.testunsplashclient.core.data.events.NetworkData;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private NetworkConnectivityAware_ receiver;
    private int screenHeight;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkConnectivityAware_();
        registerReceiver(receiver, filter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
    }

    @Override
    protected void onStop() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onNetworkStateChanged(NetworkData networkData) {
        Crouton.cancelAllCroutons();
        if (networkData.isConnected()) {
            Crouton.makeText(this, getString(R.string.network_connection_established), new Style.Builder().setBackgroundColorValue(getResources().getColor(R.color.colorDeepSkyBlue)).build()).show();
        } else {
            Crouton.makeText(this, getString(R.string.network_connection_lost), new Style.Builder().setBackgroundColorValue(getResources().getColor(R.color.colorPrimaryDark)).build()).show();
        }
    }

    public void showServerError(Throwable throwable) {
        if (throwable instanceof NoNetworkException) {
            EventBus.getDefault().post(new NetworkData(false));
        } else {
            onServerError();
        }
    }

    public void showDbError(Throwable throwable) {
        Crouton.makeText(this, getString(R.string.database_error), new Style.Builder()
                .setBackgroundColorValue(getResources().getColor(R.color.colorPrimaryDark))
                .build())
                .show();
    }

    private void onServerError() {
        Crouton.makeText(this, getString(R.string.server_error), new Style.Builder()
                .setBackgroundColorValue(getResources().getColor(R.color.colorPrimaryDark))
                .build())
                .show();
    }

    public int getScreenHeight() {
        return screenHeight;
    }

}
