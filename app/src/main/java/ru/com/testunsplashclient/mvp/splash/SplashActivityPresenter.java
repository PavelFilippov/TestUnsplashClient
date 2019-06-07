package ru.com.testunsplashclient.mvp.splash;

import org.androidannotations.annotations.EBean;

import ru.com.testunsplashclient.mvp.BasePresenter;
import ru.com.testunsplashclient.ui.Screens;
import ru.terrakok.cicerone.Router;

@EBean
public class SplashActivityPresenter implements BasePresenter {

    private Router router;

    @Override
    public void setRouter(Router router) {
        this.router = router;
    }

    @Override
    public void onBackPressed() {
        router.exit();
    }

    public void goToNextScreen() {
        router.newRootScreen(new Screens.MainScreen());
    }
}
