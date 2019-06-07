package ru.com.testunsplashclient.mvp.main.favourites;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import ru.com.testunsplashclient.app.TheApplication;
import ru.com.testunsplashclient.core.data.DataProvider;
import ru.com.testunsplashclient.core.data.model.Photo;
import ru.com.testunsplashclient.ui.Screens;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class FavouritesFragmentPresenter
        extends MvpPresenter<FavouritesFragmentView>
        implements FavouritesFragmentPresenterContract {

    private static final String TAG = "FavouritesFragmentPresenter";

    private Router router;

    public FavouritesFragmentPresenter() {
        TheApplication.INSTANCE.getAppComponent().inject(this);
    }

    @Inject
    DataProvider dataProvider;

    @Override
    public void setRouter(Router router) {
        this.router = router;
    }

    @Override
    public void onBackPressed() {
        router.exit();
    }

    @Override
    public void loadFavourites() {
        getViewState().showLoading();
        dataProvider.getFavouritesFromDb(favourites -> {
            getViewState().hideLoading();
            getViewState().showFavourites(favourites);
        }, throwable -> {
            getViewState().hideLoading();
            getViewState().showDataBaseError(throwable);
        });
    }

    @Override
    public void refreshDb() {
        dataProvider.clearFavouritesInDb(throwable -> {
            getViewState().showDataBaseError(throwable);
        });
    }

    @Override
    public void insertOrDeleteFavouriteInDb(Photo photo) {
        dataProvider.insertOrDeleteFavouriteInDb(photo, throwable -> {
            getViewState().showDataBaseError(throwable);
        });
    }

    @Override
    public void goToFavouriteInfo(String id) {
        router.navigateTo(new Screens.PhotoDetailsScreen(id));
    }

}
