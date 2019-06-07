package ru.com.testunsplashclient.ui.screens.splash;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import javax.inject.Inject;

import ru.com.testunsplashclient.R;
import ru.com.testunsplashclient.app.TheApplication;
import ru.com.testunsplashclient.mvp.splash.SplashActivityPresenter;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity {

//Set app navigation injections

    @Inject
    Router router;

    @Inject
    NavigatorHolder navigatorHolder;

    private Navigator navigator = new SupportAppNavigator(this, -1);

//Set Bundles and Beans

    @Bean
    SplashActivityPresenter presenter;

//Main methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        TheApplication.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void afterViews() {

        presenter.setRouter(router);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.goToNextScreen();
            }
        }, 1000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }

}
