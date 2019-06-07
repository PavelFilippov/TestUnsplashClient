package ru.com.testunsplashclient.mvp.main.photos;
import ru.com.testunsplashclient.core.data.model.Photo;
import ru.com.testunsplashclient.mvp.BasePresenter;

public interface PhotosFragmentPresenterContract extends BasePresenter {

    int getCurrentPage();

    void loadPhotos(boolean loadFromDb, boolean refreshData);

    void refreshDb();

    void insertOrDeleteFavouriteInDb(Photo photo);

    void goToPhotoInfo(String id);

}
