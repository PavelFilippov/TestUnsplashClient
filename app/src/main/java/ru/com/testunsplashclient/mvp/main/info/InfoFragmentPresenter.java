package ru.com.testunsplashclient.mvp.main.info;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.com.testunsplashclient.app.TheApplication;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class InfoFragmentPresenter
        extends MvpPresenter<InfoFragmentView>
        implements InfoFragmentPresenterContract{

    private static final String TAG = "InfoFragmentPresenter";

    private Router router;

    public InfoFragmentPresenter() {
        TheApplication.INSTANCE.getAppComponent().inject(this);
    }

    @Override
    public void setRouter(Router router) {
        this.router = router;
    }

    @Override
    public void onBackPressed() {
        router.exit();
    }

}
