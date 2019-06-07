package ru.com.testunsplashclient.mvp.main.photos;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import ru.com.testunsplashclient.app.TheApplication;
import ru.com.testunsplashclient.core.data.DataProvider;
import ru.com.testunsplashclient.core.data.model.Photo;
import ru.com.testunsplashclient.core.utils.Constants;
import ru.com.testunsplashclient.ui.Screens;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class PhotosFragmentPresenter
        extends MvpPresenter<PhotosFragmentView>
        implements PhotosFragmentPresenterContract{

    private static final String TAG = "PhotosFragmentPresenter";

    private Router router;

    private int page = -1;

    public PhotosFragmentPresenter() {
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
    public int getCurrentPage() {
        return page - 1;
    }

    @Override
    public void loadPhotos(boolean loadFromDb, boolean refreshData) {
        getViewState().showLoading();
        if (loadFromDb) {
            if (!dataProvider.isEmptyPhotos()) {
                loadPhotosFromDb();
            } else {
                loadPhotosFromNet(refreshData);
            }
        } else {
            loadPhotosFromNet(refreshData);
        }
    }

    @Override
    public void refreshDb() {
        dataProvider.clearPhotosInDb(throwable -> {
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
    public void goToPhotoInfo(String id) {
        router.navigateTo(new Screens.PhotoDetailsScreen(id));
    }

    // Internal methods

    private void loadPhotosFromDb() {
        dataProvider.getPhotosFromDb(photos -> {
            getViewState().hideLoading();
            setFavourites(photos);
            getViewState().showPhotosFromDb(photos);
        }, throwable -> {
            getViewState().hideLoading();
            getViewState().showDataBaseError(throwable);
        });
    }

    private void loadPhotosFromNet(boolean refreshData) {
        if(page == -1 || refreshData) page = 1;
        dataProvider.getPhotos(page, photos -> {
            getViewState().hideLoading();
            setFavourites(photos);
            dataProvider.updatePhotosInDb(photos, throwable -> {
                getViewState().showDataBaseError(throwable);
            });
            getViewState().showPhotosFormNet(page, photos);
            if(page < Constants.PAGE_COUNT) page++;
        }, throwable -> {
            getViewState().hideLoading();
            getViewState().showServerError(throwable);
        });
    }

    private void setFavourites(List<Photo> photos) {
        for(Photo photo: photos) {
            if(dataProvider.containsFavourite(photo.getId())) {
                photo.setFavourite(true);
            }
        }
    }

}
