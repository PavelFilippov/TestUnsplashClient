package ru.com.testunsplashclient.mvp.main.photo_details;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import ru.com.testunsplashclient.app.TheApplication;
import ru.com.testunsplashclient.core.data.DataProvider;
import ru.com.testunsplashclient.core.data.model.Photo;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class PhotoDetailsFragmentPresenter
        extends MvpPresenter<PhotoDetailsFragmentView>
        implements PhotoDetailsFragmentPresenterContract{

    private static final String TAG = "PhotoDetailsFragmentPresenter";

    private Router router;

    public PhotoDetailsFragmentPresenter() {
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
    public void loadPhoto(String id) {
        getViewState().showLoading();
        dataProvider.getPhoto(id, photo -> {
            getViewState().hideLoading();
            getViewState().showPhoto(setFavourite(photo));
        }, throwable -> {
            getViewState().hideLoading();
            getViewState().showServerError(throwable);
        });
    }

    @Override
    public void insertOrDeleteFavouriteInDb(Photo photo) {
        dataProvider.insertOrDeleteFavouriteInDb(photo, throwable -> {
            getViewState().showDataBaseError(throwable);
        });
    }

    private Photo setFavourite(Photo photo) {
        if(dataProvider.containsFavourite(photo.getId())) {
            photo.setFavourite(true);
        }
        return photo;
    }

}
